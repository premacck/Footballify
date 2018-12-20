package life.plank.juna.zone.view.activity.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.zone_tool_bar.*
import life.plank.juna.zone.*
import life.plank.juna.zone.data.model.Board
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.sharedpreference.PreferenceManager
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.time.DateUtil.getIsoFormattedDate
import life.plank.juna.zone.util.view.UIDisplayUtil
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.activity.home.HomeActivity
import life.plank.juna.zone.view.adapter.user.*
import life.plank.juna.zone.view.controller.BoardController
import life.plank.juna.zone.view.fragment.profile.*
import okhttp3.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.*
import java.io.File
import java.net.HttpURLConnection
import java.text.DateFormatSymbols
import java.util.*
import java.util.Calendar.*
import javax.inject.Inject


class UserProfileActivity : BaseCardActivity() {

    @Inject
    lateinit var restApi: RestApi
    @Inject
    lateinit var lastTransactionsAdapter: LastTransactionsAdapter
    @Inject
    lateinit var getCoinsAdapter: GetCoinsAdapter

    private var boardController: BoardController? = null
    private var filePath: String? = null

    companion object {
        private val TAG = UserProfileActivity::class.java.simpleName
        fun launch(from: Activity) = from.startActivity(from.intentFor<UserProfileActivity>())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        ZoneApplication.getApplication().uiComponent.inject(this)

        settings_toolbar.title = getString(R.string.settings)
        initRecyclerView()
        getUserBoards()

        initViews()
        setOnClickListeners()

        handleBoardIntentIfAny()
    }

    private fun initViews() {
        comment_enter_btn_switch.run {
            isChecked = PreferenceManager.App.isEnterToSend()
            text = if (isChecked) textOn else textOff
        }
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    private fun setOnClickListeners() {
        edit_profile_button.onDebouncingClick {
            pushPopup(EditProfilePopup.newInstance())
        }
        home_fab.onDebouncingClick { HomeActivity.launch(this, true) }
        profile_picture_image_view.onClick {
            if (UIDisplayUtil.checkPermission(this@UserProfileActivity)) {
                getImageResourceFromGallery()
            } else {
                customToast(R.string.add_permission)
            }
        }
        toolbar_profile_pic.onDebouncingClick {
            pushFragment(ProfileCardFragment.newInstance(), true)
        }
        comment_enter_btn_switch.onCheckedChange { _, isChecked ->
            PreferenceManager.App.saveEnterToSend(isChecked)
            comment_enter_btn_switch.text = if (isChecked) comment_enter_btn_switch.textOn else comment_enter_btn_switch.textOff
        }
    }

    private fun getImageResourceFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, getString(R.string.image_format))
        startActivityForResult(galleryIntent, AppConstants.GALLERY_IMAGE_RESULT)
    }

    override fun getFragmentContainer(): Int = R.id.popup_container

    override fun restApi(): RestApi? = restApi

    private fun initRecyclerView() {
        boardController = BoardController(this, restApi, true)
        my_boards_list.setController(boardController!!)
        get_coins_list.adapter = getCoinsAdapter
        last_transactions_list.adapter = lastTransactionsAdapter
    }

    private fun getUserBoards() {
        restApi.getUserBoards(getToken()).setObserverThreadsAndSmartSubscribe({ Log.e(TAG, "getUserBoards(): ", it) }, {
            when (it.code()) {
                HttpURLConnection.HTTP_OK -> {
                    doAsync {
                        val boards = it.body() as MutableList<Board>
                        if (!boards.any { board -> board.name == getString(R.string.new_) }) {
                            boards.add(Board(getString(R.string.new_)))
                        }
                        uiThread { boardController?.setData(boards, false, null) }
                    }
                }
                HttpURLConnection.HTTP_NOT_FOUND -> errorToast(R.string.cannot_find_user_boards, it)
                else -> errorToast(R.string.something_went_wrong, it)
            }
        })
    }

    private fun getUserDetails() {
        restApi.getUser(getToken()).setObserverThreadsAndSmartSubscribe({ Log.e(TAG, "getUserDetails(): ", it) }, {
            val user = it.body()
            user?.run {
                PreferenceManager.CurrentUser.saveUser(user)
                name_text_view.text = displayName
                email_text_view.text = emailAddress
                username_text_view.text = handle

                val date = getIsoFormattedDate(dateOfBirth)
                val calendar = Calendar.getInstance()
                calendar.time = date
                val dob = "${calendar.get(DAY_OF_MONTH)} ${DateFormatSymbols().shortMonths[calendar.get(MONTH)]}, ${calendar.get(YEAR)}"
                dob_text_view.text = dob
                if (profilePictureUrl != null) {
                    Glide.with(this@UserProfileActivity).load(profilePictureUrl).into(profile_picture_image_view)
                    settings_toolbar.setProfilePic(profilePictureUrl!!)
                }
                val location: String? = if (!DataUtil.isNullOrEmpty(city) && !DataUtil.equalsNullString(city)) {
                    "$city, $country"
                } else {
                    country
                }
                PreferenceManager.CurrentUser.saveLocation(location)
                location_text_view.text = location
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstants.GALLERY_IMAGE_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    data?.data?.run {
                        filePath = UIDisplayUtil.getPathForGalleryImageView(this@run, ZoneApplication.getContext())
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
        restApi.uploadProfilePicture(image, PreferenceManager.Auth.getToken())
                .setObserverThreadsAndSmartSubscribe({
                    Log.e(TAG, it.message)
                    errorToast(R.string.upload_failed, it)
                }, {
                    when (it.code()) {
                        HttpURLConnection.HTTP_NO_CONTENT -> {
                            Glide.with(this)
                                    .load(profilePicUri)
                                    .apply(RequestOptions.overrideOf(UIDisplayUtil.getDp(72f).toInt(), UIDisplayUtil.getDp(72f).toInt()))
                                    .into(profile_picture_image_view)
                            Glide.with(this).load(profilePicUri).into(toolbar_profile_pic)
                            PreferenceManager.CurrentUser.saveProfilePicUrl(it.body())
                        }
                        else -> errorToast(R.string.upload_failed, it)
                    }
                })
    }
}