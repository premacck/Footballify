package life.plank.juna.zone.view.latestMatch

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ahamed.multiviewadapter.BaseViewHolder
import com.ahamed.multiviewadapter.ItemBinder
import kotlinx.android.synthetic.main.item_league.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.interfaces.OnItemClickListener
import life.plank.juna.zone.util.common.DataUtil.getSpecifiedLeague
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.fragment.football.LeagueInfoFragment


class LeagueBinder(private val onItemClickListener: OnItemClickListener, private val activity: Activity) : ItemBinder<LeagueModel, LeagueBinder.LeagueViewHolder>() {
    override fun create(inflater: LayoutInflater, parent: ViewGroup): LeagueViewHolder {
        return LeagueViewHolder(inflater.inflate(R.layout.item_league, parent, false))
    }

    override fun bind(holder: LeagueViewHolder, item: LeagueModel) {
        // Bind the data here
        holder.itemView.all_leagues_card.setOnClickListener {
            onItemClickListener.onItemClicked()
        }

        holder.itemView.epl_card.onDebouncingClick {
            (activity as? BaseCardActivity)?.pushFragment(LeagueInfoFragment.newInstance(getSpecifiedLeague("Premier League")), true)
        }

        holder.itemView.efl_card.onDebouncingClick {
            (activity as? BaseCardActivity)?.pushFragment(LeagueInfoFragment.newInstance(getSpecifiedLeague("Champions League")), true)
        }
    }

    override fun canBindData(item: Any): Boolean {
        return item is LeagueModel
    }

    inner class LeagueViewHolder(itemView: View) : BaseViewHolder<LeagueModel>(itemView)
}