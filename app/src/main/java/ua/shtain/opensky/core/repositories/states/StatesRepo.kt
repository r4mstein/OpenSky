package ua.shtain.opensky.core.repositories.states

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import ua.shtain.opensky.core.network.api.StatesApi
import ua.shtain.opensky.core.network.http_client.HttpClientApi
import ua.shtain.opensky.core.network.network_manager.NetworkManagerApi
import ua.shtain.opensky.core.repositories.states.models.StatesRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatesRepo @Inject constructor(
    private val httpClient: HttpClientApi,
    private val networkManager: NetworkManagerApi,
) : StatesRepoApi {

    private val states = MutableStateFlow<StatesStatus>(StatesStatus.Initial)
    private val statesApi: StatesApi by lazy {
        httpClient.getApi(StatesApi::class.java)
    }

    override fun getStates(): StateFlow<StatesStatus> {
        return states.asStateFlow()
    }

    override suspend fun loadStates(request: StatesRequest) {
        withContext(Dispatchers.IO) {
            if (states.value is StatesStatus.Loading) {
                return@withContext
            }
            if (networkManager.isConnected().not()) {
                states.value = StatesStatus.NoInternet(request.loadStatesType)
                return@withContext
            }
            states.value = StatesStatus.Loading(request.loadStatesType)

            runCatching {
                statesApi.getStates(
                    mapOf(
                        "lamin" to "${request.lamin}",
                        "lomin" to "${request.lomin}",
                        "lamax" to "${request.lamax}",
                        "lomax" to "${request.lomax}",
                    )
                )
            }.onSuccess {
                states.value = StatesStatus.Success(
                    states = it,
                    loadStatesType = request.loadStatesType
                )
            }.onFailure {
                states.value = StatesStatus.Error(
                    message = it.message.orEmpty(),
                    loadStatesType = request.loadStatesType
                )
            }
        }
    }
}