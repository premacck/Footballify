package life.plank.juna.zone.view.fragment.board.fixture

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.LineChart
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_match_board.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.*
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.interfaces.PublicBoardHeaderListener
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.util.common.DataUtil
import life.plank.juna.zone.util.common.DataUtil.*
import life.plank.juna.zone.util.common.execute
import life.plank.juna.zone.util.common.getPositionFromIntentIfAny
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.football.FixtureListUpdateTask
import life.plank.juna.zone.util.sharedpreference.PreferenceManager
import life.plank.juna.zone.util.view.UIDisplayUtil.findColor
import life.plank.juna.zone.util.view.UIDisplayUtil.showBoardExpirationDialog
import life.plank.juna.zone.view.fragment.base.BaseMatchFragment
import life.plank.juna.zone.view.fragment.forum.ForumFragment
import java.lang.ref.WeakReference
import javax.inject.Inject

class MatchBoardFragment : BaseMatchFragment(), PublicBoardHeaderListener {

    @Inject
    lateinit var restApi: RestApi
    @Inject
    lateinit var gson: Gson

    lateinit var matchDetails: MatchDetails
    private var currentMatchId: Long = 0
    private lateinit var board: Board
    private lateinit var league: League
    private lateinit var feedEntries: List<FeedEntry>

    private var boardPagerAdapter: BoardPagerAdapter? = null

    companion object {
        val TAG: String = MatchBoardFragment::class.java.simpleName
        fun newInstance(board: Board, matchDetails: MatchDetails): MatchBoardFragment = MatchBoardFragment().apply {
            arguments = Bundle().apply {
                putParcelable(findString(R.string.intent_board), board)
                putParcelable(findString(R.string.intent_match_fixture), matchDetails)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)
        arguments?.run {
            board = getParcelable(getString(R.string.intent_board))!!
            matchDetails = getParcelable(getString(R.string.intent_match_fixture))!!
            league = DataUtil.getSpecifiedLeague(board.boardEvent?.leagueName)
            if (matchDetails.league == null) {
                matchDetails.league = league
            }
            currentMatchId = board.boardEvent?.foreignId!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_match_board, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        board_toolbar.setUpPopUp(activity, currentMatchId)
        updateUi()
    }

    override fun onResume() {
        super.onResume()
        board_toolbar.initListeners(this)
    }

    override fun onPause() {
        super.onPause()
        board_toolbar.dispose()
    }

    private fun updateUi() {
        FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.pref_football_match_sub) + currentMatchId)
        try {
            board_toolbar.prepare(matchDetails, league.leagueLogo)

            if (!board.isActive) applyInactiveBoardColorFilter()
            else clearColorFilter()

            setupViewPagerWithFragments()
            followBoard()

            (item_scrubber as? LineChart)?.run {
                ScrubberLoader.prepare(this, false)
                this.onDebouncingClick { pushPopup(TimelinePopup.newInstance(currentMatchId, matchDetails)) }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
        }
    }

    private fun clearColorFilter() = board_parent_layout?.background?.clearColorFilter()

    private fun applyInactiveBoardColorFilter() = board_parent_layout?.background?.setColorFilter(findColor(R.color.grey_0_7), PorterDuff.Mode.SRC_OVER)

    private fun setupViewPagerWithFragments() {
        boardPagerAdapter = BoardPagerAdapter(childFragmentManager, this)
        board_view_pager.adapter = boardPagerAdapter
        board_toolbar.setupWithViewPager(board_view_pager, getPositionFromIntentIfAny(boardPagerAdapter))
    }

    override fun onInAppNotificationReceived(feedEntry: FeedEntry) {
        (boardPagerAdapter?.currentFragment as? BoardTilesFragment)?.updateNewPost(feedEntry)
    }

    override fun onZoneLiveDataReceived(zoneLiveData: ZoneLiveData) {
        when (zoneLiveData.liveDataType) {
            SCORE_DATA -> {
                val scoreData = zoneLiveData.getScoreData(gson)
                updateScoreLocally(matchDetails, scoreData)
                board_toolbar.setScore("${scoreData.homeGoals} $DASH ${scoreData.awayGoals}")
                FixtureListUpdateTask.update(matchDetails, scoreData, null, true)
            }
            TIME_STATUS_DATA -> {
                val timeStatus = zoneLiveData.getLiveTimeStatus(gson)
                updateTimeStatusLocally(matchDetails, timeStatus)
                FixtureListUpdateTask.update(matchDetails, null, timeStatus, false)
                board_toolbar.setLiveTimeStatus(matchDetails.matchStartTime, timeStatus.timeStatus)
            }
            BOARD_ACTIVATED -> {
                board.isActive = true
                clearColorFilter()
                setupViewPagerWithFragments()
            }
            BOARD_DEACTIVATED -> {
                board.isActive = false
                applyInactiveBoardColorFilter()
                showBoardExpirationDialog(activity) { dialog, _ ->
                    setupViewPagerWithFragments()
                    dialog.cancel()
                }
            }
        }
        try {
            (boardPagerAdapter?.currentFragment as? MatchMediaFragment)?.updateZoneLiveData(zoneLiveData)
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
        }
    }

    override fun gson(): Gson = gson

    override fun restApi(): RestApi = restApi

    override fun getBackgroundBlurLayout(): ViewGroup? = blur_layout

    override fun getRootView(): ViewGroup? = root_card

    override fun getDragView(): View? = drag_area

    override fun onMatchTimeStateChange() = updateUi()

    override fun getFeedEntries(): List<FeedEntry> = feedEntries

    override fun getTheBoardId(): String? = board.id

    override fun updateFullScreenAdapter(feedEntryList: List<FeedEntry>) {
        feedEntries = feedEntryList
    }

    override fun onDestroy() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(getString(R.string.pref_football_match_sub) + currentMatchId)
        boardPagerAdapter = null
        super.onDestroy()
    }

    /** Follow board by default when entered. Nothing to do on receiving the response code */
    private fun followBoard() = restApi.followBoard(PreferenceManager.Auth.getToken(), board.id).execute()

    class BoardPagerAdapter(supportFragmentManager: FragmentManager, matchBoardFragment: MatchBoardFragment) : FragmentStatePagerAdapter(supportFragmentManager) {

        var currentFragment: Fragment? = null
        private val ref: WeakReference<MatchBoardFragment> = WeakReference(matchBoardFragment)

        override fun getItem(position: Int): Fragment? {
            return ref.get()?.run {
                when (position) {
                    0 -> MatchStatsFragment.newInstance(matchDetails)
                    1 -> LineupFragment.newInstance(matchDetails)
                    2 -> MatchMediaFragment.newInstance(matchDetails)
                    3 -> ForumFragment.newInstance(board.id)
                    4 -> BoardTilesFragment.newInstance(board.id, board.isActive)
                    else -> null
                }
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> findString(R.string.stats)
                1 -> findString(R.string.lineups)
                2 -> findString(R.string.media)
                3 -> findString(R.string.forum)
                4 -> findString(R.string.tiles)
                else -> null
            }
        }

        override fun getItemPosition(`object`: Any): Int {
            return when (`object`) {
                findString(R.string.stats) -> 0
                findString(R.string.lineups) -> 1
                findString(R.string.media) -> 2
                findString(R.string.forum) -> 3
                findString(R.string.tiles) -> 4
                else -> PagerAdapter.POSITION_NONE
            }
        }

        override fun getCount(): Int = 5

        override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
            if (currentFragment !== `object`) {
                currentFragment = `object` as Fragment
            }
            super.setPrimaryItem(container, position, `object`)
        }
    }
}