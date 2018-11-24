package life.plank.juna.zone.view.fragment.board.user

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.popup_board_preview.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.Board
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.DataUtil.findString
import life.plank.juna.zone.util.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.common.launchWithBoard
import life.plank.juna.zone.util.customToast
import life.plank.juna.zone.util.errorToast
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.view.activity.profile.UserProfileActivity
import life.plank.juna.zone.view.fragment.base.BaseBlurPopup
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
        preview_toolbar.setTitle(board.name)
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
        val fileToUpload = File(filePath)
        val requestBody = RequestBody.create(MediaType.parse(getString(R.string.media_type_image)), fileToUpload)
        val image = MultipartBody.Part.createFormData("", fileToUpload.name, requestBody)

        val name = RequestBody.create(MediaType.parse(getString(R.string.text_content_type)), board.name!!)
        val zone = RequestBody.create(MediaType.parse(getString(R.string.text_content_type)), board.zone!!)
        val description = RequestBody.create(MediaType.parse(getString(R.string.text_content_type)), board.description!!)
        val color = RequestBody.create(MediaType.parse(getString(R.string.text_content_type)), board.color!!)

        val token = getToken()
        restApi.createPrivateBoard(board.boardType, name, zone, description, color, image, token).setObserverThreadsAndSmartSubscribe({
                    Log.e(TAG, it.message)
                    errorToast(R.string.could_not_create_board, it)
                }, {
                    when (it.code()) {
                        HttpURLConnection.HTTP_OK -> navigateToBoard(it.body())
                        HttpURLConnection.HTTP_CONFLICT -> customToast(R.string.board_name_already_exists)
                        else -> errorToast(R.string.could_not_create_board, it)
                    }
                })
    }

    private fun navigateToBoard(boardId: String?) {
        activity?.launchWithBoard<UserProfileActivity>(boardId!!)
        activity?.finish()
    }
}