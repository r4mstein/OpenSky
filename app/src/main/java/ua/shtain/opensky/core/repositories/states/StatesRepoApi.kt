package ua.shtain.opensky.core.repositories.states

import kotlinx.coroutines.flow.StateFlow
import ua.shtain.opensky.core.repositories.states.models.StatesRequest

interface StatesRepoApi {

    fun getStates(): StateFlow<StatesStatus>

    suspend fun loadStates(request: StatesRequest)
}