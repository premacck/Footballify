package life.plank.juna.zone.view.fragment.board.fixture

import android.content.*
import android.graphics.PorterDuff
import android.os.Bundle
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
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.faded_card.*
import kotlinx.android.synthetic.main.fragment_match_board.*
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
import life.plank.juna.zone.util.PreferenceManager.getToken
import life.plank.juna.zone.util.UIDisplayUtil.findColor
import life.plank.juna.zone.util.UIDisplayUtil.showBoardExpirationDialog
import life.plank.juna.zone.util.facilis.findPopupDialog
import life.plank.juna.zone.util.facilis.pushPopup
import life.plank.juna.zone.util.setObserverThreadsAndSubscribe
import life.plank.juna.zone.view.fragment.base.CardTileFragment
import life.plank.juna.zone.view.fragment.clickthrough.FeedItemPeekPopup
import life.plank.juna.zone.view.fragment.forum.ForumFragment
import retrofit2.Response
import rx.Subscriber
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Named

class MatchBoardFragment : CardTileFragment(), PublicBoardHeaderListener {

    @field: [Inject Named("default")]
    lateinit var restApi: RestApi
    @field: [Inject Named("footballData")]
    lateinit var footballRestApi: RestApi
    @Inject
    lateinit var picasso: Picasso
    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var pagerSnapHelper: PagerSnapHelper

    private var currentMatchId: Long = 0
    private var isBoardActive: Boolean = false
    private var boardId: String? = null
    private lateinit var league: League
    private var fixture: MatchFixture? = null
    private var matchDetails: MatchDetails? = null
    private var poll: Poll? = null
    private lateinit var feedEntries: List<FeedEntry>

    private var boardPagerAdapter: BoardPagerAdapter? = null

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
        private val TAG = MatchBoardFragment::class.java.simpleName
        fun newInstance(fixture: MatchFixture, league: League): MatchBoardFragment = MatchBoardFragment().apply {
            arguments = Bundle().apply {
                putParcelable(findString(R.string.intent_fixture_data), fixture)
                putParcelable(findString(R.string.intent_league), league)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)

        val intent = arguments!!
        if (intent.containsKey(getString(R.string.intent_fixture_data))) {
            fixture = intent.getParcelable(getString(R.string.intent_fixture_data))
            league = intent.getParcelable(getString(R.string.intent_league))!!
            currentMatchId = fixture!!.matchId
        } else {
            currentMatchId = intent.getLong(getString(R.string.match_id_string), 0)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_match_board, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        board_toolbar.prepare(picasso, fixture, league.thumbUrl)
        getBoardIdAndMatchDetails(currentMatchId)
        board_toolbar.setUpPopUp(activity, currentMatchId)
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
        val zoneLiveData = getZoneLiveData(intent, getString(R.string.intent_zone_live_data), gson)
        when (zoneLiveData!!.liveDataType) {
            SCORE_DATA -> {
                val scoreData = zoneLiveData.getScoreData(gson)
                updateScoreLocally(fixture!!, scoreData)
                updateScoreLocally(matchDetails!!, scoreData)
                board_toolbar.setScore("${scoreData.homeGoals} $DASH ${scoreData.awayGoals}")
                FixtureListUpdateTask.update(fixture, scoreData, null, true)
            }
            TIME_STATUS_DATA -> {
                val timeStatus = zoneLiveData.getLiveTimeStatus(gson)
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
        }
        try {
            if (boardPagerAdapter?.currentFragment is BoardInfoFragment) {
                (boardPagerAdapter?.currentFragment as? BoardInfoFragment)?.updateZoneLiveData(zoneLiveData)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupViewPagerWithFragments() {
        boardPagerAdapter = BoardPagerAdapter(childFragmentManager, this)
        board_view_pager.adapter = boardPagerAdapter
        board_toolbar.setupWithViewPager(board_view_pager)
    }

    override fun getRootFadedCardLayout(): ViewGroup? = faded_card_layout

    override fun getFadedCard(): CardView? = faded_card

    override fun getRootCard(): CardView? = root_card

    override fun getDragHandle(): View? = drag_area

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
        RestApiAggregator.getBoardAndMatchDetails(restApi, footballRestApi, currentMatchId!!)
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
                                board_toolbar.prepare(picasso, MatchFixture.from(matchDetails!!), league.thumbUrl)
                            }
                            if (board != null) {
                                boardId = board.id
                                saveBoardId()
                                isBoardActive = board.isActive!!
                                prepareFullScreenRecyclerView()

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
        restApi.getBoardPoll(boardId, getToken()).setObserverThreadsAndSubscribe(object : Subscriber<Response<Poll>>() {
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
        board_parent_layout!!.background.setColorFilter(findColor(R.color.grey_0_7), PorterDuff.Mode.SRC_OVER)
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

    override fun onMatchTimeStateChange() = getBoardIdAndMatchDetails(currentMatchId)

    override fun prepareFullScreenRecyclerView() {}

    override fun updateFullScreenAdapter(feedEntryList: List<FeedEntry>) {
        feedEntries = feedEntryList
    }

    override fun setBlurBackgroundAndShowFullScreenTiles(setFlag: Boolean, position: Int) {
        isTileFullScreenActive = setFlag
        if (setFlag) {
            childFragmentManager.pushPopup(
                    R.id.peek_popup_container,
                    FeedItemPeekPopup.newInstance(feedEntries, null, true, null, position),
                    FeedItemPeekPopup.TAG
            )
        } else {
            childFragmentManager.findPopupDialog(FeedItemPeekPopup.TAG)?.run { dismiss() }
        }
    }

    override fun dismissFullScreenRecyclerView() = setBlurBackgroundAndShowFullScreenTiles(false, 0)

    override fun moveItem(position: Int, previousPosition: Int) {
        if (boardPagerAdapter?.currentFragment is BoardTilesFragment) {
            (boardPagerAdapter?.currentFragment as BoardTilesFragment).moveItem(position, previousPosition)
        }
    }

    override fun onBackPressed(): Boolean {
        return if (isTileFullScreenActive) {
            setBlurBackgroundAndShowFullScreenTiles(false, 0)
            false
        } else {
            boardFeedDetailAdapter = null
            boardPagerAdapter = null
            true
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
            return when (position) {
                0 -> ref.get()?.run { BoardInfoFragment.newInstance(gson.toJson(matchDetails)) }
                1 -> ForumFragment.newInstance()
                2 -> {
                    try {
                        return if (ref.get()!!.poll == null) boardTilesFragmentWithoutPoll else boardTilesFragmentWithPoll
                    } catch (e: Exception) {
                        Log.e(TAG, "getItem: ", e)
                        boardTilesFragmentWithoutPoll
                    }
                    null
                }
                else -> null
            }
        }

        override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE

        override fun getCount(): Int = 3

        override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
            if (currentFragment !== `object`) {
                currentFragment = `object` as Fragment
            }
            super.setPrimaryItem(container, position, `object`)
        }
    }
}