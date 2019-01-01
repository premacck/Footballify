@file:Suppress("DEPRECATION")

package life.plank.juna.zone.view.activity.camera

import android.app.*
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.*
import android.provider.MediaStore.Images.Media
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.exoplayer2.SimpleExoPlayer
import com.iceteck.silicompressorr.SiliCompressor
import com.prembros.facilis.util.*
import kotlinx.android.synthetic.main.activity_upload.*
import life.plank.juna.zone.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.camera.PermissionHandler.*
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.util.sharedpreference.PreferenceManager
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.time.DateUtil.getRequestDateStringOfNow
import life.plank.juna.zone.util.toro.*
import life.plank.juna.zone.util.view.*
import life.plank.juna.zone.util.view.UIDisplayUtil.*
import life.plank.juna.zone.view.fragment.camera.CameraFragment
import okhttp3.*
import org.apache.commons.lang3.ArrayUtils
import org.jetbrains.anko.*
import pub.devrel.easypermissions.*
import java.io.File
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection


class UploadActivity : AppCompatActivity() {

    @Inject
    lateinit var restApi: RestApi

    private var openFrom: String? = null
    private var userId: String? = null
    private var boardId: String? = null
    private var filePath: String? = null
    private var mHandler: Handler? = null
    private var exoPlayer: SimpleExoPlayer? = null

    companion object {

        private val TAG = UploadActivity::class.java.canonicalName
        private const val CAMERA_AND_STORAGE_PERMISSIONS = 15

        /**
         * Method to launch [UploadActivity] to upload image or audio which is already saved in gallery or media storage
         * Provide mediaFilePath param when uploading image or video provided from [CameraFragment]
         */
        fun launch(from: Activity, openFrom: String, boardId: String?, vararg mediaFilePath: String) {
            if (boardId != null) {
                val intent = Intent(from, UploadActivity::class.java)
                intent.putExtra(from.getString(R.string.intent_open_from), openFrom)
                intent.putExtra(from.getString(R.string.intent_board_id), boardId)
                if (mediaFilePath.isNotEmpty()) {
                    intent.putExtra(from.getString(R.string.intent_file_path), mediaFilePath[0])
                }
                from.startActivity(intent)
                from.overridePendingTransition(R.anim.float_up, R.anim.sink_up)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        (application as ZoneApplication).uiComponent.inject(this)
        userId = PreferenceManager.CurrentUser.getUserId()

        val intent = intent ?: return

        if (intent.hasExtra(getString(R.string.intent_file_path))) {
            filePath = intent.getStringExtra(getString(R.string.intent_file_path))
        }
        boardId = intent.getStringExtra(getString(R.string.intent_board_id))
        openFrom = intent.getStringExtra(getString(R.string.intent_open_from))
        handleMediaContent()
        initListeners()
    }

    override fun onPause() {
        super.onPause()
        exoPlayer?.pause()
        if (Build.VERSION.SDK_INT <= 23) releaseExoPlayer()
    }

    override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT > 23) releaseExoPlayer()
    }

    private fun releaseExoPlayer() {
        exoPlayer?.release()
        exoPlayer = null
    }

    fun initListeners() {
        post_btn.setOnClickListener {
            if (title_text.text != null && title_text.text!!.toString().trim { char -> char <= ' ' }.isEmpty()) {
                title_text.error = getString(R.string.please_enter_title)
                return@setOnClickListener
            }
            when (openFrom) {
                IMAGE, GALLERY -> postMediaContent(getString(R.string.media_type_image), IMAGE, userId, getRequestDateStringOfNow())
                VIDEO -> postMediaContent(getString(R.string.media_type_video), VIDEO, userId, getRequestDateStringOfNow())
                AUDIO -> postMediaContent(getString(R.string.media_type_audio), AUDIO, userId, getRequestDateStringOfNow())
                else -> Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @AfterPermissionGranted(CAMERA_AND_STORAGE_PERMISSIONS)
    private fun handleMediaContent() {
        if (EasyPermissions.hasPermissions(this, *ArrayUtils.addAll(CAMERA_PERMISSIONS, *STORAGE_PERMISSIONS))) {
            when (openFrom) {
                IMAGE -> prepareImageForUpload()
                VIDEO -> {
                    updateUI(VIDEO)
                    prepareVideoForUpload()
                }
                GALLERY -> getImageResourceFromGallery()
                AUDIO -> openGalleryForAudio()
            }
        } else {
            requestCameraAndStoragePermissions(this)
        }
    }

    private fun updateUI(type: String) {
        video_player_container.visibility = if (type == VIDEO) View.VISIBLE else View.GONE
        captured_image_view.visibility = if (type == VIDEO) View.GONE else View.VISIBLE

        Glide.with(this)
                .load(PreferenceManager.CurrentUser.getProfilePicUrl())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_default_profile)
                        .error(R.drawable.ic_default_profile))
                .into(profile_image_view)
    }

    private fun openGalleryForAudio() {
        val audioIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(Intent.createChooser(audioIntent, getString(R.string.select_audio)), AUDIO_PICKER_RESULT)
    }

    private fun getImageResourceFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.setDataAndType(Media.EXTERNAL_CONTENT_URI, getString(R.string.image_format))
        startActivityForResult(galleryIntent, GALLERY_IMAGE_RESULT)
    }

