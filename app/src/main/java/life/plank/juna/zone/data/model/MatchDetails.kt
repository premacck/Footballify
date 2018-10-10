package life.plank.juna.zone.data.model

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.util.*

@Entity
@Parcelize
data class MatchDetails(
        @PrimaryKey var id: Int?,
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
        var highlights: @RawValue List<Highlights>?,
        var matchEvents: @RawValue List<MatchEvent>?,
        var commentary: @RawValue List<Commentary>?,
        @SerializedName("commentaries") @Expose var isCommentaryAvailable: Boolean?,
        @Embedded(prefix = "matchStats_") var matchStats: MatchStats?,
        @Embedded(prefix = "lineups_") var lineups: @RawValue Lineups?,
        var standingsList: @RawValue List<Standings>?,
        var teamStatsList: @RawValue List<TeamStats>?,
        var scrubberDataList: @RawValue List<ScrubberData>?
) : Parcelable