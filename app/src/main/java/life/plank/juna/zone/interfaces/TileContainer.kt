package life.plank.juna.zone.interfaces

import life.plank.juna.zone.data.model.FeedEntry

interface TileContainer {

    fun dismissFullScreenRecyclerView()

    fun prepareFullScreenRecyclerView()

    fun updateFullScreenAdapter(feedEntryList: List<FeedEntry>)

    fun moveItem(position: Int, previousPosition: Int)

    fun setBlurBackgroundAndShowFullScreenTiles(setFlag: Boolean, position: Int)
}