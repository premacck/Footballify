package life.plank.juna.zone.view.fragment.board.fixture

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.prembros.asymmetricrecyclerview.base.AsymmetricRecyclerViewListener
import com.prembros.asymmetricrecyclerview.widget.AsymmetricRecyclerViewAdapter
import kotlinx.android.synthetic.main.emoji_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_board_tiles.*
import kotlinx.android.synthetic.main.item_react.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.RestApiAggregator
import life.plank.juna.zone.data.model.Emoji
import life.plank.juna.zone.data.model.FeedEntry
import life.plank.juna.zone.data.model.binder.PollBindingModel
import life.plank.juna.zone.data.model.poll.Poll
import life.plank.juna.zone.data.model.poll.PollAnswerRequest
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.interfaces.EmojiContainer
import life.plank.juna.zone.interfaces.FeedEntryContainer
import life.plank.juna.zone.interfaces.PollContainer
import life.plank.juna.zone.util.AppConstants.BoomMenuPage.BOOM_MENU_FULL
import life.plank.juna.zone.util.DataUtil.findString
import life.plank.juna.zone.util.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.UIDisplayUtil.addDefaultEmojis
import life.plank.juna.zone.util.UIDisplayUtil.setupFeedEntryByMasonryLayout
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.facilis.setEmoji
import life.plank.juna.zone.util.facilis.showFor
import life.plank.juna.zone.util.facilis.vibrate
import life.plank.juna.zone.util.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.util.setupBoomMenu
import life.plank.juna.zone.util.setupWith
import life.plank.juna.zone.view.adapter.board.tile.BoardMediaAdapter
import life.plank.juna.zone.view.adapter.common.EmojiAdapter
import life.plank.juna.zone.view.fragment.base.BaseFragment
import life.plank.juna.zone.view.fragment.base.CardTileFragment
import life.plank.juna.zone.view.fragment.board.fixture.extra.DartBoardPopup
import life.plank.juna.zone.view.fragment.board.fixture.extra.KeyBoardPopup
import life.plank.juna.zone.view.fragment.board.user.PrivateBoardFragment
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.support.v4.toast
import java.net.HttpURLConnection
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class BoardTilesFragment : BaseFragment(), AsymmetricRecyclerViewListener, PollContainer, EmojiContainer {

    @Inject
    lateinit var restApi: RestApi

    private var adapter: BoardMediaAdapter? = null

    private lateinit var boardId: String
    private var isBoardActive: Boolean = false
    private var pollBindingModel: PollBindingModel? = null
    private var emojiBottomSheetBehavior: BottomSheetBehavior<*>? = null
    private var emojiAdapter: EmojiAdapter? = null

    companion object {
        private val TAG = BoardTilesFragment::class.java.simpleName
        fun newInstance(boardId: String, isBoardActive: Boolean) = BoardTilesFragment().apply {
            arguments = Bundle().apply {
                putString(findString(R.string.intent_board_id), boardId)
                putBoolean(findString(R.string.intent_is_board_active), isBoardActive)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)
        arguments?.run {
            boardId = getString(getString(R.string.intent_board_id))!!
            isBoardActive = getBoolean(getString(R.string.intent_is_board_active))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_board_tiles, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (isNullOrEmpty(boardId)) {
            no_data.setText(R.string.login_signup_to_view_feed)
            no_data.visibility = View.VISIBLE
            board_tiles_list.visibility = View.GONE
            arc_menu.visibility = View.GONE
            progress_bar.visibility = View.GONE
            return
        }
        initRecyclerViews()
        setupEmojiBottomSheet()
        arc_menu.setupWith(nestedScrollView)

        if (isBoardActive) {
            setupBoomMenu(BOOM_MENU_FULL, Objects.requireNonNull<FragmentActivity>(activity), boardId, arc_menu, emojiBottomSheetBehavior)
        } else {
            arc_menu.onDebouncingClick { toast(R.string.board_not_active) }
        }

        if (parentFragment is PrivateBoardFragment) {
            tile_content_layout.removeView(extras_layout)
            tile_content_layout.removeView(board_poll)
        } else {
            getBoardPolls()
        }

        setListeners()
    }

    private fun setListeners() {
        swipe_refresh_layout.setOnRefreshListener { getBoardFeed(true) }
        if (parentFragment !is PrivateBoardFragment) {
            dart_board.onDebouncingClick { (parentFragment as? CardTileFragment)?.pushPopup(DartBoardPopup.newInstance()) }
            key_board.onDebouncingClick { (parentFragment as? CardTileFragment)?.pushPopup(KeyBoardPopup.newInstance()) }
            react.onDebouncingClick { emojiBottomSheetBehavior?.showFor(emojiAdapter, boardId) }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isNullOrEmpty(boardId)) {
            updateUi(false, R.string.login_signup_to_view_feed)
            return
        }
        getBoardFeed(false)
        getTopEmoji()
    }

    private fun setupEmojiBottomSheet() {
        emojiBottomSheetBehavior = BottomSheetBehavior.from(emoji_bottom_sheet)
        emojiBottomSheetBehavior?.peekHeight = 0

        emojiAdapter = EmojiAdapter(restApi, boardId, emojiBottomSheetBehavior, null, true, this)
        emoji_recycler_view.adapter = emojiAdapter
    }

    private fun initRecyclerViews() {
        adapter = BoardMediaAdapter(Glide.with(this))
        board_tiles_list.setRequestedColumnCount(3)
        val padding = resources.getDimensionPixelSize(R.dimen.recycler_padding)
        board_tiles_list.requestedHorizontalSpacing = padding
        board_tiles_list.setClickListener(this)
        board_tiles_list.adapter = AsymmetricRecyclerViewAdapter(context!!, board_tiles_list, adapter!!)
    }

    fun updateNewPost(feedItem: FeedEntry) {
        adapter?.run {
            if (getBoardFeed().isEmpty()) {
                updateUi(true, 0)
            }
            updateNewPost(feedItem)
            board_tiles_list.smoothScrollToPosition(0)
        }
    }

    private fun getTopEmoji() {
        restApi.getTopBoardEmoji(boardId, getToken()).setObserverThreadsAndSmartSubscribe({}, {
            var emojiList = it.body()
            if (isNullOrEmpty(emojiList)) {
                val emoji = emojiList?.get(0)!!
                reaction_text_view.setEmoji(emoji.emoji)
                reaction_count.text = emoji.emojiCount.toString()
            } else {
                reaction_text_view.visibility = View.GONE
                emojiList = ArrayList()
                addDefaultEmojis(emojiList)
                val random = Random()
                initial_reaction_one.setEmoji(emojiList[random.nextInt(emojiList.size - 1)].emoji)
                initial_reaction_two.setEmoji(emojiList[random.nextInt(emojiList.size - 1)].emoji)
            }
        })
    }

    private fun getBoardFeed(isRefreshing: Boolean) {
        restApi.getBoardFeedItems(boardId, getToken()).doOnSubscribe {
            runOnUiThread {
                progress_bar.visibility = View.VISIBLE
                no_data.visibility = View.GONE
                if (isRefreshing) swipe_refresh_layout.isRefreshing = true
            }
        }.doOnTerminate {
            runOnUiThread {
                progress_bar.visibility = View.GONE
                if (isRefreshing) swipe_refresh_layout.isRefreshing = false
            }
        }.setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "On Error() : getBoardFeed()", it)
            updateUi(false, R.string.something_went_wrong)
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_OK -> {
                    val feedItemList = it.body()
                    if (!isNullOrEmpty(feedItemList)) {
                        updateUi(true, 0)
                        setupFeedEntryByMasonryLayout(feedItemList!!)
                        adapter!!.update(feedItemList)
                        if (parentFragment is FeedEntryContainer) {
                            (parentFragment as FeedEntryContainer).updateFullScreenAdapter(feedItemList)
                        }
                    } else updateUi(false, R.string.board_yet_to_be_populated)
                }
                HttpURLConnection.HTTP_NOT_FOUND -> updateUi(false, R.string.board_yet_to_be_populated)
                else -> updateUi(false, R.string.failed_to_retrieve_board)
            }
        })
    }

    private fun getBoardPolls() {
        RestApiAggregator.getPoll(restApi, boardId).setObserverThreadsAndSmartSubscribe<Poll>({
            Log.e(TAG, "getBoardPolls()", it)
        }, {
            if (parentFragment is MatchBoardFragment) {
                pollBindingModel = PollBindingModel.from(it, (parentFragment as MatchBoardFragment).matchDetails)
                if (activity != null) {
                    board_poll.prepare(Glide.with(activity!!), pollBindingModel!!, this)
                }
            }
        })
    }

    private fun updateUi(isDataAvailable: Boolean, @StringRes message: Int) {
        board_tiles_list.visibility = if (isDataAvailable) View.VISIBLE else View.GONE
        no_data.visibility = if (isDataAvailable) View.GONE else View.VISIBLE
        if (no_data.text.toString().isEmpty() && message != 0) {
            no_data.setText(message)
        }
    }

    override fun onEmojiPosted(emoji: Emoji) = getTopEmoji()

    override fun fireOnItemClick(index: Int, v: View) {
//        adapter?.getBoardFeed()?.run {
//            if (!isNullOrEmpty(this)) {
//                (parentFragment as? FeedEntryContainer)?.openFeedEntry(this, boardId!!, index, BOARD)
//            }
//        }
    }

    override fun fireOnItemLongClick(index: Int, v: View): Boolean {
        if (parentFragment is FeedEntryContainer) {
            vibrate(20)
            (parentFragment as FeedEntryContainer).showFeedItemPeekPopup(index)
        }
        return true
    }

    override fun onPollSelected(pollAnswerRequest: PollAnswerRequest) {
        restApi.postBoardPollAnswer(pollAnswerRequest, getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "onPollSelected(): ", it)
        }, {
            val pollAnswer = it.body()
            if (pollAnswer != null) {
                pollBindingModel!!.poll.totalVotes = pollAnswer.totalVotes
                pollBindingModel!!.poll.userSelection = pollAnswer.userSelection
                pollBindingModel!!.poll.choices = pollAnswer.choices
                board_poll.pollSelected(pollBindingModel!!.poll)
            }
        })
    }
}