package life.plank.juna.zone.view.fragment.football

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_fixture.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.local.model.LeagueInfo
import life.plank.juna.zone.data.model.FixtureByMatchDay
import life.plank.juna.zone.data.model.League
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.interfaces.LeagueContainer
import life.plank.juna.zone.util.AppConstants.PAST_MATCHES
import life.plank.juna.zone.util.AppConstants.TODAY_MATCHES
import life.plank.juna.zone.util.DataUtil.findString
import life.plank.juna.zone.util.DataUtil.isNullOrEmpty
import life.plank.juna.zone.view.adapter.fixture.FixtureMatchdayAdapter
import life.plank.juna.zone.view.fragment.base.BaseLeagueFragment
import life.plank.juna.zone.view.fragment.football.LeagueInfoFragment.Companion.fixtureByMatchDayList
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

class FixtureFragment : BaseLeagueFragment(), LeagueContainer {

    @Inject
    lateinit var restApi: RestApi

    private var isDataLocal: Boolean = false
    private var isFixtureListReady: Boolean = false
    private lateinit var league: League
    private var fixtureMatchdayAdapter: FixtureMatchdayAdapter? = null

    companion object {
        val TAG: String = FixtureFragment::class.java.simpleName
        fun newInstance(league: League) = FixtureFragment().apply { arguments = Bundle().apply { putParcelable(findString(R.string.intent_league), league) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)
        if (arguments == null) {
            onNoMatchesFound()
            return
        }
        league = arguments?.getParcelable(getString(R.string.intent_league))!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_fixture, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fixtureMatchdayAdapter = FixtureMatchdayAdapter(this)
        fixtures_section_list.adapter = fixtureMatchdayAdapter
        if (!isNullOrEmpty(fixtureByMatchDayList)) {
            updateFixtures()
        } else getLeagueInfoFromRoomDb()
        fixtures_section_list.setupWithParentFragmentBoomMenu()
    }

    private fun getLeagueInfoFromRoomDb() {
        isDataLocal = true
        leagueViewModel.fixtureLiveData.observe(this, Observer { handleLeagueInfoData(it as MutableList<FixtureByMatchDay>) })
        leagueViewModel.getFixtureListFromDb(league.id)
    }

    private fun getLeagueInfoFromRestApi() {
        if (isDataLocal) {
            isDataLocal = false
            leagueViewModel.getFixturesFromRestApi(league, restApi)
        }
    }

    private fun handleLeagueInfoData(fixtureList: MutableList<FixtureByMatchDay>) {
        if (!isNullOrEmpty(fixtureList)) {
//            Update new data in DB
            if (!isDataLocal) {
                leagueViewModel.leagueRepository.insertLeagueInfo(LeagueInfo(league, fixtureList))
            }
            fixtureByMatchDayList = fixtureList
            updateFixtures()
            fixtureByMatchDayList

            getLeagueInfoFromRestApi()
        } else {
            getLeagueInfoFromRestApi()
        }
    }

    private fun updateFixtures() {
        if (!isNullOrEmpty(fixtureByMatchDayList)) {
            updateFixtureAdapter()
        } else onNoMatchesFound()
    }

    override fun restApi(): RestApi = restApi

    override fun getTheLeague() = league

    private fun onNoMatchesFound() {
        no_data.visibility = View.VISIBLE
        progress_bar.visibility = View.GONE
        fixtures_section_list.visibility = View.GONE
    }

    private fun updateFixtureAdapter() {
        progress_bar.visibility = View.VISIBLE
        doAsync {
            var recyclerViewScrollIndex = 0
            if (!isFixtureListReady) {
                fixtureByMatchDayList?.run {
                    if (!isNullOrEmpty(this)) {
                        for (matchDay in this) {
                            if (matchDay.daySection == PAST_MATCHES) {
                                recyclerViewScrollIndex =
                                        if (this.indexOf(matchDay) < this.size - 1) {
                                            this.indexOf(matchDay) + 1
                                        } else {
                                            this.indexOf(matchDay)
                                        }
                            } else if (matchDay.daySection == TODAY_MATCHES) {
                                recyclerViewScrollIndex = this.indexOf(matchDay)
                            }
                        }
                    }
                }
            }
            uiThread {
                progress_bar.visibility = View.GONE
                fixtureMatchdayAdapter?.updateFixtures()
                if (!isFixtureListReady) {
                    fixtureByMatchDayList?.run {
                        (parentFragment as? LeagueInfoFragment)?.setMatchday(this[recyclerViewScrollIndex].matchDay)
                    }
                    fixtures_section_list.scrollToPosition(recyclerViewScrollIndex)
                    isFixtureListReady = true
                }
            }
        }
    }

    override fun onDestroy() {
        fixtureMatchdayAdapter = null
        super.onDestroy()
    }
}