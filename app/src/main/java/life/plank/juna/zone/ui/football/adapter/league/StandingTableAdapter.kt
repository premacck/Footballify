package life.plank.juna.zone.ui.football.adapter.league

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.standing_row.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.football.Standings
import java.util.*

class StandingTableAdapter(private val glide: RequestManager, private val isSerialNumberHidden: Boolean) : RecyclerView.Adapter<StandingTableAdapter.StandingScoreTableViewHolder>() {

    var standings: List<Standings> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StandingScoreTableViewHolder {
        return StandingScoreTableViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.standing_row, parent, false))
    }

    override fun onBindViewHolder(holder: StandingScoreTableViewHolder, position: Int) {
        holder.itemView.run {
            if (isSerialNumberHidden) {
                serial_number_text_view.visibility = View.GONE
            } else {
                serial_number_text_view.visibility = View.VISIBLE
                serial_number_text_view.text = (position + 1).toString()
            }
            team_name_text_view.text = standings[position].teamName
            played_text_view.text = standings[position].matchesPlayed.toString()
            win_text_view.text = standings[position].wins.toString()
            draw_text_view.text = standings[position].draws.toString()
            loss_text_view.text = standings[position].losses.toString()
            goal_for_text_view.text = standings[position].goalsFor.toString()
            goal_against_text_view.text = standings[position].goalsAgainst.toString()
            goal_difference_text_view.text = standings[position].goalDifference.toString()
            point_table_text_view.text = standings[position].points.toString()
            glide.load(standings[position].footballTeamLogo)
                    .apply(RequestOptions.centerCropTransform()
                            .placeholder(R.drawable.ic_place_holder)
                            .error(R.drawable.ic_place_holder))
                    .into(team_logo_image_view)
        }
    }

    fun update(standingsList: List<Standings>) {
        this.standings = standingsList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = standings.size

    class StandingScoreTableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}