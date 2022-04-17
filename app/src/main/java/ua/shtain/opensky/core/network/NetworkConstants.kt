package ua.shtain.opensky.core.network

import ua.shtain.opensky.core.repositories.states.models.StatesRequest

object NetworkConstants {

    const val BASE_URL = "https://opensky-network.org/api/"

    const val UPDATE_STATES_DELAY = 10_000L

    val czechRepublicStatesRequest = StatesRequest(
        lamin = 48.55f,
        lomin = 12.9f,
        lamax = 51.06f,
        lomax = 18.87f,
    )

}