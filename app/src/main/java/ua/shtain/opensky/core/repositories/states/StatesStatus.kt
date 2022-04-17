package ua.shtain.opensky.core.repositories.states

import ua.shtain.opensky.core.repositories.states.models.LoadStatesType
import ua.shtain.opensky.core.repositories.states.models.StatesResponse

sealed class StatesStatus {

    object Initial : StatesStatus()

    class Loading(val loadStatesType: LoadStatesType) : StatesStatus()

    class NoInternet(val loadStatesType: LoadStatesType) : StatesStatus()

    class Error(
        val message: String,
        val loadStatesType: LoadStatesType,
    ) : StatesStatus()

    class Success(
        val states: StatesResponse,
        val loadStatesType: LoadStatesType,
    ) : StatesStatus()
}
