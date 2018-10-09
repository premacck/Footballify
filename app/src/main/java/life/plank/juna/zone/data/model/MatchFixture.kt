package life.plank.juna.zone.data.model

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.util.*

@Entity
@Parcelize
data class MatchFixture(
        var id: Int?,
        @SerializedName("foreignId") @Expose var matchId: Long,
        @Embedded(prefix = "homeTeam_") var homeTeam: @RawValue FootballTeam,
        @Embedded(prefix = "awayTeam_") var awayTeam: @RawValue FootballTeam,
        var matchDay: Int?,
        var homeGoals: Int,
        var awayGoals: Int,
        var hometeamFormation: String?,
        var awayteamFormation: String?,
        var homeTeamPenaltyScore: Int,
        var awayTeamPenaltyScore: Int,
        var timeStatus: String?,
        var minute: Int?,
        var extraMinute: Int,
        var matchStartTime: @RawValue Date,
        @Embedded(prefix = "stadium_") var venue: Stadium?,
        @Embedded(prefix = "league_") var league: League?
) : Parcelable {
    companion object {
        fun from(matchDetails: MatchDetails): MatchFixture {
            return MatchFixture(
                    matchDetails.id,
                    matchDetails.matchId,
                    matchDetails.homeTeam,
                    matchDetails.awayTeam,
                    matchDetails.matchDay,
                    matchDetails.homeGoals,
                    matchDetails.awayGoals,
                    matchDetails.hometeamFormation,
                    matchDetails.awayteamFormation,
                    matchDetails.homeTeamPenaltyScore,
                    matchDetails.awayTeamPenaltyScore,
                    matchDetails.timeStatus,
                    matchDetails.minute,
                    matchDetails.extraMinute,
                    matchDetails.matchStartTime,
                    matchDetails.venue,
                    matchDetails.league
            )
        }
    }
}