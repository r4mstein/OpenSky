package ua.shtain.opensky.core.navigator

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import ua.shtain.opensky.core.extensions.addFragment
import ua.shtain.opensky.core.extensions.replaceFragmentAndAddToBackStack
import ua.shtain.opensky.core.repositories.states.models.StatesRequest
import ua.shtain.opensky.states.StatesFragment
import ua.shtain.opensky.states_map.StatesMapFragment
import javax.inject.Inject

class NavigatorImpl @Inject constructor() : NavigatorApi {

    override fun showStatesFragment(
        activity: AppCompatActivity,
        containerId: Int,
        statesRequest: StatesRequest?,
    ) {
        activity.addFragment(
            containerId = containerId,
            fragment = StatesFragment.newInstance(statesRequest)
        )
    }

    override fun showStatesMapFragment(
        activity: AppCompatActivity,
        containerId: Int,
        statesRequest: StatesRequest?,
        startLatLng: LatLng?,
    ) {
        activity.replaceFragmentAndAddToBackStack(
            containerId = containerId,
            fragment = StatesMapFragment.newInstance(statesRequest, startLatLng)
        )
    }
}