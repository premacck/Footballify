package life.plank.juna.zone.data.local.repository

import android.support.annotation.WorkerThread
import life.plank.juna.zone.data.local.dao.LeagueDao
import life.plank.juna.zone.data.local.model.LeagueInfo
import life.plank.juna.zone.data.model.FixtureByMatchDay
import life.plank.juna.zone.data.model.PlayerStats
import life.plank.juna.zone.data.model.Standings
import life.plank.juna.zone.data.model.TeamStats
import life.plank.juna.zone.data.viewmodel.LeagueViewModel
import org.jetbrains.anko.doAsync

/**
 * Repository class for interacting with the [LeagueDao] and updating the [LeagueViewModel]
 * This class also handles inserting, updating and deleting elements in the [LeagueInfo] table
 */
class LeagueRepository : BaseRepository() {

    var leagueDao: LeagueDao = roomDb.leagueDao()

    @WorkerThread
    fun getLeagueInfo(leagueId: Long): LeagueInfo {
        return leagueDao.getLeague(leagueId)
    }

    fun insertLeagueInfo(leagueInfo: LeagueInfo) {
        doAsync { leagueDao.insertLeagueInfo(leagueInfo) }
    }

    fun updateFixtures(fixtureByMatchDayList: List<FixtureByMatchDay>, leagueId: Long) {
        doAsync { leagueDao.updateFixtures(fixtureByMatchDayList, leagueId) }
    }

    fun updateStandings(standingsList: List<Standings>, leagueId: Long) {
        doAsync { leagueDao.updateStandings(standingsList, leagueId) }
    }

    fun updateTeamStats(teamStatsList: List<TeamStats>, leagueId: Long) {
        doAsync { leagueDao.updateTeamStats(teamStatsList, leagueId) }
    }

    fun updatePlayerStats(playerStatsList: List<PlayerStats>, leagueId: Long) {
        doAsync { leagueDao.updatePlayerStats(playerStatsList, leagueId) }
    }

    fun deleteLeagueInfo(leagueInfo: LeagueInfo) {
        doAsync { leagueDao.deleteLeagueInfo(leagueInfo) }
    }
}