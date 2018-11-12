package life.plank.juna.zone.view.activity.profile

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_user_profile.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.Board
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.DataUtil
import life.plank.juna.zone.util.PreferenceManager.getToken
import life.plank.juna.zone.util.common.handlePrivateBoardIntentIfAny
import life.plank.juna.zone.util.errorToast
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.activity.home.HomeActivity
import life.plank.juna.zone.view.adapter.GetCoinsAdapter
import life.plank.juna.zone.view.adapter.LastTransactionsAdapter
import life.plank.juna.zone.view.adapter.UserBoardsAdapter
import life.plank.juna.zone.view.fragment.profile.EditProfilePopup
import org.jetbrains.anko.intentFor
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Named

class UserProfileActivity : BaseCardActivity() {

    @field: [Inject Named("default")]
    lateinit var restApi: RestApi
    @Inject
    lateinit var lastTransactionsAdapter: LastTransactionsAdapter
    @Inject
    lateinit var getCoinsAdapter: GetCoinsAdapter

    private var userBoardsAdapter: UserBoardsAdapter? = null

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
        setOnClickListeners()

        handlePrivateBoardIntentIfAny(restApi)
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
    }

    override fun getFragmentContainer(): Int = R.id.popup_container

    private fun initRecyclerView() {
        my_boards_list.layoutManager = GridLayoutManager(applicationContext, 5)
        userBoardsAdapter = UserBoardsAdapter(this, restApi, Glide.with(this), true)
        my_boards_list.adapter = userBoardsAdapter
        get_coins_list.adapter = getCoinsAdapter
        last_transactions_list.adapter = lastTransactionsAdapter
    }

    private fun getUserBoards() {
        restApi.getUserBoards(getToken()).setObserverThreadsAndSmartSubscribe({ Log.e(TAG, "getUserBoards(): ", it) }, {
            when (it.code()) {
                HttpURLConnection.HTTP_OK -> {
                    val boards = it.body() as MutableList<Board>
                    val board = Board(getString(R.string.new_))
                    boards.add(board)
                    userBoardsAdapter?.setUserBoards(boards)
                }
                HttpURLConnection.HTTP_NOT_FOUND -> errorToast(R.string.cannot_find_user_boards, it)
                else -> errorToast(R.string.something_went_wrong, it)
            }
        })
    }

    private fun getUserDetails() {
        restApi.getUser(getToken()).setObserverThreadsAndSmartSubscribe({ Log.e(TAG, "getUserDetails(): ", it) }, {
            val user = it.body()
            if (user != null) {
                name_text_view.text = user.displayName
                email_text_view.text = user.emailAddress
                if (user.profilePictureUrl != null) {
                    Glide.with(this).load(user.profilePictureUrl).into(profile_picture_image_view)
                    settings_toolbar.setProfilePic(user.profilePictureUrl)
                }
                val location: String? = if (!DataUtil.isNullOrEmpty(user.city) && !DataUtil.equalsNullString(user.city)) {
                    user.city + ", " + user.country
                } else {
                    user.country
                }
                location_text_view.text = location
                val editor = getSharedPreferences(getString(R.string.pref_user_details), Context.MODE_PRIVATE).edit()
                editor.putString(getString(R.string.pref_profile_pic_url), user.profilePictureUrl).apply()
                editor.putString(getString(R.string.pref_display_name), user.displayName)
            }
        })
    }
}