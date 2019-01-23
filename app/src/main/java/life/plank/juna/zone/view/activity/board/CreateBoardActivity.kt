package life.plank.juna.zone.view.activity.board

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.provider.MediaStore.Images.Media
import android.view.View
import android.widget.ToggleButton
import com.prembros.facilis.util.*
import kotlinx.android.synthetic.main.activity_create_board.*
import life.plank.juna.zone.*
import life.plank.juna.zone.data.model.Board
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.AppConstants.GALLERY_IMAGE_RESULT
import life.plank.juna.zone.util.common.customToast
import life.plank.juna.zone.util.sharedpreference.PreferenceManager
import life.plank.juna.zone.util.view.UIDisplayUtil
import life.plank.juna.zone.util.view.UIDisplayUtil.*
import life.plank.juna.zone.view.activity.base.BaseJunaCardActivity
import life.plank.juna.zone.view.adapter.board.creation.*
import life.plank.juna.zone.view.fragment.board.user.BoardPreviewPopup
import org.jetbrains.anko.sdk27.coroutines.onClick
import javax.inject.Inject

class CreateBoardActivity : BaseJunaCardActivity() {

    @Inject
    lateinit var boardColorThemeAdapter: BoardColorThemeAdapter
    @Inject
    lateinit var boardIconAdapter: BoardIconAdapter

    private var zones: Array<ToggleButton>? = null
    private var zone: String? = null
    private var filePath: String? = null
    private var isIconSelected: Boolean? = false

    companion object {
        fun launch(packageContext: Context, username: String) {
            val intent = Intent(packageContext, CreateBoardActivity::class.java)
            intent.putExtra(ZoneApplication.getContext().getString(R.string.username), username)
            packageContext.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_board)
        ZoneApplication.getApplication().uiComponent.inject(this)

        zones = arrayOf(football, music, drama, tune, skill, other)
        private_board_color_list.adapter = boardColorThemeAdapter
        private_board_icon_list.adapter = boardIconAdapter

        UIDisplayUtil.checkPermission(this@CreateBoardActivity)
        boardIconAdapter.boardIconList.clear()

        validateCreateBoardContent()

        PreferenceManager.CurrentUser.getProfilePicUrl()?.run { tool_bar.setProfilePic(this) }
        tool_bar.isNotificationViewVisible(View.GONE)
        user_greeting.text = getString(R.string.hi_user, intent.getStringExtra(ZoneApplication.getContext().getString(R.string.username)))

        setupListeners()
    }

    override fun getFragmentContainer(): Int = R.id.board_maker_fragment_container

    override fun restApi(): RestApi? = null

    private fun setupListeners() {
        arrayOf(football, music, drama, tune, skill, other).onClick { toggleView(it as ToggleButton) }

        arrayOf(board_name_edit_text, board_description_edit_text).onTextChanged { validateCreateBoardContent() }

        create_board_button.onClick { onCreateBoardButtonClicked() }

        upload_board_icon.onClick { onUploadButtonClicked() }
    }

    private fun toggleView(view: ToggleButton) {
        for (zoneView in zones!!) {
            if (zoneView.id == view.id) {
                checkAction(zoneView, !zoneView.isChecked)
                continue
            }
            checkAction(zoneView, false)
        }
        validateCreateBoardContent()
    }

    private fun checkAction(toggleButton: ToggleButton, isChecked: Boolean) {
        if (zone == toggleButton.text.toString()) {
            if (!isChecked) {
                zone = null
            }
        } else {
            if (isChecked) {
                zone = toggleButton.text.toString()
            }
        }
        toggleZone(this, toggleButton, isChecked)
    }

    private fun onCreateBoardButtonClicked() {
        board_name_edit_text.clearFocus()
        board_description_edit_text.clearFocus()
        hideSoftKeyboard(create_board_button)

        val board = Board(
                board_name_edit_text.text!!.toString().trim(),
                getString(R.string.private_lowercase),
                zone!!.toLowerCase().trim(),
                board_description_edit_text.text!!.toString().trim(),
                boardColorThemeAdapter.selectedColor!!
        )

        boardIconAdapter.selectedPath?.run { createBoard(board, this) }
    }

    private fun onUploadButtonClicked() {
        if (UIDisplayUtil.checkPermission(this@CreateBoardActivity)) {
            getImageResourceFromGallery()
        } else {
            customToast(R.string.add_permission)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            GALLERY_IMAGE_RESULT -> when (resultCode) {
                Activity.RESULT_OK -> {
                    filePath = getPathForGalleryImageView(data!!.data, this)
                    if (filePath != null) {
                        boardIconAdapter.boardIconList.add(0, filePath!!)
                        boardIconAdapter.selectedIndex = 0
                        boardIconAdapter.selectedPath = filePath
                        boardIconAdapter.notifyDataSetChanged()
                        isIconSelected = true
                        validateCreateBoardContent()
                    } else {
                        customToast(R.string.image_not_supported)
                    }
                }
                Activity.RESULT_CANCELED -> {/*Do nothing*/
                }
                else -> customToast(R.string.failed_to_process_image)
            }
        }
    }

    private fun getImageResourceFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.setDataAndType(Media.EXTERNAL_CONTENT_URI, getString(R.string.image_format))
        startActivityForResult(galleryIntent, GALLERY_IMAGE_RESULT)
    }

    private fun validateCreateBoardContent() {
        enableOrDisableView(
                create_board_button,
                !(isNullOrEmpty(zone) || isNullOrEmpty(zone!!.toLowerCase().trim()) ||
                        board_name_edit_text.text == null || isNullOrEmpty(board_name_edit_text.text!!.toString().trim()) ||
                        board_description_edit_text.text == null || isNullOrEmpty(board_description_edit_text.text!!.toString().trim()) ||
                        isNullOrEmpty(boardColorThemeAdapter.selectedColor) || (!isIconSelected!!))
        )
    }

    private fun createBoard(board: Board, file: String) {
        if (isNullOrEmpty(file)) {
            customToast(R.string.select_image_to_upload)
            return
        }
        pushPopup(BoardPreviewPopup.newInstance(board, file))
    }

    override fun onBackPressed() {
        if (supportFragmentManager.removeActivePopupsIfAny()) {
            finish()
        }
    }
}