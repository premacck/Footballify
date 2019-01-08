package life.plank.juna.zone.view.fragment.board.user

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import com.prembros.facilis.dialog.BaseBlurPopup
import com.prembros.facilis.util.onDebouncingClick
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.popup_board_preview.*
import life.plank.juna.zone.*
import life.plank.juna.zone.data.model.Board
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.util.common.JunaDataUtil.findString
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import life.plank.juna.zone.view.activity.profile.UserProfileActivity
import java.io.File
import java.net.HttpURLConnection
import javax.inject.Inject

class BoardPreviewPopup : BaseBlurPopup() {

    @Inject
    lateinit var restApi: RestApi

    private lateinit var board: Board
    private lateinit var filePath: String

    companion object {
        val TAG: String = BoardPreviewPopup::class.java.simpleName
        fun newInstance(board: Board, filePath: String) = BoardPreviewPopup().apply {
            arguments = Bundle().apply {
                putParcelable(findString(R.string.intent_board), board)
                putString(findString(R.string.intent_file_path), filePath)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)

        arguments?.run {
            board = getParcelable(getString(R.string.intent_board))!!
            filePath = getString(getString(R.string.intent_file_path))!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.popup_board_preview, container, false)

    override fun doOnStart() {
        preview_title.setText(R.string.preview_your_board)
        create_board_button.visibility = View.VISIBLE
        board_invite_action_button_layout.visibility = View.GONE
        preview_toolbar.setTitle(board.displayName)
        board_parent_layout.setCardBackgroundColor(Color.parseColor(board.color))
        description.text = board.description
        preview_toolbar.setLeagueLogo(Uri.fromFile(File(filePath)).toString())

        preview_toolbar.setupForPreview()

        create_board_button.onDebouncingClick { createBoard() }
    }

    override fun getBlurLayout(): BlurLayout? = root_blur_layout

    override fun getDragHandle(): View? = drag_area

    override fun getRootView(): View? = root_card

    override fun getBackgroundLayout(): ViewGroup? = root_blur_layout

    private fun createBoard() {
        val image = File(filePath).createMultiPartImage()
        val name = board.displayName?.createRequestBody()
        val zone = board.zone?.createRequestBody()
        val description = board.description?.createRequestBody()
        val color = board.color?.createRequestBody()

        restApi.createPrivateBoard(board.boardType, name, zone, description, color, image, getToken()).setObserverThreadsAndSmartSubscribe({
                    Log.e(TAG, it.message)
            errorToast(R.string.could_not_create_board, it)
                }, {
                    when (it.code()) {
                        HttpURLConnection.HTTP_OK -> it.body()?.run {
                            restApi.followBoard(getToken(), this).execute()
                            navigateToBoard(this)
                        }
                        HttpURLConnection.HTTP_CONFLICT -> customToast(R.string.board_name_already_exists)
                        else -> errorToast(R.string.could_not_create_board, it)
                    }
        }, this)
    }

    private fun navigateToBoard(boardId: String) {
        activity?.launchWithBoard<UserProfileActivity>(boardId)
    }
}