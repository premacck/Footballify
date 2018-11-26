package life.plank.juna.zone.view.adapter.board.match.binder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ahamed.multiviewadapter.ItemBinder
import com.ahamed.multiviewadapter.ItemViewHolder

import life.plank.juna.zone.R

class ScheduledMatchFooterBinder : ItemBinder<String, ScheduledMatchFooterBinder.ScheduledMatchFooterViewHolder>() {

    override fun create(inflater: LayoutInflater, parent: ViewGroup): ScheduledMatchFooterViewHolder = ScheduledMatchFooterViewHolder(inflater.inflate(R.layout.item_scheduled_match_footer, parent, false))

    override fun bind(holder: ScheduledMatchFooterViewHolder, item: String) {}

    override fun canBindData(item: Any): Boolean = item is String

    class ScheduledMatchFooterViewHolder(itemView: View) : ItemViewHolder<String>(itemView)
}