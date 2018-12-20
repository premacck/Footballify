package life.plank.juna.zone.view.adapter.fixture

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import kotlinx.android.synthetic.main.item_fixture.view.*
import kotlinx.android.synthetic.main.item_fixture_league.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.MatchFixture
import life.plank.juna.zone.interfaces.LeagueContainer
import life.plank.juna.zone.util.common.AppConstants.MatchTimeVal.MATCH_LIVE
import life.plank.juna.zone.util.common.DataUtil.getSeparator
import life.plank.juna.zone.util.common.loadScrubber
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.time.DateUtil.getMatchTimeValue
import life.plank.juna.zone.util.view.UIDisplayUtil.*
import java.util.*

class FixtureAdapter(
        private var matchFixtureList: List<MatchFixture> = ArrayList(),
        private val leagueContainer: LeagueContainer
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val FIXTURE_LIVE = 0
        private const val FIXTURE_NOT_LIVE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FIXTURE_LIVE -> LiveFixtureViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_fixture_league, parent, false))
            else -> NotLiveFixtureViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_fixture, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val scoreFixture = matchFixtureList[position]
        when (holder.itemViewType) {
            FIXTURE_LIVE -> {
                holder.itemView.run {
                    alternateBackgroundColor(this, position)

                    leagueContainer.getGlide()
                            .load(scoreFixture.homeTeam.logoLink)
                            .apply(RequestOptions.centerCropTransform()
                                    .placeholder(R.drawable.shimmer_circle)
                                    .error(R.drawable.ic_place_holder)
                                    .override(getDp(12f).toInt(), getDp(12f).toInt()))
                            .into<SimpleTarget<Drawable>>(getEndDrawableTarget(live_home_team_name))

                    leagueContainer.getGlide()
                            .load(scoreFixture.awayTeam.logoLink)
                            .apply(RequestOptions.centerCropTransform()
                                    .placeholder(R.drawable.shimmer_circle)
                                    .error(R.drawable.ic_place_holder)
                                    .override(getDp(12f).toInt(), getDp(12f).toInt()))
                            .into<SimpleTarget<Drawable>>(getStartDrawableTarget(live_visiting_team_name))

                    live_home_team_name.text = scoreFixture.homeTeam.name
                    live_visiting_team_name.text = scoreFixture.awayTeam.name
                    live_score.text = getSeparator(scoreFixture.toMatchDetails(), win_pointer, false)
                    live_time_status.text = scoreFixture.timeStatus

                    loadScrubber(live_scrubber, false)
                }
            }
            else -> {
                holder.itemView.run {
                    alternateBackgroundColor(this, position)

                    leagueContainer.getGlide()
                            .load(scoreFixture.homeTeam.logoLink)
                            .apply(RequestOptions.centerCropTransform().placeholder(R.drawable.shimmer_circle).error(R.drawable.ic_place_holder))
                            .into(home_team_logo)

                    leagueContainer.getGlide()
                            .load(scoreFixture.awayTeam.logoLink)
                            .apply(RequestOptions.centerCropTransform().placeholder(R.drawable.shimmer_circle).error(R.drawable.ic_place_holder))
                            .into(visiting_team_logo)

                    home_team_name.text = scoreFixture.homeTeam.name
                    visiting_team_name.text = scoreFixture.awayTeam.name
                    date_schedule.text = getSeparator(scoreFixture.toMatchDetails(), win_pointer, false)

                }
            }
        }
        holder.itemView.onDebouncingClick { leagueContainer.onFixtureSelected(scoreFixture, leagueContainer.getTheLeague()) }
    }

    override fun getItemCount(): Int = matchFixtureList.size

    override fun getItemViewType(position: Int): Int = if (getMatchTimeValue(matchFixtureList[position].matchStartTime, false) == MATCH_LIVE) FIXTURE_LIVE else FIXTURE_NOT_LIVE

    fun update(matchFixtureList: List<MatchFixture>) {
        this.matchFixtureList = matchFixtureList
        notifyDataSetChanged()
    }

    class NotLiveFixtureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class LiveFixtureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}