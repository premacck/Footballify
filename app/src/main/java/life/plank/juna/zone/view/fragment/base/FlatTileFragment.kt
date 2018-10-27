package life.plank.juna.zone.view.fragment.base

import android.support.annotation.IdRes
import life.plank.juna.zone.data.model.FeedEntry
import life.plank.juna.zone.interfaces.FeedEntryContainer
import life.plank.juna.zone.util.facilis.pushFragment
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.adapter.BoardFeedDetailAdapter
import life.plank.juna.zone.view.fragment.post.PostDetailContainerFragment

/**
 * Parent class for fragments that contain tiles, but lack the card layout
 */
abstract class FlatTileFragment : BaseFragment(), FeedEntryContainer {

    protected var isTileFullScreenActive: Boolean = false
    protected var boardFeedDetailAdapter: BoardFeedDetailAdapter? = null

    override fun openFeedEntry(feedEntryList: MutableList<FeedEntry>, boardId: String, position: Int, target: String) {
        childFragmentManager.pushFragment(
                getSecondaryFragmentResId(),
                PostDetailContainerFragment.newInstance(feedEntryList, boardId, position),
                PostDetailContainerFragment.TAG,
                getParentActivity().index
        )
    }

    @IdRes
    abstract fun getSecondaryFragmentResId(): Int

    private fun getParentActivity(): BaseCardActivity {
        if (activity is BaseCardActivity)
            return activity as BaseCardActivity
        else throw IllegalStateException("Fragment must be attached to a BaseCardActivity")
    }
}