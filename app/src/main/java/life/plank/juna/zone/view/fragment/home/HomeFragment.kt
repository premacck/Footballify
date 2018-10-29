package life.plank.juna.zone.view.fragment.home

import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.PagerSnapHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.onboarding_bottom_sheet.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.Board
import life.plank.juna.zone.data.model.FeedEntry
import life.plank.juna.zone.data.model.User
import life.plank.juna.zone.data.model.UserPreference
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.interfaces.ZoneToolbarListener
import life.plank.juna.zone.util.AppConstants.BoomMenuPage.BOOM_MENU_FULL
import life.plank.juna.zone.util.AuthUtil
import life.plank.juna.zone.util.DataUtil.getStaticLeagues
import life.plank.juna.zone.util.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.PreferenceManager.getToken
import life.plank.juna.zone.util.facilis.pushPopup
import life.plank.juna.zone.util.facilis.removeActiveCardsIfAny
import life.plank.juna.zone.util.facilis.removeActivePopupsIfAny
import life.plank.juna.zone.util.hideAndShowBoomMenu
import life.plank.juna.zone.util.setObserverThreadsAndSubscribe
import life.plank.juna.zone.util.setupBoomMenu
import life.plank.juna.zone.view.activity.UserNotificationActivity
import life.plank.juna.zone.view.activity.UserProfileActivity
import life.plank.juna.zone.view.adapter.OnboardingAdapter
import life.plank.juna.zone.view.adapter.UserBoardsAdapter
import life.plank.juna.zone.view.adapter.UserFeedAdapter
import life.plank.juna.zone.view.adapter.UserZoneAdapter
import life.plank.juna.zone.view.fragment.base.FlatTileFragment
import life.plank.juna.zone.view.fragment.clickthrough.FeedItemPeekPopup
import net.openid.appauth.AuthorizationService
import retrofit2.Response
import rx.Subscriber
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Named

class HomeFragment : FlatTileFragment(), ZoneToolbarListener {

    @Inject
    lateinit var gson: Gson
    @field: [Inject Named("default")]
    lateinit var restApi: RestApi
    @Inject
    lateinit var pagerSnapHelper: PagerSnapHelper

    private var authService: AuthorizationService? = null
    private var onBoardingBottomSheetBehavior: BottomSheetBehavior<*>? = null
    private var onBoardingAdapter: OnboardingAdapter? = null
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
        val editor = ZoneApplication.getContext().getSharedPreferences(getString(R.string.pref_user_details), MODE_PRIVATE)
        val userObjectId = editor.getString(ZoneApplication.getContext().getString(R.string.pref_object_id), getString(R.string.na))

        val topic = getString(R.string.juna_user_topic) + userObjectId!!
        FirebaseMessaging.getInstance().subscribeToTopic(topic)

        setupOnBoardingBottomSheet()
        initBottomSheetRecyclerView()

        initRecyclerView()
        initZoneRecyclerView()
        initBoardsRecyclerView()

        getLeagues()
        getUserZones()

        setUpToolbarAndBoomMenu()
        hideAndShowBoomMenu(nestedScrollView, arc_menu)

        getUserFeed()
        getUserBoards()

