package life.plank.juna.zone.data.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.persistence.room.RoomDatabase
import android.util.Log
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.RestApiAggregator
import life.plank.juna.zone.data.local.model.LeagueInfo
import life.plank.juna.zone.data.local.repository.LeagueRepository
import life.plank.juna.zone.data.model.*
import life.plank.juna.zone.data.network.interfaces.RestApi
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import rx.Subscriber

/**
 * [ViewModel] class for getting data from the [RoomDatabase] and API calls, and updating the [LiveData] that the UI will be observing.
 */
class LeagueViewModel : ViewModel() {

    private val TAG = LeagueViewModel::class.java.simpleName
    private val leagueRepository: LeagueRepository = LeagueRepository()
    private val leagueInfoLiveData: MutableLiveData<LeagueInfo> = MutableLiveData()
    private val fixturesLiveData: MutableLiveData<List<FixtureByMatchDay>> = MutableLiveData()
    private val standingsLiveData: MutableLiveData<List<Standings>> = MutableLiveData()
    private val teamStatsLiveData: MutableLiveData<List<TeamStats>> = MutableLiveData()
    private val playerStatsLiveData: MutableLiveData<List<PlayerStats>> = MutableLiveData()

    init {
        ZoneApplication.getApplication().viewModelComponent.inject(this)
    }

    /**
     * Function to update leagueInfoLiveData after getting the response from the API
     * The calling view should start observing leagueInfoLiveData before calling this method
     */
    fun fetchLeagueInfo(league: League, restApi: RestApi) {
        RestApiAggregator.getLeagueInfo(league, restApi)
                .subscribe(object : Subscriber<LeagueInfo>() {
                    override fun onCompleted() {
                        Log.i(TAG, "onCompleted")
                    }

                    override fun onError(e: Throwable) {
                        Log.e(TAG, e.message)
                    }

                    override fun onNext(leagueInfo: LeagueInfo) {
                        leagueInfoLiveData.value = leagueInfo
                    }
                })
    }

    /**
     * Function to update leagueInfoLiveData after getting the data from [RoomDatabase]
     * The calling view should start observing leagueInfoLiveData before calling this method
     */
    fun getLeagueInfo(leagueId: Long) {
        doAsync {
            val leagueInfo = leagueRepository.getLeagueInfo(leagueId)
            uiThread {
                leagueInfoLiveData.value = leagueInfo
            }
        }
    }

    /**
     * Function to update fixturesLiveData after getting the data from [RoomDatabase]
     * The calling view should start observing fixturesLiveData before calling this method
     */
    fun getFixtures(leagueId: Long) {
        doAsync {
            val fixtureByMatchDayList = leagueRepository.getFixtures(leagueId)
            uiThread {
                fixturesLiveData.value = fixtureByMatchDayList
            }
        }
    }

    /**
     * Function to update standingsLiveData after getting the data from [RoomDatabase]
     * The calling view should start observing standingsLiveData before calling this method
     */
    fun getStandings(leagueId: Long) {
        doAsync {
            val standingsList = leagueRepository.getStandings(leagueId)
            uiThread {
                standingsLiveData.value = standingsList
            }
        }
    }

    /**
     * Function to update teamStatsLiveData after getting the data from [RoomDatabase]
     * The calling view should start observing teamStatsLiveData before calling this method
     */
    fun getTeamStats(leagueId: Long) {
        doAsync {
            val teamStatsList = leagueRepository.getTeamStats(leagueId)
            uiThread {
                teamStatsLiveData.value = teamStatsList
            }
        }
    }

    /**
     * Function to update playerStatsLiveData after getting the data from [RoomDatabase]
     * The calling view should start observing playerStatsLiveData before calling this method
     */
    fun getPlayerStats(leagueId: Long) {
        doAsync {
            val playerStatsList = leagueRepository.getPlayerStats(leagueId)
            uiThread {
                playerStatsLiveData.value = playerStatsList
            }
        }
    }
}