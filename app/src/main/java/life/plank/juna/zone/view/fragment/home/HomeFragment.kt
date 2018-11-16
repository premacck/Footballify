package life.plank.juna.zone.view.fragment.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.RecyclerView
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
import life.plank.juna.zone.util.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.common.launch
import life.plank.juna.zone.util.customview.ShimmerRelativeLayout
import life.plank.juna.zone.util.facilis.doAfterDelay
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
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Named

class HomeFragment : FlatTileFragment(), ZoneToolbarListener {

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
        val userId = PreferenceManager.CurrentUser.getUserId()

        val topic = getString(R.string.juna_user_topic) + userId!!
        FirebaseMessaging.getInstance().subscribeToTopic(topic)

        setupOnBoardingBottomSheet()
        initBottomSheetRecyclerView()

        startShimmers()
        initBoardsRecyclerView()
        initFeedRecyclerView()
        initZoneRecyclerView()

        context?.doAfterDelay(1000) {
            getUserZones()
            getUserFeed()
        }

        setUpToolbarAndBoomMenu()
        arc_menu.setupWith(nestedScrollView)

        prepareSearchEditText()

        feed_header.initListeners(this)
        feed_header.setProfilePic(PreferenceManager.CurrentUser.getProfilePicUrl())
    }

    override fun onResume() {
        super.onResume()
        context?.doAfterDelay(1000) { getUserBoards() }
    }

    private fun initBottomSheetRecyclerView() {
        onBoardingAdapter = OnboardingAdapter(activity, teamList)
        onboarding_recycler_view.adapter = onBoardingAdapter
    }

    private fun setupOnBoardingBottomSheet() {
        onBoardingBottomSheetBehavior = BottomSheetBehavior.from(onboarding_bottom_sheet)
        onBoardingBottomSheetBehavior?.peekHeight = 0
    }

    private fun prepareSearchEditText() {
        search_edit_text.textChangedListener {
            onTextChanged { charSequence, _, _, _ ->
                if (!isNullOrEmpty(charSequence.toString())) {
                    getFootballTeams(charSequence.toString())
                } else {
                    teamList.clear()
                    onBoardingAdapter?.notifyDataSetChanged()
                }
            }
        }
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

    private fun initFeedRecyclerView() {
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
            onRecyclerViewContentsFailedToLoad(user_zone_recycler_view, shimmer_user_zones)
            shimmer_user_zones.visibility = View.GONE
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
                        onRecyclerViewContentsLoaded(user_zone_recycler_view, shimmer_user_zones)
                    } else {
                        onRecyclerViewContentsFailedToLoad(user_zone_recycler_view, shimmer_user_zones)
                    }
                }
                else -> {
                    errorToast(R.string.failed_to_retrieve_zones, it)
                    onRecyclerViewContentsFailedToLoad(user_zone_recycler_view, shimmer_user_zones)
                }
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
                        onRecyclerViewContentsLoaded(user_feed_recycler_view, shimmer_user_feed)
                    } else {
                        errorToast(R.string.failed_to_retrieve_feed, it)
                        onRecyclerViewContentsFailedToLoad(user_feed_recycler_view, shimmer_user_feed)
                    }
                }
                HttpURLConnection.HTTP_NOT_FOUND -> {
                    onRecyclerViewContentsFailedToLoad(user_feed_recycler_view, shimmer_user_feed)
                }
                else -> {
                    errorToast(R.string.failed_to_retrieve_feed, it)
                    onRecyclerViewContentsFailedToLoad(user_feed_recycler_view, shimmer_user_feed)
                }
            }
        })
    }

    private fun getUserBoards() {
        if (isNullOrEmpty(getToken())) {
            shimmer_user_boards.visibility = View.GONE
            user_boards_recycler_view.visibility = View.GONE
            return
        }

        restApi.getFollowingBoards(getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "getUserBoards(): onError(): ", it)
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_OK -> {
                    if (!isNullOrEmpty(it.body())) {
                        userBoardsAdapter!!.setUserBoards(it.body()!!)
                        onRecyclerViewContentsLoaded(user_boards_recycler_view, shimmer_user_boards)
                    } else onRecyclerViewContentsFailedToLoad(user_boards_recycler_view, shimmer_user_boards)
                }
                HttpURLConnection.HTTP_NOT_FOUND -> {
                    shimmer_user_boards.visibility = View.GONE
                    user_boards_recycler_view.visibility = View.GONE
                }
                else -> {
                    errorToast(R.string.failed_to_retrieve_board, it)
                    onRecyclerViewContentsFailedToLoad(user_boards_recycler_view, shimmer_user_boards)
                }
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

    private fun startShimmers() {
        (shimmer_user_boards as ShimmerRelativeLayout).startShimmerAnimation()
        (shimmer_user_feed as ShimmerRelativeLayout).startShimmerAnimation()
        (shimmer_user_zones as ShimmerRelativeLayout).startShimmerAnimation()
    }

    private fun onRecyclerViewContentsLoaded(recyclerView: RecyclerView, shimmerRelativeLayout: View) {
        recyclerView.visibility = View.VISIBLE
        (shimmerRelativeLayout as? ShimmerRelativeLayout)?.stopShimmerAnimation()
        shimmerRelativeLayout.visibility = View.INVISIBLE
    }

    private fun onRecyclerViewContentsFailedToLoad(recyclerView: RecyclerView, shimmerRelativeLayout: View) {
        recyclerView.visibility = View.INVISIBLE
        (shimmerRelativeLayout as? ShimmerRelativeLayout)?.stopShimmerAnimation()
        (shimmerRelativeLayout as? ShimmerRelativeLayout)?.alpha = 0.2f
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