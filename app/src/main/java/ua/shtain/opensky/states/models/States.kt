package ua.shtain.opensky.states.models

data class States(
    val time: Long,
    val states: List<State>,
)

data class State(
    val transponder: String,
    val callsign: String,
    val country: String,
    val longitude: Double?,
    val latitude: Double?,
)
