package life.plank.juna.zone.view.fragment.football

import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_league_stats.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.RestApiAggregator
import life.plank.juna.zone.data.model.League
import life.plank.juna.zone.data.model.PlayerStats
import life.plank.juna.zone.data.model.TeamStats
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.AppConstants.PLAYER_STATS
import life.plank.juna.zone.util.AppConstants.TEAM_STATS
import life.plank.juna.zone.util.DataUtil.findString
import life.plank.juna.zone.util.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.view.adapter.PlayerStatsAdapter
import life.plank.juna.zone.view.adapter.TeamStatsAdapter
import life.plank.juna.zone.view.fragment.base.BaseLeagueFragment
import javax.inject.Inject

class LeagueStatsFragment : BaseLeagueFragment() {

    @Inject
    lateinit var restApi: RestApi

    private var playerStatsAdapter: PlayerStatsAdapter? = null
    private var teamStatsAdapter: TeamStatsAdapter? = null
    private lateinit var league: League

    companion object {
        fun newInstance(league: League) = LeagueStatsFragment().apply { arguments = Bundle().apply { putParcelable(findString(R.string.intent_league), league) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)
        arguments?.run { league = getParcelable(getString(R.string.intent_league))!! }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_league_stats, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setAdapters()
        getLeagueStats(false)
        league_stats_swipe_refresh_layout.setOnRefreshListener { getLeagueStats(true) }
        league_stats_nested_scroll_view.setupWithParentFragmentBoomMenu()
    }

    override fun restApi(): RestApi = restApi

    override fun getTheLeague() = league

    private fun setAdapters() {
        teamStatsAdapter = TeamStatsAdapter(Glide.with(this))
        team_stats_recycler_view.adapter = teamStatsAdapter
        playerStatsAdapter = PlayerStatsAdapter()
        player_stats_recycler_view.adapter = playerStatsAdapter
    }

    private fun getLeagueStats(isRefreshing: Boolean) {
        RestApiAggregator.getLeagueStats(league, restApi)
                .doOnTerminate { if (isRefreshing) league_stats_swipe_refresh_layout.isRefreshing = false }
                .setObserverThreadsAndSmartSubscribe({}, {
                    setTeamStats(it.first)
                    setPlayerStats(it.second)
                })
    }

    private fun setTeamStats(teamStatsList: List<TeamStats>?) {
        if (isNullOrEmpty(teamStatsList)) {
            updateUI(false, team_stats_recycler_view, see_more_team_stats, no_team_stats)
        } else {
            updateUI(true, team_stats_recycler_view, see_more_team_stats, no_team_stats)
            teamStatsAdapter!!.update(teamStatsList)
            see_more_team_stats.onDebouncingClick { pushPopup(LeagueInfoDetailPopup.newInstance(TEAM_STATS, teamStatsList as ArrayList<out Parcelable>)) }
        }
        if (!isNullOrEmpty(teamStatsList)) {
            teamStatsAdapter?.update(teamStatsList)
        }
    }

    private fun setPlayerStats(playerStatsList: List<PlayerStats>?) {
        if (isNullOrEmpty(playerStatsList)) {
            updateUI(false, player_stats_recycler_view, see_more_player_stats, no_player_stats)
        } else {
            updateUI(true, player_stats_recycler_view, see_more_player_stats, no_player_stats)
            playerStatsAdapter!!.update(playerStatsList)
            see_more_player_stats.onDebouncingClick { pushPopup(LeagueInfoDetailPopup.newInstance(PLAYER_STATS, playerStatsList as ArrayList<out Parcelable>)) }
        }
        if (!isNullOrEmpty(playerStatsList)) {
            playerStatsAdapter?.update(playerStatsList)
        }
    }

    private fun updateUI(available: Boolean, recyclerView: RecyclerView, seeMoreView: TextView, noDataView: TextView) {
        recyclerView.visibility = if (available) View.VISIBLE else View.INVISIBLE
        seeMoreView.visibility = if (available) View.VISIBLE else View.GONE
        noDataView.visibility = if (available) View.GONE else View.VISIBLE
    }
}