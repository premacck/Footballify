package life.plank.juna.zone.view.fragment.board.fixture

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.*
import androidx.viewpager.widget.PagerAdapter
import com.github.mikephil.charting.charts.LineChart
import com.google.gson.Gson
import com.prembros.facilis.util.*
import kotlinx.android.synthetic.main.fragment_match_board.*
import kotlinx.android.synthetic.main.layout_board_engagement.*
import life.plank.juna.zone.*
import life.plank.juna.zone.data.model.*
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.interfaces.BoardHeaderListener
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.util.common.AppConstants.LIVE
import life.plank.juna.zone.util.common.JunaDataUtil.findString
import life.plank.juna.zone.util.customview.PublicBoardToolbar
import life.plank.juna.zone.util.sharedpreference.*
import life.plank.juna.zone.util.view.UIDisplayUtil.*
import life.plank.juna.zone.view.fragment.base.BaseMatchFragment
import life.plank.juna.zone.view.fragment.forum.ForumFragment
import java.lang.ref.WeakReference
import java.util.*
import javax.inject.Inject

class MatchBoardFragment : BaseMatchFragment(), BoardHeaderListener {

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
            league = JunaDataUtil.getSpecifiedLeague(board.boardEvent?.leagueName)
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
        board_toolbar.setUpPopUp(activity!!, currentMatchId)
        updateUi()
        val topic = getString(R.string.pref_football_match_sub) + currentMatchId
        if (!isSubscribed(topic)) subscribeTo(topic)

        updateCommentaryMarquee()
    }

    override fun onResume() {
        super.onResume()
        board_toolbar.initListeners(this)
    }

    override fun onPause() {
        super.onPause()
        board_toolbar.dispose()
    }

    fun updateCommentaryMarquee() {
        if (matchDetails.timeStatus == LIVE) {
            matchDetails.commentary?.reversed()?.run {
                commentary_marquee.visibility = View.VISIBLE
                if (size > 5) {
                    commentary_marquee.setCommentaryText(subList(0, 5))
                } else {
                    commentary_marquee.setCommentaryText(this)
                }
                commentary_marquee.isSelected = true
            }
        } else commentary_marquee.visibility = View.GONE
    }

    private fun updateUi() {
        try {
            board_toolbar.prepare(matchDetails, league.leagueLogo)
            if (!isNullOrEmpty(board.interactions)) {
                board.interactions?.run {
                    people_count.text = followers.toString()
                    post_count.text = posts.toString()
                    interaction_count.text = (followers + posts + emojiReacts).toString()
                }
            }
            if (!board.isActive) applyInactiveBoardColorFilter()
            else clearColorFilter()

            setupViewPagerWithFragments()
            followBoard()

            (item_scrubber as? LineChart)?.run {
                if (matchDetails.matchStartTime.time <= Date().time) {
                    if (!isNullOrEmpty(matchDetails.commentary) && !isNullOrEmpty(matchDetails.matchEvents)) {
                        loadScrubber(this, matchDetails.commentary!!, matchDetails.matchEvents!!)
                        this.onDebouncingClick { pushPopup(TimelinePopup.newInstance(currentMatchId, matchDetails)) }
                    } else {
                        item_scrubber.visibility = View.GONE
                    }
                } else {
                    item_scrubber.visibility = View.GONE
                }
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

    override fun onNewFeedEntry(feedEntry: FeedEntry) {
        (boardPagerAdapter?.currentFragment as? BoardTilesFragment)?.updateNewPost(feedEntry)
    }

    override fun updateForumComments() {
        (boardPagerAdapter?.currentFragment as? ForumFragment)?.getComments(false)
    }

    override fun gson(): Gson = gson

    override fun restApi(): RestApi = restApi

    override fun getBackgroundBlurLayout(): ViewGroup? = blur_layout

    override fun getRootView(): ViewGroup? = root_card

    override fun getDragView(): View? = drag_area

    override fun onMatchTimeStateChange() = updateUi()

    override fun onShareClick() = shareBoardExternally(activity, matchDetails.homeTeam.name, matchDetails.awayTeam.name, board.id)

    override fun getFeedEntries(): List<FeedEntry> = feedEntries

    override fun getFeedEntry(position: Int): FeedEntry = feedEntries[position]

    override fun getTheBoardId(): String? = board.id

    override fun currentChildFragment(): Fragment? = boardPagerAdapter?.currentFragment

    override fun publicBoardToolbar(): PublicBoardToolbar = board_toolbar

    override fun matchDetails(): MatchDetails = matchDetails

    override fun onBoardStateChange(isActive: Boolean) {
        if (isActive) {
            item_scrubber.visibility = View.VISIBLE
            board.isActive = true
            clearColorFilter()
            setupViewPagerWithFragments()
        } else {
            board.isActive = false
            applyInactiveBoardColorFilter()
            showBoardExpirationDialog(activity) { dialog, _ ->
                setupViewPagerWithFragments()
                dialog.cancel()
            }
        }
    }

    override fun updateFullScreenAdapter(feedEntryList: List<FeedEntry>) {
        feedEntries = feedEntryList
    }

    override fun onDestroy() {
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