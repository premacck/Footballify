package life.plank.juna.zone.view.fragment.clickthrough

import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.BottomSheetBehavior
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
import life.plank.juna.zone.util.DataUtil
import life.plank.juna.zone.util.facilis.SwipeDownToDismissListener
import life.plank.juna.zone.view.adapter.BoardFeedDetailAdapter
import life.plank.juna.zone.view.adapter.EmojiAdapter
import life.plank.juna.zone.view.fragment.base.BaseDialogFragment
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class FeedItemPeekPopup : BaseDialogFragment() {

    @field: [Inject Named("default")]
    lateinit var restApi: RestApi

    lateinit var feedEntries: List<FeedEntry>
    var boardFeedDetailAdapter: BoardFeedDetailAdapter? = null
    private var emojiBottomSheetBehavior: BottomSheetBehavior<*>? = null
    private var emojiAdapter: EmojiAdapter? = null
    private var boardId: String? = null
    private var isBoardActive: Boolean = false
    private var target: String? = null
    private var position: Int = 0

    companion object {
        fun newInstance(feedEntries: List<FeedEntry>, boardId: String?, isBoardActive: Boolean = true, target: String?, position: Int) =
                FeedItemPeekPopup().apply {
                    arguments = Bundle().apply {
                        putParcelableArrayList(DataUtil.getString(R.string.intent_feed_items), feedEntries as ArrayList<out Parcelable>)
                        putString(DataUtil.getString(R.string.intent_board_id), boardId)
                        putBoolean(DataUtil.getString(R.string.intent_is_board_active), isBoardActive)
                        putString(DataUtil.getString(R.string.intent_target), target)
                        putInt(DataUtil.getString(R.string.intent_position), position)
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
            position = getInt(getString(R.string.intent_position))
        }
        ZoneApplication.getApplication().uiComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.popup_feed_item_peek, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initBottomSheet()
        initAdapter()
        setupPeekRecyclerViewSwipeGesture()
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
//        TODO: un-comment when committing BoardFeedDetailAdapter
//        boardFeedDetailAdapter = BoardFeedDetailAdapter(restApi, boardId, isBoardActive, emojiBottomSheetBehavior, null)
        board_tiles_full_recycler_view.adapter = boardFeedDetailAdapter
        board_tiles_full_recycler_view.scrollToPosition(position)
        board_tiles_full_recycler_view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(200)
                .setInterpolator(OvershootInterpolator(2f))
                .start()
    }

    private fun setupPeekRecyclerViewSwipeGesture() {
        recycler_view_drag_area.setOnTouchListener(object : SwipeDownToDismissListener(activity!!, recycler_view_drag_area, board_tiles_full_recycler_view, root_peek_layout) {
            override fun onSwipeDown() {
                dismiss()
            }
        })
    }

    override fun onBackPressed(): Boolean {
        if (emojiBottomSheetBehavior?.peekHeight!! > 0 || emojiBottomSheetBehavior?.state != BottomSheetBehavior.STATE_COLLAPSED) {
            emojiBottomSheetBehavior!!.peekHeight = 0
            emojiBottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
            return false
        }
        return true
    }
}