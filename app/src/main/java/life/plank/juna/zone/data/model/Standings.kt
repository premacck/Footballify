package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Standings(
        var teamName: String,
        var position: Int,
        var footballTeamLogo: String,
        var matchesPlayed: Int,
        var wins: Int,
        var draws: Int,
        var losses: Int,
        var goalsFor: Int,
        var goalsAgainst: Int,
        var goalDifference: Int,
        var points: Int
) : Parcelable