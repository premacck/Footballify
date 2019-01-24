package life.plank.juna.zone.view.football.adapter.league

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.team_stats_row.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.football.TeamStats
import java.util.*

class TeamStatsAdapter(private val glide: RequestManager) : RecyclerView.Adapter<TeamStatsAdapter.TeamStateViewHolder>() {

    private var teamStats: MutableList<TeamStats> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamStateViewHolder =
            TeamStateViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.team_stats_row, parent, false))

    override fun onBindViewHolder(holder: TeamStateViewHolder, position: Int) {
        holder.itemView.run {
            team_stats_serial_number_text_view.text = (position + 1).toString()
            team_stats_team_name_text_view.text = teamStats[position].teamName.toString()
            team_stats_wins_text_view.text = teamStats[position].win.toString()
            team_stats_losses_text_view.text = teamStats[position].loss.toString()
            team_stats_goals_text_view.text = teamStats[position].goal.toString()
            team_stats_pass_text_view.text = teamStats[position].pass.toString()
            team_stats_shot_text_view.text = teamStats[position].shot.toString()
            team_stats_red_card.text = teamStats[position].redCard.toString()
            team_stats_yellow_card.text = teamStats[position].yellowCard.toString()
            glide.load(teamStats[position].footballTeamLogo)
                    .apply(RequestOptions.centerCropTransform()
                            .placeholder(R.drawable.ic_place_holder)
                            .error(R.drawable.ic_place_holder))
                    .into(team_stats_team_logo)
        }
    }

    override fun getItemCount(): Int = teamStats.size

    fun update(teamStatsList: MutableList<TeamStats>) {
        this.teamStats = teamStatsList
        notifyDataSetChanged()
    }

    class TeamStateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}