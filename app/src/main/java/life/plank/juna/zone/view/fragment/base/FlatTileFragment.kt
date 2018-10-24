package life.plank.juna.zone.view.fragment.base

import life.plank.juna.zone.interfaces.TileContainer
import life.plank.juna.zone.view.adapter.BoardFeedDetailAdapter

/**
 * Parent class for fragments that contain tiles, but lack the card layout
 */
abstract class FlatTileFragment : BaseFragment(), TileContainer {

    protected var isTileFullScreenActive: Boolean = false
    protected var boardFeedDetailAdapter: BoardFeedDetailAdapter? = null
}