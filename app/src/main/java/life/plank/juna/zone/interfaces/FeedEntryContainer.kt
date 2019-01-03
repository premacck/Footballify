package life.plank.juna.zone.interfaces

import com.prembros.facilis.dialog.BaseBlurPopup
import life.plank.juna.zone.data.model.FeedEntry

interface FeedEntryContainer {

    fun updateFullScreenAdapter(feedEntryList: List<FeedEntry>)

    fun getFeedItemPeekPopup(position: Int): BaseBlurPopup

    fun openFeedEntry(position: Int)

    fun openFeedEntry(feedEntryList: MutableList<FeedEntry>, boardId: String, position: Int)
}