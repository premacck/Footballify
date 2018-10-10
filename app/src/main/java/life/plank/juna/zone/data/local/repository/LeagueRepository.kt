package life.plank.juna.zone.data.local.repository

import android.arch.lifecycle.LiveData
import life.plank.juna.zone.data.local.dao.LeagueDao
import life.plank.juna.zone.data.local.model.LeagueInfo
import life.plank.juna.zone.data.model.PlayerStats
import life.plank.juna.zone.data.model.Standings
import life.plank.juna.zone.data.model.TeamStats
import life.plank.juna.zone.data.viewmodel.LeagueViewModel
import org.jetbrains.anko.doAsync

/**
 * Repository class for interacting with the [LeagueDao] and updating the [LeagueViewModel]
 * This class also handles inserting, updating and deleting elements in the [LeagueInfo] table
 */
class LeagueRepository(leagueId: Long) : BaseRepository() {

    var leagueDao: LeagueDao = roomDb.leagueDao()
    var leagueInfoLiveData: LiveData<LeagueInfo>

    init {
        leagueInfoLiveData = leagueDao.getLeague(leagueId)
    }

    fun getLeagueInfo(leagueId: Long) : LiveData<LeagueInfo> {
        return leagueDao.getLeague(leagueId)
    }

    fun getStandings(leagueId: Long) : LiveData<List<Standings>> {
        return leagueDao.getStandings(leagueId)
    }

    fun getTeamStats(leagueId: Long) : LiveData<List<TeamStats>> {
        return leagueDao.getTeamStats(leagueId)
    }

    fun getPlayerStats(leagueId: Long) : LiveData<List<PlayerStats>> {
        return leagueDao.getPlayerStats(leagueId)
    }

    fun insertLeagueInfo(leagueInfo: LeagueInfo) {
        doAsync { leagueDao.insertLeagueInfo(leagueInfo) }
    }

    fun deleteLeagueInfo(leagueInfo: LeagueInfo) {
        doAsync { leagueDao.deleteLeagueInfo(leagueInfo) }
    }
}