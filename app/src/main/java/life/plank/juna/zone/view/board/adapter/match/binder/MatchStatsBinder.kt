package life.plank.juna.zone.view.board.adapter.match.binder

import android.view.*
import android.widget.ImageView
import com.ahamed.multiviewadapter.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_match_stats.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.util.view.UIDisplayUtil.getDp
import life.plank.juna.zone.view.board.adapter.match.bindingmodel.MatchStatsBindingModel

class MatchStatsBinder(private val glide: RequestManager) : ItemBinder<MatchStatsBindingModel, MatchStatsBinder.MatchStatsViewHolder>() {

    override fun create(inflater: LayoutInflater, parent: ViewGroup): MatchStatsViewHolder = MatchStatsViewHolder(inflater.inflate(R.layout.item_match_stats, parent, false))

    override fun bind(holder: MatchStatsViewHolder, item: MatchStatsBindingModel) {
        holder.itemView.run {
            progress_bar.visibility = View.GONE
            if (item.errorMessage != null) {
                no_data.setText(item.errorMessage!!)
                no_data.visibility = View.VISIBLE
                teams_logo_layout.visibility = View.INVISIBLE
                match_team_stats_layout.visibility = View.INVISIBLE
                return
            }

            no_data.visibility = View.GONE
            teams_logo_layout.visibility = View.VISIBLE
            match_team_stats_layout.visibility = View.VISIBLE

            venue_name.text = item.venue?.name
            home_team_shots.text = item.matchStats?.homeShots.toString()
            home_team_shots_on_target.text = item.matchStats?.homeShotsOnTarget.toString()
            val homeTeamPossession = "${item.matchStats?.homePossession}%"
            home_team_possession.text = homeTeamPossession
            home_team_fouls.text = item.matchStats?.homeFouls.toString()
            home_team_yellow_card?.text = item.matchStats?.homeYellowCards.toString()
            home_team_red_card.text = item.matchStats?.homeRedCards.toString()
            home_team_offside.text = item.matchStats?.homeOffsides.toString()
            home_team_corner.text = item.matchStats?.homeCorners.toString()

            visiting_team_shots.text = item.matchStats?.awayShots.toString()
            visiting_team_shots_on_target.text = item.matchStats?.awayShotsOnTarget.toString()
            val visitingTeamPossession = "${item.matchStats?.awayPossession}%"
            visiting_team_possession.text = visitingTeamPossession
            visiting_team_fouls.text = item.matchStats?.awayFouls.toString()
            visiting_team_yellow_card.text = item.matchStats?.awayYellowCards.toString()
            visiting_team_red_card.text = item.matchStats?.awayRedCards.toString()
            visiting_team_offside.text = item.matchStats?.awayOffsides.toString()
            visiting_team_corner.text = item.matchStats?.awayCorners.toString()

            loadLogo(item.homeTeam?.logoLink, home_team_logo)
            loadLogo(item.awayTeam?.logoLink, visiting_team_logo)
        }
    }

    private fun loadLogo(logoUrl: String?, imageView: ImageView) {
        glide.load(logoUrl).apply(RequestOptions.overrideOf(getDp(14f).toInt(), getDp(14f).toInt())
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder))
                .into(imageView)
    }

    override fun canBindData(item: Any): Boolean = item is MatchStatsBindingModel

    class MatchStatsViewHolder(itemView: View) : ItemViewHolder<MatchStatsBindingModel>(itemView)
}