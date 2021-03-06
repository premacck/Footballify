package life.plank.juna.zone.ui.board.adapter.match.bindingmodel

import android.os.Parcelable
import androidx.annotation.StringRes
import com.prembros.facilis.util.isNullOrEmpty
import kotlinx.android.parcel.Parcelize
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.football.*

@Parcelize
data class TeamStatsBindingModel(
        var teamStatsList: List<TeamStats>? = null,
        var league: League? = null,
        var homeTeam: FootballTeam? = null,
        var awayTeam: FootballTeam? = null,
        @field:StringRes var errorMessage: Int? = null
) : Parcelable {
    companion object {
        fun from(matchDetails: MatchDetails): TeamStatsBindingModel {
            return TeamStatsBindingModel(
                    matchDetails.teamStatsList,
                    matchDetails.league,
                    matchDetails.homeTeam,
                    matchDetails.awayTeam,
                    if (isNullOrEmpty(matchDetails.teamStatsList)) R.string.team_stats_not_available_yet else null
            )
        }
    }
}
