package ua.shtain.opensky.states

import ua.shtain.opensky.states.models.States

sealed class ViewStatesStatus {

    object Initial : ViewStatesStatus()

    object Loading : ViewStatesStatus()

    object Updating : ViewStatesStatus()

    class LoadDataError(val message: Int) : ViewStatesStatus()

    class LoadDataSuccess(val data: States) : ViewStatesStatus()

    class UpdateDataError(val message: Int) : ViewStatesStatus()

    class UpdateDataSuccess(val data: States) : ViewStatesStatus()
}
