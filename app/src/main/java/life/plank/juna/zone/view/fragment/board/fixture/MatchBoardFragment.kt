package life.plank.juna.zone.view.fragment.board.fixture


import android.app.Activity
import android.content.*
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.CardView
import android.support.v7.widget.PagerSnapHelper
import android.util.Log
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_match_board.*
import kotlinx.android.synthetic.main.emoji_bottom_sheet.*
import kotlinx.android.synthetic.main.faded_card.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.RestApiAggregator
import life.plank.juna.zone.data.model.*
import life.plank.juna.zone.data.model.binder.PollBindingModel
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.interfaces.PublicBoardHeaderListener
import life.plank.juna.zone.util.AppConstants
import life.plank.juna.zone.util.AppConstants.*
import life.plank.juna.zone.util.DataUtil.*
import life.plank.juna.zone.util.FixtureListUpdateTask
import life.plank.juna.zone.util.OnSwipeTouchListener
import life.plank.juna.zone.util.PreferenceManager.getToken
import life.plank.juna.zone.util.UIDisplayUtil.*
import life.plank.juna.zone.util.setObserverThreadsAndSubscribe
import life.plank.juna.zone.view.activity.base.BaseBoardActivity
import life.plank.juna.zone.view.adapter.EmojiAdapter
import life.plank.juna.zone.view.fragment.base.CardTileFragment
import life.plank.juna.zone.view.fragment.home.HomeFragment
import retrofit2.Response
import rx.Subscriber
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Named

class MatchBoardFragment : CardTileFragment(), PublicBoardHeaderListener {

    @field: [Inject Named("default")]
    internal var restApi: RestApi? = null
    @field: [Inject Named("footballData")]
    internal var footballRestApi: RestApi? = null
    @Inject
    internal var picasso: Picasso? = null
    @Inject
    internal var gson: Gson? = null
    @Inject
    internal var pagerSnapHelper: PagerSnapHelper? = null

    private var currentMatchId: Long = 0
    private var isBoardActive: Boolean = false
    private var boardId: String? = null
    private var league: League? = null
    private var fixture: MatchFixture? = null
    private var matchDetails: MatchDetails? = null
    private var poll: Poll? = null

