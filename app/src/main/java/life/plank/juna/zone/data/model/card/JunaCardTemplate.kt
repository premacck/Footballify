package life.plank.juna.zone.data.model.card

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import life.plank.juna.zone.util.common.AppConstants.CardNotificationType
import java.util.*

@Parcelize
data class JunaCardTemplate(var cardColor: String, var datePublished: Date, @CardNotificationType var status: String, var issuer: CardUser) : Parcelable