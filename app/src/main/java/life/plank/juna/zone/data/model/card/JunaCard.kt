package life.plank.juna.zone.data.model.card

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class JunaCard(var id: String, var dateAcquired: Date, var template: JunaCardTemplate, var owner: CardUser? = null) : Parcelable

fun MutableList<JunaCard>.getCardLimit() = when {
    size < 10 -> 10
    size < 100 -> 100
    size < 1000 -> 1000
    size < 10000 -> 10000
    size < 100000 -> 100000
    else -> 1000000
}