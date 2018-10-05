package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LiveScoreData(
        var timeStatus: String = "",
        var minute: Int = 0,
        var extraMinute: Int = 0,
        var homeGoals: Int = 0,
        var awayGoals: Int = 0,
        var homeTeamPenaltyScore: Int = 0,
        var awayTeamPenaltyScore: Int = 0,
        var halfTimeScore: String = "",
        var fullTimeScore: String = "",
        var extraTimeScore: String = ""
) : Parcelable