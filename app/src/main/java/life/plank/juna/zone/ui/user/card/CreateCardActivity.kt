package life.plank.juna.zone.ui.user.card

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.FaceDetector
import android.net.Uri
import android.os.Bundle
import com.bumptech.glide.Glide
import com.prembros.facilis.util.*
import kotlinx.android.synthetic.main.activity_create_card.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.api.*
import life.plank.juna.zone.data.local.CardMockData
import life.plank.juna.zone.data.model.card.*
import life.plank.juna.zone.service.CommonDataService.findString
import life.plank.juna.zone.sharedpreference.CurrentUser
import life.plank.juna.zone.ui.base.BaseJunaCardActivity
import life.plank.juna.zone.ui.camera.CustomCameraActivity
import life.plank.juna.zone.util.common.customToast
import life.plank.juna.zone.util.common.errorToast
import org.jetbrains.anko.*
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.net.HttpURLConnection.*
import javax.inject.Inject

class CreateCardActivity : BaseJunaCardActivity() {

    @Inject
    lateinit var restApi: RestApi
    private var filePath: String? = null
    private var junaCard: JunaCard? = null
    private var detector: FaceDetector? = null
    private var imageUri: Uri? = null

    companion object {
        const val GET_PHOTO_FOR_CARD = 101
        const val MAX_FACES_LIMIT = 20
        const val CREATE_CARD_REQUEST_CODE = 1
        private val TAG = CreateCardActivity::class.java.simpleName
        fun launch(from: Activity, mediaFilePath: String? = null) = from.run {
            startActivityForResult(intentFor<CreateCardActivity>(findString(R.string.intent_file_path) to mediaFilePath), CREATE_CARD_REQUEST_CODE)
        }
        fun launch(from: Activity, junaCard: JunaCard) = from.run {
            startActivityForResult(intentFor<CreateCardActivity>(findString(R.string.intent_juna_card) to junaCard), CREATE_CARD_REQUEST_CODE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_card)
        ZoneApplication.application.uiComponent.inject(this)

        name_text_view.text = CurrentUser.displayName
        when {
            intent.hasExtra(getString(R.string.intent_file_path)) ->
                resolveCardPicForCreation(intent)
            intent.hasExtra(getString(R.string.intent_juna_card)) ->
                resolveCardPicForEditing()
        }
    }

    private fun resolveCardPicForCreation(intent: Intent) {
        filePath = intent.getStringExtra(getString(R.string.intent_file_path))
        if (!isNullOrEmpty(filePath)) no_photo_text_view.makeGone() else no_photo_text_view.makeVisible()
        proceed_button.setText(R.string.create_card)

        doAsync {
            var bitmap: Bitmap? = null
            if (!isNullOrEmpty(filePath)) {
                bitmap = BitmapFactory.decodeFile(filePath)
            }
            uiThread {
                profile_pic.setImageBitmap(bitmap)

                setOnClickListeners(false)
            }
        }
    }

    private fun resolveCardPicForEditing() {
        junaCard = intent.getParcelableExtra(getString(R.string.intent_juna_card))
        Glide.with(this).load(junaCard?.template?.issuer?.cardPictureUrl).into(profile_pic)

        no_photo_text_view.makeGone()
        proceed_button.setText(R.string.update_card)
        setOnClickListeners(true)
    }

    private fun setOnClickListeners(isEditing: Boolean) {
        arrayOf(camera, profile_pic, no_photo_text_view).onClick {
            CustomCameraActivity.launch(this@CreateCardActivity, "", false)
        }
        proceed_button.onDebouncingClick {
//        TODO: remove following line and un-comment the commented part below to use API calls instead of mocked data
            handleCardResponse(Response.success(CardMockData.randomMockedCardTemplate))

            /*if (!isNullOrEmpty(filePath)) {
                try {
                    scanFace(isEditing)
                } catch (e: Exception) {
                    Log.e(TAG, "scanFace(): ", e)
                }
            } else customToast(R.string.select_image_to_upload)*/
        }
    }

    private fun scanFace(isEditing: Boolean) {
        progress_bar.makeVisible()
        val faces = arrayOfNulls<FaceDetector.Face>(MAX_FACES_LIMIT)
        doAsync {
            imageUri = Uri.fromFile(File(filePath))
            val bitmap = decodeBitmapUri(imageUri!!)
            if (bitmap != null) {
                bitmap.let { localBitmap ->
                    detector = FaceDetector(localBitmap.width, localBitmap.height, MAX_FACES_LIMIT)
                }
                val faceCount = detector?.findFaces(bitmap, faces) ?: 0

                uiThread {
                    progress_bar.makeGone()
                    if (faceCount == 0) {
                        customToast(R.string.no_face_detected)
                    } else {
                        if (isEditing) updateCard() else createCard()
                    }
                }
            } else {
                uiThread {
                    customToast(R.string.failed_to_recognise_face)
                    progress_bar.makeGone()
                }
            }
        }
    }

    @Throws(FileNotFoundException::class)
    private fun decodeBitmapUri(uri: Uri): Bitmap? {
        val targetW = 600
        val targetH = 600
        val bmOptions = BitmapFactory.Options()
        bmOptions.inPreferredConfig = Bitmap.Config.RGB_565         //Required for Face detection!
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeStream(contentResolver.openInputStream(uri), null, bmOptions)
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight

        val scaleFactor = Math.min(photoW / targetW, photoH / targetH)
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor

        return BitmapFactory.decodeStream(contentResolver.openInputStream(uri), null, bmOptions)
    }

    private fun createCard() {
        if (isNullOrEmpty(filePath)) {
            no_photo_text_view.makeVisible()
            return
        }
        restApi.createCard("BLUE", File(filePath).createMultiPartImage())
                .setObserverThreadsAndSmartSubscribe({
                    errorToast(R.string.failed_to_create_card, it)
                }, { handleCardResponse(it) })
    }

    private fun updateCard() {
        if (isNullOrEmpty(filePath)) {
            customToast(R.string.cannot_update_same_photo_twice)
            return
        }
        restApi.updateCard(junaCard!!.template.cardColor, File(filePath).createMultiPartImage())
                .setObserverThreadsAndSmartSubscribe({
                    errorToast(R.string.failed_to_update_card, it)
                }, { handleCardResponse(it) })
    }

    private fun handleCardResponse(response: Response<JunaCardTemplate>) {
        when (response.code()) {
            HTTP_OK, HTTP_CREATED -> response.body()?.run {
                setResult(RESULT_OK, Intent().putExtra(getString(R.string.intent_juna_card), this))
            } ?: setResult(RESULT_CANCELED, Intent().putExtra(getString(R.string.response_code), response.code()))
            else -> setResult(RESULT_CANCELED, Intent().putExtra(getString(R.string.response_code), response.code()))
        }
        finish()
    }

    override fun getFragmentContainer(): Int = R.id.popup_container

    override fun restApi(): RestApi? = restApi

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            GET_PHOTO_FOR_CARD -> if (resultCode == Activity.RESULT_OK && data != null) resolveCardPicForCreation(data)
        }
    }
}