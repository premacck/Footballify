package life.plank.juna.zone.ui.board.fragment.fixture

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.prembros.asymrecycler.library.base.AsymRecyclerListener
import com.prembros.asymrecycler.library.widget.AsymRecyclerAdapter
import com.prembros.facilis.activity.BaseCardActivity
import com.prembros.facilis.util.*
import kotlinx.android.synthetic.main.emoji_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_board_tiles.*
import kotlinx.android.synthetic.main.item_react.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.api.*
import life.plank.juna.zone.data.model.board.Emoji
import life.plank.juna.zone.data.model.board.poll.Poll
import life.plank.juna.zone.data.model.board.poll.PollAnswerRequest
import life.plank.juna.zone.data.model.feed.FeedEntry
import life.plank.juna.zone.service.CommonDataService.findString
import life.plank.juna.zone.ui.base.fragment.BaseJunaFragment
import life.plank.juna.zone.ui.base.fragment.CardTileFragment
import life.plank.juna.zone.ui.base.setEmoji
import life.plank.juna.zone.ui.base.showFor
import life.plank.juna.zone.ui.board.adapter.match.bindingmodel.PollBindingModel
import life.plank.juna.zone.ui.board.adapter.tile.BoardMediaAdapter
import life.plank.juna.zone.ui.board.fragment.fixture.extra.DartBoardPopup
import life.plank.juna.zone.ui.board.fragment.fixture.extra.KeyBoardPopup
import life.plank.juna.zone.ui.board.fragment.user.PrivateBoardFragment
import life.plank.juna.zone.ui.emoji.EmojiAdapter
import life.plank.juna.zone.ui.emoji.EmojiContainer
import life.plank.juna.zone.ui.feed.FeedEntryContainer
import life.plank.juna.zone.ui.feed.FeedItemPeekPopup
import life.plank.juna.zone.util.common.AppConstants.BoomMenuPage.BOOM_MENU_FULL
import life.plank.juna.zone.util.view.*
import life.plank.juna.zone.util.view.UIDisplayUtil.addDefaultEmojis
import org.jetbrains.anko.support.v4.toast
import java.net.HttpURLConnection
import java.util.*
import java.util.Objects.requireNonNull
import javax.inject.Inject
import kotlin.collections.ArrayList

class BoardTilesFragment : BaseJunaFragment(), AsymRecyclerListener, PollContainer, EmojiContainer {

    @Inject
    lateinit var restApi: RestApi

    private var adapter: BoardMediaAdapter? = null

    private lateinit var boardId: String
    private var isBoardActive: Boolean = false
    private var pollBindingModel: PollBindingModel? = null
    private var emojiBottomSheetBehavior: BottomSheetBehavior<*>? = null
    private var emojiAdapter: EmojiAdapter? = null
    private var feedItemList: MutableList<FeedEntry>? = ArrayList()

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
        ZoneApplication.application.uiComponent.inject(this)
        arguments?.run {
            boardId = getString(getString(R.string.intent_board_id))!!
            isBoardActive = getBoolean(getString(R.string.intent_is_board_active))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_board_tiles, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (isNullOrEmpty(boardId)) {
            board_tiles_list.visibility = View.GONE
            boomMenu().visibility = View.GONE
            return
        }
        initRecyclerViews()
        setupEmojiBottomSheet()
        boomMenu().setupWith(nestedScrollView)

        if (isBoardActive) {
            setupBoomMenu(BOOM_MENU_FULL, requireNonNull<FragmentActivity>(activity), boardId, emojiBottomSheetBehavior)
        } else {
            boomMenu().onDebouncingClick { toast(R.string.board_not_active) }
        }

        if (parentFragment is PrivateBoardFragment) {
            tile_content_layout.removeView(extras_layout)
            tile_content_layout.removeView(board_poll)
        } else {
//            TODO: un-comment after the go-ahead from backend
//            getBoardPolls()
        }

        getBoardFeed(false)
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
        if (parentFragment is MatchBoardFragment) {
            getTopEmoji()
        }
    }

    private fun setupEmojiBottomSheet() {
        emojiBottomSheetBehavior = BottomSheetBehavior.from(emoji_bottom_sheet)
        emojiBottomSheetBehavior?.peekHeight = 0

        emojiAdapter = EmojiAdapter(restApi, boardId, emojiBottomSheetBehavior, null, false, this)
        emoji_recycler_view.adapter = emojiAdapter
    }

    private fun initRecyclerViews() {
        adapter = BoardMediaAdapter(Glide.with(this))
        board_tiles_list.setRequestedColumnCount(3)
        val padding = resources.getDimensionPixelSize(R.dimen.recycler_padding)
        board_tiles_list.requestedHorizontalSpacing = padding
        board_tiles_list.setClickListener(this)
        board_tiles_list.adapter = AsymRecyclerAdapter(context!!, board_tiles_list, adapter!!).withPopupClick()
    }

