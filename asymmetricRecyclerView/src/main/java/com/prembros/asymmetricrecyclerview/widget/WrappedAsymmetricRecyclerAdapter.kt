package com.prembros.asymmetricrecyclerview.widget

import androidx.recyclerview.widget.RecyclerView
import com.prembros.asymmetricrecyclerview.base.AsymmetricItem

abstract class WrappedAsymmetricRecyclerAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    abstract fun getItem(position: Int): AsymmetricItem
}