        feed_header.initListeners(this)
        feed_header.setProfilePic(editor.getString(getString(R.string.pref_profile_pic_url), null))
    }

    override fun getSecondaryFragmentResId(): Int = R.id.peek_popup_container

    private fun initBottomSheetRecyclerView() {
        onBoardingAdapter = OnboardingAdapter(activity)
        onboarding_recycler_view.adapter = onBoardingAdapter
    }

    private fun getLeagues() {
        onBoardingAdapter?.setLeagueList(getStaticLeagues())
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
        userZoneAdapter = UserZoneAdapter(context, userPreferences as MutableList<UserPreference>?)
        user_zone_recycler_view?.adapter = userZoneAdapter
    }

    private fun initBoardsRecyclerView() {
        userBoardsAdapter = UserBoardsAdapter(context, restApi, Glide.with(this))
        user_boards_recycler_view?.adapter = userBoardsAdapter
    }

    private fun setUpUserZoneAdapter(userPreferenceList: List<UserPreference>?) {
        userPreferences.clear()
        userPreferences.addAll(userPreferenceList!!)
        userZoneAdapter!!.notifyDataSetChanged()
    }

    private fun getUserZones() {
        if (isNullOrEmpty(getToken())) {
            user_zone_recycler_view.visibility = View.GONE
            return
        }

        restApi.getUser(getToken()).setObserverThreadsAndSubscribe(object : Subscriber<Response<User>>() {
            override fun onCompleted() {
                Log.d(TAG, "getUserZones(): onCompleted")
            }

            override fun onError(e: Throwable) {
                Log.e(TAG, "getUserZones(): onError: $e")
                Toast.makeText(ZoneApplication.getContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show()
            }

            override fun onNext(response: Response<User>) {
                when (response.code()) {
                    HttpURLConnection.HTTP_OK -> {
                        val user = response.body()
                        if (user != null) {
                            setUpUserZoneAdapter(user.userPreferences)
                        }
                    }
                    HttpURLConnection.HTTP_NOT_FOUND -> Toast.makeText(ZoneApplication.getContext(), R.string.failed_to_retrieve_zones, Toast.LENGTH_LONG).show()
                    else -> Log.e(TAG, response.message())
                }
            }
        })
    }

    private fun getUserFeed() {
        val userFeedApiCall = if (isNullOrEmpty(getToken())) restApi.getUserFeed() else restApi.getUserFeed(getToken())
        userFeedApiCall.setObserverThreadsAndSubscribe(object : Subscriber<Response<List<FeedEntry>>>() {
            override fun onCompleted() {
                Log.e(TAG, "getUserFeed(): onCompleted: ")
            }

            override fun onError(e: Throwable) {
                Log.e(TAG, "getUserFeed(): onError(): $e")
                Toast.makeText(ZoneApplication.getContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show()
            }

            override fun onNext(response: Response<List<FeedEntry>>) {
                when (response.code()) {
                    HttpURLConnection.HTTP_OK -> {
                        feedEntries = response.body() as ArrayList<FeedEntry>
                        if (!isNullOrEmpty(feedEntries)) {
                            userFeedAdapter!!.setUserFeed(feedEntries)
                        } else
                            Toast.makeText(ZoneApplication.getContext(), R.string.failed_to_retrieve_feed, Toast.LENGTH_SHORT).show()
                    }
                    HttpURLConnection.HTTP_NOT_FOUND -> Toast.makeText(ZoneApplication.getContext(), R.string.failed_to_retrieve_feed, Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(ZoneApplication.getContext(), R.string.failed_to_retrieve_feed, Toast.LENGTH_SHORT).show()
                }

            }
        })
    }

    private fun getUserBoards() {
        if (isNullOrEmpty(getToken())) return

        restApi.getFollowingBoards(getToken()).setObserverThreadsAndSubscribe(object : Subscriber<Response<List<Board>>>() {
            override fun onCompleted() {
                Log.e(TAG, "getUserBoards(): onCompleted: ")
            }

            override fun onError(e: Throwable) {
                Log.e(TAG, "getUserBoards(): onError(): $e")
                Toast.makeText(ZoneApplication.getContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show()
            }

            override fun onNext(response: Response<List<Board>>) {
                when (response.code()) {
                    HttpURLConnection.HTTP_OK -> {
                        if (response.body() != null) {
                            userBoardsAdapter!!.setUserBoards(response.body())
                        } else
                            user_boards_recycler_view.visibility = View.GONE
                    }
                    HttpURLConnection.HTTP_NOT_FOUND -> user_boards_recycler_view.visibility = View.GONE
                    else -> Toast.makeText(ZoneApplication.getContext(), R.string.failed_to_retrieve_board, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun profilePictureClicked(profilePicture: ImageView) {
        if (isNullOrEmpty(getToken())) {
            showPopup()
        } else {
            UserProfileActivity.launch(activity)
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

    override fun showFeedItemPeekPopup(position: Int) {
        childFragmentManager.pushPopup(
                R.id.peek_popup_container,
                FeedItemPeekPopup.newInstance(feedEntries, null, true, null, position),
                FeedItemPeekPopup.TAG
        )
    }

    override fun onDestroy() {
        if (authService != null) {
            authService!!.dispose()
        }
        boardFeedDetailAdapter = null
        onBoardingAdapter = null
        userBoardsAdapter = null
        userFeedAdapter = null
        userZoneAdapter = null
        super.onDestroy()
    }

    override fun onBackPressed(): Boolean {
        val areAllPopupsDismissed = childFragmentManager.removeActivePopupsIfAny()
        val areAllCardsRemoved = childFragmentManager.removeActiveCardsIfAny()
        return areAllPopupsDismissed && areAllCardsRemoved
    }
}