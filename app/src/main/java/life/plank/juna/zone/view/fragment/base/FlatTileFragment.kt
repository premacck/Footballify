package life.plank.juna.zone.view.fragment.base

import life.plank.juna.zone.data.model.FeedEntry
import life.plank.juna.zone.interfaces.FeedEntryContainer
import life.plank.juna.zone.view.fragment.post.PostDetailContainerFragment

/**
 * Parent class for fragments that contain tiles, but lack the card layout
 */
abstract class FlatTileFragment : FlatFragment(), FeedEntryContainer {

    override fun openFeedEntry(feedEntryList: MutableList<FeedEntry>, boardId: String, position: Int) =
            pushFragment(PostDetailContainerFragment.newInstance(feedEntryList, boardId, position))
}