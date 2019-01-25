package life.plank.juna.zone.ui.football.adapter.fixture

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.prembros.facilis.util.isNullOrEmpty
import kotlinx.android.synthetic.main.item_fixture_date.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.football.FixtureByDate
import life.plank.juna.zone.util.time.DateUtil.getDateHeader
import life.plank.juna.zone.ui.football.LeagueContainer

class FixtureDateAdapter(
        private val fixtureByDateList: List<FixtureByDate>,
        private val leagueContainer: LeagueContainer
) : RecyclerView.Adapter<FixtureDateAdapter.FixtureDateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixtureDateViewHolder =
            FixtureDateViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_fixture_date, parent, false))

    override fun onBindViewHolder(holder: FixtureDateViewHolder, position: Int) {
        holder.itemView.run {
            val (date, fixtures) = fixtureByDateList[position]
            date_time.text = if (!isNullOrEmpty(fixtures))
                getDateHeader(fixtures[0].matchStartTime)
            else
                getDateHeader(date)
            fixtures_date_list.adapter = FixtureAdapter(fixtures, leagueContainer)
        }
    }

    override fun getItemCount(): Int = fixtureByDateList.size

    class FixtureDateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
