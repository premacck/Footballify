package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ScrubberData(
        var millisecondsX: Long,
        var interactionY: Long,
        var eventType: String,
        var isHomeTeam: Boolean
) : Parcelable {
    @IgnoredOnParcel
    var event: MatchEvent = MatchEvent(this.isHomeTeam, this.eventType)

    constructor(millisecondsX: Long, interactionY: Long, event: MatchEvent) : this(millisecondsX, interactionY, event.eventType, event.isHomeTeam) {
        this.event = event
    }
}