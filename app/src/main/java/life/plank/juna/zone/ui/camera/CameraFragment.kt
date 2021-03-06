package life.plank.juna.zone.ui.camera


import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.prembros.facilis.util.onDebouncingClick
import com.wonderkiln.camerakit.CameraKit.Constants.*
import kotlinx.android.synthetic.main.fragment_camera.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.service.CommonDataService.findString
import life.plank.juna.zone.service.FileHandler
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.util.view.UIDisplayUtil.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk27.coroutines.onTouch
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.uiThread
import java.io.File

class CameraFragment : Fragment() {

    private var boardId: String? = null
    private var isBoard: Boolean = true
    private var pendingVideoCapture: Boolean = false
    private var capturingVideo: Boolean = false

    private lateinit var imageFolder: File

    companion object {
        private val TAG = CameraFragment::class.java.simpleName
        fun newInstance(boardId: String, isBoard: Boolean) = CameraFragment().apply {
            arguments = Bundle().apply {
                putString(findString(R.string.intent_board_id), boardId)
                putBoolean(findString(R.string.intent_is_board), isBoard)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            boardId = getString(getString(R.string.intent_board_id))
            isBoard = getBoolean(getString(R.string.intent_is_board))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_camera, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        imageFolder = FileHandler.createMediaFolderIfNotExists(true)
        initListeners()
    }

    private fun initListeners() {
        camera_flash_toggle.onDebouncingClick { switchFlash() }
        camera_capture.onTouch { v, event -> onTouchCapture(v, event) }
        camera_flip.onDebouncingClick { flipCamera() }
    }

    override fun onResume() {
        super.onResume()
        camera_view.start()
    }

    override fun onPause() {
        camera_view.stop()
        super.onPause()
    }

    private fun switchFlash() {
        when (camera_view.flash) {
            FLASH_OFF -> {
                camera_view.flash = FLASH_ON
                rotateAndChangeImageResource(camera_flash_toggle, R.drawable.ic_flash_on)
            }
            FLASH_ON -> {
                camera_view.flash = FLASH_AUTO
                rotateAndChangeImageResource(camera_flash_toggle, R.drawable.ic_flash_auto)
            }
            FLASH_AUTO -> {
                camera_view.flash = FLASH_OFF
                rotateAndChangeImageResource(camera_flash_toggle, R.drawable.ic_flash_off)
            }
            FLASH_TORCH -> {
            }
        }
    }

    private fun onTouchCapture(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                if (isBoard) {
                    tap_hold_hint.visibility = View.GONE
                    touchFeedBackAnimation(view, true)
                    pendingVideoCapture = true
                    view.postDelayed({
                        if (pendingVideoCapture) {
                            capturingVideo = true
                            camera_capture.setImageResource(R.drawable.camera_active)
                            camera_capture.startAnimation(AnimationUtils.loadAnimation(ZoneApplication.appContext, R.anim.camera_active))
                            setExtraButtonsVisibility(View.INVISIBLE)
                            camera_view.captureVideo { cameraKitVideo ->
                                runOnUiThread {
                                    layout_busy_progress_bar.visibility = View.GONE
                                    UploadActivity.launch(activity!!, VIDEO, boardId, cameraKitVideo.videoFile.absolutePath)
                                    activity?.finish()
                                }
                            }
                        }
                    }, 250)
                    return true
                }
            }
            MotionEvent.ACTION_UP -> {
                pendingVideoCapture = false
                if (capturingVideo) {
                    capturingVideo = false
                    camera_capture.setImageResource(R.drawable.camera_inactive)
                    camera_capture.clearAnimation()
                    touchFeedBackAnimation(view, false)
                    layout_busy_progress_bar.visibility = View.VISIBLE
                    setExtraButtonsVisibility(View.VISIBLE)
                    camera_view.stopVideo()
                } else {
                    touchFeedBackAnimation(view, false)
                    layout_busy_progress_bar.visibility = View.VISIBLE
                    camera_view.captureImage { cameraKitImage ->
                        try {
                            runOnUiThread {
                                layout_busy_progress_bar.visibility = View.GONE
                                if (isBoard) {
                                    UploadActivity.launch(activity!!, IMAGE, boardId, FileHandler.saveImageFile(imageFolder, cameraKitImage.bitmap))
                                    activity?.finish()
                                } else {
                                    doAsync {
                                        val filePath = FileHandler.saveImageFile(imageFolder, cameraKitImage.bitmap)
                                        uiThread {
                                            (activity as? CustomCameraActivity)?.setResult(filePath)
                                        }
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "onTouchCapture(): ", e)
                        }
                    }
                }
                return true
            }
        }
        return false
    }

    private fun flipCamera() {
        flipView(camera_flip, true)
        when (camera_view.facing) {
            FACING_BACK -> camera_view.facing = FACING_FRONT
            FACING_FRONT -> camera_view.facing = FACING_BACK
        }
    }

    private fun setExtraButtonsVisibility(visibility: Int) {
        camera_flash_toggle.visibility = visibility
        camera_flip.visibility = visibility
        swipe_up_message.visibility = visibility

    }
}