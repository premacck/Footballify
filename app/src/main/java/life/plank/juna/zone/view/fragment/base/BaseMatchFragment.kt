package life.plank.juna.zone.view.fragment.base

import androidx.fragment.app.Fragment
import com.google.gson.Gson
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.FootballLiveData
import life.plank.juna.zone.data.model.MatchDetails
import life.plank.juna.zone.data.model.notification.SocialNotification
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.util.common.DataUtil.*
import life.plank.juna.zone.util.common.customToast
import life.plank.juna.zone.util.customview.PublicBoardToolbar
import life.plank.juna.zone.util.football.FixtureListUpdateTask
import life.plank.juna.zone.view.fragment.board.fixture.LineupFragment
import life.plank.juna.zone.view.fragment.board.fixture.MatchBoardFragment
import life.plank.juna.zone.view.fragment.board.fixture.MatchMediaFragment
import life.plank.juna.zone.view.fragment.board.fixture.MatchStatsFragment

abstract class BaseMatchFragment : CardTileFragment() {

    override fun onSocialNotificationReceive(socialNotification: SocialNotification) {
        socialNotification.run {
            when (action) {
                findString(R.string.intent_post) -> getFeedEntryDetails(restApi(), this)
                findString(R.string.intent_comment) -> updateForumComments()
                findString(R.string.intent_react) -> {
                }
            }
        }
    }

    fun onZoneLiveDataReceived(footballLiveData: FootballLiveData) {
        when (footballLiveData.liveDataType) {
            SCORE_DATA -> footballLiveData.getScoreData(gson())?.run {
                updateScoreLocally(matchDetails(), this)
                publicBoardToolbar().setScore("$homeGoals$DASH$awayGoals")
                FixtureListUpdateTask.update(matchDetails(), this, null, true)
            }
            TIME_STATUS_DATA -> footballLiveData.getLiveTimeStatus(gson())?.run {
                updateTimeStatusLocally(matchDetails(), this)
                FixtureListUpdateTask.update(matchDetails(), null, this, false)
                publicBoardToolbar().setLiveTimeStatus(matchDetails().matchStartTime, timeStatus)
            }
            MATCH_EVENTS -> {
                footballLiveData.getMatchEventList(gson())?.run {
                    if (isNullOrEmpty(matchDetails().matchEvents)) {
                        matchDetails().matchEvents = ArrayList()
                    }
                    matchDetails().matchEvents?.addAll(this)
                    (currentChildFragment() as? LineupFragment)?.updateMatchEvents(this)
                }
            }
            COMMENTARY_DATA -> {
                footballLiveData.getCommentaryList(gson())?.run {
                    if (isNullOrEmpty(matchDetails().commentary)) {
                        matchDetails().commentary = ArrayList()
                        (this@BaseMatchFragment as? MatchBoardFragment)?.updateCommentaryMarquee()
                    }
                    matchDetails().commentary?.addAll(0, this)
                    (currentChildFragment() as? MatchStatsFragment)?.updateCommentary(this)
                }
            }
            MATCH_STATS_DATA ->
                footballLiveData.getMatchStats(gson())?.run { (currentChildFragment() as? MatchStatsFragment)?.updateMatchStats(this) }
            HIGHLIGHTS_DATA ->
                footballLiveData.getHighlightsList(gson())?.run { (currentChildFragment() as? MatchMediaFragment)?.updateHighlights(this) }
            BOARD_ACTIVATED -> onBoardStateChange(true)
            BOARD_DEACTIVATED -> onBoardStateChange(false)
            LINEUPS_DATA -> (currentChildFragment() as? LineupFragment)?.getLineupFormation(false)
                    ?: customToast(R.string.lineups_now_available_for_this_match)
        }
    }

    abstract fun gson(): Gson

    abstract fun restApi(): RestApi

    abstract fun matchDetails(): MatchDetails

    abstract fun onBoardStateChange(isActive: Boolean)

    abstract fun currentChildFragment(): Fragment?

    abstract fun publicBoardToolbar(): PublicBoardToolbar
}