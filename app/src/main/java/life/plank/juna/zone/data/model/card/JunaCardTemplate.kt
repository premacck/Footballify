package life.plank.juna.zone.data.model.card

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import life.plank.juna.zone.util.common.AppConstants.CardNotificationType
import life.plank.juna.zone.util.common.AppConstants.CardNotificationType.READY_TO_CREATE
import java.util.*

@Parcelize
data class JunaCardTemplate(
        var id: String,
        var cardColor: String = "GOLD",
        var datePublished: Date = Date(),
        @CardNotificationType var status: String = READY_TO_CREATE,
        var issuer: CardUser? = null,
        var layout: String = "34"
) : Parcelable {

    class Builder {

        private val template: StringBuilder = StringBuilder("{")

        fun color(color: String): Builder = apply { template.append("\"cardColor\":\"$color\", ") }

        fun layout(layout: String): Builder = apply { template.append("\"layout\":\"$layout\"") }

        fun build() = template.append("}").toString()
    }
}