package ua.shtain.opensky.states_map

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ua.shtain.opensky.R
import ua.shtain.opensky.core.base.BaseFragment
import ua.shtain.opensky.core.extensions.makeSnackbar
import ua.shtain.opensky.core.extensions.orZero
import ua.shtain.opensky.core.repositories.states.models.StatesRequest
import ua.shtain.opensky.databinding.FrStatesMapBinding

@AndroidEntryPoint
class StatesMapFragment : BaseFragment<FrStatesMapBinding>(FrStatesMapBinding::inflate),
    OnMapReadyCallback {

    private val viewModel: StatesMapViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        (childFragmentManager.findFragmentById(R.id.frMap) as SupportMapFragment).getMapAsync(this)
        viewModel.collectStates()
    }

    private fun setupUI() {
        binding?.mapToolbar?.toolbar?.apply {
            navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        moveCameraToStartPoint(map)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.states.collect { status ->
                renderState(status = status, map = map)
            }
        }
        updateDataWithDelay()
    }

    private fun updateDataWithDelay() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.updateStatesWithDelay(arguments?.getParcelable(STATES_MAP_REQUEST_KEY))
            }
        }
    }

    private fun moveCameraToStartPoint(map: GoogleMap) {
        val latLng = arguments?.getParcelable(START_LAT_LNG_KEY)
            ?: LatLng(PRAGUE_LATITUDE, PRAGUE_LONGITUDE)
        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(latLng, 7f)
        )
    }

    private fun renderState(status: ViewStatesMapStatus, map: GoogleMap) {
        when (status) {
            is ViewStatesMapStatus.Error -> {
                showError(status.message)
            }
            is ViewStatesMapStatus.Success -> {
                renderSuccessfulData(status, map)
            }
            ViewStatesMapStatus.Initial -> {
                // do nothing
            }
        }
    }

    private fun renderSuccessfulData(
        status: ViewStatesMapStatus.Success,
        map: GoogleMap
    ) {
        map.clear()
        status.data.states.forEach { state ->
            map.addMarker(
                MarkerOptions()
                    .position(
                        LatLng(state.latitude.orZero(), state.longitude.orZero())
                    )
                    .title(state.transponder)
            )
        }
    }

    private fun showError(error: Int) {
        requireContext().makeSnackbar(
            message = error,
            view = requireView(),
        ).show()
    }

    companion object {

        private const val STATES_MAP_REQUEST_KEY = "states_map_request_key"
        private const val START_LAT_LNG_KEY = "start_lat_lng_key"

        private const val PRAGUE_LATITUDE = 50.073658
        private const val PRAGUE_LONGITUDE = 14.418540

        fun newInstance(
            statesRequest: StatesRequest?,
            startLatLng: LatLng?
        ): StatesMapFragment {
            return StatesMapFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(STATES_MAP_REQUEST_KEY, statesRequest)
                    putParcelable(START_LAT_LNG_KEY, startLatLng)
                }
            }
        }
    }
}