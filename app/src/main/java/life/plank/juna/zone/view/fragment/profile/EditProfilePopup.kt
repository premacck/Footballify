package life.plank.juna.zone.view.fragment.profile

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.popup_edit_profile.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.AppConstants.GALLERY_IMAGE_RESULT
import life.plank.juna.zone.util.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.PreferenceManager.*
import life.plank.juna.zone.util.UIDisplayUtil
import life.plank.juna.zone.util.UIDisplayUtil.getDp
import life.plank.juna.zone.util.UIDisplayUtil.getPathForGalleryImageView
import life.plank.juna.zone.util.customToast
import life.plank.juna.zone.util.errorToast
import life.plank.juna.zone.util.facilis.floatUp
import life.plank.juna.zone.util.facilis.sinkDown
import life.plank.juna.zone.util.smartSubscribe
import life.plank.juna.zone.view.fragment.base.BaseBlurPopup
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.sdk27.coroutines.onClick
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.net.HttpURLConnection
import java.text.DateFormatSymbols
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class EditProfilePopup : BaseBlurPopup() {

    @field: [Inject Named("default")]
    lateinit var restApi: RestApi

    private var filePath: String? = null
    private lateinit var profilePicString: String

    companion object {
        val TAG: String = EditProfilePopup::class.java.simpleName
        fun newInstance() = EditProfilePopup()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)
        profilePicString = getSavedProfilePicUrl()
        UIDisplayUtil.checkPermission(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.popup_edit_profile, container, false)

    override fun doOnStart() {
        root_card.floatUp()
        if (!isNullOrEmpty(profilePicString) && profilePicString != getString(R.string.na)) {
            Glide.with(this)
                    .load(profilePicString)
                    .apply(RequestOptions.errorOf(R.drawable.ic_default_profile)
                            .placeholder(R.drawable.ic_default_profile))
                    .into(profile_picture_image_view)
        }
        setOnClickListeners()
    }

    override fun dismiss() {
        root_card.sinkDown()
        super.dismiss()
    }

    override fun getBlurLayout(): BlurLayout? = root_blur_layout

    override fun getDragHandle(): View? = drag_area

    override fun getRootView(): View? = root_card

    override fun getBackgroundLayout(): ViewGroup? = root_blur_layout

    private fun setOnClickListeners() {
        change_picture_button.onClick {
            if (UIDisplayUtil.checkPermission(activity!!)) {
                getImageResourceFromGallery()
            } else {
                customToast(R.string.add_permission)
            }
        }
        dob_edit_text.onClick { showCalendar() }
        save_button.onClick { dismiss() }
    }

    private fun getImageResourceFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, getString(R.string.image_format))
        startActivityForResult(galleryIntent, GALLERY_IMAGE_RESULT)
    }

    private fun showCalendar() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(context!!,
                { _, year, monthOfYear, dayOfMonth ->
                    val dob = dayOfMonth.toString() + " " + DateFormatSymbols().months[monthOfYear] + ", " + year
                    dob_edit_text.setText(dob)
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_IMAGE_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    data?.data?.run {
                        filePath = getPathForGalleryImageView(this@run, ZoneApplication.getContext())
                        uploadProfilePicture(this@run)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "onActivityResult()", e)
                }
            }
        }
    }

    private fun uploadProfilePicture(profilePicUri: Uri) {
        val fileToUpload = File(filePath)
        val requestBody = RequestBody.create(MediaType.parse(getString(R.string.media_type_image)), fileToUpload)
        val image = MultipartBody.Part.createFormData("", fileToUpload.name, requestBody)
        restApi.uploadProfilePicture(image, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { profile_picture_uploading_progress.visibility = View.VISIBLE }
                .doOnTerminate { profile_picture_uploading_progress.visibility = View.GONE }
                .smartSubscribe({
                    Log.e(TAG, it.message)
                    errorToast(R.string.upload_failed, it)
                }, {
                    when (it.code()) {
                        HttpURLConnection.HTTP_NO_CONTENT -> {
                            Glide.with(this@EditProfilePopup)
                                    .load(profilePicUri)
                                    .apply(RequestOptions.overrideOf(getDp(72f).toInt(), getDp(72f).toInt()))
                                    .into(profile_picture_image_view)
                            saveProfilePicUrl(it.body())
                        }
                        else -> errorToast(R.string.upload_failed, it)
                    }
                })
    }
}