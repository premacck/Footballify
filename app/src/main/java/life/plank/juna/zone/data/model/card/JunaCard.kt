package life.plank.juna.zone.data.model.card

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class JunaCard(var id: String, var dateAcquired: Date, var template: JunaCardTemplate, var owner: CardUser) : Parcelable