package life.plank.juna.zone.data.model.football

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Standings(
        var teamName: String = "",
        var position: Int = 0,
        var footballTeamLogo: String = "",
        var matchesPlayed: Int = 0,
        var wins: Int = 0,
        var draws: Int = 0,
        var losses: Int = 0,
        var goalsFor: Int = 0,
        var goalsAgainst: Int = 0,
        var goalDifference: Int = 0,
        var points: Int = 0
) : Parcelable