    private fun prepareImageForUpload() {
        try {
            updateUI(IMAGE)
            val imgFile = File(filePath!!)
            if (imgFile.exists()) {
                val bitmap = Image().compress(imgFile, filePath)
                setImagePreview(bitmap)
            }
        } catch (e: Exception) {
            Log.e("TAG", "CAMERA_IMAGE_RESULT : $e")
            Toast.makeText(applicationContext, R.string.failed_to_process_image, Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun setImagePreview(bitmap: Bitmap) {
        val width = (getScreenSize(windowManager.defaultDisplay)[0] - getDp(16f)).toInt()
        val params = captured_image_view.layoutParams as RelativeLayout.LayoutParams
        params.height = width * bitmap.height / bitmap.width
        captured_image_view.layoutParams = params
        captured_image_view.setImageBitmap(bitmap)
    }

    private fun prepareVideoForUpload() {
        enableOrDisableView(post_btn, false)
        progress_bar.makeVisible()
        play_pause.makeGone()
        post_btn.setText(R.string.optimizing_video)
        doAsync {
            try {
                val compressedVideoPath: String? = SiliCompressor.with(this@UploadActivity).compressVideo(filePath, cacheDir.absolutePath)
                uiThread {
                    progress_bar.makeGone()
                    play_pause.makeVisible()
                    enableOrDisableView(post_btn, true)
                    post_btn.setText(R.string.post)
                    if (compressedVideoPath != null) {
                        filePath = compressedVideoPath
                    }
                    video_player_container.setAspectRatioFrom(filePath!!)
                    prepareVideoPreview(filePath!!)
                }
            } catch (e: Exception) {
                Log.e(TAG, "prepareVideoForUpload(): Compressing video - ", e)
            }
        }
    }

    private fun prepareVideoPreview(videoPath: String) {
        exoPlayer = ExoBuilder.with(this)
                .withMediaSource(Uri.parse(videoPath))
                .withContainer(video_player_container)
                .applyTo(video_player)
                .addPlayPauseListener(video_player, play_pause, progress_bar)
    }

    private fun postMediaContent(mediaType: String, contentType: String, userId: String?, dateCreated: String) {
        if (title_text.text == null || title_text.text!!.toString().trim { it <= ' ' }.isEmpty()) {
            return
        }
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage(getString(R.string.just_a_moment))
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
        val file = File(filePath!!)
        val requestBody: RequestBody
        requestBody = RequestBody.create(MediaType.parse(mediaType), file)
        val body = MultipartBody.Part.createFormData("", file.name, requestBody)

        restApi.postMediaContentToServer(body, boardId, contentType, userId, dateCreated, AppConstants.BOARD, title_text.text?.toString()?.trim(), getToken())
                .onTerminate { progressDialog.cancel() }
                .setObserverThreadsAndSmartSubscribe({
                    Log.e(TAG, "onError: postMediaContentToServer$", it)
                    progress_bar.visibility = View.INVISIBLE
                    errorToast(R.string.something_went_wrong, it)
                    Toast.makeText(applicationContext, R.string.something_went_wrong, Toast.LENGTH_LONG).show()
                }, {
                    when (it.code()) {
                        HttpsURLConnection.HTTP_CREATED -> {
                            customToast(R.string.upload_successful)
                            finish()
                        }
                        else -> errorToast(R.string.upload_failed, it)
                    }
                })
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            AUDIO_PICKER_RESULT -> when (resultCode) {
                Activity.RESULT_OK -> if (data != null) {
                    val uri = data.data
                    if (uri != null) {
                        try {
                            updateUI(VIDEO)
                            filePath = UIDisplayUtil.getAudioPath(uri)
                            val absoluteFile = File(filePath!!)
                            val fileSizeInMB = absoluteFile.length() / (1024 * 1024)
                            if (fileSizeInMB <= 8) {
//                            TODO: implement audio player using "this.filePath"
                            } else {
                                Toast.makeText(this, R.string.file_too_large, Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Log.e("TAG", "AUDIO_PICKER_RESULT : $e")
                            Toast.makeText(this@UploadActivity, R.string.unable_to_process, Toast.LENGTH_SHORT).show()
                            finish()
                        }

                    }
                }
                Activity.RESULT_CANCELED -> finish()
                else -> {
                    Toast.makeText(this, R.string.failed_to_get_audio_file, Toast.LENGTH_LONG).show()
                    finish()
                }
            }
            GALLERY_IMAGE_RESULT -> when (resultCode) {
                Activity.RESULT_OK -> if (data != null) {
                    filePath = getPathForGalleryImageView(data.data, this)
                    prepareImageForUpload()
                }
                Activity.RESULT_CANCELED -> finish()
                else -> {
                    Toast.makeText(this, R.string.failed_to_process_image, Toast.LENGTH_LONG).show()
                    finish()
                }
            }
            else -> {
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onDestroy() {
        mHandler?.removeCallbacksAndMessages(null)
        mHandler = null
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.float_down, R.anim.sink_down)
    }
}
