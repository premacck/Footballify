package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import life.plank.juna.zone.util.common.AppConstants.WHISTLE_EVENT

@Parcelize
data class MatchEvent(
        var isHomeTeam: Boolean,
        var eventType: String,
        var playerName: String = "",
        var relatedPlayerName: String? = "",
        var minute: Int = 0,
        var extraMinute: Int = 0,
        var injured: Boolean? = false,
        var reason: String? = "",
        var result: String? = "",
        var liveTimeStatus: LiveTimeStatus?
) : Parcelable {
    constructor(isHomeTeam: Boolean, eventType: String) : this(isHomeTeam, eventType, "", "", 0, 0, false, "", "", null)

    constructor(liveTimeStatus: LiveTimeStatus) : this(false, WHISTLE_EVENT, "", "", liveTimeStatus.minute, liveTimeStatus.extraMinute, false, "", "", liveTimeStatus)
}