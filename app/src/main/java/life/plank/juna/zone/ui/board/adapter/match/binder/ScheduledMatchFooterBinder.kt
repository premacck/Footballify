package life.plank.juna.zone.ui.board.adapter.match.binder

import android.view.*
import com.ahamed.multiviewadapter.*
import life.plank.juna.zone.R

class ScheduledMatchFooterBinder : ItemBinder<String, ScheduledMatchFooterBinder.ScheduledMatchFooterViewHolder>() {

    override fun create(inflater: LayoutInflater, parent: ViewGroup): ScheduledMatchFooterViewHolder = ScheduledMatchFooterViewHolder(inflater.inflate(R.layout.item_scheduled_match_footer, parent, false))

    override fun bind(holder: ScheduledMatchFooterViewHolder, item: String) {}

    override fun canBindData(item: Any): Boolean = item is String

    class ScheduledMatchFooterViewHolder(itemView: View) : ItemViewHolder<String>(itemView)
}