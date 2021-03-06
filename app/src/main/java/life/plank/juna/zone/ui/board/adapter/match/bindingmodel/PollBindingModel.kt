package life.plank.juna.zone.ui.board.adapter.match.bindingmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import life.plank.juna.zone.data.model.board.poll.Poll
import life.plank.juna.zone.data.model.football.MatchDetails
import java.util.*

@Parcelize
data class PollBindingModel(
        var poll: Poll,
        var homeTeamName: String,
        var awayTeamName: String,
        var homeTeamLogo: String,
        var awayTeamLogo: String,
        var leagueLogo: Int,
        var matchStartTime: Date,
        var background: String? = null,
        var timeStatus: String?
) : Parcelable {
    companion object {
        fun from(poll: Poll, matchDetails: MatchDetails): PollBindingModel {
            return PollBindingModel(
                    poll,
                    matchDetails.homeTeam.name!!,
                    matchDetails.awayTeam.name!!,
                    matchDetails.homeTeam.logoLink,
                    matchDetails.awayTeam.logoLink,
                    matchDetails.league?.leagueLogo!!,
                    matchDetails.matchStartTime,
                    matchDetails.venue?.imagePath,
                    matchDetails.timeStatus
            )
        }
    }
}