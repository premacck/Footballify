package life.plank.juna.zone.data.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.util.*

@Entity
@Parcelize
data class MatchFixture(
        var id: Int,
        @SerializedName("foreignId") @Expose var matchId: Long,
        @Embedded(prefix = "homeTeam_") var homeTeam: @RawValue FootballTeam,
        @Embedded(prefix = "awayTeam_") var awayTeam: @RawValue FootballTeam,
        var matchDay: Int = 0,
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
    fun toMatchDetails(): MatchDetails {
        return MatchDetails(
                id,
                matchId,
                homeTeam,
                awayTeam,
                matchDay,
                homeGoals,
                awayGoals,
                hometeamFormation,
                awayteamFormation,
                homeTeamPenaltyScore,
                awayTeamPenaltyScore,
                timeStatus,
                minute,
                extraMinute,
                matchStartTime,
                venue,
                league
        )
    }
}