    private var boardPagerAdapter: BoardPagerAdapter? = null
    private var emojiBottomSheetBehavior: BottomSheetBehavior<*>? = null
    private var emojiAdapter: EmojiAdapter? = null

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.hasExtra(getString(R.string.intent_content_type))) {
                setDataReceivedFromPushNotification(intent)
            } else if (intent.hasExtra(getString(R.string.intent_zone_live_data))) {
                setZoneLiveData(intent)
            }
        }
    }

    companion object {
        private val TAG = HomeFragment::class.java.simpleName
        fun newInstance(fixture: MatchFixture, league: League): HomeFragment = HomeFragment().apply {
            arguments = Bundle().apply {
                putParcelable(getString(R.string.intent_fixture_data), fixture)
                putParcelable(getString(R.string.intent_league), league)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)

        val intent = arguments!!
        if (intent.containsKey(getString(R.string.intent_fixture_data))) {
            fixture = intent.getParcelable(getString(R.string.intent_fixture_data))
            league = intent.getParcelable(getString(R.string.intent_league))
            currentMatchId = fixture!!.matchId
            board_toolbar.prepare(picasso, fixture, league!!.thumbUrl)
        } else {
            currentMatchId = intent.getLong(getString(R.string.match_id_string), 0)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.activity_match_board, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBoardIdAndMatchDetails(currentMatchId)
        setupFullScreenRecyclerViewSwipeGesture(activity!!, recycler_view_drag_area, recycler_view_drag_area)
        board_toolbar.setUpPopUp(activity, currentMatchId)
        board_blur_background_image_view.setOnClickListener { dismissFullScreenRecyclerView() }
    }

    fun setDataReceivedFromPushNotification(intent: Intent) {
        val title = intent.getStringExtra(getString(R.string.intent_comment_title))
        val contentType = intent.getStringExtra(getString(R.string.intent_content_type))
        val thumbnailHeight = intent.getIntExtra(getString(R.string.intent_thumbnail_height), 0)
        val thumbnailWidth = intent.getIntExtra(getString(R.string.intent_thumbnail_width), 0)
        val imageUrl = intent.getStringExtra(getString(R.string.intent_image_url))
        val feed = FeedEntry()
        Log.d(TAG, "content_type: $contentType")

        feed.feedItem.contentType = contentType
        if (contentType == AppConstants.ROOT_COMMENT) {
            feed.feedItem.title = title
        } else {
            val thumbnail = Thumbnail()
            thumbnail.imageWidth = thumbnailWidth
            thumbnail.imageHeight = thumbnailHeight
            thumbnail.imageUrl = imageUrl
            feed.feedItem.thumbnail = thumbnail
            feed.feedItem.url = imageUrl
        }
        try {
            if (boardPagerAdapter!!.currentFragment is BoardTilesFragment) {
                (boardPagerAdapter!!.currentFragment as BoardTilesFragment).updateNewPost(feed)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }

    }

    private fun setZoneLiveData(intent: Intent) {
        val zoneLiveData = getZoneLiveData(intent, getString(R.string.intent_zone_live_data), gson!!)
        when (zoneLiveData!!.liveDataType) {
            SCORE_DATA -> {
                val scoreData = zoneLiveData.getScoreData(gson!!)
                updateScoreLocally(fixture!!, scoreData)
                updateScoreLocally(matchDetails!!, scoreData)
                board_toolbar.setScore("${scoreData.homeGoals} $DASH ${scoreData.awayGoals}")
                FixtureListUpdateTask.update(fixture, scoreData, null, true)
            }
            TIME_STATUS_DATA -> {
                val timeStatus = zoneLiveData.getLiveTimeStatus(gson!!)
                updateTimeStatusLocally(fixture!!, timeStatus)
                updateTimeStatusLocally(matchDetails!!, timeStatus)
                FixtureListUpdateTask.update(fixture, null, timeStatus, false)
                val matchStartTime = when {
                    fixture != null -> fixture!!.matchStartTime
                    matchDetails != null -> matchDetails!!.matchStartTime
                    else -> null
                }
                if (matchStartTime != null) {
                    board_toolbar.setLiveTimeStatus(matchStartTime, timeStatus.timeStatus)
                }
            }
            BOARD_ACTIVATED -> {
                isBoardActive = true
                clearColorFilter()
                setupViewPagerWithFragments()
            }
            BOARD_DEACTIVATED -> {
                isBoardActive = false
                applyInactiveBoardColorFilter()
                showBoardExpirationDialog(activity) { dialog, _ ->
                    setupViewPagerWithFragments()
                    dialog.cancel()
                }
            }
            else -> {
            }
        }
        try {
            if (boardPagerAdapter!!.currentFragment is BoardInfoFragment) {
                (boardPagerAdapter!!.currentFragment as BoardInfoFragment).updateZoneLiveData(zoneLiveData)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initBottomSheetRecyclerView() {
        emojiAdapter = EmojiAdapter(ZoneApplication.getContext(), boardId, emojiBottomSheetBehavior)
        emoji_recycler_view.adapter = emojiAdapter
    }

    private fun setupBottomSheet() {
        emojiBottomSheetBehavior = BottomSheetBehavior.from(emoji_bottom_sheet)
        emojiBottomSheetBehavior!!.peekHeight = 0
        emoji_bottom_sheet.visibility = View.VISIBLE
    }

    override fun prepareFullScreenRecyclerView() {
        pagerSnapHelper!!.attachToRecyclerView(board_tiles_full_recycler_view)
//        TODO: un-comment after making changes to BoardFeedDetailAdapter
//        boardFeedDetailAdapter = BoardFeedDetailAdapter(restApi, boardId, isBoardActive, emojiBottomSheetBehavior, BOARD)
        board_tiles_full_recycler_view.adapter = boardFeedDetailAdapter
    }

    private fun setupViewPagerWithFragments() {
        boardPagerAdapter = BoardPagerAdapter(childFragmentManager, this)
        board_view_pager.adapter = boardPagerAdapter
        board_toolbar.setupWithViewPager(board_view_pager)
    }

    override fun getRootFadedCardLayout(): ViewGroup = faded_card

    override fun getRootCard(): CardView = root_card

    override fun getDragHandle(): View = drag_area

    override fun onResume() {
        super.onResume()
        context?.registerReceiver(mMessageReceiver, IntentFilter(getString(R.string.intent_board)))
        board_toolbar.initListeners(this)
    }

    override fun onPause() {
        super.onPause()
        context?.unregisterReceiver(mMessageReceiver)
        board_toolbar.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseMessaging.getInstance().unsubscribeFromTopic(getString(R.string.pref_football_match_sub) + currentMatchId)
        if (!isNullOrEmpty(boardId) && !isBoardActive) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(getString(R.string.board_id_prefix) + boardId!!)
        }
    }

    private fun getBoardIdAndMatchDetails(currentMatchId: Long?) {
        RestApiAggregator.getBoardAndMatchDetails(restApi!!, footballRestApi!!, currentMatchId!!)
                .doOnSubscribe { board_progress_bar!!.visibility = View.VISIBLE }
                .doOnTerminate { board_progress_bar!!.visibility = View.GONE }
                .subscribe(object : Subscriber<Pair<Board, MatchDetails>>() {
                    override fun onCompleted() {
                        Log.i(TAG, "onCompleted: getBoardIdAndMatchDetails")
                    }

                    override fun onError(e: Throwable) {
                        Log.e(TAG, "In onError() : getBoardIdAndMatchDetails$e")
                        Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG).show()
                    }

                    override fun onNext(boardMatchDetailsPair: Pair<Board, MatchDetails>?) {
                        if (boardMatchDetailsPair != null) {
                            matchDetails = boardMatchDetailsPair.second
                            val board = boardMatchDetailsPair.first
                            if (matchDetails != null) {
                                matchDetails!!.league = league
                                board_toolbar.prepare(picasso, MatchFixture.from(matchDetails!!), league!!.thumbUrl)
                            }
                            if (board != null) {
                                boardId = board.id
                                saveBoardId()
                                isBoardActive = board.isActive!!
                                prepareFullScreenRecyclerView()
                                setupBottomSheet()
                                initBottomSheetRecyclerView()

                                if (isBoardActive) {
                                    FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.board_id_prefix) + boardId!!)
                                    FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.pref_football_match_sub) + currentMatchId)
                                } else
                                    applyInactiveBoardColorFilter()
                            } else
                                applyInactiveBoardColorFilter()

                            getBoardPolls()
                        } else {
                            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG).show()
                        }
                    }
                })
    }

    private fun getBoardPolls() {
        restApi!!.getBoardPoll(boardId, getToken()).setObserverThreadsAndSubscribe(object : Subscriber<Response<Poll>>() {
            override fun onCompleted() {
                Log.i(TAG, "getBoardPolls() : onCompleted")
            }

            override fun onError(e: Throwable) {
                Log.e(TAG, "getBoardPolls() : ", e)
            }

            override fun onNext(response: Response<Poll>) {
                when (response.code()) {
                    HttpURLConnection.HTTP_OK -> {
                        poll = response.body()
                        setupViewPagerWithFragments()
                    }
                    HttpURLConnection.HTTP_NOT_FOUND -> {
                    }
                    else -> {
                    }
                }
            }
        })
    }

    private fun clearColorFilter() {
        board_parent_layout!!.background.clearColorFilter()
    }

    private fun applyInactiveBoardColorFilter() {
        board_parent_layout!!.background.setColorFilter(getColor(R.color.grey_0_7), PorterDuff.Mode.SRC_OVER)
    }

    fun saveBoardId() {
        val boardIdEditor: SharedPreferences.Editor = activity?.getSharedPreferences(getString(R.string.pref_enter_board_id), Context.MODE_PRIVATE)!!.edit()
        boardIdEditor.putString(getString(R.string.pref_enter_board_id), boardId).apply()
    }

    override fun followClicked(followBtn: TextView) {
        if (isBoardActive) {
            val id = getString(R.string.board_id_prefix) + boardId!!
            if (followBtn.text.toString().equals(getString(R.string.follow), ignoreCase = true)) {
                followBtn.setText(R.string.unfollow)
                FirebaseMessaging.getInstance().subscribeToTopic(id)
            } else {
                followBtn.setText(R.string.follow)
                FirebaseMessaging.getInstance().unsubscribeFromTopic(id)
            }
        } else {
            Toast.makeText(context, R.string.board_not_active, Toast.LENGTH_LONG).show()
        }
    }

    override fun onMatchTimeStateChange() {
        getBoardIdAndMatchDetails(currentMatchId)
    }

    override fun updateFullScreenAdapter(feedEntryList: List<FeedEntry>) {
        boardFeedDetailAdapter?.update(feedEntryList)
    }

    override fun setBlurBackgroundAndShowFullScreenTiles(setFlag: Boolean, position: Int) {
        isTileFullScreenActive = setFlag
        BaseBoardActivity.boardParentViewBitmap = if (setFlag) loadBitmap(root_layout!!, root_layout!!, context) else null
        board_blur_background_image_view.setImageBitmap(BaseBoardActivity.boardParentViewBitmap)

        val listener = object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                recycler_view_drag_area.visibility = View.INVISIBLE
                board_tiles_full_recycler_view.visibility = View.INVISIBLE
                recycler_view_drag_area.translationY = 0f
                board_tiles_full_recycler_view.translationY = 0f
                board_blur_background_image_view.visibility = View.INVISIBLE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        }
        val recyclerViewAnimation = AnimationUtils.loadAnimation(context, if (setFlag) R.anim.zoom_in else R.anim.zoom_out)
        val blurBackgroundAnimation = AnimationUtils.loadAnimation(context, if (setFlag) android.R.anim.fade_in else android.R.anim.fade_out)
        if (!setFlag) {
            recyclerViewAnimation.setAnimationListener(listener)
            blurBackgroundAnimation.setAnimationListener(listener)
        }
        board_tiles_full_recycler_view.startAnimation(recyclerViewAnimation)
        board_blur_background_image_view.startAnimation(blurBackgroundAnimation)

        if (setFlag) {
            board_tiles_full_recycler_view.scrollToPosition(position)
            recycler_view_drag_area.visibility = View.VISIBLE
            board_tiles_full_recycler_view.visibility = View.VISIBLE
            board_blur_background_image_view.visibility = View.VISIBLE
        }
    }

    override fun setupFullScreenRecyclerViewSwipeGesture(activity: Activity, recyclerViewDragArea: View, boardTilesFullRecyclerView: View) {
        recyclerViewDragArea.setOnTouchListener(object : OnSwipeTouchListener(activity, recyclerViewDragArea, boardTilesFullRecyclerView) {
            override fun onSwipeDown() {
                dismissFullScreenRecyclerView()
            }
        })
    }

    override fun dismissFullScreenRecyclerView() {
        emojiBottomSheetBehavior!!.peekHeight = 0
        emojiBottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        setBlurBackgroundAndShowFullScreenTiles(false, 0)
    }

    override fun moveItem(position: Int, previousPosition: Int) {
        if (boardPagerAdapter!!.currentFragment is BoardTilesFragment) {
            (boardPagerAdapter!!.currentFragment as BoardTilesFragment).moveItem(position, previousPosition)
        }
    }

    internal class BoardPagerAdapter(supportFragmentManager: FragmentManager, matchBoardFragment: MatchBoardFragment) : FragmentStatePagerAdapter(supportFragmentManager) {

        var currentFragment: Fragment? = null
        private val ref: WeakReference<MatchBoardFragment> = WeakReference(matchBoardFragment)

        private val boardTilesFragmentWithPoll: BoardTilesFragment?
            get() = ref.get()?.run {
                BoardTilesFragment.newInstance(
                        boardId,
                        isBoardActive,
                        PollBindingModel.from(poll!!, matchDetails!!)
                )
            }

        private val boardTilesFragmentWithoutPoll: Fragment?
            get() = ref.get()?.run { BoardTilesFragment.newInstance(boardId, isBoardActive) }

        override fun getItem(position: Int): Fragment? {
            when (position) {
                0 -> return ref.get()?.run { BoardInfoFragment.newInstance(gson!!.toJson(matchDetails)) }
                1 -> {
                    try {
                        return if (ref.get()!!.poll == null) boardTilesFragmentWithoutPoll else boardTilesFragmentWithPoll
                    } catch (e: Exception) {
                        Log.e(TAG, "getItem: ", e)
                        boardTilesFragmentWithoutPoll
                    }

                    return null
                }
                else -> return null
            }
        }

        override fun getItemPosition(`object`: Any): Int {
            return PagerAdapter.POSITION_NONE
        }

        override fun getCount(): Int {
            return 2
        }

        override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
            if (currentFragment !== `object`) {
                currentFragment = `object` as Fragment
            }
            super.setPrimaryItem(container, position, `object`)
        }
    }

    override fun onBackPressed(): Boolean {
        emojiBottomSheetBehavior!!.peekHeight = 0
        emojiBottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        return if (isTileFullScreenActive) {
            setBlurBackgroundAndShowFullScreenTiles(false, 0)
            false
        } else {
            boardFeedDetailAdapter = null
            boardPagerAdapter = null
            true
        }
    }
}