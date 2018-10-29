package life.plank.juna.zone.view.fragment.clickthrough

import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.PagerSnapHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.emoji_bottom_sheet.*
import kotlinx.android.synthetic.main.popup_feed_item_peek.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.FeedEntry
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.DataUtil.findString
import life.plank.juna.zone.util.facilis.zoomOut
import life.plank.juna.zone.view.adapter.BoardFeedDetailAdapter
import life.plank.juna.zone.view.adapter.EmojiAdapter
import life.plank.juna.zone.view.fragment.base.BaseBlurPopup
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@Suppress("DeferredResultUnused")
class FeedItemPeekPopup : BaseBlurPopup() {

    @field: [Inject Named("default")]
    lateinit var restApi: RestApi

    private lateinit var feedEntries: List<FeedEntry>
    var boardFeedDetailAdapter: BoardFeedDetailAdapter? = null
    private var emojiBottomSheetBehavior: BottomSheetBehavior<*>? = null
    private var emojiAdapter: EmojiAdapter? = null
    private var boardId: String? = null
    private var isBoardActive: Boolean = false
    private var target: String? = null
    private var position: Int = 0

    companion object {
        val TAG: String = FeedItemPeekPopup::class.java.simpleName
        fun newInstance(feedEntries: List<FeedEntry>, boardId: String?, isBoardActive: Boolean = true, target: String?, position: Int) =
                FeedItemPeekPopup().apply {
                    arguments = Bundle().apply {
                        putParcelableArrayList(findString(R.string.intent_feed_items), feedEntries as ArrayList<out Parcelable>)
                        putString(findString(R.string.intent_board_id), boardId)
                        putBoolean(findString(R.string.intent_is_board_active), isBoardActive)
                        putString(findString(R.string.intent_target), target)
                        putInt(findString(R.string.intent_position), position)
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

    override fun doOnStart() {
        initBottomSheet()
        initRecyclerView()
    }

    override fun dismiss() {
        if (emojiBottomSheetBehavior?.peekHeight!! > 0 || emojiBottomSheetBehavior?.state != BottomSheetBehavior.STATE_COLLAPSED) {
            emojiBottomSheetBehavior!!.peekHeight = 0
            emojiBottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            board_tiles_full_recycler_view?.zoomOut()
            super.dismiss()
        }
    }

    override fun getBlurLayout(): BlurLayout? = blur_layout

    override fun getDragHandle(): View? = recycler_view_drag_area

    override fun getRootView(): View? = board_tiles_full_recycler_view

    override fun getBackgroundLayout(): ViewGroup? = root_peek_layout

    private fun initBottomSheet() {
        emojiAdapter = EmojiAdapter(ZoneApplication.getContext(), if (boardId == null) "" else boardId, emojiBottomSheetBehavior)
        emoji_recycler_view.adapter = emojiAdapter
        emojiBottomSheetBehavior = BottomSheetBehavior.from(emoji_bottom_sheet)
        emojiBottomSheetBehavior?.peekHeight = 0
        emoji_bottom_sheet.visibility = View.VISIBLE
    }

    private fun initRecyclerView() {
        boardFeedDetailAdapter = BoardFeedDetailAdapter(restApi, boardId, isBoardActive, emojiBottomSheetBehavior, null)
        board_tiles_full_recycler_view.adapter = boardFeedDetailAdapter
        boardFeedDetailAdapter?.update(feedEntries)
        PagerSnapHelper().attachToRecyclerView(board_tiles_full_recycler_view)
        board_tiles_full_recycler_view.scrollToPosition(position)
        board_tiles_full_recycler_view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in))
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