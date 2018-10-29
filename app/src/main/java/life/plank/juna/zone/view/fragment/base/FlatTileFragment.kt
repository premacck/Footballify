package life.plank.juna.zone.view.fragment.base

import life.plank.juna.zone.interfaces.FeedEntryContainer
import life.plank.juna.zone.view.adapter.BoardFeedDetailAdapter

/**
 * Parent class for fragments that contain tiles, but lack the card layout
 */
abstract class FlatTileFragment : BaseFragment(), FeedEntryContainer {

    protected var isTileFullScreenActive: Boolean = false
    protected var boardFeedDetailAdapter: BoardFeedDetailAdapter? = null
}