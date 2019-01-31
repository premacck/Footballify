package life.plank.juna.zone.ui.board.fragment.user

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.*
import com.prembros.facilis.util.floatUp
import kotlinx.android.synthetic.main.fragment_private_board.*
import kotlinx.android.synthetic.main.layout_board_engagement.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.component.helper.getPositionFromIntentIfAny
import life.plank.juna.zone.data.api.RestApi
import life.plank.juna.zone.data.api.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.data.model.board.Board
import life.plank.juna.zone.data.model.feed.FeedEntry
import life.plank.juna.zone.data.model.notification.SocialNotification
import life.plank.juna.zone.service.CommonDataService.findString
import life.plank.juna.zone.sharedpreference.CurrentUser
import life.plank.juna.zone.ui.base.BaseJunaCardActivity
import life.plank.juna.zone.ui.base.fragment.BaseJunaCard
import life.plank.juna.zone.ui.base.fragment.CardTileFragment
import life.plank.juna.zone.ui.board.fragment.fixture.BoardTilesFragment
import life.plank.juna.zone.ui.common.BoardHeaderListener
import life.plank.juna.zone.ui.forum.ForumFragment
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.util.common.customToast
import life.plank.juna.zone.util.common.errorToast
import java.net.HttpURLConnection
import javax.inject.Inject

class PrivateBoardFragment : CardTileFragment(), BoardHeaderListener {

    @Inject
    lateinit var restApi: RestApi

    private lateinit var feedEntries: List<FeedEntry>
    lateinit var boardId: String
    lateinit var board: Board
    private var pagerAdapter: PrivateBoardPagerAdapter? = null
    private val deleteBoardListener = View.OnClickListener { deletePrivateBoard() }

    companion object {
        val TAG: String = PrivateBoardFragment::class.java.simpleName
        fun newInstance(board: Board): PrivateBoardFragment = PrivateBoardFragment().apply {
            arguments = Bundle().apply { putParcelable(findString(R.string.intent_board), board) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.application.uiComponent.inject(this)
        board = arguments?.getParcelable(getString(R.string.intent_board))!!
        boardId = board.id
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_private_board, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        root_card.floatUp()

        if (board.owner.displayName == CurrentUser.displayName) {
            activity?.let { private_board_toolbar.setUpPrivateBoardPopUp(it, PRIVATE_BOARD_OWNER_POPUP, deleteBoardListener) }
        } else {
            activity?.let { private_board_toolbar.setUpPrivateBoardPopUp(it, PRIVATE_BOARD_USER_POPUP, deleteBoardListener) }
        }
        board.interactions?.run {
            people_count.text = followers.toString()
            post_count.text = posts.toString()
            interaction_count.text = (followers + posts + emojiReacts).toString()
        }
        private_board_toolbar.setTitle(board.displayName)
        private_board_toolbar.setBoardTitle(if (board.boardType == getString(R.string.public_lowercase)) R.string.public_board else R.string.private_board)
        private_board_toolbar.setLeagueLogo(board.boardIconUrl!!)
        val color = Color.parseColor(board.color)
        private_board_toolbar.setBackgroundColor(color)
        root_card.setCardBackgroundColor(color)

        setupViewPagerWithFragments()
    }

    private fun setupViewPagerWithFragments() {
        pagerAdapter = PrivateBoardPagerAdapter(childFragmentManager, board)
        private_board_view_pager.adapter = pagerAdapter
        private_board_toolbar.setupWithViewPager(private_board_view_pager, getPositionFromIntentIfAny(pagerAdapter))
    }

    override fun onShareClick() {}

    override fun getBackgroundBlurLayout(): ViewGroup? = root_blur_layout

    override fun getRootView(): CardView? = root_card

    override fun getDragView(): View? = drag_area

    override fun updateFullScreenAdapter(feedEntryList: List<FeedEntry>) {
        feedEntries = feedEntryList
    }

    override fun getFeedEntries(): List<FeedEntry> = feedEntries

    override fun getFeedEntry(position: Int): FeedEntry = feedEntries[position]

    override fun getTheBoardId(): String? = boardId

    override fun onSocialNotificationReceive(socialNotification: SocialNotification) {
        socialNotification.run {
            when (action) {
                findString(R.string.intent_post) -> getFeedEntryDetails(restApi, this)
                findString(R.string.intent_comment) -> updateForumComments()
                findString(R.string.intent_react) -> {
                }
            }
        }
    }

    override fun onNewFeedEntry(feedEntry: FeedEntry) {
        (pagerAdapter?.currentFragment as? BoardTilesFragment)?.updateNewPost(feedEntry)
    }

    override fun updateForumComments() {
        (pagerAdapter?.currentFragment as? ForumFragment)?.getComments(false)
    }

    private fun deletePrivateBoard() {
        restApi.deleteBoard(boardId).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "onError: ", it)
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_NO_CONTENT -> {
                    customToast(R.string.board_deletion)
                    if (parentFragment != null && parentFragment is BaseJunaCard) {
                        (parentFragment as BaseJunaCard).parentActivity().popBackStack()
                    } else if (activity is BaseJunaCardActivity) {
                        (activity as BaseJunaCardActivity).popBackStack()
                    }
                }
                else -> errorToast(R.string.something_went_wrong, it)
            }
        }, this)
    }

    class PrivateBoardPagerAdapter(fm: FragmentManager, private val board: Board) : FragmentPagerAdapter(fm) {

        var currentFragment: Fragment? = null

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> PrivateBoardInfoFragment.newInstance(board.description!!, board.id, board.owner.displayName, board.displayName!!)
                1 -> ForumFragment.newInstance(board.id)
                2 -> BoardTilesFragment.newInstance(board.id, true)
                else -> BoardTilesFragment.newInstance(board.id, true)
            }
        }

        override fun getItemPosition(`object`: Any): Int {
            return when (`object`) {
                findString(R.string.info) -> 0
                findString(R.string.forum) -> 1
                findString(R.string.tiles) -> 2
                else -> super.getItemPosition(`object`)
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> findString(R.string.info)
                1 -> findString(R.string.forum)
                2 -> findString(R.string.tiles)
                else -> null
            }
        }

        override fun getCount(): Int = 3

        override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
            if (currentFragment !== `object`) {
                currentFragment = `object` as Fragment
            }
            super.setPrimaryItem(container, position, `object`)
        }
    }
}