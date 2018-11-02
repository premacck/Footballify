package life.plank.juna.zone.util.common

import android.app.Activity
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.Board
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.DataUtil.findString
import life.plank.juna.zone.util.facilis.launchPrivateBoard
import life.plank.juna.zone.util.launchMatchBoard
import life.plank.juna.zone.util.launchPrivateBoard
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.fragment.board.fixture.MatchBoardFragment
import life.plank.juna.zone.view.fragment.board.user.PrivateBoardFragment
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor

inline fun <reified T : Activity> Activity.launch() = startActivity(intentFor<T>())

/**
 * Function Launch any [BaseCardActivity] with [PrivateBoardFragment] from any [Activity] with the [Board] object
 */
inline fun <reified T : BaseCardActivity> Activity.launchWithPrivateBoard(board: Board) {
    startActivity(intentFor<T>(findString(R.string.intent_private_board) to board).clearTop())
}

/**
 * Function Launch [BaseCardActivity] with [PrivateBoardFragment] from any [Activity] with the Private Board's ID
 */
inline fun <reified T : BaseCardActivity> Activity.launchWithPrivateBoard(boardId: String) =
        startActivity(intentFor<T>(findString(R.string.intent_private_board_id) to boardId).clearTop())

/**
 * Function to handle the private board intent (if any) passed to the [BaseCardActivity] and launch [PrivateBoardFragment]
 */
fun BaseCardActivity.handlePrivateBoardIntentIfAny(restApi: RestApi) {
    if (intent.hasExtra(getString(R.string.intent_private_board))) {
        supportFragmentManager.launchPrivateBoard(
                getFragmentContainer(),
                intent.getParcelableExtra(getString(R.string.intent_private_board))
        )
    } else if (intent.hasExtra(getString(R.string.intent_private_board_id))) {
        restApi.launchPrivateBoard(
                intent.getStringExtra(getString(R.string.intent_private_board_id)),
                getFragmentContainer(),
                supportFragmentManager
        )
    }
}

/**
 * Function to handle the match board intent (if any) passed to the [BaseCardActivity] and launch [MatchBoardFragment]
 */
fun BaseCardActivity.handleMatchBoardIntentIfAny(restApi: RestApi, footballRestApi: RestApi) {
    if (intent.hasExtra(getString(R.string.match_id_string))) {
        restApi.launchMatchBoard(
                footballRestApi,
                intent.getLongExtra(getString(R.string.match_id_string), 0),
                getFragmentContainer(),
                this
        )
    }
}