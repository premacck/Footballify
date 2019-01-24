package life.plank.juna.zone.data.viewmodel

import android.util.Log
import androidx.lifecycle.*
import androidx.room.RoomDatabase
import life.plank.juna.zone.R
import life.plank.juna.zone.data.api.*
import life.plank.juna.zone.data.local.repository.MatchDetailRepository
import life.plank.juna.zone.data.model.football.MatchDetails
import life.plank.juna.zone.util.common.errorToast
import org.jetbrains.anko.*
import java.net.HttpURLConnection.HTTP_OK

/**
 * [ViewModel] class for getting data from the [RoomDatabase] and API calls, and updating the [LiveData] that the UI will be observing.
 */
class MatchDetailViewModel : ViewModel() {

    companion object {
        private val TAG = MatchDetailViewModel::class.java.simpleName
    }

    private val matchDetailRepository: MatchDetailRepository = MatchDetailRepository()
    private val matchDetailsLiveData: MutableLiveData<MatchDetails> = MutableLiveData()

    fun getMatchDetailsFromRestApi(restApi: RestApi, matchId: Long) {
        restApi.getMatchDetails(matchId).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "getMatchDetailsFromRestApi(): ", it)
        }, {
            when (it.code()) {
                HTTP_OK -> matchDetailsLiveData.value = it.body()
                else -> errorToast(R.string.failed_to_get_match_details, it)
            }
        })
    }

    fun getMatchDetailsFromDb(matchId: Long) {
        doAsync {
            val matchDetails = matchDetailRepository.getMatchDetails(matchId)
            uiThread {
                matchDetailsLiveData.value = matchDetails
            }
        }
    }
}