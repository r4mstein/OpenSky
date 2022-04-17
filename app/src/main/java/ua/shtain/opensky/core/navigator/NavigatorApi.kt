package ua.shtain.opensky.core.navigator

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import ua.shtain.opensky.core.repositories.states.models.StatesRequest

interface NavigatorApi {

    fun showStatesFragment(
        activity: AppCompatActivity,
        containerId: Int,
        statesRequest: StatesRequest? = null,
    )

    fun showStatesMapFragment(
        activity: AppCompatActivity,
        containerId: Int,
        statesRequest: StatesRequest? = null,
        startLatLng: LatLng? = null
    )
}