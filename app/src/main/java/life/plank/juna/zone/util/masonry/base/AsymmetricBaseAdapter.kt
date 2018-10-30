package life.plank.juna.zone.util.masonry.base

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import life.plank.juna.zone.util.masonry.widget.AsymmetricViewHolder

interface AsymmetricBaseAdapter<T : RecyclerView.ViewHolder> {

    val actualItemCount: Int

    fun getItem(position: Int): AsymmetricItem

    fun notifyDataSetChanged()

    fun getItemViewType(actualIndex: Int): Int

    fun onCreateAsymmetricViewHolder(position: Int, parent: ViewGroup, viewType: Int): AsymmetricViewHolder<T>

    fun onBindAsymmetricViewHolder(holder: AsymmetricViewHolder<T>, parent: ViewGroup, position: Int)
}
