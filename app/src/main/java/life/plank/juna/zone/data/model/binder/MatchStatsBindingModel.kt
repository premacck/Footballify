package life.plank.juna.zone.data.model.binder

import android.os.Parcelable
import android.support.annotation.StringRes
import kotlinx.android.parcel.Parcelize

import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.FootballTeam
import life.plank.juna.zone.data.model.MatchDetails
import life.plank.juna.zone.data.model.MatchStats
import life.plank.juna.zone.data.model.Stadium
import life.plank.juna.zone.view.adapter.multiview.binder.MatchStatsBinder

@Parcelize
data class MatchStatsBindingModel(
        var matchStats: MatchStats? = null,
        var venue: Stadium? = null,
        var homeTeam: FootballTeam? = null,
        var awayTeam: FootballTeam? = null,
        @field:StringRes var errorMessage: Int? = null
) : Parcelable {
    companion object {
        fun from(matchDetails: MatchDetails): MatchStatsBindingModel {
            return MatchStatsBindingModel(
                    matchDetails.matchStats,
                    matchDetails.venue,
                    matchDetails.homeTeam,
                    matchDetails.awayTeam,
                    if (matchDetails.matchStats == null) R.string.match_stats_not_available_yet else null
            )
        }
    }
}
