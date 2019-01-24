package life.plank.juna.zone.view.football.adapter.league

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.player_stats_row.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.football.PlayerStats
import java.util.*

class PlayerStatsAdapter : RecyclerView.Adapter<PlayerStatsAdapter.MatchLeagueViewHolder>() {

    private var playerStats: MutableList<PlayerStats> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchLeagueViewHolder =
            MatchLeagueViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.player_stats_row, parent, false))

    override fun onBindViewHolder(holder: MatchLeagueViewHolder, position: Int) {
        holder.itemView.run {
            player_stats_serial_number_text.text = (position + 1).toString()
            player_stats_player_name.text = playerStats[position].playerName.toString()
            player_stats_goals_text.text = playerStats[position].goal.toString()
            player_stats_assist_text.text = playerStats[position].assist.toString()
            player_stats_yellow_card_text.text = playerStats[position].yellowCard.toString()
            player_stats_red_card_text.text = playerStats[position].redCard.toString()
            Glide.with(player_stats_team_logo.context)
                    .load(playerStats[position].footballTeamLogo)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_place_holder)
                            .error(R.drawable.ic_place_holder))
                    .into(player_stats_team_logo)
        }
    }

    override fun getItemCount(): Int = playerStats.size

    fun update(playerStatsList: MutableList<PlayerStats>) {
        this.playerStats = playerStatsList
        notifyDataSetChanged()
    }

    inner class MatchLeagueViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView)
}