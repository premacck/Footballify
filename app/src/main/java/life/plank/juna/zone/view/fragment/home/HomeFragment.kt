package life.plank.juna.zone.view.fragment.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.shimmer_user_boards.*
import kotlinx.android.synthetic.main.shimmer_user_feed.*
import kotlinx.android.synthetic.main.shimmer_user_zones.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.FeedEntry
import life.plank.juna.zone.data.model.UserPreference
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.interfaces.ZoneToolbarListener
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.util.common.AppConstants.BoomMenuPage.BOOM_MENU_BASIC_INTERACTION
import life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.customview.ShimmerRelativeLayout
import life.plank.juna.zone.util.facilis.doAfterDelay
import life.plank.juna.zone.util.sharedpreference.PreferenceManager
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.sharedpreference.isSubscribed
import life.plank.juna.zone.util.sharedpreference.subscribeTo
import life.plank.juna.zone.util.view.boomMenu
import life.plank.juna.zone.util.view.setupBoomMenu
import life.plank.juna.zone.util.view.setupWith
import life.plank.juna.zone.view.activity.UserNotificationActivity
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.activity.profile.UserProfileActivity
import life.plank.juna.zone.view.adapter.board.user.UserBoardsAdapter
import life.plank.juna.zone.view.adapter.user.UserFeedAdapter
import life.plank.juna.zone.view.adapter.user.UserZoneAdapter
import life.plank.juna.zone.view.fragment.base.FlatTileFragment
import life.plank.juna.zone.view.fragment.clickthrough.FeedItemPeekPopup
import net.openid.appauth.AuthorizationService
import java.net.HttpURLConnection
import javax.inject.Inject

class HomeFragment : FlatTileFragment(), ZoneToolbarListener {

    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var restApi: RestApi

    private var authService: AuthorizationService? = null
    private var userFeedAdapter: UserFeedAdapter? = null
    private var userZoneAdapter: UserZoneAdapter? = null
    private var userBoardsAdapter: UserBoardsAdapter? = null
    private val userPreferences = ArrayList<UserPreference>()
    private var feedEntries = ArrayList<FeedEntry>()

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
        PreferenceManager.CurrentUser.getUserId()?.run {
            val topic = getString(R.string.juna_user_topic) + this
            if (!isSubscribed(topic)) subscribeTo(topic)
        }

        startShimmers()
        initBoardsRecyclerView()
        initFeedRecyclerView()
        initZoneRecyclerView()

        context?.doAfterDelay(1000) {
            getUserZones()
            getUserFeed(false)
            swipe_refresh_layout?.setOnRefreshListener { getUserFeed(true) }
        }

        setUpToolbarAndBoomMenu()
        boomMenu().setupWith(user_feed_recycler_view)

