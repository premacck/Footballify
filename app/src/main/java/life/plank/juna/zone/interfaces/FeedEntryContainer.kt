package life.plank.juna.zone.interfaces

import life.plank.juna.zone.data.model.FeedEntry

interface FeedEntryContainer {

    fun dismissFullScreenRecyclerView()

    fun prepareFullScreenRecyclerView()

    fun updateFullScreenAdapter(feedEntryList: List<FeedEntry>)

    fun moveItem(position: Int, previousPosition: Int)

    fun setBlurBackgroundAndShowFullScreenTiles(setFlag: Boolean, position: Int)

    fun openFeedEntry(feedEntryList: MutableList<FeedEntry>, boardId: String, position: Int, target: String)
}