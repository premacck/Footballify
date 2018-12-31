package com.prembros.asymmetricrecyclerview.widget

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prembros.asymmetricrecyclerview.base.*
import com.prembros.asymmetricrecyclerview.implementation.AdapterImpl
import com.prembros.asymmetricrecyclerview.implementation.AdapterImpl.ViewHolder

class AsymmetricRecyclerViewAdapter<T : RecyclerView.ViewHolder>(
        context: Context,
        private val recyclerView: AsymmetricRecyclerView,
        private val wrappedAdapter: WrappedAsymmetricRecyclerAdapter<T>
) : RecyclerView.Adapter<ViewHolder>(), AsymmetricBaseAdapter<T> {

    private val adapterImpl: AdapterImpl<T> = AdapterImpl(context, this, recyclerView)

    override val actualItemCount: Int
        get() = wrappedAdapter.itemCount

    init {
        wrappedAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                recalculateItemsPerRow()
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = adapterImpl.onCreateViewHolder()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = adapterImpl.onBindViewHolder(holder, position, recyclerView)

    /**
     * This is the row count for RecyclerView display purposes, not the actual item count
     */
    override fun getItemCount(): Int = adapterImpl.rowCount

    override fun getItem(position: Int): AsymmetricItem = wrappedAdapter.getItem(position)

    override fun onCreateAsymmetricViewHolder(position: Int, parent: ViewGroup, viewType: Int): AsymmetricViewHolder<T> = AsymmetricViewHolder(wrappedAdapter.onCreateViewHolder(parent, viewType))

    override fun onBindAsymmetricViewHolder(holder: AsymmetricViewHolder<T>, parent: ViewGroup, position: Int) = wrappedAdapter.onBindViewHolder(holder.wrappedViewHolder!!, position)

    override fun getItemViewType(actualIndex: Int): Int = wrappedAdapter.getItemViewType(actualIndex)

    internal fun recalculateItemsPerRow() = adapterImpl.recalculateItemsPerRow()
}
