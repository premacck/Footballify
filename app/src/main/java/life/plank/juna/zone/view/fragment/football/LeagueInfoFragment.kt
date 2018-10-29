package life.plank.juna.zone.view.fragment.football


import android.arch.lifecycle.Observer
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
import life.plank.juna.zone.data.model.FixtureByMatchDay
import life.plank.juna.zone.data.model.League
import life.plank.juna.zone.data.model.MatchFixture
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.AppConstants.*
import life.plank.juna.zone.util.DataUtil
import life.plank.juna.zone.util.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.DateUtil.getDateDiffFromToday
import life.plank.juna.zone.util.UIDisplayUtil.findColor
import life.plank.juna.zone.util.facilis.pushPopup
import life.plank.juna.zone.util.facilis.removeActivePopupsIfAny
import life.plank.juna.zone.util.setupBoomMenu
import life.plank.juna.zone.util.setupWith
import life.plank.juna.zone.view.adapter.FixtureAdapter
import life.plank.juna.zone.view.adapter.PlayerStatsAdapter
import life.plank.juna.zone.view.adapter.StandingTableAdapter
import life.plank.juna.zone.view.adapter.TeamStatsAdapter
import life.plank.juna.zone.view.fragment.base.BaseLeagueFragment
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.uiThread
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class LeagueInfoFragment : BaseLeagueFragment() {

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
        lateinit var fixtureByMatchDayList: MutableList<FixtureByMatchDay>
        fun newInstance(league: League) = LeagueInfoFragment().apply { arguments = Bundle().apply { putParcelable(DataUtil.findString(R.string.intent_league), league) } }
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
        parent_layout.setBackgroundColor(findColor(league.dominantColor!!))
        arc_menu.setupWith(nestedScrollView)

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        see_all_fixtures.onClick {
            if (!isNullOrEmpty(fixtureByMatchDayList)) {
                childFragmentManager.pushPopup(R.id.popup_container, FixtureFragment.newInstance(league), FixtureFragment.TAG)
            }
        }
        see_all_standings.onClick {
            childFragmentManager.pushPopup(
                    R.id.popup_container,
                    LeagueInfoDetailPopup.newInstance(STANDINGS, standingTableAdapter!!.standings as ArrayList<out Parcelable>),
                    LeagueInfoDetailPopup.TAG
            )
        }
        see_more_team_stats.onClick {
            childFragmentManager.pushPopup(
                    R.id.popup_container,
                    LeagueInfoDetailPopup.newInstance(TEAM_STATS, teamStatsAdapter!!.teamStats as ArrayList<out Parcelable>),
                    LeagueInfoDetailPopup.TAG
            )
        }
        see_more_player_stats.onClick {
            childFragmentManager.pushPopup(
                    R.id.popup_container,
                    LeagueInfoDetailPopup.newInstance(PLAYER_STATS, playerStatsAdapter!!.playerStats as ArrayList<out Parcelable>),
                    LeagueInfoDetailPopup.TAG
            )
        }
    }

    override fun getBackgroundBlurLayout(): ViewGroup? = root_blur_layout

    override fun getRootCard(): CardView? = root_card

    override fun getDragHandle(): View? = drag_area

    override fun getGlide() = Glide.with(activity!!)

    override fun getTheGson() = gson

    override fun getTheLeague() = league

    private fun prepareRecyclerViews() {
        fixtureAdapter = FixtureAdapter(null, this)
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
            if (isNullOrEmpty(leagueInfo.fixtureByMatchDayList)) {
                updateUI(false, fixtures_section_list, see_all_fixtures, fixture_no_data)
            } else {
                fixtureByMatchDayList = leagueInfo.fixtureByMatchDayList.toMutableList()
                updateFixtures()
            }

            standings_progress_bar.visibility = View.GONE
            if (leagueInfo.standingsList == emptyList<Any>() || isNullOrEmpty(leagueInfo.standingsList)) {
                updateUI(false, standing_recycler_view, see_all_standings, no_standings)
            } else {
                updateUI(true, standing_recycler_view, see_all_standings, no_standings)
                standingTableAdapter!!.update(leagueInfo.standingsList)
            }

            team_stats_progress_bar.visibility = View.GONE
            if (leagueInfo.teamStatsList == emptyList<Any>() || isNullOrEmpty(leagueInfo.teamStatsList)) {
                updateUI(false, team_stats_recycler_view, see_more_team_stats, no_team_stats)
            } else {
                updateUI(true, team_stats_recycler_view, see_more_team_stats, no_team_stats)
                teamStatsAdapter!!.update(leagueInfo.teamStatsList)
            }

            player_stats_progress_bar.visibility = View.GONE
            if (leagueInfo.playerStatsList == emptyList<Any>() || isNullOrEmpty(leagueInfo.playerStatsList)) {
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
        if (!isNullOrEmpty(fixtureByMatchDayList)) {
            fixtureByMatchDayList.clear()
        }
        super.onDestroy()
    }

    override fun onBackPressed(): Boolean = childFragmentManager.removeActivePopupsIfAny()

    private fun updateFixtures() {
        fixture_progress_bar.visibility = View.VISIBLE
        see_all_fixtures.isEnabled = false
        see_all_fixtures.isClickable = false
        doAsync {
            var isPastMatches = true
            var recyclerViewScrollIndex = 0
            if (!isNullOrEmpty(fixtureByMatchDayList)) {
                for (matchDay in fixtureByMatchDayList) {
                    try {
                        if (matchDay.daySection == PAST_MATCHES) {
                            isPastMatches = true
                            recyclerViewScrollIndex = fixtureByMatchDayList.indexOf(matchDay)
                        } else if (matchDay.daySection == TODAY_MATCHES) {
                            isPastMatches = false
                            recyclerViewScrollIndex = fixtureByMatchDayList.indexOf(matchDay)
                        }
                    } catch (e: Exception) {
                        Log.e("FixtureAdapterTask", "doInBackground: recyclerViewScrollIndex ", e)
                    }

                }
                var matchFixtures: MutableList<MatchFixture> = ArrayList()
                val fixtureByDateList = fixtureByMatchDayList[recyclerViewScrollIndex].fixtureByDateList
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
                matchFixtures = if (matchFixtures.size >= 4) ArrayList(matchFixtures.subList(0, 4)) else matchFixtures
                uiThread {
                    if (!isNullOrEmpty(matchFixtures)) {
                        if (fixtureAdapter != null) {
                            fixtureAdapter!!.update(matchFixtures)
                        }
                        fixtures_section_list.scrollToPosition(recyclerViewScrollIndex)
                        see_all_fixtures.isEnabled = true
                        see_all_fixtures.isClickable = true
                        updateUI(true, fixtures_section_list, see_all_fixtures, fixture_no_data)
                    }
                    fixture_progress_bar.visibility = View.GONE
                }
            }
        }
    }
}