package com.prembros.asymmetricrecyclerview.base

import android.view.ViewGroup
import com.prembros.asymmetricrecyclerview.widget.AsymmetricViewHolder

interface AsymmetricBaseAdapter<T : androidx.recyclerview.widget.RecyclerView.ViewHolder> {

    val actualItemCount: Int

    fun getItem(position: Int): AsymmetricItem

    fun notifyDataSetChanged()

    fun getItemViewType(actualIndex: Int): Int

    fun onCreateAsymmetricViewHolder(position: Int, parent: ViewGroup, viewType: Int): AsymmetricViewHolder<T>

    fun onBindAsymmetricViewHolder(holder: AsymmetricViewHolder<T>, parent: ViewGroup, position: Int)
}
