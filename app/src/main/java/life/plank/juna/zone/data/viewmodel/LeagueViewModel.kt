package life.plank.juna.zone.data.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.persistence.room.RoomDatabase
import android.util.Log
import android.widget.Toast
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.RestApiAggregator
import life.plank.juna.zone.data.local.model.LeagueInfo
import life.plank.juna.zone.data.local.repository.LeagueRepository
import life.plank.juna.zone.data.model.League
import life.plank.juna.zone.data.network.interfaces.RestApi
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import rx.Subscriber

/**
 * [ViewModel] class for getting data from the [RoomDatabase] and API calls, and updating the [LiveData] that the UI will be observing.
 */
class LeagueViewModel : ViewModel() {

    companion object {
        private val TAG = LeagueViewModel::class.java.simpleName
    }

    val leagueRepository: LeagueRepository = LeagueRepository()
    val leagueInfoLiveData: MutableLiveData<LeagueInfo> = MutableLiveData()

    /**
     * Function to update leagueInfoLiveData after getting the response from the API
     * The calling view should start observing leagueInfoLiveData before calling this method
     */
    fun getLeagueInfoFromRestApi(league: League, restApi: RestApi) {
        RestApiAggregator.getLeagueInfo(league, restApi)
                .subscribe(object : Subscriber<LeagueInfo>() {
                    override fun onCompleted() {
                        Log.i(Companion.TAG, "onCompleted")
                    }

                    override fun onError(e: Throwable) {
                        Log.e(Companion.TAG, e.message)
                        Toast.makeText(ZoneApplication.getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
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
    fun getLeagueInfoFromDb(leagueId: Long) {
        doAsync {
            val leagueInfo = leagueRepository.getLeagueInfo(leagueId)
            uiThread {
                leagueInfoLiveData.value = leagueInfo
            }
        }
    }
}