package life.plank.juna.zone.view.fragment.base

import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.FeedEntry
import life.plank.juna.zone.interfaces.FeedEntryContainer
import life.plank.juna.zone.util.facilis.BaseCard
import life.plank.juna.zone.util.facilis.pushPopup
import life.plank.juna.zone.view.adapter.BoardFeedDetailAdapter
import life.plank.juna.zone.view.fragment.clickthrough.FeedItemPeekPopup
import life.plank.juna.zone.view.fragment.post.PostDetailContainerFragment

/**
 * Parent class for fragments that contain tiles as well as the card layout
 */
abstract class CardTileFragment : BaseCard(), FeedEntryContainer {

    protected var isTileFullScreenActive: Boolean = false
    protected var boardFeedDetailAdapter: BoardFeedDetailAdapter? = null

    override fun openFeedEntry(feedEntryList: MutableList<FeedEntry>, boardId: String, position: Int, target: String) {
        pushFragment(PostDetailContainerFragment.newInstance(feedEntryList, boardId, position), true)
    }

    override fun showFeedItemPeekPopup(position: Int) {
        childFragmentManager.pushPopup(
                R.id.peek_popup_container,
                FeedItemPeekPopup.newInstance(getFeedEntries(), null, true, null, position),
                FeedItemPeekPopup.TAG
        )
    }

    abstract fun getFeedEntries(): List<FeedEntry>
}