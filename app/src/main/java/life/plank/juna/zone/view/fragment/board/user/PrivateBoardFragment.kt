package life.plank.juna.zone.view.fragment.board.user

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.CardView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.fragment_private_board.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.Board
import life.plank.juna.zone.data.model.FeedEntry
import life.plank.juna.zone.data.model.FeedItem
import life.plank.juna.zone.data.model.Thumbnail
import life.plank.juna.zone.data.model.notification.JunaNotification
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.notification.getIntentActionFromActivity
import life.plank.juna.zone.util.AppConstants.PRIVATE_BOARD_OWNER_POPUP
import life.plank.juna.zone.util.AppConstants.PRIVATE_BOARD_USER_POPUP
import life.plank.juna.zone.util.DataUtil.findString
import life.plank.juna.zone.util.PreferenceManager
import life.plank.juna.zone.util.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.customToast
import life.plank.juna.zone.util.errorToast
import life.plank.juna.zone.util.facilis.BaseCard
import life.plank.juna.zone.util.facilis.doAfterDelay
import life.plank.juna.zone.util.facilis.floatUp
import life.plank.juna.zone.util.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.fragment.base.CardTileFragment
import life.plank.juna.zone.view.fragment.board.fixture.BoardTilesFragment
import life.plank.juna.zone.view.fragment.forum.ForumFragment
import java.net.HttpURLConnection
import javax.inject.Inject

class PrivateBoardFragment : CardTileFragment() {

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
        ZoneApplication.getApplication().uiComponent.inject(this)
        board = arguments?.getParcelable(getString(R.string.intent_board))!!
        boardId = board.id
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_private_board, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        root_card.floatUp()

        if (board.owner.displayName == PreferenceManager.CurrentUser.getDisplayName()) {
            activity?.let { private_board_toolbar.setUpPrivateBoardPopUp(it, PRIVATE_BOARD_OWNER_POPUP, deleteBoardListener) }
        } else {
            activity?.let { private_board_toolbar.setUpPrivateBoardPopUp(it, PRIVATE_BOARD_USER_POPUP, deleteBoardListener) }
        }

        private_board_toolbar.setTitle(board.name)
        private_board_toolbar.setBoardTitle(if (board.boardType == getString(R.string.public_lowercase)) R.string.public_board else R.string.private_board)
        private_board_toolbar.setLeagueLogo(board.boardIconUrl!!)
        val color = Color.parseColor(board.color)
        private_board_toolbar.setBackgroundColor(color)
        root_card.setCardBackgroundColor(color)

        context?.doAfterDelay(400) { setupViewPagerWithFragments() }
        val topic = getString(R.string.board_id_prefix) + board.id
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
    }

    private fun setupViewPagerWithFragments() {
        pagerAdapter = PrivateBoardPagerAdapter(childFragmentManager, board)
        private_board_view_pager.adapter = pagerAdapter
        val defaultTabSelection = getIntentActionFromActivity()?.run {
            //            TODO: refine the following hardcoded integer constants
            when (this) {
                getString(R.string.intent_post), getString(R.string.intent_react) -> 2
                getString(R.string.intent_comment) -> 1
                else -> 2
            }
        } ?: 2
        private_board_toolbar.setupWithViewPager(private_board_view_pager, defaultTabSelection)
    }

    override fun getBackgroundBlurLayout(): ViewGroup? = root_blur_layout

    override fun getRootCard(): CardView? = root_card

    override fun getDragHandle(): View? = drag_area

    override fun updateFullScreenAdapter(feedEntryList: List<FeedEntry>) {
        feedEntries = feedEntryList
    }

    override fun getFeedEntries(): List<FeedEntry> = feedEntries

    override fun getTheBoardId(): String? = boardId

    private fun deletePrivateBoard() {
        restApi.deleteBoard(boardId, getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "onError: ", it)
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_NO_CONTENT -> {
                    customToast(R.string.board_deletion)
                    if (parentFragment != null && parentFragment is BaseCard) {
                        (parentFragment as BaseCard).getParentActivity().popBackStack()
                    } else if (activity is BaseCardActivity) {
                        (activity as BaseCardActivity).popBackStack()
                    }
                }
                else -> errorToast(R.string.something_went_wrong, it)
            }
        })
    }

    internal class PrivateBoardPagerAdapter(fm: FragmentManager, private val board: Board) : FragmentPagerAdapter(fm) {

        var currentFragment: Fragment? = null

        override fun getItem(position: Int): Fragment? {
            return when (position) {
                0 -> PrivateBoardInfoFragment.newInstance(board.description!!, board.id, board.owner.displayName, board.name!!)
                1 -> ForumFragment.newInstance(board.id)
                2 -> BoardTilesFragment.newInstance(board.id, true)
                else -> null
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