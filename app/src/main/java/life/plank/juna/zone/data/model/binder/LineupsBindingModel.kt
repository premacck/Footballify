package life.plank.juna.zone.data.model.binder

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.android.parcel.Parcelize

import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.FootballTeam
import life.plank.juna.zone.data.model.Lineups
import life.plank.juna.zone.data.model.MatchDetails
import life.plank.juna.zone.data.model.MatchEvent

@Parcelize
data class LineupsBindingModel(
        var lineups: Lineups? = null,
        var homeTeam: FootballTeam? = null,
        var awayTeam: FootballTeam? = null,
        var matchEventList: List<MatchEvent>? = null,
        var homeFormation: String? = null,
        var awayFormation: String? = null,
        @field:StringRes var errorMessage: Int? = null
) : Parcelable {
    companion object {
        fun from(matchDetails: MatchDetails): LineupsBindingModel {
            return LineupsBindingModel(
                    matchDetails.lineups,
                    matchDetails.homeTeam,
                    matchDetails.awayTeam,
                    matchDetails.matchEvents,
                    matchDetails.hometeamFormation,
                    matchDetails.awayteamFormation,
                    if (matchDetails.lineups == null) R.string.line_ups_not_available else null
            )
        }
    }
}
