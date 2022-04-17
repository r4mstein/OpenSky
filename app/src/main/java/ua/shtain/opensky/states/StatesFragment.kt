package ua.shtain.opensky.states

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ua.shtain.opensky.R
import ua.shtain.opensky.core.base.BaseFragment
import ua.shtain.opensky.core.extensions.hideView
import ua.shtain.opensky.core.extensions.makeSnackbar
import ua.shtain.opensky.core.extensions.showView
import ua.shtain.opensky.core.navigator.NavigatorApi
import ua.shtain.opensky.core.repositories.states.models.StatesRequest
import ua.shtain.opensky.databinding.FrStatesBinding
import ua.shtain.opensky.states.adapters.StatesAdapter
import javax.inject.Inject

@AndroidEntryPoint
class StatesFragment : BaseFragment<FrStatesBinding>(FrStatesBinding::inflate) {

    private val viewModel: StatesViewModel by viewModels()

    @Inject
    lateinit var statesAdapter: StatesAdapter

    @Inject
    lateinit var navigator: NavigatorApi

    private var loadDataErrorSnackbar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        viewModel.collectStates()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.states.collect {
                renderState(state = it)
            }
        }
        loadData()
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadStates(arguments?.getParcelable(STATES_REQUEST_KEY))
            }
        }
    }

    private fun setupUI() {
        binding?.apply {
            statesToolbar.toolbar.title = getString(R.string.toolbar_list_of_states_label)
            tvUpdating.text = getString(R.string.updating_states_label)
            btnOpenMap.apply {
                text = getString(R.string.btn_open_map_label)
                setOnClickListener {
                    showStatesMapFragment()
                }
            }
            rvStates.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = statesAdapter
            }
        }
    }

    private fun showStatesMapFragment() {
        navigator.showStatesMapFragment(
            activity = requireActivity() as AppCompatActivity,
            containerId = R.id.fragmentContainer,
        )
    }

    private fun renderState(state: ViewStatesStatus) {
        when (state) {
            is ViewStatesStatus.LoadDataError -> {
                renderLoadDataError(state.message)
            }
            is ViewStatesStatus.LoadDataSuccess -> {
                renderLoadDataSuccessful(state)
            }
            is ViewStatesStatus.UpdateDataError -> {
                renderUpdateDataError(state.message)
            }
            is ViewStatesStatus.UpdateDataSuccess -> {
                renderUpdateDataSuccessful(state)
            }
            ViewStatesStatus.Loading -> {
                showLoader()
                disableWindow()
            }
            ViewStatesStatus.Updating -> {
                showUpdatingUI()
            }
            ViewStatesStatus.Initial -> {
                // do nothing
            }
        }
    }

    private fun renderLoadDataSuccessful(state: ViewStatesStatus.LoadDataSuccess) {
        statesAdapter.submitList(state.data.states)
        binding?.btnOpenMap?.showView()
        hideLoader()
        enableWindow()
    }

    private fun renderUpdateDataSuccessful(state: ViewStatesStatus.UpdateDataSuccess) {
        statesAdapter.submitList(state.data.states)
        binding?.btnOpenMap?.showView()
        hideUpdatingUI()
        loadDataErrorSnackbar?.let {
            it.dismiss()
            loadDataErrorSnackbar = null
        }
    }

    private fun renderLoadDataError(error: Int) {
        hideLoader()
        enableWindow()
        binding?.btnOpenMap?.hideView()
        showLoadDataError(error)
    }

    private fun renderUpdateDataError(error: Int) {
        hideUpdatingUI()
        if (loadDataErrorSnackbar == null) {
            showUpdateDataError(error)
        }
    }

    private fun showLoader() {
        binding?.loader?.flLoaderContainer?.showView()
    }

    private fun hideLoader() {
        binding?.loader?.flLoaderContainer?.hideView()
    }

    private fun disableWindow() {
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    private fun enableWindow() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun showLoadDataError(error: Int) {
        loadDataErrorSnackbar = requireContext().makeSnackbar(
            message = error,
            view = requireView(),
            length = Snackbar.LENGTH_INDEFINITE,
            actionText = R.string.btn_reload_label,
            action = {
                viewModel.reloadStatesClicked(arguments?.getParcelable(STATES_REQUEST_KEY))
            }
        ).also {
            it.show()
        }
    }

    private fun showUpdateDataError(error: Int) {
        requireContext().makeSnackbar(
            message = error,
            view = requireView(),
        ).show()
    }

    private fun showUpdatingUI() {
        binding?.apply {
            ConstraintSet().apply {
                clone(clRoot)

                clear(tvUpdating.id, ConstraintSet.BOTTOM)
                connect(
                    tvUpdating.id,
                    ConstraintSet.TOP,
                    statesToolbar.toolbar.id,
                    ConstraintSet.BOTTOM
                )

                TransitionManager.beginDelayedTransition(clRoot)
                applyTo(clRoot)
            }
        }
    }

    private fun hideUpdatingUI() {
        binding?.apply {
            ConstraintSet().apply {
                clone(clRoot)

                clear(tvUpdating.id, ConstraintSet.TOP)
                connect(
                    tvUpdating.id,
                    ConstraintSet.BOTTOM,
                    statesToolbar.toolbar.id,
                    ConstraintSet.BOTTOM
                )

                TransitionManager.beginDelayedTransition(clRoot)
                applyTo(clRoot)
            }
        }
    }

    companion object {

        private const val STATES_REQUEST_KEY = "states_request_key"

        fun newInstance(statesRequest: StatesRequest?): StatesFragment {
            return StatesFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(STATES_REQUEST_KEY, statesRequest)
                }
            }
        }
    }
}