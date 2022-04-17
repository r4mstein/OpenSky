package ua.shtain.opensky.core.repositories.states.models

import com.google.gson.annotations.SerializedName

data class StatesResponse(
    @SerializedName("time")
    val time: Long,
    @SerializedName("states")
    val states: List<List<String>>
)
