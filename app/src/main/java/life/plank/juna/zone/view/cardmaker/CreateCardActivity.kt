package life.plank.juna.zone.view.cardmaker

import android.app.Activity
import android.content.*
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.FaceDetector
import kotlinx.android.synthetic.main.activity_create_card.*
import life.plank.juna.zone.*
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.customToast
import life.plank.juna.zone.view.CardPreviewFragment
import life.plank.juna.zone.view.activity.base.BaseJunaCardActivity
import life.plank.juna.zone.view.activity.camera.CustomCameraActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.io.*
import javax.inject.Inject


class CreateCardActivity : BaseJunaCardActivity() {

    @Inject
    lateinit var restApi: RestApi
    private var filePath: String? = null
    private lateinit var detector: FaceDetector
    private var imageUri: Uri? = null
    private lateinit var editedBitmap: Bitmap

    companion object {
        private val TAG = CreateCardActivity::class.java.simpleName
        fun launch(from: Activity, mediaFilePath: String) {
            val intent = Intent(from, CreateCardActivity::class.java)
            if (mediaFilePath.isNotEmpty()) {
                intent.putExtra(from.getString(R.string.intent_file_path), mediaFilePath)
            }
            from.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_card)
        ZoneApplication.getApplication().uiComponent.inject(this)

        if (intent.hasExtra(getString(R.string.intent_file_path))) {
            filePath = intent.getStringExtra(getString(R.string.intent_file_path))
        }

        if (!filePath.isNullOrEmpty()) {
            doAsync {
                val imageBitmap = BitmapFactory.decodeFile(filePath)
                uiThread {
                    profile_image_view.setImageBitmap(imageBitmap)
                }
            }
        }
        detector = FaceDetector.Builder(applicationContext)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build()
        progress_bar.visibility = View.INVISIBLE
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        camera.onClick {
            CustomCameraActivity.launch(this@CreateCardActivity, "", false)
            finish()
        }
        proceed_button.onClick {
            if (!filePath.isNullOrEmpty()) {
                try {
                    progress_bar.visibility = View.VISIBLE
                    scanFace()
                } catch (e: Exception) {
                    progress_bar.visibility = View.INVISIBLE
                    Log.e(TAG, "scanFace(): ", e)
                }
            }
        }
    }

    private fun scanFace() {

        imageUri = Uri.fromFile(File(filePath))
        val bitmap = decodeBitmapUri(this, imageUri!!)
        if (detector.isOperational && bitmap != null) {
            editedBitmap = Bitmap.createBitmap(bitmap.width, bitmap
                    .height, bitmap.config)

            val canvas = Canvas(editedBitmap)
            canvas.drawBitmap(bitmap, 0f, 0f, null)
            val frame = Frame.Builder().setBitmap(editedBitmap).build()
            val faces = detector.detect(frame)

            if (faces.size() == 0) {
                customToast(R.string.no_face_detected)
            } else {
                pushFragment(CardPreviewFragment.newInstance(filePath!!))
            }
        } else {
            customToast(R.string.failed_to_recognise_face)
        }

        progress_bar.visibility = View.INVISIBLE
    }

    @Throws(FileNotFoundException::class)
    private fun decodeBitmapUri(ctx: Context, uri: Uri): Bitmap? {
        val targetW = 600
        val targetH = 600
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeStream(ctx.contentResolver.openInputStream(uri), null, bmOptions)
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight

        val scaleFactor = Math.min(photoW / targetW, photoH / targetH)
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor

        return BitmapFactory.decodeStream(ctx.contentResolver
                .openInputStream(uri), null, bmOptions)
    }

    override fun getFragmentContainer(): Int = R.id.popup_container

    override fun restApi(): RestApi? = restApi

    override fun onDestroy() {
        super.onDestroy()
        detector.release()
    }
}