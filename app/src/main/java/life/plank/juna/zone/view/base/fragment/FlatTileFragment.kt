package life.plank.juna.zone.view.base.fragment

import life.plank.juna.zone.data.model.feed.FeedEntry
import life.plank.juna.zone.view.feed.*

/**
 * Parent class for fragments that contain tiles, but lack the card layout
 */
abstract class FlatTileFragment : FlatFragment(), FeedEntryContainer {

    override fun openFeedEntry(feedEntryList: MutableList<FeedEntry>, boardId: String, position: Int) =
            pushFragment(PostDetailContainerFragment.newInstance(feedEntryList, boardId, position))
}