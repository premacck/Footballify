package life.plank.juna.zone.data.model.football

import android.os.Parcelable
import androidx.room.*
import com.google.gson.annotations.*
import kotlinx.android.parcel.*
import life.plank.juna.zone.util.common.AppConstants
import life.plank.juna.zone.util.time.DateUtil
import life.plank.juna.zone.view.football.fragment.LeagueInfoFragment
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

fun List<MatchFixture>.convertToFixtureByMatchDayList(): MutableList<FixtureByMatchDay> {
    LeagueInfoFragment.fixtureByMatchDayList.clear()
    forEach {
        val now = Date().time
        val matchDate = it.matchStartTime.time
        if (!LeagueInfoFragment.fixtureByMatchDayList.any { fixtureByMatchDay -> fixtureByMatchDay.matchDay == it.matchDay }) {
            val fixtureByMatchDay = FixtureByMatchDay(it.matchDay)
            if (fixtureByMatchDay.daySection != AppConstants.TODAY_MATCHES) {
                fixtureByMatchDay.daySection = when {
                    matchDate < now -> AppConstants.PAST_MATCHES
                    matchDate == now -> AppConstants.TODAY_MATCHES
                    else -> AppConstants.SCHEDULED_MATCHES
                }
            }
            val fixtureByDate = FixtureByDate().put(it.matchStartTime, it)
            fixtureByMatchDay.fixtureByDateList.add(fixtureByDate)
            LeagueInfoFragment.fixtureByMatchDayList.add(fixtureByMatchDay)
        } else {
            val fixtureByMatchDay = LeagueInfoFragment.fixtureByMatchDayList.find { fixtureByMatchDay -> fixtureByMatchDay.matchDay == it.matchDay }!!
            if (fixtureByMatchDay.daySection != AppConstants.TODAY_MATCHES) {
                fixtureByMatchDay.daySection = when {
                    matchDate < now -> AppConstants.PAST_MATCHES
                    matchDate == now -> AppConstants.TODAY_MATCHES
                    else -> AppConstants.SCHEDULED_MATCHES
                }
            }
            if (!fixtureByMatchDay.fixtureByDateList.any { fixtureByDate -> DateUtil.getDateDiff(fixtureByDate.date, it.matchStartTime) == 0 }) {
                val fixtureByDate = FixtureByDate().put(it.matchStartTime, it)
                fixtureByMatchDay.fixtureByDateList.add(fixtureByDate)
            } else {
                val fixtureByDate = fixtureByMatchDay.fixtureByDateList.find { fixtureByDate -> DateUtil.getDateDiff(fixtureByDate.date, it.matchStartTime) == 0 }!!
                fixtureByDate.fixtures.add(it)
            }
        }
    }
    LeagueInfoFragment.fixtureByMatchDayList.sortWith(MatchDaySorter())
    return LeagueInfoFragment.fixtureByMatchDayList
}

class MatchDaySorter : Comparator<FixtureByMatchDay> {
    override fun compare(o1: FixtureByMatchDay, o2: FixtureByMatchDay): Int = Integer.compare(o1.matchDay, o2.matchDay)
}