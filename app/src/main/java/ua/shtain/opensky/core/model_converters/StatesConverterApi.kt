package ua.shtain.opensky.core.model_converters

import ua.shtain.opensky.core.repositories.states.models.StatesResponse
import ua.shtain.opensky.states.models.States

interface StatesConverterApi {

    fun convert(states: StatesResponse): States
}