package life.plank.juna.zone.view.fragment.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.prembros.facilis.util.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.shimmer_user_boards.*
import kotlinx.android.synthetic.main.shimmer_user_feed.*
import kotlinx.android.synthetic.main.shimmer_user_zones.*
import life.plank.juna.zone.*
import life.plank.juna.zone.data.model.FeedEntry
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.interfaces.ZoneToolbarListener
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.util.common.AppConstants.BoomMenuPage.BOOM_MENU_BASIC_INTERACTION
import life.plank.juna.zone.util.customview.ShimmerRelativeLayout
import life.plank.juna.zone.util.sharedpreference.*
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.view.*
import life.plank.juna.zone.view.activity.UserNotificationActivity
import life.plank.juna.zone.view.activity.base.BaseJunaCardActivity
import life.plank.juna.zone.view.activity.profile.UserProfileActivity
import life.plank.juna.zone.view.controller.*
import life.plank.juna.zone.view.fragment.base.FlatTileFragment
import life.plank.juna.zone.view.fragment.clickthrough.FeedItemPeekPopup
import life.plank.juna.zone.view.fragment.post.PostDetailContainerFragment
import net.openid.appauth.AuthorizationService
import java.net.HttpURLConnection
import javax.inject.Inject

class HomeFragment : FlatTileFragment(), ZoneToolbarListener {

    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var restApi: RestApi

    private var authService: AuthorizationService? = null
    private var feedEntryGridController: FeedEntryGridController? = null
    private var zoneController: ZoneController? = null
    private var boardController: BoardController? = null
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
        PreferenceManager.CurrentUser.getUserId()?.run { subscribeTo(getString(R.string.juna_user_topic) + this) }

        startShimmers()
        initBoardsRecyclerView()
        initFeedRecyclerView()
        initZoneRecyclerView()

        doAfterDelay(1000) {
            getUserZones()
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
        getUserFeed(false)
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
        feedEntryGridController = FeedEntryGridController(this)
        user_feed_recycler_view.adapter = feedEntryGridController?.adapter
    }

    private fun initZoneRecyclerView() {
        zoneController = ZoneController(activity as BaseJunaCardActivity, restApi)
        user_zone_recycler_view.setController(zoneController!!)
    }

    private fun initBoardsRecyclerView() {
        if (activity is BaseJunaCardActivity) {
            boardController = BoardController(activity as BaseJunaCardActivity, restApi, false)
            user_boards_recycler_view.setController(boardController!!)
        }
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
                                zoneController?.setData(user.userPreferences)
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
                    feedEntryGridController?.setData(ArrayList(), true, R.string.failed_to_retrieve_feed)
                    Log.e(TAG, "getUserFeed(): onError(): ", it)
                }, {
                    when (it.code()) {
                        HttpURLConnection.HTTP_OK -> {
                            feedEntries = it.body() as ArrayList<FeedEntry>
                            if (!isNullOrEmpty(feedEntries)) {
                                feedEntryGridController?.setData(feedEntries, false, null)
                                onRecyclerViewContentsLoaded(user_feed_recycler_view, shimmer_user_feed)
                            } else {
                                errorToast(R.string.failed_to_retrieve_feed, it)
                                onRecyclerViewContentsFailedToLoad(null, shimmer_user_feed)
                                feedEntryGridController?.setData(ArrayList(), false, R.string.no_feed_entries_found)
                            }
                        }
                        HttpURLConnection.HTTP_NOT_FOUND -> {
                            onRecyclerViewContentsFailedToLoad(null, shimmer_user_feed)
                            feedEntryGridController?.setData(ArrayList(), false, R.string.no_feed_entries_found)
                        }
                        else -> {
                            errorToast(R.string.failed_to_retrieve_feed, it)
                            onRecyclerViewContentsFailedToLoad(user_feed_recycler_view, shimmer_user_feed)
                            feedEntryGridController?.setData(ArrayList(), false, R.string.failed_to_retrieve_feed)
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
            onRecyclerViewContentsFailedToLoad(null, shimmer_user_boards)
            boardController?.setData(emptyList(), true, R.string.failed_to_retrieve_board)
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_OK -> {
                    if (!isNullOrEmpty(it.body())) {
                        boardController?.setData(it.body(), false, null)
                        onRecyclerViewContentsLoaded(user_boards_recycler_view, shimmer_user_boards)
                    } else onRecyclerViewContentsFailedToLoad(user_boards_recycler_view, shimmer_user_boards)
                }
                HttpURLConnection.HTTP_NOT_FOUND -> {
                    shimmer_user_boards.visibility = View.GONE
                    boardController?.setData(emptyList(), false, R.string.cannot_find_user_boards)
                }
                else -> {
                    errorToast(R.string.failed_to_retrieve_board, it)
                    onRecyclerViewContentsFailedToLoad(null, shimmer_user_boards)
                    boardController?.setData(emptyList(), true, R.string.failed_to_retrieve_board)
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

    private fun onRecyclerViewContentsLoaded(recyclerView: RecyclerView, shimmerRelativeLayout: ShimmerRelativeLayout, isLoading: Boolean = false) {
        recyclerView.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        if (isLoading) shimmerRelativeLayout.startShimmerAnimation() else shimmerRelativeLayout.stopShimmerAnimation()
        if (isLoading) shimmerRelativeLayout.alpha = 1f
        shimmerRelativeLayout.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun onRecyclerViewContentsFailedToLoad(recyclerView: RecyclerView?, shimmerRelativeLayout: ShimmerRelativeLayout) {
        recyclerView?.visibility = View.INVISIBLE
        shimmerRelativeLayout.stopShimmerAnimation()
        (shimmerRelativeLayout as? ShimmerRelativeLayout)?.alpha = 0.2f
    }

    override fun updateFullScreenAdapter(feedEntryList: List<FeedEntry>) {}

    override fun getFeedItemPeekPopup(position: Int) = FeedItemPeekPopup.newInstance(feedEntries[position], null, true)

    override fun openFeedEntry(position: Int) = pushFragment(PostDetailContainerFragment.newInstance(feedEntries, "", position))

    override fun onDestroy() {
        authService?.dispose()
        boardController = null
        feedEntryGridController = null
        zoneController = null
        super.onDestroy()
    }
}