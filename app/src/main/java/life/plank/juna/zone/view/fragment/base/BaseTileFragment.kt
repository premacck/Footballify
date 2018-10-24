package life.plank.juna.zone.view.fragment.base

import android.annotation.SuppressLint
import android.view.View
import life.plank.juna.zone.data.model.FeedEntry
import life.plank.juna.zone.util.OnSwipeTouchListener
import life.plank.juna.zone.view.adapter.BoardFeedDetailAdapter

abstract class BaseTileFragment : BaseFragment() {

    protected var isTileFullScreenActive: Boolean = false
    protected var boardFeedDetailAdapter: BoardFeedDetailAdapter? = null

    abstract fun dismissFullScreenRecyclerView()

    abstract fun prepareFullScreenRecyclerView()

    abstract fun updateFullScreenAdapter(feedEntryList: List<FeedEntry>)

    abstract fun moveItem(position: Int, previousPosition: Int)

    abstract fun setBlurBackgroundAndShowFullScreenTiles(setFlag: Boolean, position: Int)

    @SuppressLint("ClickableViewAccessibility")
    protected fun setupFullScreenRecyclerViewSwipeGesture(recyclerViewDragArea: View, boardTilesFullRecyclerView: View) {
        recyclerViewDragArea.setOnTouchListener(object : OnSwipeTouchListener(activity, recyclerViewDragArea, boardTilesFullRecyclerView) {
            override fun onSwipeDown() {
                dismissFullScreenRecyclerView()
            }
        })
    }
}