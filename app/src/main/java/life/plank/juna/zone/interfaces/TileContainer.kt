package life.plank.juna.zone.interfaces

import android.app.Activity
import android.view.View
import life.plank.juna.zone.data.model.FeedEntry
import life.plank.juna.zone.util.OnSwipeTouchListener

interface TileContainer {

    fun dismissFullScreenRecyclerView()

    fun prepareFullScreenRecyclerView()

    fun updateFullScreenAdapter(feedEntryList: List<FeedEntry>)

    fun moveItem(position: Int, previousPosition: Int)

    fun setBlurBackgroundAndShowFullScreenTiles(setFlag: Boolean, position: Int)

    fun setupFullScreenRecyclerViewSwipeGesture(activity: Activity, recyclerViewDragArea: View, boardTilesFullRecyclerView: View) {
        recyclerViewDragArea.setOnTouchListener(object : OnSwipeTouchListener(activity, recyclerViewDragArea, boardTilesFullRecyclerView) {
            override fun onSwipeDown() {
                dismissFullScreenRecyclerView()
            }
        })
    }
}