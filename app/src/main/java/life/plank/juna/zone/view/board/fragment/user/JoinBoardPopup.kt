package life.plank.juna.zone.view.board.fragment.user

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import com.prembros.facilis.dialog.BaseBlurPopup
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.popup_board_preview.*
import life.plank.juna.zone.*
import life.plank.juna.zone.component.helper.launchWithBoard
import life.plank.juna.zone.data.api.*
import life.plank.juna.zone.data.model.board.Board
import life.plank.juna.zone.service.CommonDataService.findString
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.view.home.HomeActivity
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.net.HttpURLConnection
import javax.inject.Inject

class JoinBoardPopup : BaseBlurPopup() {

    @Inject
    lateinit var restApi: RestApi

    lateinit var boardId: String
    lateinit var board: Board

    companion object {
        private val TAG = JoinBoardPopup::class.java.simpleName
        fun newInstance(boardId: String) = JoinBoardPopup().apply { arguments = Bundle().apply { putString(findString(R.string.intent_board_id), boardId) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)
        arguments?.run { boardId = getString(getString(R.string.intent_board_id))!! }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.popup_board_preview, container, false)

    override fun doOnStart() {
        preview_title.setText(R.string.you_have_been_invited_to_this_board)
        create_board_button.visibility = View.GONE
        board_invite_action_button_layout.visibility = View.VISIBLE
        getBoardDetails(boardId)
        getBlurLayout()?.setOnClickListener(null)
    }

    private fun getBoardDetails(boardId: String) {
        restApi.getBoardById(boardId).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "getBoardDetails(): ", it)
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_OK -> {
                    it.body()?.run {
                        board = this
                        updateUi()
                    }
                }
                else -> errorToast(R.string.could_not_navigate_to_board, it)
            }
        }, this)
    }

    private fun updateUi() {
        preview_toolbar.setTitle(board.displayName)
        preview_toolbar.setBoardTitle(if (board.boardType == getString(R.string.public_lowercase)) R.string.public_board else R.string.private_board)
        preview_toolbar.setLeagueLogo(board.boardIconUrl!!)
        preview_toolbar.setBackgroundColor(Color.parseColor(board.color))
        board_parent_layout.setCardBackgroundColor(Color.parseColor(board.color))
        description.text = board.description

        accept_invite_button.onClick { followBoard() }
        reject_invite_button.onClick { dismiss() }
    }

    private fun followBoard() {
        restApi.followBoard(boardId).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "followBoard(): ", it)
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_CREATED -> activity?.run {
                    launchWithBoard<HomeActivity>(board)
                    dismiss()
                }
                HttpURLConnection.HTTP_CONFLICT -> {
                    customToast(R.string.already_following_board)
                    activity?.run {
                        launchWithBoard<HomeActivity>(board)
                        dismiss()
                    }
                }
                HttpURLConnection.HTTP_FORBIDDEN -> customToast(R.string.cannot_follow_board)
                HttpURLConnection.HTTP_NOT_FOUND -> customToast(R.string.failed_to_follow_board)
                else -> errorToast(R.string.something_went_wrong, it)
            }
        }, this)
    }

    override fun getBlurLayout(): BlurLayout? = root_blur_layout

    override fun getDragHandle(): View? = drag_area

    override fun getRootView(): View? = root_card

    override fun getBackgroundLayout(): ViewGroup? = root_blur_layout
}
