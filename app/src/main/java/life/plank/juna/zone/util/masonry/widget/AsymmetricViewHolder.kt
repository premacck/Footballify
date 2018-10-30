package life.plank.juna.zone.util.masonry.widget

import android.support.v7.widget.RecyclerView
import android.view.View

class AsymmetricViewHolder<VH : RecyclerView.ViewHolder> : RecyclerView.ViewHolder {
    internal val wrappedViewHolder: VH?

    constructor(wrappedViewHolder: VH) : super(wrappedViewHolder.itemView) {
        this.wrappedViewHolder = wrappedViewHolder
    }

    constructor(view: View) : super(view) {
        wrappedViewHolder = null
    }
}
