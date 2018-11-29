package life.plank.juna.zone.view.LatestMatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ahamed.multiviewadapter.BaseViewHolder
import com.ahamed.multiviewadapter.ItemBinder

import life.plank.juna.zone.R

internal class LeagueBinder : ItemBinder<LeagueModel, LeagueBinder.LeagueViewHolder>() {

    override fun create(inflater: LayoutInflater, parent: ViewGroup): LeagueViewHolder {
        return LeagueViewHolder(inflater.inflate(R.layout.item_league, parent, false))
    }

    override fun bind(holder: LeagueViewHolder, item: LeagueModel) {
        // Bind the data here
    }

    override fun canBindData(item: Any): Boolean {
        return item is LeagueModel
    }

    internal inner class LeagueViewHolder(itemView: View) : BaseViewHolder<LeagueModel>(itemView)
}
