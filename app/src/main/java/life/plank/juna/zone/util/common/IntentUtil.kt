package life.plank.juna.zone.util.common

import android.app.Activity
import android.support.annotation.StringRes
import android.util.Log
import life.plank.juna.zone.R
import life.plank.juna.zone.data.RestApiAggregator
import life.plank.juna.zone.data.model.Board
import life.plank.juna.zone.data.model.MatchDetails
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.notification.getBoardIdFromIntent
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.util.facilis.removeFragmentIfExists
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.activity.home.HomeActivity
import life.plank.juna.zone.view.fragment.board.fixture.MatchBoardFragment
import life.plank.juna.zone.view.fragment.board.user.PrivateBoardFragment
import org.jetbrains.anko.*
import retrofit2.Response
import java.net.HttpURLConnection

inline fun <reified T : Activity> Activity.launch() = startActivity(intentFor<T>())

/**
 * Function Launch any [BaseCardActivity] with [PrivateBoardFragment] or [MatchBoardFragment] from any [Activity] with the [Board] object
 */
inline fun <reified T : BaseCardActivity> Activity.launchWithBoard(board: Board) {
    if (this is T) {
        restApi()?.run { launchPrivateOrMatchBoard(this, board) }
                ?: toast(R.string.rest_api_is_null)
    } else {
        startActivity(intentFor<T>(findString(R.string.intent_board) to board).clearTop())
        finish()
    }
}

/**
 * Function Launch [BaseCardActivity] with [PrivateBoardFragment] or [MatchBoardFragment] from any [Activity] with the Private Board's ID
 */
inline fun <reified T : BaseCardActivity> Activity.launchWithBoard(boardId: String) {
    if (this is T) {
        restApi()?.run { findAndLaunchBoardById(this, boardId) }
    } else {
        startActivity(intentFor<T>(findString(R.string.intent_board_id) to boardId).clearTop())
        finish()
    }
}

fun BaseCardActivity.handleDeepLinkIntentIfAny() {
    intent?.data?.pathSegments?.run {
        if (isNotEmpty() && size >= 2) {
            get(1).run { launchWithBoard<HomeActivity>(this) }
        }
    }
}

/**
 * Function to handle the board/boardId intent (if any) passed to the [BaseCardActivity] and launch [PrivateBoardFragment] or [MatchBoardFragment]
 */
fun BaseCardActivity.handleBoardIntentIfAny() {
    if (intent.hasExtra(getString(R.string.intent_action))) return
    restApi()?.run {
        when {
            intent.hasExtra(getString(R.string.intent_board)) -> launchPrivateOrMatchBoard(this, intent.getParcelableExtra(getString(R.string.intent_board)))
            intent.hasExtra(getString(R.string.intent_board_id)) -> findAndLaunchBoardById(this)
        }
    }
}

fun BaseCardActivity.findAndLaunchBoardById(restApi: RestApi, boardId: String? = null) {
    restApi.getBoardById(boardId ?: getBoardIdFromIntent(), getToken())
            .setObserverThreadsAndSmartSubscribe({}, {
                it.body()?.run { launchPrivateOrMatchBoard(restApi, this) }
            })
}

fun BaseCardActivity.launchPrivateOrMatchBoard(restApi: RestApi, board: Board) {
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

fun BaseCardActivity.launchPrivateBoard(board: Board) {
    removeFragmentIfExists<PrivateBoardFragment>()
    pushFragment(PrivateBoardFragment.newInstance(board), true)
}

fun BaseCardActivity.launchPrivateBoard(restApi: RestApi, boardId: String) {
    RestApiAggregator.getPrivateBoardToOpen(boardId, restApi).setObserverThreadsAndSmartSubscribe({
        Log.e("launchPrivateBoard", "onError(): ", it)
        customToast(R.string.could_not_navigate_to_board)
    }, {
        it?.run { launchPrivateBoard(this) }
    })
}

fun BaseCardActivity.launchMatchBoard(board: Board, matchDetails: MatchDetails) {
    removeFragmentIfExists<MatchBoardFragment>()
    pushFragment(MatchBoardFragment.newInstance(board, matchDetails), true)
}

fun BaseCardActivity.launchMatchBoard(restApi: RestApi, boardId: String) {
    restApi.getBoardById(boardId, getToken()).flatMap<Response<MatchDetails>, Unit>({
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
                matchDetails.league = DataUtil.getSpecifiedLeague(board.boardEvent?.leagueName)
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

fun BaseCardActivity.launchMatchBoard(restApi: RestApi, matchId: Long) {
    RestApiAggregator.getBoardAndMatchDetails(restApi, matchId).setObserverThreadsAndSmartSubscribe({
        errorToast(R.string.could_not_navigate_to_board, it)
    }, {
        launchMatchBoard(it.first, it.second)
    })
}

fun Activity.removeIntentExtra(@StringRes vararg extraKeys: Int) = extraKeys.forEach { intent?.removeExtra(getString(it)) }