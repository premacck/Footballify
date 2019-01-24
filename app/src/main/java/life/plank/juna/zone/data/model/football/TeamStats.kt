package life.plank.juna.zone.data.model.football

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TeamStats(
        var teamName: String?,
        var footballTeamLogo: String?,
        var win: Long = 0,
        var loss: Long = 0,
        var goal: Long = 0,
        var pass: Long = 0,
        var shot: Long = 0,
        var yellowCard: Long = 0,
        var redCard: Long = 0
) : Parcelable