package life.plank.juna.zone.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_onboarding.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.League
import life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty

class LeagueSelectionAdapter(
        private val leagueList: MutableList<League>
) : RecyclerView.Adapter<LeagueSelectionAdapter.LeagueViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder =
            LeagueViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_onboarding, parent, false))

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        val league = leagueList[position]
        holder.itemView.title.text = league.name
        holder.itemView.image.setImageResource(league.leagueLogo)
//        TODO:Fix background color issue
//        holder.itemView.card.setCardBackgroundColor(league.dominantColor!!)
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
