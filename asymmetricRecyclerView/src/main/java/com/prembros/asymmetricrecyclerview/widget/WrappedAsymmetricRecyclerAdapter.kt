package com.prembros.asymmetricrecyclerview.widget

import com.prembros.asymmetricrecyclerview.base.AsymmetricItem

abstract class WrappedAsymmetricRecyclerAdapter<VH : androidx.recyclerview.widget.RecyclerView.ViewHolder> : androidx.recyclerview.widget.RecyclerView.Adapter<VH>() {
    abstract fun getItem(position: Int): AsymmetricItem
}
