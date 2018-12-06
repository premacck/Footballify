package life.plank.juna.zone.view.fragment.base

import life.plank.juna.zone.data.model.FeedEntry
import life.plank.juna.zone.interfaces.FeedEntryContainer
import life.plank.juna.zone.util.facilis.BaseCard
import life.plank.juna.zone.view.fragment.clickthrough.FeedItemPeekPopup
import life.plank.juna.zone.view.fragment.post.PostDetailContainerFragment

/**
 * Parent class for fragments that contain tiles as well as the card layout
 */
abstract class CardTileFragment : BaseCard(), FeedEntryContainer {

    override fun openFeedEntry(feedEntryList: MutableList<FeedEntry>, boardId: String, position: Int) =
            pushFragment(PostDetailContainerFragment.newInstance(feedEntryList, boardId, position), true)

    override fun showFeedItemPeekPopup(position: Int) =
            pushPopup(FeedItemPeekPopup.newInstance(getFeedEntries(), getTheBoardId(), true, null, position))

    abstract fun getFeedEntries(): List<FeedEntry>

    abstract fun getTheBoardId(): String?
}