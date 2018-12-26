package life.plank.juna.zone.view.adapter.board.match.binder

import android.view.*
import android.widget.ImageView
import com.ahamed.multiviewadapter.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.prembros.facilis.util.isNullOrEmpty
import kotlinx.android.synthetic.main.item_team_stats.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.binder.TeamStatsBindingModel
import life.plank.juna.zone.util.view.UIDisplayUtil.getDp

class TeamStatsBinder(private val glide: RequestManager) : ItemBinder<TeamStatsBindingModel, TeamStatsBinder.TeamStatsViewHolder>() {

    override fun create(inflater: LayoutInflater, parent: ViewGroup): TeamStatsViewHolder = TeamStatsViewHolder(inflater.inflate(R.layout.item_team_stats, parent, false))

    override fun bind(holder: TeamStatsViewHolder, item: TeamStatsBindingModel) {
        holder.itemView.run {
            progress_bar.visibility = View.GONE
            if (item.errorMessage != null || isNullOrEmpty(item.teamStatsList) || item.teamStatsList!!.size < 2) {
                no_data.setText(item.errorMessage!!)
                no_data.visibility = View.VISIBLE
                teams_logo_layout.visibility = View.GONE
                match_team_stats_layout.visibility = View.GONE
                return
            }

            val homeTeamStats = item.teamStatsList!![1]
            val visitingTeamStats = item.teamStatsList!![0]

            no_data.visibility = View.GONE
            teams_logo_layout.visibility = View.VISIBLE
            match_team_stats_layout.visibility = View.VISIBLE
            league_name.text = item.league?.name

            home_team_win.text = homeTeamStats.win.toString()
            home_team_loss.text = homeTeamStats.loss.toString()
            home_team_goal.text = homeTeamStats.goal.toString()
            home_team_passes.text = homeTeamStats.pass.toString()
            home_team_shots.text = homeTeamStats.shot.toString()
            home_team_yellow_card.text = homeTeamStats.yellowCard.toString()
            home_team_red_card.text = homeTeamStats.redCard.toString()

            visiting_team_win.text = visitingTeamStats.win.toString()
            visiting_team_loss.text = visitingTeamStats.loss.toString()
            visiting_team_goal.text = visitingTeamStats.goal.toString()
            visiting_team_passes.text = visitingTeamStats.pass.toString()
            visiting_team_shots.text = visitingTeamStats.shot.toString()
            visiting_team_yellow_card.text = visitingTeamStats.yellowCard.toString()
            visiting_team_red_card.text = visitingTeamStats.redCard.toString()

            loadLogo(item.homeTeam?.logoLink, home_team_logo)
            loadLogo(item.awayTeam?.logoLink, visiting_team_logo)
        }
    }

    private fun loadLogo(url: String?, imageView: ImageView) {
        glide.load(url).apply(RequestOptions.overrideOf(getDp(14f).toInt(), getDp(14f).toInt())
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder))
                .into(imageView)
    }

    override fun canBindData(item: Any): Boolean = item is TeamStatsBindingModel

    class TeamStatsViewHolder(itemView: View) : ItemViewHolder<TeamStatsBindingModel>(itemView)
}
