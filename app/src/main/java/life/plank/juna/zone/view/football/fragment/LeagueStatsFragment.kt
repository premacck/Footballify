package life.plank.juna.zone.view.football.fragment

import android.os.*
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prembros.facilis.util.*
import kotlinx.android.synthetic.main.fragment_league_stats.*
import life.plank.juna.zone.*
import life.plank.juna.zone.data.api.*
import life.plank.juna.zone.data.model.football.*
import life.plank.juna.zone.service.CommonDataService.findString
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.view.base.fragment.BaseLeagueFragment
import life.plank.juna.zone.view.football.adapter.league.*
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
                .onTerminate { if (isRefreshing) league_stats_swipe_refresh_layout.isRefreshing = false }
                .setObserverThreadsAndSmartSubscribe({}, {
                    setTeamStats(it.first)
                    setPlayerStats(it.second)
                }, this)
    }

    private fun setTeamStats(teamStatsList: MutableList<TeamStats>?) {
        if (isNullOrEmpty(teamStatsList)) {
            updateUI(false, team_stats_recycler_view, see_more_team_stats, no_team_stats)
        } else {
            updateUI(true, team_stats_recycler_view, see_more_team_stats, no_team_stats)
            teamStatsList?.run {
                teamStatsAdapter?.update(this)
                leagueViewModel.updateTeamStats(league.id, this)
                see_more_team_stats.onDebouncingClick { pushPopup(LeagueInfoDetailPopup.newInstance(TEAM_STATS, this as ArrayList<out Parcelable>)) }
            }
        }
    }

    private fun setPlayerStats(playerStatsList: MutableList<PlayerStats>?) {
        if (isNullOrEmpty(playerStatsList)) {
            updateUI(false, player_stats_recycler_view, see_more_player_stats, no_player_stats)
        } else {
            updateUI(true, player_stats_recycler_view, see_more_player_stats, no_player_stats)
            playerStatsList?.run {
                playerStatsAdapter?.update(this)
                leagueViewModel.updatePlayerStats(league.id, this)
                see_more_player_stats.onDebouncingClick { pushPopup(LeagueInfoDetailPopup.newInstance(PLAYER_STATS, this as ArrayList<out Parcelable>)) }
            }
        }
    }

    private fun updateUI(available: Boolean, recyclerView: RecyclerView, seeMoreView: TextView, noDataView: TextView) {
        recyclerView.visibility = if (available) View.VISIBLE else View.INVISIBLE
        seeMoreView.visibility = if (available) View.VISIBLE else View.GONE
        noDataView.visibility = if (available) View.GONE else View.VISIBLE
    }
}