    fun updateNewPost(feedItem: FeedEntry) {
        adapter?.run {
            if (boardFeed.isEmpty()) {
                updateUi(true, 0)
            }
            updateNewPost(feedItem)
            board_tiles_list.smoothScrollToPosition(0)
        }
    }

    private fun getTopEmoji() {
        restApi.getTopBoardEmoji(boardId).setObserverThreadsAndSmartSubscribe({}, {
            var emojiList = it.body()
            if (!isNullOrEmpty(emojiList)) {
                val emoji = emojiList?.get(0)!!
                onReactionUpdate(true)
                reaction_text_view.setEmoji(emoji.emoji)
                reaction_count.text = emoji.emojiCount.toString()
            } else {
                onReactionUpdate(false)
                emojiList = ArrayList()
                addDefaultEmojis(emojiList)
                val random = Random()
                initial_reaction_one.setEmoji(emojiList[random.nextInt(emojiList.size - 1)].emoji)
                initial_reaction_two.setEmoji(emojiList[random.nextInt(emojiList.size - 1)].emoji)
            }
        }, this)
    }

    private fun onReactionUpdate(isAvailable: Boolean) {
        react_message.visibility = if (isAvailable) View.GONE else View.VISIBLE
        reaction_text_view.visibility = if (isAvailable) View.VISIBLE else View.GONE
        initial_reaction_layout.visibility = if (isAvailable) View.GONE else View.VISIBLE
    }

    private fun getBoardFeed(isRefreshing: Boolean) {
        restApi.getBoardFeedItems(boardId)
                .onSubscribe {
                    if (isRefreshing) swipe_refresh_layout.isRefreshing = true
                }.onTerminate {
                    if (isRefreshing) swipe_refresh_layout.isRefreshing = false
                }.setObserverThreadsAndSmartSubscribe({
                    Log.e(TAG, "On Error() : getBoardFeed()", it)
                    updateUi(false, R.string.something_went_wrong)
                }, {
                    when (it.code()) {
                        HttpURLConnection.HTTP_OK -> {
                            feedItemList = it.body()
                            if (!isNullOrEmpty(feedItemList)) {
                                updateUi(true, 0)
                                adapter?.update(feedItemList!!)
                                if (parentFragment is FeedEntryContainer) {
                                    (parentFragment as FeedEntryContainer).updateFullScreenAdapter(feedItemList!!)
                                }
                            } else updateUi(false, R.string.board_yet_to_be_populated)
                        }
                        HttpURLConnection.HTTP_NOT_FOUND -> updateUi(false, R.string.board_yet_to_be_populated)
                        else -> updateUi(false, R.string.failed_to_retrieve_board)
                    }
                }, this)
    }

    private fun getBoardPolls() {
        RestApiAggregator.getPoll(restApi, boardId).setObserverThreadsAndSmartSubscribe<Poll>({
            Log.e(TAG, "getBoardPolls()", it)
        }, {
            if (parentFragment is MatchBoardFragment) {
                pollBindingModel = PollBindingModel.from(it, (parentFragment as MatchBoardFragment).matchDetails)
                if (activity != null) {
                    board_poll.visibility = View.VISIBLE
                    board_poll.prepare(Glide.with(activity!!), pollBindingModel!!, this)
                }
            }
        }, this)
    }

    private fun updateUi(isDataAvailable: Boolean, @StringRes message: Int) {
        board_tiles_list.visibility = if (isDataAvailable) View.VISIBLE else View.GONE
        no_data.visibility = if (isDataAvailable) View.GONE else View.VISIBLE
        if (no_data.text.toString().isEmpty() && message != 0) {
            no_data.setText(message)
        }
    }

    override fun onEmojiPosted(emoji: Emoji) = getTopEmoji()

    override fun onAsymmetricRecyclerItemClick(index: Int, v: View) {
        adapter?.boardFeed?.get(index)?.run {
            (parentFragment as? FeedEntryContainer)?.openFeedEntry(adapter?.boardFeed!!, boardId, index)
        }
    }

    override fun setOnItemPopupListener(index: Int, view: View) {
        feedItemList?.get(index)?.run {
            LongPopupClickListener.inside(activity as BaseCardActivity)
                    .withVibration()
                    .withPopup(FeedItemPeekPopup.newInstance(this, boardId, true))
                    .setOn(view)
        }
    }

    override fun onPollSelected(pollAnswerRequest: PollAnswerRequest) {
        restApi.postBoardPollAnswer(pollAnswerRequest).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "onPollSelected(): ", it)
        }, {
            val pollAnswer = it.body()
            if (pollAnswer != null) {
                pollBindingModel?.poll?.totalVotes = pollAnswer.totalVotes
                pollBindingModel?.poll?.userSelection = pollAnswer.userSelection
                pollBindingModel?.poll?.choices = pollAnswer.choices
                board_poll.pollSelected(pollBindingModel!!.poll)
            }
        }, this)
    }
}