        feed_header.initListeners(this)
        if (PreferenceManager.CurrentUser.getProfilePicUrl() != null) {
            feed_header.setProfilePic(PreferenceManager.CurrentUser.getProfilePicUrl())
        }
    }

    override fun onResume() {
        super.onResume()
        getUserBoards()
    }

    private fun setUpToolbarAndBoomMenu() {
        if (isNullOrEmpty(getToken())) {
            feed_header.setProfilePic(R.drawable.ic_default_profile)
            boomMenu().visibility = View.GONE
            feed_header.isNotificationViewVisible(View.GONE)
            feed_header.userGreeting = getString(R.string.hello_stranger)
        } else {
            //TODO: show notification badge only if there are new notifications
            feed_header.showNotificationBadge(true)
            setupBoomMenu(BOOM_MENU_BASIC_INTERACTION, activity!!, null, null)
        }
    }

    private fun initFeedRecyclerView() {
        userFeedAdapter = UserFeedAdapter(this, Glide.with(this))
        user_feed_recycler_view.adapter = userFeedAdapter
    }

    private fun initZoneRecyclerView() {
        userZoneAdapter = UserZoneAdapter(activity!!, restApi, userPreferences)
        user_zone_recycler_view?.adapter = userZoneAdapter
    }

    private fun initBoardsRecyclerView() {
        if (activity is BaseCardActivity) {
            userBoardsAdapter = UserBoardsAdapter(activity as BaseCardActivity, restApi, Glide.with(this), false)
            user_boards_recycler_view?.adapter = userBoardsAdapter
        }
    }

    private fun setUpUserZoneAdapter(userPreferenceList: List<UserPreference>?) {
        userPreferences.clear()
        userPreferences.addAll(userPreferenceList!!)
        userZoneAdapter?.notifyDataSetChanged()
    }

    private fun getUserZones() {
        if (isNullOrEmpty(getToken())) {
            onRecyclerViewContentsFailedToLoad(user_zone_recycler_view, shimmer_user_zones)
            shimmer_user_zones.visibility = View.GONE
            user_zone_recycler_view.visibility = View.GONE
            return
        }
        restApi.getUser(getToken())
                .setObserverThreadsAndSmartSubscribe({
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
                }, this)
    }

    private fun getUserFeed(isRefreshing: Boolean) {
        if (isRefreshing) {
            onRecyclerViewContentsLoaded(user_feed_recycler_view, shimmer_user_feed, true)
        }
        val userFeedApiCall = if (isNullOrEmpty(getToken())) restApi.getUserFeed() else restApi.getUserFeed(getToken())
        userFeedApiCall
                .onTerminate { if (isRefreshing) swipe_refresh_layout.isRefreshing = false }
                .setObserverThreadsAndSmartSubscribe({
                    Log.e(TAG, "getUserFeed(): onError(): ", it)
                }, {
                    when (it.code()) {
                        HttpURLConnection.HTTP_OK -> {
                            feedEntries = it.body() as ArrayList<FeedEntry>
                            if (!isNullOrEmpty(feedEntries)) {
                                userFeedAdapter?.setUserFeed(feedEntries)
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
                }, this)
    }

    private fun getUserBoards() {
        if (isNullOrEmpty(getToken())) {
            shimmer_user_boards.visibility = View.GONE
            user_boards_recycler_view.visibility = View.GONE
            return
        }

        restApi.getFollowingBoards(getString(R.string.football), getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "getUserBoards(): onError(): ", it)
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_OK -> {
                    if (!isNullOrEmpty(it.body())) {
                        userBoardsAdapter?.setUserBoards(it.body()!!)
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
        }, this)
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
            UserNotificationActivity.launch(context!!)
            feed_header.showNotificationBadge(false)
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
        shimmer_user_boards.startShimmerAnimation()
        shimmer_user_feed.startShimmerAnimation()
        shimmer_user_zones.startShimmerAnimation()
    }

    private fun onRecyclerViewContentsLoaded(recyclerView: androidx.recyclerview.widget.RecyclerView, shimmerRelativeLayout: ShimmerRelativeLayout, isLoading: Boolean = false) {
        recyclerView.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        if (isLoading) shimmerRelativeLayout.startShimmerAnimation() else shimmerRelativeLayout.stopShimmerAnimation()
        shimmerRelativeLayout.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun onRecyclerViewContentsFailedToLoad(recyclerView: androidx.recyclerview.widget.RecyclerView, shimmerRelativeLayout: ShimmerRelativeLayout) {
        recyclerView.visibility = View.INVISIBLE
        shimmerRelativeLayout.stopShimmerAnimation()
        (shimmerRelativeLayout as? ShimmerRelativeLayout)?.alpha = 0.2f
    }

    override fun updateFullScreenAdapter(feedEntryList: List<FeedEntry>) {}

    override fun showFeedItemPeekPopup(position: Int) = pushPopup(FeedItemPeekPopup.newInstance(feedEntries, null, true, null, position))

    override fun onDestroy() {
        authService?.dispose()
        userBoardsAdapter = null
        userFeedAdapter = null
        userZoneAdapter = null
        super.onDestroy()
    }
}