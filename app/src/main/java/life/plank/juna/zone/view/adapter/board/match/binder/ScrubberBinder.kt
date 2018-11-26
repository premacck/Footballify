package life.plank.juna.zone.view.adapter.board.match.binder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ahamed.multiviewadapter.ItemBinder
import com.ahamed.multiviewadapter.ItemViewHolder

import kotlinx.android.synthetic.main.item_scrubber.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.binder.ScrubberBindingModel
import life.plank.juna.zone.util.DataUtil.ScrubberLoader
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.view.adapter.board.match.BoardMediaAdapter

class ScrubberBinder(private val listener: BoardMediaAdapter.BoardMediaAdapterListener) : ItemBinder<ScrubberBindingModel, ScrubberBinder.ScrubberViewHolder>() {

    override fun create(inflater: LayoutInflater, parent: ViewGroup): ScrubberViewHolder = ScrubberViewHolder(inflater.inflate(R.layout.item_scrubber, parent, false))

    override fun bind(holder: ScrubberViewHolder, item: ScrubberBindingModel) {
        holder.itemView.run {
            ScrubberLoader.prepare(scrubber, false)
            scrubber.onDebouncingClick { listener.onScrubberClick(scrubber) }
        }
    }

    override fun canBindData(item: Any): Boolean = item is ScrubberBindingModel

    class ScrubberViewHolder(itemView: View) : ItemViewHolder<ScrubberBindingModel>(itemView)
}
