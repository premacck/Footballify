package life.plank.juna.zone.view.activity.camera

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore.Images.Media
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.JsonObject
import com.iceteck.silicompressorr.SiliCompressor
import kotlinx.android.synthetic.main.activity_upload.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.camera.PermissionHandler.*
import life.plank.juna.zone.util.common.AppConstants
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.util.common.customToast
import life.plank.juna.zone.util.common.errorToast
import life.plank.juna.zone.util.sharedpreference.PreferenceManager
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.time.DateUtil.getRequestDateStringOfNow
import life.plank.juna.zone.util.view.Image
import life.plank.juna.zone.util.view.UIDisplayUtil
import life.plank.juna.zone.util.view.UIDisplayUtil.*
import life.plank.juna.zone.view.fragment.camera.CameraFragment
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.apache.commons.lang3.ArrayUtils
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Response
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

class UploadActivity : AppCompatActivity() {

    @Inject
    lateinit var restApi: RestApi

    //    private var capturedImageView: ImageView? = null
//    private var profilePicture: ImageView? = null
//    private var titleEditText: TextInputEditText? = null
//    private var capturedVideoView: VideoView? = null
//    private var playBtn: ImageButton? = null
//    private var postBtn: Button? = null
//    private var progressBar: ProgressBar? = null
//    private var scrollView: ScrollView? = null

    private var openFrom: String? = null
    private var userId: String? = null
    private var boardId: String? = null
    private var filePath: String? = null
    private var mHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    fun initListeners() {
        post_btn.setOnClickListener {
            if (title_text.text != null && title_text.text!!.toString().trim { it <= ' ' }.isEmpty()) {
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
        play_btn.setOnClickListener {
            play_btn.visibility = View.GONE
            captured_video_view.start()
        }
        captured_video_view.setOnClickListener {
            play_btn.visibility = View.VISIBLE
            captured_video_view.pause()
        }
    }

    @AfterPermissionGranted(CAMERA_AND_STORAGE_PERMISSIONS)
    private fun handleMediaContent() {
        if (EasyPermissions.hasPermissions(this, *ArrayUtils.addAll(CAMERA_PERMISSIONS, *STORAGE_PERMISSIONS))) {
            when (openFrom) {
                IMAGE -> prepareImageForUpload()
                VIDEO -> {
                    updateUI(VIDEO)
                    VideoCompressionTask(this@UploadActivity, filePath!!, cacheDir.absolutePath).execute()
                }
                GALLERY -> getImageResourceFromGallery()
                AUDIO -> openGalleryForAudio()
            }
        } else {
            requestCameraAndStoragePermissions(this)
        }
    }

    private fun updateUI(type: String) {
        setContentView(R.layout.activity_upload)
        captured_video_view.visibility = if (type == VIDEO) View.VISIBLE else View.GONE
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
                                //                                    TODO: implement audio player using "this.filePath"
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

        restApi!!.postMediaContentToServer(body, boardId, contentType, userId,
                dateCreated, AppConstants.BOARD, title_text.text!!.toString(), getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<JsonObject>>() {
                    override fun onCompleted() {
                        Log.i(TAG, "onCompleted: ")
                    }

                    override fun onError(e: Throwable) {
                        progressDialog.cancel()
                        Log.e(TAG, "onError: postMediaContentToServer$e")
                        progress_bar.visibility = View.INVISIBLE
                        errorToast(R.string.something_went_wrong, e)
                        Toast.makeText(applicationContext, R.string.something_went_wrong, Toast.LENGTH_LONG).show()
                    }

                    override fun onNext(jsonObjectResponse: Response<JsonObject>) {
                        progressDialog.cancel()
                        when (jsonObjectResponse.code()) {
                            HttpsURLConnection.HTTP_CREATED -> {
                                customToast(R.string.upload_successful)
                                finish()
                            }
                            else -> errorToast(R.string.upload_failed, jsonObjectResponse)
                        }
                    }
                })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onDestroy() {
        if (captured_video_view != null && captured_video_view.isPlaying) {
            captured_video_view.stopPlayback()
        }
        if (mHandler != null) {
            mHandler!!.removeCallbacksAndMessages(null)
            mHandler = null
        }
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.float_down, R.anim.sink_down)
    }

    internal class VideoCompressionTask(uploadActivity: UploadActivity, private val videoPath: String, private val destinationPath: String) : AsyncTask<Void, Void, String>() {

        private val ref: WeakReference<UploadActivity> = WeakReference(uploadActivity)

        override fun onPreExecute() {
            super.onPreExecute()
            enableOrDisableView(ref.get()!!.post_btn, false)
            ref.get()!!.progress_bar.visibility = View.VISIBLE
            ref.get()!!.post_btn.setText(R.string.optimizing_video)
        }

        override fun doInBackground(vararg voids: Void): String? {
            try {
                return SiliCompressor.with(ref.get()).compressVideo(videoPath, destinationPath)
            } catch (e: Exception) {
                Log.e(TAG, "doInBackground: Compressing video - ", e)
            }

            return null
        }

        override fun onPostExecute(compressedVideoPath: String?) {
            enableOrDisableView(ref.get()!!.post_btn, true)
            ref.get()!!.post_btn.setText(R.string.post)
            if (compressedVideoPath != null) {
                //                Deleting original video file and using the compressed one.
                ref.get()!!.filePath = compressedVideoPath
                ref.get()!!.captured_video_view.setVideoPath(compressedVideoPath)
                val originalVideo = File(videoPath)
                if (originalVideo.exists()) {
                    originalVideo.delete()
                }
            } else {
                //                Using the original video in case of compression failure.
                ref.get()!!.captured_video_view.setVideoPath(videoPath)
            }
            ref.get()!!.captured_video_view.setOnPreparedListener { mediaPlayer ->
                mediaPlayer.isLooping = true
                ref.get()!!.progress_bar.visibility = View.GONE
                ref.get()!!.play_btn.visibility = View.VISIBLE
                ref.get()!!.mHandler = Handler()
                ref.get()!!.mHandler!!.postDelayed({ ref.get()!!.root_layout.smoothScrollTo(0, ref.get()!!.root_layout.bottom) }, 1500)
            }
        }
    }

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
                if (mediaFilePath != null && mediaFilePath.size > 0) {
                    intent.putExtra(from.getString(R.string.intent_file_path), mediaFilePath[0])
                }
                from.startActivity(intent)
                from.overridePendingTransition(R.anim.float_up, R.anim.sink_up)
            }
        }
    }
}
