package life.plank.juna.zone.view.fragment.clickthrough

import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import kotlinx.android.synthetic.main.emoji_bottom_sheet.*
import kotlinx.android.synthetic.main.popup_feed_item_peek.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.FeedEntry
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.view.adapter.BoardFeedDetailAdapter
import life.plank.juna.zone.view.adapter.EmojiAdapter
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class FeedItemPeekPopup : DialogFragment() {

    @field: [Inject Named("default")]
    lateinit var restApi: RestApi

    lateinit var feedEntries: List<FeedEntry>
    var boardFeedDetailAdapter: BoardFeedDetailAdapter? = null
    private var emojiBottomSheetBehavior: BottomSheetBehavior<*>? = null
    private var emojiAdapter: EmojiAdapter? = null
    private var boardId: String? = null
    private var isBoardActive: Boolean = false
    private var target: String? = null

    companion object {
        fun newInstance(feedEntries: List<FeedEntry>, boardId: String?, isBoardActive: Boolean = true, target: String?) = FeedItemPeekPopup().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(getString(R.string.intent_feed_items), feedEntries as ArrayList<out Parcelable>)
                putString(getString(R.string.intent_board_id), boardId)
                putBoolean(getString(R.string.intent_is_board_active), isBoardActive)
                putString(getString(R.string.intent_target), target)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            feedEntries = getParcelableArrayList(getString(R.string.intent_feed_items))!!
            boardId = getString(getString(R.string.intent_board_id))
            isBoardActive = getBoolean(getString(R.string.intent_is_board_active))
            target = getString(getString(R.string.intent_target))
        }
        ZoneApplication.getApplication().uiComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.popup_feed_item_peek, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initBottomSheet()
        initAdapter()
    }

    override fun onStart() {
        super.onStart()
        blur_layout.startBlur()
    }

    override fun onStop() {
        blur_layout.pauseBlur()
        super.onStop()
    }

    private fun initBottomSheet() {
        emojiAdapter = EmojiAdapter(ZoneApplication.getContext(), if (boardId == null) "" else boardId, emojiBottomSheetBehavior)
        emoji_recycler_view.adapter = emojiAdapter
        emojiBottomSheetBehavior = BottomSheetBehavior.from(emoji_bottom_sheet)
        emojiBottomSheetBehavior?.peekHeight = 0
    }

    private fun initAdapter() {
        boardFeedDetailAdapter = BoardFeedDetailAdapter(activity as BaseBoardActivity, boardId, isBoardActive, emojiBottomSheetBehavior, null)
        board_tiles_full_recycler_view.adapter = boardFeedDetailAdapter
        board_tiles_full_recycler_view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(200)
                .setInterpolator(OvershootInterpolator(2f))
                .start()
    }
}