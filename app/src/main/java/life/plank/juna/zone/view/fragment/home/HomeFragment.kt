package life.plank.juna.zone.view.fragment.home

import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.android.synthetic.main.custom_search_view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.onboarding_bottom_sheet.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.FeedEntry
import life.plank.juna.zone.data.model.FootballTeam
import life.plank.juna.zone.data.model.UserPreference
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.interfaces.ZoneToolbarListener
import life.plank.juna.zone.util.*
import life.plank.juna.zone.util.AppConstants.BoomMenuPage.BOOM_MENU_FULL
import life.plank.juna.zone.util.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.PreferenceManager.getToken
import life.plank.juna.zone.util.common.launch
import life.plank.juna.zone.view.activity.UserNotificationActivity
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.activity.profile.UserProfileActivity
import life.plank.juna.zone.view.adapter.OnboardingAdapter
import life.plank.juna.zone.view.adapter.UserBoardsAdapter
import life.plank.juna.zone.view.adapter.UserFeedAdapter
import life.plank.juna.zone.view.adapter.UserZoneAdapter
import life.plank.juna.zone.view.fragment.base.FlatTileFragment
import life.plank.juna.zone.view.fragment.clickthrough.FeedItemPeekPopup
import net.openid.appauth.AuthorizationService
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Named

class HomeFragment : FlatTileFragment(), ZoneToolbarListener, TextWatcher {

    @Inject
    lateinit var gson: Gson
    @field: [Inject Named("default")]
    lateinit var restApi: RestApi
    @field: [Inject Named("footballData")]
    lateinit var footballRestApi: RestApi

    private var authService: AuthorizationService? = null
    private var onBoardingBottomSheetBehavior: BottomSheetBehavior<*>? = null
    private var onBoardingAdapter: OnboardingAdapter? = null
    private var userFeedAdapter: UserFeedAdapter? = null
    private var userZoneAdapter: UserZoneAdapter? = null
    private var userBoardsAdapter: UserBoardsAdapter? = null
    private val userPreferences = ArrayList<UserPreference>()
    private var feedEntries = ArrayList<FeedEntry>()
    private var teamList = ArrayList<FootballTeam>()

    companion object {
        private val TAG = HomeFragment::class.java.simpleName
        fun newInstance(): HomeFragment = HomeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val editor = ZoneApplication.getContext().getSharedPreferences(getString(R.string.pref_user_details), MODE_PRIVATE)
        val userObjectId = editor.getString(ZoneApplication.getContext().getString(R.string.pref_object_id), getString(R.string.na))

        val topic = getString(R.string.juna_user_topic) + userObjectId!!
        FirebaseMessaging.getInstance().subscribeToTopic(topic)

        setupOnBoardingBottomSheet()
        initBottomSheetRecyclerView()

        initRecyclerView()
        initZoneRecyclerView()
        initBoardsRecyclerView()

        getUserZones()

        setUpToolbarAndBoomMenu()
        arc_menu.setupWith(nestedScrollView)

        getUserFeed()

        feed_header.initListeners(this)
        feed_header.setProfilePic(editor.getString(getString(R.string.pref_profile_pic_url), null))
        search_edit_text.addTextChangedListener(this)
    }


    override fun onResume() {
        super.onResume()
        getUserBoards()
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (charSequence != null) {
            if (!charSequence.isEmpty()) {
                getFootballTeams(charSequence.toString())
            } else {
                teamList.clear()
                onBoardingAdapter?.notifyDataSetChanged()
            }
        }

    }

    private fun initBottomSheetRecyclerView() {
        onBoardingAdapter = OnboardingAdapter(activity, teamList)
        onboarding_recycler_view.adapter = onBoardingAdapter
    }

    private fun setupOnBoardingBottomSheet() {
        onBoardingBottomSheetBehavior = BottomSheetBehavior.from(onboarding_bottom_sheet)
        onBoardingBottomSheetBehavior?.peekHeight = 0
    }

    private fun setUpToolbarAndBoomMenu() {
        if (isNullOrEmpty(getToken())) {
            feed_header.setProfilePic(R.drawable.ic_default_profile)
            arc_menu.visibility = View.GONE
            feed_header.isNotificationViewVisible(View.GONE)
            feed_header.userGreeting = getString(R.string.hello_stranger)
        } else {
            setupBoomMenu(BOOM_MENU_FULL, activity!!, null, arc_menu, null)
        }
    }

