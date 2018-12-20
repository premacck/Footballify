package life.plank.juna.zone.view.adapter.fixture

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_fixture_date.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.FixtureByDate
import life.plank.juna.zone.interfaces.LeagueContainer
import life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.time.DateUtil.getDateHeader

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
