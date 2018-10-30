package life.plank.juna.zone.util.masonry.widget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import life.plank.juna.zone.util.masonry.base.AsymmetricBaseAdapter
import life.plank.juna.zone.util.masonry.base.AsymmetricItem
import life.plank.juna.zone.util.masonry.implementation.AdapterImpl

class AsymmetricRecyclerViewAdapter<T : RecyclerView.ViewHolder>(
        context: Context,
        private val recyclerView: AsymmetricRecyclerView,
        private val wrappedAdapter: WrappedAsymmetricRecyclerAdapter<T>
) : RecyclerView.Adapter<AdapterImpl.ViewHolder>(), AsymmetricBaseAdapter<T> {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterImpl.ViewHolder {
        return adapterImpl.onCreateViewHolder()
    }

    override fun onBindViewHolder(holder: AdapterImpl.ViewHolder, position: Int) {
        adapterImpl.onBindViewHolder(holder, position, recyclerView)
    }

    override fun getItemCount(): Int {
        // This is the row count for RecyclerView display purposes, not the actual item count
        return adapterImpl.rowCount
    }

    override fun getItem(position: Int): AsymmetricItem {
        return wrappedAdapter.getItem(position)
    }

    override fun onCreateAsymmetricViewHolder(position: Int, parent: ViewGroup, viewType: Int): AsymmetricViewHolder<T> {
        return AsymmetricViewHolder(wrappedAdapter.onCreateViewHolder(parent, viewType))
    }

    override fun onBindAsymmetricViewHolder(holder: AsymmetricViewHolder<T>, parent: ViewGroup, position: Int) {
        wrappedAdapter.onBindViewHolder(holder.wrappedViewHolder!!, position)
    }

    override fun getItemViewType(actualIndex: Int): Int {
        return wrappedAdapter.getItemViewType(actualIndex)
    }

    internal fun recalculateItemsPerRow() {
        adapterImpl.recalculateItemsPerRow()
    }
}
