package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TeamStats(
        var teamName: String?,
        var footballTeamLogo: String?,
        var win: Long?,
        var loss: Long?,
        var goal: Long?,
        var pass: Long?,
        var shot: Long?,
        var yellowCard: Long?,
        var redCard: Long?
) : Parcelable