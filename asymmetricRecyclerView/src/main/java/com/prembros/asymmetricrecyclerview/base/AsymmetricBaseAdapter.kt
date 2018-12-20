package com.prembros.asymmetricrecyclerview.base

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.prembros.asymmetricrecyclerview.widget.AsymmetricViewHolder

interface AsymmetricBaseAdapter<T : ViewHolder> {

    val actualItemCount: Int

    fun getItem(position: Int): AsymmetricItem

    fun notifyDataSetChanged()

    fun getItemViewType(actualIndex: Int): Int

    fun onCreateAsymmetricViewHolder(position: Int, parent: ViewGroup, viewType: Int): AsymmetricViewHolder<T>

    fun onBindAsymmetricViewHolder(holder: AsymmetricViewHolder<T>, parent: ViewGroup, position: Int)
}
