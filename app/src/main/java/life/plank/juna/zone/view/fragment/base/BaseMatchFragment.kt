package life.plank.juna.zone.view.fragment.base

import android.content.Intent
import android.support.v4.app.Fragment
import android.util.Log
import com.google.gson.Gson
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.FeedEntry
import life.plank.juna.zone.data.model.MatchDetails
import life.plank.juna.zone.data.model.ZoneLiveData
import life.plank.juna.zone.data.model.notification.JunaNotification
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.util.common.DataUtil.*
import life.plank.juna.zone.util.common.customToast
import life.plank.juna.zone.util.common.errorToast
import life.plank.juna.zone.util.common.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.util.customview.PublicBoardToolbar
import life.plank.juna.zone.util.football.FixtureListUpdateTask
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import life.plank.juna.zone.view.fragment.board.fixture.LineupFragment
import life.plank.juna.zone.view.fragment.board.fixture.MatchMediaFragment
import life.plank.juna.zone.view.fragment.board.fixture.MatchStatsFragment
import java.net.HttpURLConnection.HTTP_OK

abstract class BaseMatchFragment : CardTileFragment() {

    fun handleInAppNotificationIntent(intent: Intent) {
        val junaNotification = intent.getParcelableExtra<JunaNotification>(findString(R.string.intent_juna_notification))
        restApi().getFeedEntry(junaNotification.feedItemId, getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e("getFeedEntry()", "ERROR: ", it)
        }, {
            when (it.code()) {
                HTTP_OK -> it.body()?.run { onInAppNotificationReceived(this) }
                else -> errorToast(R.string.failed_to_retrieve_feed, it)
            }
        }, this)
    }

    fun onZoneLiveDataReceived(zoneLiveData: ZoneLiveData) {
        when (zoneLiveData.liveDataType) {
            SCORE_DATA -> zoneLiveData.getScoreData(gson())?.run {
                updateScoreLocally(matchDetails(), this)
                publicBoardToolbar().setScore("$homeGoals$DASH$awayGoals")
                FixtureListUpdateTask.update(matchDetails(), this, null, true)
            }
            TIME_STATUS_DATA -> zoneLiveData.getLiveTimeStatus(gson())?.run {
                updateTimeStatusLocally(matchDetails(), this)
                FixtureListUpdateTask.update(matchDetails(), null, this, false)
                publicBoardToolbar().setLiveTimeStatus(matchDetails().matchStartTime, timeStatus)
            }
            MATCH_EVENTS ->
                zoneLiveData.getMatchEventList(gson())?.run { (currentChildFragment() as? LineupFragment)?.updateMatchEvents(this) }
            COMMENTARY_DATA ->
                zoneLiveData.getCommentaryList(gson())?.run { (currentChildFragment() as? MatchStatsFragment)?.updateCommentary(this) }
            MATCH_STATS_DATA ->
                zoneLiveData.getMatchStats(gson())?.run { (currentChildFragment() as? MatchStatsFragment)?.updateMatchStats(this) }
            HIGHLIGHTS_DATA ->
                zoneLiveData.getHighlightsList(gson())?.run { (currentChildFragment() as? MatchMediaFragment)?.updateHighlights(this) }
            BOARD_ACTIVATED -> onBoardStateChange(true)
            BOARD_DEACTIVATED -> onBoardStateChange(false)
            LINEUPS_DATA -> (currentChildFragment() as? LineupFragment)?.getLineupFormation(false)
                    ?: customToast(R.string.lineups_now_available_for_this_match)
        }
    }

    abstract fun gson(): Gson

    abstract fun restApi(): RestApi

    abstract fun onInAppNotificationReceived(feedEntry: FeedEntry)

    abstract fun matchDetails(): MatchDetails

    abstract fun onBoardStateChange(isActive: Boolean)

    abstract fun currentChildFragment(): Fragment?

    abstract fun publicBoardToolbar(): PublicBoardToolbar
}