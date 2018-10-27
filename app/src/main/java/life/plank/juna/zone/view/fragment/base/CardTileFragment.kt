package life.plank.juna.zone.view.fragment.base

import life.plank.juna.zone.interfaces.PeekPreviewContainer
import life.plank.juna.zone.util.facilis.BaseCard
import life.plank.juna.zone.view.adapter.BoardFeedDetailAdapter

/**
 * Parent class for fragments that contain tiles as well as the card layout
 */
abstract class CardTileFragment : BaseCard(), PeekPreviewContainer {

    protected var isTileFullScreenActive: Boolean = false
    protected var boardFeedDetailAdapter: BoardFeedDetailAdapter? = null
}