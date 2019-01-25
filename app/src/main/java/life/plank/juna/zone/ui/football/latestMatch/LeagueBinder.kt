package life.plank.juna.zone.ui.football.latestMatch

import android.app.Activity
import android.view.*
import com.ahamed.multiviewadapter.*
import com.prembros.facilis.util.onReducingClick
import kotlinx.android.synthetic.main.item_league.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.service.CommonDataService.findString
import life.plank.juna.zone.service.LeagueDataService
import life.plank.juna.zone.ui.base.BaseJunaCardActivity
import life.plank.juna.zone.ui.common.OnItemClickListener
import life.plank.juna.zone.ui.football.fragment.LeagueInfoFragment

class LeagueBinder(private val onItemClickListener: OnItemClickListener, private val activity: Activity) : ItemBinder<LeagueModel, LeagueBinder.LeagueViewHolder>() {

    override fun create(inflater: LayoutInflater, parent: ViewGroup): LeagueViewHolder = LeagueViewHolder(inflater.inflate(R.layout.item_league, parent, false))

    override fun bind(holder: LeagueViewHolder, item: LeagueModel) {
        holder.itemView.all_leagues_card.onReducingClick(0) {
            onItemClickListener.onItemClicked()
        }

        holder.itemView.epl_card.onReducingClick {
            (activity as? BaseJunaCardActivity)?.pushFragment(LeagueInfoFragment.newInstance(
                    LeagueDataService.getSpecifiedLeague(findString(R.string.premier_league))), true)
        }

        holder.itemView.efl_card.onReducingClick {
            (activity as? BaseJunaCardActivity)?.pushFragment(LeagueInfoFragment.newInstance(
                    LeagueDataService.getSpecifiedLeague(findString(R.string.champions_league))), true)
        }
    }

    override fun canBindData(item: Any): Boolean = item is LeagueModel

    class LeagueViewHolder(itemView: View) : ItemViewHolder<LeagueModel>(itemView)
}