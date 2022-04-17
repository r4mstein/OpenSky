package ua.shtain.opensky.core.model_converters

import ua.shtain.opensky.core.repositories.states.models.StatesResponse
import ua.shtain.opensky.states.models.State
import ua.shtain.opensky.states.models.States
import javax.inject.Inject

class StatesConverter @Inject constructor() : StatesConverterApi {

    override fun convert(states: StatesResponse): States {
        return States(
            time = states.time,
            states = states.states.map {
                State(
                    transponder = it.getOrNull(0).orEmpty().trim(),
                    callsign = it.getOrNull(1).orEmpty().trim(),
                    country = it.getOrNull(2).orEmpty().trim(),
                    longitude = it.getOrNull(5)?.toDoubleOrNull(),
                    latitude = it.getOrNull(6)?.toDoubleOrNull(),
                )
            }
        )
    }
}