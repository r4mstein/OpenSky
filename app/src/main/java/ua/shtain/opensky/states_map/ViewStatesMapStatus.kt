package ua.shtain.opensky.states_map

import ua.shtain.opensky.states.models.States

sealed class ViewStatesMapStatus {

    object Initial : ViewStatesMapStatus()

    class Error(val message: Int) : ViewStatesMapStatus()

    class Success(val data: States) : ViewStatesMapStatus()
}
