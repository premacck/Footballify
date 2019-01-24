package life.plank.juna.zone.component.helper

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.util.Log
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.prembros.facilis.util.removeFragmentIfExists
import life.plank.juna.zone.*
import life.plank.juna.zone.data.api.*
import life.plank.juna.zone.data.model.board.Board
import life.plank.juna.zone.data.model.football.MatchDetails
import life.plank.juna.zone.notification.getBoardIdFromIntent
import life.plank.juna.zone.service.CommonDataService.findString
import life.plank.juna.zone.service.LeagueDataService
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.view.base.BaseJunaCardActivity
import life.plank.juna.zone.view.board.fragment.fixture.MatchBoardFragment
import life.plank.juna.zone.view.board.fragment.user.PrivateBoardFragment
import life.plank.juna.zone.view.home.HomeActivity
import org.jetbrains.anko.*
import retrofit2.Response
import java.net.HttpURLConnection

inline fun <reified T : Activity> Activity.launch() = startActivity(intentFor<T>())

/**
 * Function Launch any [BaseJunaCardActivity] with [PrivateBoardFragment] or [MatchBoardFragment] from any [Activity] with the [Board] object
 */
inline fun <reified T : BaseJunaCardActivity> Activity.launchWithBoard(board: Board) {
    if (this is T) {
        restApi()?.run { launchPrivateOrMatchBoard(this, board) }
                ?: toast(R.string.rest_api_is_null)
    } else {
        startActivity(intentFor<T>(findString(R.string.intent_board) to board).clearTop())
        finish()
    }
}

/**
 * Function Launch [BaseJunaCardActivity] with [PrivateBoardFragment] or [MatchBoardFragment] from any [Activity] with the Private Board's ID
 */
inline fun <reified T : BaseJunaCardActivity> Activity.launchWithBoard(boardId: String) {
    if (this is T) {
        restApi()?.run { findAndLaunchBoardById(this, boardId) }
    } else {
        startActivity(intentFor<T>(findString(R.string.intent_board_id) to boardId).clearTop())
        finish()
    }
}

fun BaseJunaCardActivity.handleDeepLinkIntentIfAny() {
    intent?.data?.pathSegments?.run {
        if (isNotEmpty() && size >= 2) {
            get(1).run { launchWithBoard<HomeActivity>(this) }
        }
    }
}

/**
 * Function to handle the board/boardId intent (if any) passed to the [BaseJunaCardActivity] and launch [PrivateBoardFragment] or [MatchBoardFragment]
 */
fun BaseJunaCardActivity.handleBoardIntentIfAny() {
    if (intent.hasExtra(getString(R.string.intent_action))) return
    restApi()?.run {
        when {
            intent.hasExtra(getString(R.string.intent_board)) -> launchPrivateOrMatchBoard(this, intent.getParcelableExtra(getString(R.string.intent_board)))
            intent.hasExtra(getString(R.string.intent_board_id)) -> findAndLaunchBoardById(this)
        }
    }
}

fun BaseJunaCardActivity.findAndLaunchBoardById(restApi: RestApi, boardId: String? = null) {
    restApi.getBoardById(boardId ?: getBoardIdFromIntent()!!)
            .setObserverThreadsAndSmartSubscribe({}, {
                it.body()?.run { launchPrivateOrMatchBoard(restApi, this) }
            })
}

fun BaseJunaCardActivity.launchPrivateOrMatchBoard(restApi: RestApi, board: Board) {
    when (board.boardType) {
        findString(R.string.board_type_football_match) -> {
            board.boardEvent?.apply {
                restApi.getMatchDetails(foreignId).setObserverThreadsAndSmartSubscribe({
                    errorToast(R.string.match_details_not_found, it)
                }, {
                    it.body()?.run { launchMatchBoard(board, this) }
                            ?: errorToast(R.string.match_details_not_found, it)
                })
            }
        }
        findString(R.string.private_) -> launchPrivateBoard(board)
    }
}

fun BaseJunaCardActivity.launchPrivateBoard(board: Board) {
    removeFragmentIfExists<PrivateBoardFragment>()
    pushFragment(PrivateBoardFragment.newInstance(board), true)
}

fun BaseJunaCardActivity.launchPrivateBoard(restApi: RestApi, boardId: String) {
    RestApiAggregator.getPrivateBoardToOpen(boardId, restApi).setObserverThreadsAndSmartSubscribe({
        Log.e("launchPrivateBoard", "onError(): ", it)
        customToast(R.string.could_not_navigate_to_board)
    }, {
        it?.run { launchPrivateBoard(this) }
    })
}

fun BaseJunaCardActivity.launchMatchBoard(board: Board, matchDetails: MatchDetails) {
    removeFragmentIfExists<MatchBoardFragment>()
    pushFragment(MatchBoardFragment.newInstance(board, matchDetails), true)
}

fun BaseJunaCardActivity.launchMatchBoard(restApi: RestApi, boardId: String) {
    restApi.getBoardById(boardId).flatMap<Response<MatchDetails>, Unit>({
        when (it.code()) {
            HttpURLConnection.HTTP_OK -> it.body()?.boardEvent?.run { restApi.getMatchDetails(this.foreignId) }
            else -> {
                errorToast(R.string.failed_to_retrieve_board, it)
                restApi.getMatchDetails(0)
            }
        }
    }, { boardResponse, matchDetailsResponse ->
        val board = boardResponse.body()
        val matchDetails = matchDetailsResponse.body()
        if (board != null && matchDetails != null) {
            doAsync {
                matchDetails.league = LeagueDataService.getSpecifiedLeague(board.boardEvent?.leagueName)
                uiThread {
                    it.launchMatchBoard(board, matchDetails)
                }
            }
        } else {
            if (matchDetails == null) {
                errorToast(R.string.failed_to_get_match_details, matchDetailsResponse)
                errorLog("RestApi.launchMatchBoard(): ", R.string.failed_to_get_match_details, matchDetailsResponse)
            }
        }
    })
}

fun BaseJunaCardActivity.launchMatchBoard(restApi: RestApi, matchId: Long) {
    RestApiAggregator.getBoardAndMatchDetails(restApi, matchId).setObserverThreadsAndSmartSubscribe({
        errorToast(R.string.could_not_navigate_to_board, it)
    }, {
        launchMatchBoard(it.first, it.second)
    })
}

fun Activity.removeIntentExtra(@StringRes vararg extraKeys: Int) = extraKeys.forEach { intent?.removeExtra(getString(it)) }

fun startVoiceRecognitionActivity(fragment: Fragment) {
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, ZoneApplication.getApplication().getString(R.string.voice_search))
    fragment.startActivityForResult(intent, AppConstants.VOICE_RECOGNITION_REQUEST_CODE)
}