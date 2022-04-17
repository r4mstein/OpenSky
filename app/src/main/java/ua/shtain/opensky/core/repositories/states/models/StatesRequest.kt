package ua.shtain.opensky.core.repositories.states.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ua.shtain.opensky.core.repositories.states.models.LoadStatesType.LOAD

@Parcelize
data class StatesRequest(
    val lamin: Float,
    val lomin: Float,
    val lamax: Float,
    val lomax: Float,
    val loadStatesType: LoadStatesType = LOAD
) : Parcelable

sealed class LoadStatesType : Parcelable {

    @Parcelize
    object LOAD : LoadStatesType()

    @Parcelize
    object UPDATE : LoadStatesType()
}
