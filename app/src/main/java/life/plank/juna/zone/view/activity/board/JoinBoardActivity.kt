package life.plank.juna.zone.view.activity.board

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_join_board.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.Board
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.common.launch
import life.plank.juna.zone.util.common.launchWithBoard
import life.plank.juna.zone.util.customToast
import life.plank.juna.zone.util.errorToast
import life.plank.juna.zone.util.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.view.activity.home.HomeActivity
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.net.HttpURLConnection
import javax.inject.Inject

class JoinBoardActivity : AppCompatActivity() {

    @Inject
    lateinit var restApi: RestApi

    lateinit var boardId: String
    internal var board: Board? = null

    companion object {
        private val TAG = JoinBoardActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_board)
        ZoneApplication.getApplication().uiComponent.inject(this)

        if (!intent.hasExtra(getString(R.string.intent_board_id))) {
//            Board ID must be passed as an intent to start this activity
            finish()
            return
        }

        boardId = intent.getStringExtra(getString(R.string.intent_board_id))
        getBoardDetails(boardId)
    }

    private fun getBoardDetails(boardId: String) {
        restApi.getBoardById(boardId, getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "getBoardDetails(): ", it)
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_OK -> {
                    board = it.body()
                    updateUi(board!!)
                }
                else -> errorToast(R.string.could_not_navigate_to_board, it)
            }
        })
    }

    private fun updateUi(board: Board) {
        preview_toolbar.setTitle(board.name)
        preview_toolbar.setBoardTitle(if (board.boardType == getString(R.string.public_lowercase)) R.string.public_board else R.string.private_board)
        preview_toolbar.setLeagueLogo(board.boardIconUrl!!)
        preview_toolbar.setBackgroundColor(Color.parseColor(board.color))
        board_parent_layout.setCardBackgroundColor(Color.parseColor(board.color))
        description.text = board.description

        accept_invite_button.onClick { followBoard() }
        reject_invite_button.onClick {
            launch<HomeActivity>()
            finish()
        }
    }

    private fun followBoard() {
        restApi.followBoard(getToken(), boardId).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "followBoard(): ", it)
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_CREATED -> {
                    launchWithBoard<HomeActivity>(boardId)
                    finish()
                }
                HttpURLConnection.HTTP_CONFLICT -> {
                    customToast(R.string.already_following_board)
                    launchWithBoard<HomeActivity>(boardId)
                    finish()
                }
                HttpURLConnection.HTTP_FORBIDDEN -> customToast(R.string.cannot_follow_board)
                HttpURLConnection.HTTP_NOT_FOUND -> customToast(R.string.failed_to_follow_board)
                else -> errorToast(R.string.something_went_wrong, it)
            }
        })
    }
}
