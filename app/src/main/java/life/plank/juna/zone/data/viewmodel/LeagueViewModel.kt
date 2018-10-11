package life.plank.juna.zone.data.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.persistence.room.RoomDatabase
import life.plank.juna.zone.data.local.model.LeagueInfo
import life.plank.juna.zone.data.local.repository.LeagueRepository
import life.plank.juna.zone.data.model.FixtureByMatchDay
import life.plank.juna.zone.data.model.PlayerStats
import life.plank.juna.zone.data.model.Standings
import life.plank.juna.zone.data.model.TeamStats

/**
 * [ViewModel] class for getting data from the [RoomDatabase] and API calls, and updating the [LiveData] that the UI will be observing.
 */
class LeagueViewModel(leagueId: Long) : ViewModel() {

    private val leagueRepository: LeagueRepository = LeagueRepository(leagueId)
    private val leagueInfoLiveData: LiveData<LeagueInfo>

    init {
        leagueInfoLiveData = leagueRepository.leagueInfoLiveData
    }

    fun getLeagueInfo(leagueId: Long): LiveData<LeagueInfo> {
        return leagueRepository.getLeagueInfo(leagueId)
    }

    fun getStandings(leagueId: Long): LiveData<List<Standings>> {
        return leagueRepository.getStandings(leagueId)
    }

    fun getTeamStats(leagueId: Long): LiveData<List<TeamStats>> {
        return leagueRepository.getTeamStats(leagueId)
    }

    fun getPlayerStats(leagueId: Long): LiveData<List<PlayerStats>> {
        return leagueRepository.getPlayerStats(leagueId)
    }

    fun insertLeagueInfo(leagueInfo: LeagueInfo) {
        leagueRepository.insertLeagueInfo(leagueInfo)
    }

    fun updateFixtures(fixtureByMatchDayList: List<FixtureByMatchDay>, leagueId: Long) {
        leagueRepository.updateFixtures(fixtureByMatchDayList, leagueId)
    }

    fun updateStandings(standingsList: List<Standings>, leagueId: Long) {
        leagueRepository.updateStandings(standingsList, leagueId)
    }

    fun updateTeamStats(teamStatsList: List<TeamStats>, leagueId: Long) {
        leagueRepository.updateTeamStats(teamStatsList, leagueId)
    }

    fun updatePlayerStats(playerStatsList: List<PlayerStats>, leagueId: Long) {
        leagueRepository.updatePlayerStats(playerStatsList, leagueId)
    }

    fun deleteLeagueInfo(leagueInfo: LeagueInfo) {
        leagueRepository.deleteLeagueInfo(leagueInfo)
    }
}