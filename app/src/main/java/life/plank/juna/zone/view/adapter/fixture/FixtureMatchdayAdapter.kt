package life.plank.juna.zone.view.adapter.fixture

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.prembros.facilis.util.isNullOrEmpty
import kotlinx.android.synthetic.main.item_fixture_matchday.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.FixtureByDate
import life.plank.juna.zone.interfaces.LeagueContainer
import life.plank.juna.zone.util.common.AppConstants.TODAY_MATCHES
import life.plank.juna.zone.util.common.JunaDataUtil.findString
import life.plank.juna.zone.util.view.UIDisplayUtil.findColor
import life.plank.juna.zone.view.fragment.football.LeagueInfoFragment
import life.plank.juna.zone.view.fragment.football.LeagueInfoFragment.Companion.fixtureByMatchDayList

class FixtureMatchdayAdapter(private val leagueContainer: LeagueContainer) : RecyclerView.Adapter<FixtureMatchdayAdapter.FixtureMatchDayViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixtureMatchDayViewHolder {
        return FixtureMatchDayViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_fixture_matchday, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FixtureMatchDayViewHolder, position: Int) {
        val (matchDay, daySection, fixtureByDateList) = fixtureByMatchDayList[position]

        holder.itemView.run {
            if (!isNullOrEmpty<FixtureByDate>(fixtureByDateList)) {
                matchday_header.setBackgroundResource(
                        if (daySection == TODAY_MATCHES)
                            R.color.light_header
                        else
                            R.color.lighter_header
                )
                matchday_header.setTextColor(findColor(
                        if (daySection == TODAY_MATCHES)
                            R.color.fab_button_pink
                        else
                            R.color.grey
                ))
                val matchdayHeaderText = findString(if (leagueContainer.getTheLeague().isCup) R.string.round_ else R.string.matchday_) + if (leagueContainer.getTheLeague().isCup) matchDay else matchDay
                matchday_header.text = matchdayHeaderText
                fixtures_matchday_list.adapter = FixtureDateAdapter(fixtureByDateList, leagueContainer)
            }
        }
    }

    override fun getItemCount(): Int = if (isNullOrEmpty(fixtureByMatchDayList)) 0 else fixtureByMatchDayList.size

    fun updateFixtures() {
        fixtureByMatchDayList = LeagueInfoFragment.fixtureByMatchDayList
        notifyDataSetChanged()
    }

    class FixtureMatchDayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}