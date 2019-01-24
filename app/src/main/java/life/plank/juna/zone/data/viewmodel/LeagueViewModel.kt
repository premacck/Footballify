package life.plank.juna.zone.data.viewmodel

import android.util.Log
import androidx.lifecycle.*
import androidx.room.RoomDatabase
import life.plank.juna.zone.data.api.*
import life.plank.juna.zone.data.local.model.LeagueInfo
import life.plank.juna.zone.data.local.repository.LeagueRepository
import life.plank.juna.zone.data.model.football.*
import org.jetbrains.anko.*

/**
 * [ViewModel] class for getting data from the [RoomDatabase] and API calls, and updating the [LiveData] that the UI will be observing.
 */
class LeagueViewModel : ViewModel() {

    companion object {
        private val TAG = LeagueViewModel::class.java.simpleName
    }

    val leagueRepository: LeagueRepository = LeagueRepository()
    val leagueInfoLiveData: MutableLiveData<LeagueInfo> = MutableLiveData()
    val fixtureLiveData: MutableLiveData<List<FixtureByMatchDay>> = MutableLiveData()

    /**
     * Function to update fixtureLiveData after getting the response from the API
     * The calling view should start observing fixtureLiveData before calling this method
     */
    fun getFixturesFromRestApi(league: League, restApi: RestApi) {
        restApi.getFixtures(league.seasonName!!, league.name, league.countryName!!).setObserverThreadsAndSmartSubscribe({ Log.e(TAG, it.message, it) }, {
            it.body()?.run { fixtureLiveData.value = convertToFixtureByMatchDayList() }
        })
    }

    /**
     * Function to update fixtureLiveData after getting the data from [RoomDatabase]
     * The calling view should start observing fixtureLiveData before calling this method
     */
    fun getFixtureListFromDb(leagueId: Long) {
        doAsync {
            val localFixtureList = leagueRepository.getLeagueInfo(leagueId)?.fixtureByMatchDayList
            uiThread {
                fixtureLiveData.value = localFixtureList
            }
        }
    }

    /**
     * Function to update leagueInfoLiveData after getting the data from [RoomDatabase]
     * The calling view should start observing leagueInfoLiveData before calling this method
     */
    fun getLeagueInfoFromDb(leagueId: Long) {
        doAsync {
            val leagueInfo = leagueRepository.getLeagueInfo(leagueId)
            uiThread {
                leagueInfoLiveData.value = leagueInfo
            }
        }
    }

    fun updateFixtures(leagueId: Long, fixtureList: List<FixtureByMatchDay>) = leagueRepository.updateFixtures(fixtureList, leagueId)

    fun updateStandings(leagueId: Long, standingsList: List<Standings>) = leagueRepository.updateStandings(standingsList, leagueId)

    fun updateTeamStats(leagueId: Long, teamStatsList: List<TeamStats>) = leagueRepository.updateTeamStats(teamStatsList, leagueId)

    fun updatePlayerStats(leagueId: Long, playerStatsList: List<PlayerStats>) = leagueRepository.updatePlayerStats(playerStatsList, leagueId)
}