    private fun initRecyclerView() {
        userFeedAdapter = UserFeedAdapter(this, Glide.with(this))
        user_feed_recycler_view.adapter = userFeedAdapter
    }

    private fun initZoneRecyclerView() {
        userZoneAdapter = UserZoneAdapter(activity!!, userPreferences)
        user_zone_recycler_view?.adapter = userZoneAdapter
    }

    private fun initBoardsRecyclerView() {
        if (activity is BaseCardActivity) {
            userBoardsAdapter = UserBoardsAdapter(activity as BaseCardActivity, restApi, footballRestApi, Glide.with(this), false)
            user_boards_recycler_view?.adapter = userBoardsAdapter
        }
    }

    private fun setUpUserZoneAdapter(userPreferenceList: List<UserPreference>?) {
        userPreferences.clear()
        userPreferences.addAll(userPreferenceList!!)
        userZoneAdapter!!.notifyDataSetChanged()
    }

    private fun getFootballTeams(teamName: String) {
        restApi.getSearchedFootballTeams(teamName, getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "getFootballTeamDetails: ", it)
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_OK -> {
                    onBoardingAdapter?.setTeamList(it.body())
                }
                HttpURLConnection.HTTP_NOT_FOUND -> {
                    teamList.clear()
                    onBoardingAdapter?.notifyDataSetChanged()
                }
                else -> errorToast(R.string.team_not_found, it)
            }
        })
    }

    private fun getUserZones() {
        if (isNullOrEmpty(getToken())) {
            user_zone_recycler_view.visibility = View.GONE
            return
        }
        restApi.getUser(getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "getUserZones(): onError: ", it)
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_OK -> {
                    val user = it.body()
                    if (user != null) {
                        setUpUserZoneAdapter(user.userPreferences)
                    }
                }
                else -> errorToast(R.string.failed_to_retrieve_zones, it)
            }
        })
    }

    private fun getUserFeed() {
        val userFeedApiCall = if (isNullOrEmpty(getToken())) restApi.getUserFeed() else restApi.getUserFeed(getToken())
        userFeedApiCall.setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "getUserFeed(): onError(): ", it)
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_OK -> {
                    feedEntries = it.body() as ArrayList<FeedEntry>
                    if (!isNullOrEmpty(feedEntries)) {
                        userFeedAdapter!!.setUserFeed(feedEntries)
                    } else
                        errorToast(R.string.failed_to_retrieve_feed, it)
                }
                else -> errorToast(R.string.failed_to_retrieve_feed, it)
            }
        })
    }

    private fun getUserBoards() {
        if (isNullOrEmpty(getToken())) return

        restApi.getFollowingBoards(getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "getUserBoards(): onError(): ", it)
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_OK -> {
                    if (!isNullOrEmpty(it.body())) {
                        userBoardsAdapter!!.setUserBoards(it.body()!!)
                    } else
                        user_boards_recycler_view.visibility = View.GONE
                }
                HttpURLConnection.HTTP_NOT_FOUND -> user_boards_recycler_view.visibility = View.GONE
                else -> errorToast(R.string.failed_to_retrieve_board, it)
            }
        })
    }

    override fun profilePictureClicked(profilePicture: ImageView) {
        if (isNullOrEmpty(getToken())) {
            showPopup()
        } else {
            activity?.launch<UserProfileActivity>()
        }
    }

    override fun notificationIconClicked(notificationIcon: ImageView) {
        if (isNullOrEmpty(getToken())) {
            showPopup()
        } else {
            UserNotificationActivity.launch(activity)
        }
    }

    private fun showPopup() {
        val signUpDialog = Dialog(context!!)
        authService = AuthorizationService(context!!)
        signUpDialog.setContentView(R.layout.signup_dialogue)

        signUpDialog.findViewById<View>(R.id.drag_handle).setOnClickListener { signUpDialog.dismiss() }

        signUpDialog.findViewById<View>(R.id.signup_button).setOnClickListener { AuthUtil.loginOrRefreshToken(activity, authService, null, false) }
        val window = signUpDialog.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        signUpDialog.show()
    }

    override fun updateFullScreenAdapter(feedEntryList: List<FeedEntry>) {}

    override fun showFeedItemPeekPopup(position: Int) = pushPopup(FeedItemPeekPopup.newInstance(feedEntries, null, true, null, position))

    override fun onDestroy() {
        if (authService != null) {
            authService!!.dispose()
        }
        onBoardingAdapter = null
        userBoardsAdapter = null
        userFeedAdapter = null
        userZoneAdapter = null
        super.onDestroy()
    }
}