package life.plank.juna.zone.util.football

import life.plank.juna.zone.data.model.FixtureByDate
import life.plank.juna.zone.data.model.FixtureByMatchDay
import life.plank.juna.zone.data.model.MatchFixture
import life.plank.juna.zone.data.model.put
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.util.time.DateUtil.getDateDiff
import life.plank.juna.zone.view.fragment.football.LeagueInfoFragment.Companion.fixtureByMatchDayList
import java.util.*
// todo: this belongs in data //
fun MutableList<MatchFixture>.convertToFixtureByMatchDayList(): MutableList<FixtureByMatchDay> {
    fixtureByMatchDayList.clear()
    forEach {
        val now = Date().time
        val matchDate = it.matchStartTime.time
        if (!fixtureByMatchDayList.any { fixtureByMatchDay -> fixtureByMatchDay.matchDay == it.matchDay }) {
            val fixtureByMatchDay = FixtureByMatchDay(it.matchDay)
            if (fixtureByMatchDay.daySection != TODAY_MATCHES) {
                fixtureByMatchDay.daySection = when {
                    matchDate < now -> PAST_MATCHES
                    matchDate == now -> TODAY_MATCHES
                    else -> SCHEDULED_MATCHES
                }
            }
            val fixtureByDate = FixtureByDate().put(it.matchStartTime, it)
            fixtureByMatchDay.fixtureByDateList.add(fixtureByDate)
            fixtureByMatchDayList.add(fixtureByMatchDay)
        } else {
            val fixtureByMatchDay = fixtureByMatchDayList.find { fixtureByMatchDay -> fixtureByMatchDay.matchDay == it.matchDay }!!
            if (fixtureByMatchDay.daySection != TODAY_MATCHES) {
                fixtureByMatchDay.daySection = when {
                    matchDate < now -> PAST_MATCHES
                    matchDate == now -> TODAY_MATCHES
                    else -> SCHEDULED_MATCHES
                }
            }
            if (!fixtureByMatchDay.fixtureByDateList.any { fixtureByDate -> getDateDiff(fixtureByDate.date, it.matchStartTime) == 0 }) {
                val fixtureByDate = FixtureByDate().put(it.matchStartTime, it)
                fixtureByMatchDay.fixtureByDateList.add(fixtureByDate)
            } else {
                val fixtureByDate = fixtureByMatchDay.fixtureByDateList.find { fixtureByDate -> getDateDiff(fixtureByDate.date, it.matchStartTime) == 0 }!!
                fixtureByDate.fixtures.add(it)
            }
        }
    }
    fixtureByMatchDayList.sortWith(MatchDaySorter())
    return fixtureByMatchDayList
}

class MatchDaySorter : Comparator<FixtureByMatchDay> {
    override fun compare(o1: FixtureByMatchDay, o2: FixtureByMatchDay): Int = Integer.compare(o1.matchDay, o2.matchDay)
}
