package ua.shtain.opensky.states

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ua.shtain.opensky.R
import ua.shtain.opensky.core.model_converters.StatesConverterApi
import ua.shtain.opensky.core.network.NetworkConstants.UPDATE_STATES_DELAY
import ua.shtain.opensky.core.network.NetworkConstants.czechRepublicStatesRequest
import ua.shtain.opensky.core.repositories.states.StatesRepoApi
import ua.shtain.opensky.core.repositories.states.StatesStatus
import ua.shtain.opensky.core.repositories.states.models.LoadStatesType
import ua.shtain.opensky.core.repositories.states.models.StatesRequest
import javax.inject.Inject

@HiltViewModel
class StatesViewModel @Inject constructor(
    private val repo: StatesRepoApi,
    private val statesConverter: StatesConverterApi,
) : ViewModel() {

    private val _states = MutableStateFlow<ViewStatesStatus>(ViewStatesStatus.Initial)
    val states = _states.asStateFlow()

    suspend fun loadStates(statesRequest: StatesRequest? = null) {
        repo.loadStates(
            when (_states.value) {
                ViewStatesStatus.Initial -> {
                    statesRequest ?: czechRepublicStatesRequest
                }
                else -> {
                    (statesRequest ?: czechRepublicStatesRequest)
                        .copy(loadStatesType = LoadStatesType.UPDATE)
                }
            }
        )
        delay(UPDATE_STATES_DELAY)
        loadStates(statesRequest)
    }

    fun collectStates() {
        viewModelScope.launch {
            repo.getStates().collect { statesStatus ->
                when (statesStatus) {
                    is StatesStatus.Success -> {
                        dataLoadedSuccessfully(statesStatus)
                    }
                    is StatesStatus.Error -> {
                        failedLoadData(statesStatus.loadStatesType)
                    }
                    StatesStatus.Initial -> {
                        _states.value = ViewStatesStatus.Initial
                    }
                    is StatesStatus.Loading -> {
                        _states.value = when (statesStatus.loadStatesType) {
                            LoadStatesType.LOAD -> ViewStatesStatus.Loading
                            LoadStatesType.UPDATE -> ViewStatesStatus.Updating
                        }
                    }
                    is StatesStatus.NoInternet -> {
                        noInternetAction(statesStatus.loadStatesType)
                    }
                }
            }
        }
    }

    private fun noInternetAction(stateType: LoadStatesType) {
        when (stateType) {
            LoadStatesType.LOAD -> {
                _states.value = ViewStatesStatus.LoadDataError(
                    message = R.string.error_no_internet
                )
            }
            LoadStatesType.UPDATE -> {
                _states.value = ViewStatesStatus.UpdateDataError(
                    message = R.string.error_no_internet_update_states
                )
            }
        }
    }

    private fun failedLoadData(stateType: LoadStatesType) {
        when (stateType) {
            LoadStatesType.LOAD -> {
                _states.value = ViewStatesStatus.LoadDataError(
                    message = R.string.error_load_states
                )
            }
            LoadStatesType.UPDATE -> {
                _states.value = ViewStatesStatus.UpdateDataError(
                    message = R.string.error_update_states
                )
            }
        }
    }

    private fun dataLoadedSuccessfully(status: StatesStatus.Success) {
        when (status.loadStatesType) {
            LoadStatesType.LOAD -> {
                _states.value = ViewStatesStatus.LoadDataSuccess(
                    data = statesConverter.convert(status.states)
                )
            }
            LoadStatesType.UPDATE -> {
                viewModelScope.launch {
                    delay(1_000)
                    _states.value = ViewStatesStatus.UpdateDataSuccess(
                        data = statesConverter.convert(status.states)
                    )
                }
            }
        }
    }

    fun reloadStatesClicked(statesRequest: StatesRequest? = null) {
        viewModelScope.launch {
            repo.loadStates(request = statesRequest ?: czechRepublicStatesRequest)
        }
    }
}