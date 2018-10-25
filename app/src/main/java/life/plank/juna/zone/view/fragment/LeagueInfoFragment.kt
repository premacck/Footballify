package life.plank.juna.zone.view.fragment


import android.arch.lifecycle.Observer
import android.os.AsyncTask
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_league_info.*
import kotlinx.android.synthetic.main.item_standings.*
import kotlinx.android.synthetic.main.layout_league_info.*
import kotlinx.android.synthetic.main.league_toolbar.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.local.model.LeagueInfo
import life.plank.juna.zone.data.model.*
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.AppConstants.*
import life.plank.juna.zone.util.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.DateUtil.getDateDiffFromToday
import life.plank.juna.zone.util.hideAndShowBoomMenu
import life.plank.juna.zone.util.setupBoomMenu
import life.plank.juna.zone.view.activity.FixtureActivity
import life.plank.juna.zone.view.activity.LeagueInfoActivity
import life.plank.juna.zone.view.activity.LeagueInfoActivity.fixtureByMatchDayList
import life.plank.juna.zone.view.activity.LeagueInfoDetailActivity
import life.plank.juna.zone.view.adapter.FixtureAdapter
import life.plank.juna.zone.view.adapter.PlayerStatsAdapter
import life.plank.juna.zone.view.adapter.StandingTableAdapter
import life.plank.juna.zone.view.adapter.TeamStatsAdapter
import life.plank.juna.zone.view.fragment.base.BaseLeagueFragment
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.lang.ref.WeakReference
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class LeagueInfoFragment : BaseLeagueFragment() {

    lateinit var fixtureByMatchDayList: MutableList<FixtureByMatchDay>

    @Inject
    lateinit var picasso: Picasso
    @Inject
    lateinit var gson: Gson
    @field: [Inject Named("footballData")]
    lateinit var restApi: RestApi
    private var fixtureAdapter: FixtureAdapter? = null
    private var standingTableAdapter: StandingTableAdapter? = null
    private var playerStatsAdapter: PlayerStatsAdapter? = null
    private var teamStatsAdapter: TeamStatsAdapter? = null
    private var isDataLocal: Boolean = false
    private lateinit var league: League

    companion object {
        private var TAG = LeagueInfoActivity::class.java.simpleName
        fun newInstance(league: League) = LeagueInfoFragment().apply { arguments = Bundle().apply { putParcelable(getString(R.string.intent_league), league) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)
        arguments?.apply { league = getParcelable(getString(R.string.intent_league))!! }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_league_info, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBoomMenu(BoomMenuPage.BOOM_MENU_SETTINGS_AND_HOME, activity!!, null, arc_menu, null)

        prepareRecyclerViews()
        getLeagueInfoFromRoomDb()

        title.text = league.name
        logo.setImageResource(league.leagueLogo)
        root_layout.setBackgroundColor(resources.getColor(league.dominantColor!!, null))
        hideAndShowBoomMenu(nestedScrollView, arc_menu)

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        see_all_fixtures.onClick {
            if (!isNullOrEmpty<FixtureByMatchDay>(fixtureByMatchDayList)) {
                FixtureActivity.launch(activity, league)
            }
        }
        see_all_standings.onClick {
            LeagueInfoDetailActivity.launch(activity, STANDINGS, standingTableAdapter!!.standings as ArrayList<out Parcelable>, standings_layout)
        }
        see_more_team_stats.onClick {
            LeagueInfoDetailActivity.launch(activity, TEAM_STATS, teamStatsAdapter!!.teamStats as ArrayList<out Parcelable>, team_stats_layout)
        }
        see_more_player_stats.onClick {
            LeagueInfoDetailActivity.launch(activity, PLAYER_STATS, playerStatsAdapter!!.playerStats as ArrayList<out Parcelable>, player_stats_layout)
        }
    }

    override fun getRootFadedCardLayout(): ViewGroup = faded_card_layout

    override fun getRootCard(): CardView = root_card

    override fun getDragHandle(): View = drag_area

    override fun getGlide() = Glide.with(this)

    override fun getTheGson() = gson

    override fun getTheLeague() = league

    private fun prepareRecyclerViews() {
//        fixtureAdapter = FixtureAdapter(null, activity)
        fixtures_section_list.adapter = fixtureAdapter

        standingTableAdapter = StandingTableAdapter(picasso)
        standing_recycler_view.adapter = standingTableAdapter

        playerStatsAdapter = PlayerStatsAdapter()
        player_stats_recycler_view.adapter = playerStatsAdapter

        teamStatsAdapter = TeamStatsAdapter(picasso)
        team_stats_recycler_view.adapter = teamStatsAdapter
    }

    private fun getLeagueInfoFromRoomDb() {
        isDataLocal = true
        leagueViewModel.leagueInfoLiveData.observe(this, Observer<LeagueInfo> { handleLeagueInfoData(it) })
        leagueViewModel.getLeagueInfoFromDb(league.id)
    }

    private fun getLeagueInfoFromRestApi() {
        if (isDataLocal) {
            isDataLocal = false
            leagueViewModel.getLeagueInfoFromRestApi(league, restApi)
        }
    }

    private fun handleLeagueInfoData(leagueInfo: LeagueInfo?) {
        if (leagueInfo != null) {
//            Update new data in DB
            if (!isDataLocal) {
                leagueViewModel.leagueRepository.insertLeagueInfo(leagueInfo)
            }

            fixture_progress_bar.visibility = View.GONE
            if (leagueInfo.fixtureByMatchDayList == emptyList<Any>() || isNullOrEmpty<FixtureByMatchDay>(leagueInfo.fixtureByMatchDayList)) {
                updateUI(false, fixtures_section_list, see_all_fixtures, fixture_no_data)
            } else {
                fixtureByMatchDayList = leagueInfo.fixtureByMatchDayList.toMutableList()
                UpdateFixtureAdapterTask.parse(this)
                updateUI(true, fixtures_section_list, see_all_fixtures, fixture_no_data)
            }

            standings_progress_bar.visibility = View.GONE
            if (leagueInfo.standingsList == emptyList<Any>() || isNullOrEmpty<Standings>(leagueInfo.standingsList)) {
                updateUI(false, standing_recycler_view, see_all_standings, no_standings)
            } else {
                updateUI(true, standing_recycler_view, see_all_standings, no_standings)
                standingTableAdapter!!.update(leagueInfo.standingsList)
            }

            team_stats_progress_bar.visibility = View.GONE
            if (leagueInfo.teamStatsList == emptyList<Any>() || isNullOrEmpty<TeamStats>(leagueInfo.teamStatsList)) {
                updateUI(false, team_stats_recycler_view, see_more_team_stats, no_team_stats)
            } else {
                updateUI(true, team_stats_recycler_view, see_more_team_stats, no_team_stats)
                teamStatsAdapter!!.update(leagueInfo.teamStatsList)
            }

            player_stats_progress_bar.visibility = View.GONE
            if (leagueInfo.playerStatsList == emptyList<Any>() || isNullOrEmpty<PlayerStats>(leagueInfo.playerStatsList)) {
                updateUI(false, player_stats_recycler_view, see_more_player_stats, no_player_stats)
            } else {
                updateUI(true, player_stats_recycler_view, see_more_player_stats, no_player_stats)
                playerStatsAdapter!!.update(leagueInfo.playerStatsList)
            }
            getLeagueInfoFromRestApi()
        } else {
            getLeagueInfoFromRestApi()
        }
    }

    private fun updateUI(available: Boolean, recyclerView: RecyclerView, seeMoreView: TextView, noDataView: TextView) {
        recyclerView.visibility = if (available) View.VISIBLE else View.INVISIBLE
        seeMoreView.visibility = if (available) View.VISIBLE else View.GONE
        noDataView.visibility = if (available) View.GONE else View.VISIBLE
    }

    override fun onDestroy() {
        fixtureAdapter = null
        standingTableAdapter = null
        teamStatsAdapter = null
        playerStatsAdapter = null
        if (!isNullOrEmpty<FixtureByMatchDay>(fixtureByMatchDayList)) {
            fixtureByMatchDayList.clear()
        }
        super.onDestroy()
    }

    private class UpdateFixtureAdapterTask private constructor(leagueInfoFragment: LeagueInfoFragment) : AsyncTask<Void, Void, List<MatchFixture>>() {

        private val ref: WeakReference<LeagueInfoFragment> = WeakReference(leagueInfoFragment)
        private var recyclerViewScrollIndex = 0

        companion object {
            internal fun parse(leagueInfoFragment: LeagueInfoFragment) {
                UpdateFixtureAdapterTask(leagueInfoFragment).execute()
            }
        }

        override fun onPreExecute() {
            ref.get()?.run {
                fixture_progress_bar.visibility = View.VISIBLE
                see_all_fixtures.isEnabled = false
                see_all_fixtures.isClickable = false
            }
        }

        override fun doInBackground(vararg voids: Void): List<MatchFixture>? {
            var isPastMatches = true
            if (!isNullOrEmpty<FixtureByMatchDay>(fixtureByMatchDayList)) {
                for (matchDay in fixtureByMatchDayList!!) {
                    try {
                        if (matchDay.daySection == PAST_MATCHES) {
                            isPastMatches = true
                            recyclerViewScrollIndex = fixtureByMatchDayList!!.indexOf(matchDay)
                        } else if (matchDay.daySection == TODAY_MATCHES) {
                            isPastMatches = false
                            recyclerViewScrollIndex = fixtureByMatchDayList!!.indexOf(matchDay)
                        }
                    } catch (e: Exception) {
                        Log.e("FixtureAdapterTask", "doInBackground: recyclerViewScrollIndex ", e)
                    }

                }
                val matchFixtures = ArrayList<MatchFixture>()
                getMatchesToShow(matchFixtures, isPastMatches)
                return if (matchFixtures.size >= 4) matchFixtures.subList(0, 4) else matchFixtures
            }
            return null
        }

        private fun getMatchesToShow(matchFixtures: MutableList<MatchFixture>, isPastMatches: Boolean) {
            val fixtureByDateList = fixtureByMatchDayList!![recyclerViewScrollIndex].fixtureByDateList
            for (fixtureByDate in fixtureByDateList) {
                for (matchFixture in fixtureByDate.fixtures) {
                    try {
                        if (isPastMatches && getDateDiffFromToday(matchFixture.matchStartTime) <= 0) {
                            matchFixtures.add(matchFixture)
                        } else if (getDateDiffFromToday(matchFixture.matchStartTime) <= 1) {
                            matchFixtures.add(matchFixture)
                        }
                    } catch (e: Exception) {
                        Log.e("FixtureAdapterTask", "doInBackground: getDateDiffFromToday() ", e)
                    }

                }
            }
            if (isPastMatches) {
                matchFixtures.reverse()
            }
        }

        override fun onPostExecute(matchFixtures: List<MatchFixture>) {
            ref.get()?.run {
                if (!isNullOrEmpty<MatchFixture>(matchFixtures)) {
                    if (fixtureAdapter != null) {
                        fixtureAdapter!!.update(matchFixtures)
                    }
                    fixtures_section_list.scrollToPosition(recyclerViewScrollIndex)
                    see_all_fixtures.isEnabled = true
                    see_all_fixtures.isClickable = true
                }
                fixture_progress_bar.visibility = View.GONE
            }
            if (ref.get() != null) {
            }
        }
    }
}
