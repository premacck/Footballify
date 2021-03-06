package life.plank.juna.zone.data.model.football

import android.os.Parcelable
import androidx.room.*
import com.google.gson.annotations.*
import kotlinx.android.parcel.*
import java.util.*

@Entity
@Parcelize
data class MatchDetails(
        @PrimaryKey var id: Int,
        @SerializedName("foreignId") @Expose var matchId: Long,
        @Embedded(prefix = "homeTeam_") var homeTeam: @RawValue FootballTeam,
        @Embedded(prefix = "awayTeam_") var awayTeam: @RawValue FootballTeam,
        var matchDay: Int?,
        var homeGoals: Int,
        var awayGoals: Int,
        var hometeamFormation: String?,
        var awayteamFormation: String?,
        var homeTeamScore: Int?,
        var awayTeamScore: Int?,
        var homeTeamPenaltyScore: Int,
        var awayTeamPenaltyScore: Int,
        var halfTimeScore: String?,
        var fullTimeScore: String?,
        var timeStatus: String?,
        var minute: Int?,
        var extraMinute: Int,
        var injuryTime: Int?,
        var matchStartTime: @RawValue Date,
        @Embedded(prefix = "league_") var league: League?,
        @Embedded(prefix = "stadium_") var venue: Stadium?,
        var highlights: @RawValue MutableList<Highlights>?,
        var matchEvents: @RawValue MutableList<MatchEvent>?,
        var commentary: @RawValue MutableList<Commentary>?,
        @SerializedName("commentaries") @Expose var isCommentaryAvailable: Boolean?,
        @Embedded(prefix = "matchStats_") var matchStats: MatchStats?,
        @Embedded(prefix = "lineups_") var lineups: @RawValue Lineups?,
        var standingsList: @RawValue MutableList<Standings>?,
        var teamStatsList: @RawValue MutableList<TeamStats>?,
        var scrubberDataList: @RawValue MutableList<ScrubberData>?
) : Parcelable {
    @Ignore
    constructor(
            id: Int, matchId: Long, homeTeam: FootballTeam, awayTeam: FootballTeam, matchDay: Int?,
            homeGoals: Int, awayGoals: Int, hometeamFormation: String?, awayteamFormation: String?,
            homeTeamPenaltyScore: Int, awayTeamPenaltyScore: Int, timeStatus: String?, minute: Int?,
            extraMinute: Int, matchStartTime: Date, venue: Stadium?, league: League?
    ) : this(
            id, matchId, homeTeam, awayTeam, matchDay, homeGoals, awayGoals, hometeamFormation, awayteamFormation, 0, 0,
            homeTeamPenaltyScore, awayTeamPenaltyScore, "0-0", "0-0", timeStatus, minute, extraMinute, 0, matchStartTime,
            league, venue, null, null, null, true, null, null, null, null, null
    )
}