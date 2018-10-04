package life.plank.juna.zone.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.util.*

@Parcelize
data class MatchDetails(
        var id: Int?,
        @SerializedName("foreignId") @Expose var matchId: Long,
        var homeTeam: @RawValue FootballTeam,
        var awayTeam: @RawValue FootballTeam,
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
        var league: League?,
        var venue: Stadium?,
        var highlights: @RawValue List<Highlights>?,
        var matchEvents: @RawValue List<MatchEvent>?,
        var commentary: @RawValue List<Commentary>?,
        @SerializedName("commentaries") @Expose var isCommentaryAvailable: Boolean?,
        var matchStats: MatchStats?,
        var lineups: @RawValue Lineups?,
        var standingsList: @RawValue List<Standings>?,
        var teamStatsList: @RawValue List<TeamStats>?,
        var scrubberDataList: @RawValue List<ScrubberData>?
) : Parcelable