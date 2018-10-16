package life.plank.juna.zone.view.fragment.camera


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnTouch
import com.wonderkiln.camerakit.CameraKit.Constants.*
import kotlinx.android.synthetic.main.fragment_camera.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.util.AppConstants.IMAGE
import life.plank.juna.zone.util.AppConstants.VIDEO
import life.plank.juna.zone.util.FileHandler
import life.plank.juna.zone.util.UIDisplayUtil.*
import life.plank.juna.zone.view.activity.camera.UploadActivity
import org.jetbrains.anko.support.v4.runOnUiThread
import java.io.File
import java.io.IOException

class CameraFragment : Fragment() {

    private var boardId: String? = null
    private var pendingVideoCapture: Boolean = false
    private var capturingVideo: Boolean = false

    private lateinit var imageFolder: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments ?: return
        boardId = args.getString(getString(R.string.intent_board_id))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_camera, container, false)
        ButterKnife.bind(this, rootView)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        imageFolder = FileHandler.createMediaFolderIfNotExists(true)
    }

    override fun onResume() {
        super.onResume()
        camera_view.start()
    }

    override fun onPause() {
        camera_view.stop()
        super.onPause()
    }

    @OnClick(R.id.camera_flash_toggle)
    fun switchFlash() {
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

    @OnTouch(R.id.camera_capture)
    internal fun onTouchCapture(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                touchFeedBackAnimation(view, true)
                pendingVideoCapture = true
                view.postDelayed({
                    if (pendingVideoCapture) {
                        capturingVideo = true
                        camera_capture.setImageResource(R.drawable.camera_active)
                        camera_capture.startAnimation(AnimationUtils.loadAnimation(ZoneApplication.getContext(), R.anim.camera_active))
                        setExtraButtonsVisibility(View.INVISIBLE)
                        camera_view.captureVideo { cameraKitVideo ->
                            runOnUiThread {
                                layout_busy_progress_bar.visibility = View.GONE
                                UploadActivity.launch(activity, VIDEO, boardId, cameraKitVideo.videoFile.absolutePath)
                                activity?.finish()
                            }
                        }
                    }
                }, 250)
                return true
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
                                UploadActivity.launch(activity, IMAGE, boardId, FileHandler.saveImageFile(imageFolder, cameraKitImage.bitmap))
                                activity?.finish()
                            }
                        } catch (e: IOException) {
                            Log.e(TAG, "onTouchCapture(): ", e)
                        }
                    }
                }
                return true
            }
        }
        return false
    }

    @OnClick(R.id.camera_flip)
    fun flipCamera() {
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

    companion object {

        private val TAG = CameraFragment::class.java.simpleName

        fun newInstance(boardId: String): CameraFragment {
            val fragment = CameraFragment()
            val args = Bundle()
            args.putString(ZoneApplication.getContext().getString(R.string.intent_board_id), boardId)
            fragment.arguments = args
            return fragment
        }
    }
}