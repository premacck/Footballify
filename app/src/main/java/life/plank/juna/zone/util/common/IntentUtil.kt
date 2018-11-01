package life.plank.juna.zone.util.common

import android.app.Activity
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.Board
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.DataUtil.findString
import life.plank.juna.zone.util.facilis.launchPrivateBoard
import life.plank.juna.zone.util.launchPrivateBoard
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.fragment.board.user.PrivateBoardFragment
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor

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
fun AppCompatActivity.handlePrivateBoardIntent(restApi: RestApi, @IdRes resId: Int) {
    if (intent.hasExtra(getString(R.string.intent_private_board))) {
        supportFragmentManager.launchPrivateBoard(
                resId,
                intent.getParcelableExtra(getString(R.string.intent_private_board))
        )
    } else if (intent.hasExtra(getString(R.string.intent_private_board_id))) {
        launchPrivateBoard(
                intent.getStringExtra(getString(R.string.intent_private_board_id)),
                restApi,
                resId,
                supportFragmentManager
        )
    }
}