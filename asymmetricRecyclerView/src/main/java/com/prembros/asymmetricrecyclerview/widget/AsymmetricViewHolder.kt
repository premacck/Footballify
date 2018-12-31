package com.prembros.asymmetricrecyclerview.widget

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class AsymmetricViewHolder<VH : ViewHolder> : ViewHolder {

    internal val wrappedViewHolder: VH?

    constructor(wrappedViewHolder: VH) : super(wrappedViewHolder.itemView) {
        this.wrappedViewHolder = wrappedViewHolder
    }

    constructor(view: View) : super(view) {
        wrappedViewHolder = null
    }
}
