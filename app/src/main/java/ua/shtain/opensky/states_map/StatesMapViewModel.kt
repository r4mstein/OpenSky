package ua.shtain.opensky.states_map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ua.shtain.opensky.R
import ua.shtain.opensky.core.model_converters.StatesConverterApi
import ua.shtain.opensky.core.network.NetworkConstants
import ua.shtain.opensky.core.network.NetworkConstants.UPDATE_STATES_DELAY
import ua.shtain.opensky.core.repositories.states.StatesRepoApi
import ua.shtain.opensky.core.repositories.states.StatesStatus
import ua.shtain.opensky.core.repositories.states.models.LoadStatesType
import ua.shtain.opensky.core.repositories.states.models.StatesRequest
import ua.shtain.opensky.core.repositories.states.models.StatesResponse
import javax.inject.Inject

@HiltViewModel
class StatesMapViewModel @Inject constructor(
    private val repo: StatesRepoApi,
    private val statesConverter: StatesConverterApi,
) : ViewModel() {

    private val _states = MutableStateFlow<ViewStatesMapStatus>(ViewStatesMapStatus.Initial)
    val states = _states.asStateFlow()

    suspend fun updateStatesWithDelay(statesRequest: StatesRequest?) {
        delay(UPDATE_STATES_DELAY)
        repo.loadStates(
            request = (statesRequest ?: NetworkConstants.czechRepublicStatesRequest)
                .copy(loadStatesType = LoadStatesType.UPDATE)
        )
        updateStatesWithDelay(statesRequest)
    }

    fun collectStates() {
        viewModelScope.launch {
            repo.getStates().collect { statesStatus ->
                when (statesStatus) {
                    is StatesStatus.Success -> {
                        dataLoadedSuccessfully(statesStatus.states)
                    }
                    is StatesStatus.Error -> {
                        failedLoadData()
                    }
                    StatesStatus.Initial -> {
                        _states.value = ViewStatesMapStatus.Initial
                    }
                    is StatesStatus.Loading -> {
                        // do nothing
                    }
                    is StatesStatus.NoInternet -> {
                        noInternetAction()
                    }
                }
            }
        }
    }

    private fun noInternetAction() {
        _states.value = ViewStatesMapStatus.Error(
            message = R.string.error_no_internet_update_states
        )
    }

    private fun failedLoadData() {
        _states.value = ViewStatesMapStatus.Error(
            message = R.string.error_update_states
        )
    }

    private fun dataLoadedSuccessfully(statesResponse: StatesResponse) {
        _states.value = ViewStatesMapStatus.Success(
            data = statesConverter.convert(statesResponse)
        )
    }
}