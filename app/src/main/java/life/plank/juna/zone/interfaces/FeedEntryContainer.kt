package life.plank.juna.zone.interfaces

import life.plank.juna.zone.data.model.FeedEntry

interface FeedEntryContainer {

    fun updateFullScreenAdapter(feedEntryList: List<FeedEntry>)

    fun showFeedItemPeekPopup(position: Int)

    fun openFeedEntry(feedEntryList: MutableList<FeedEntry>, boardId: String, position: Int)
}