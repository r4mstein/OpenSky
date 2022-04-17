package ua.shtain.opensky.core.network.api

import retrofit2.http.GET
import retrofit2.http.QueryMap
import ua.shtain.opensky.core.repositories.states.models.StatesResponse

interface StatesApi {

    @GET("states/all")
    suspend fun getStates(
        @QueryMap queries: Map<String, String>
    ): StatesResponse
}