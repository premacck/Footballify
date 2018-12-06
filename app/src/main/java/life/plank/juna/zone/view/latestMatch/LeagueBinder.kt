package life.plank.juna.zone.view.latestMatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ahamed.multiviewadapter.BaseViewHolder
import com.ahamed.multiviewadapter.ItemBinder
import kotlinx.android.synthetic.main.item_league.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.interfaces.OnItemClickListener


class LeagueBinder internal constructor(private val onItemClickListener: OnItemClickListener) : ItemBinder<LeagueModel, LeagueBinder.LeagueViewHolder>() {
    override fun create(inflater: LayoutInflater, parent: ViewGroup): LeagueViewHolder {
        return LeagueViewHolder(inflater.inflate(R.layout.item_league, parent, false))
    }

    override fun bind(holder: LeagueViewHolder, item: LeagueModel) {
        // Bind the data here
        holder.itemView.all_leagues_card.setOnClickListener {
            onItemClickListener.onItemClicked()
        }
    }

    override fun canBindData(item: Any): Boolean {
        return item is LeagueModel
    }

    inner class LeagueViewHolder(itemView: View) : BaseViewHolder<LeagueModel>(itemView)
}
