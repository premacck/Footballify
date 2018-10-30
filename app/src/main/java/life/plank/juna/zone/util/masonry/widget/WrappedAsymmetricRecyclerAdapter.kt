package life.plank.juna.zone.util.masonry.widget

import android.support.v7.widget.RecyclerView
import life.plank.juna.zone.util.masonry.base.AsymmetricItem

abstract class WrappedAsymmetricRecyclerAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    abstract fun getItem(position: Int): AsymmetricItem
}
