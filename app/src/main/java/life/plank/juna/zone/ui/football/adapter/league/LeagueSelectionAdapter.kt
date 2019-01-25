package life.plank.juna.zone.ui.football.adapter.league

import android.app.Activity
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.prembros.facilis.util.*
import kotlinx.android.synthetic.main.item_onboarding.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.football.League
import life.plank.juna.zone.service.LeagueDataService.getSpecifiedLeague
import life.plank.juna.zone.util.view.UIDisplayUtil.findColor
import life.plank.juna.zone.ui.base.BaseJunaCardActivity
import life.plank.juna.zone.ui.football.fragment.LeagueInfoFragment

class LeagueSelectionAdapter(private val activity: Activity,
                             private val leagueList: MutableList<League>
) : RecyclerView.Adapter<LeagueSelectionAdapter.LeagueViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder =
            LeagueViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_onboarding, parent, false))

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        val league = leagueList[position]
        holder.itemView.title.text = league.name
        holder.itemView.image.setImageResource(league.leagueLogo)

        holder.itemView.card.setCardBackgroundColor(findColor(league.dominantColor!!))
        holder.itemView.card.onDebouncingClick {
            (activity as? BaseJunaCardActivity)?.pushFragment(LeagueInfoFragment.newInstance(getSpecifiedLeague(league.name)), true)
        }
    }

    override fun getItemCount(): Int = leagueList.size

    fun setLeagueList(league: List<League>?) {
        if (isNullOrEmpty(league)) {
            return
        }
        leagueList.clear()
        leagueList.addAll(league as ArrayList)
        notifyDataSetChanged()
    }

    class LeagueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
