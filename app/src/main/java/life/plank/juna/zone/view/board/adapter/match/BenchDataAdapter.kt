package life.plank.juna.zone.view.board.adapter.match

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_bench_data.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.football.MatchEvent
import java.util.*

class BenchDataAdapter : RecyclerView.Adapter<BenchDataAdapter.SubstitutionViewHolder>() {

    private val matchEventList: MutableList<MatchEvent> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubstitutionViewHolder =
            SubstitutionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_bench_data, parent, false))

    override fun onBindViewHolder(holder: SubstitutionViewHolder, position: Int) {
        holder.itemView.run {
            val (isHomeTeam, _, playerName, _, minute, extraMinute) = matchEventList[position]
            if (isHomeTeam) {
                home_player_name.text = playerName
                visiting_player_name.visibility = View.GONE
            } else {
                visiting_player_name.text = playerName
                home_player_name.visibility = View.GONE
            }
            home_player_name.setCompoundDrawablesWithIntrinsicBounds(if (isHomeTeam) R.drawable.ic_substitute_in else 0, 0, 0, 0)
            visiting_player_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, if (isHomeTeam) 0 else R.drawable.ic_substitute_in, 0)
            val timeText: String
            timeText = if (extraMinute > 0) "$minute + $extraMinute"
            else "$minute'"
            minute_view.text = timeText
        }
    }

    override fun getItemCount(): Int {
        return matchEventList.size
    }

    fun update(matchEventList: List<MatchEvent>) {
        this.matchEventList.addAll(matchEventList)
        notifyDataSetChanged()
    }

    fun updateNew(matchEventList: List<MatchEvent>) {
        val previousIndex = this.matchEventList.size - 1
        this.matchEventList.addAll(matchEventList)
        notifyItemRangeInserted(previousIndex, matchEventList.size)
    }

    class SubstitutionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
