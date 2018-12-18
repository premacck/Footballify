package life.plank.juna.zone.view.fragment.football

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_standings.*
import life.plank.juna.zone.*
import life.plank.juna.zone.data.model.*
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.facilis.doAfterDelay
import life.plank.juna.zone.view.adapter.league.StandingTableAdapter
import life.plank.juna.zone.view.fragment.base.BaseLeagueFragment
import javax.inject.Inject

class StandingsFragment : BaseLeagueFragment() {

    @Inject
    lateinit var restApi: RestApi

    private var standingTableAdapter: StandingTableAdapter? = null
    private lateinit var league: League

    companion object {
        fun newInstance(league: League) = StandingsFragment().apply { arguments = Bundle().apply { putParcelable(DataUtil.findString(R.string.intent_league), league) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)
        arguments?.run {
            league = getParcelable(getString(R.string.intent_league))!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_standings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        doAfterDelay(300) {
            standingTableAdapter = StandingTableAdapter(Glide.with(this), false)
            standing_recycler_view.adapter = standingTableAdapter

            getStandings(false)
            standings_swipe_refresh_layout.setOnRefreshListener { getStandings(true) }
            standing_recycler_view.setupWithParentFragmentBoomMenu()
        }
    }

    override fun restApi(): RestApi = restApi

    override fun getTheLeague() = league

    private fun getStandings(isRefreshing: Boolean) {
        restApi.getStandings(league.name, league.seasonName, league.countryName)
                .onTerminate { if (isRefreshing) standings_swipe_refresh_layout.isRefreshing = false }
                .setObserverThreadsAndSmartSubscribe({
                    Log.e("getStandings()", "ERROR: ", it)
                }, {
                    setStandings(it.body())
                }, this)
    }

    private fun setStandings(standingsList: List<Standings>?) {
        standings_progress_bar.visibility = View.GONE
        if (isNullOrEmpty(standingsList)) {
            updateUI(false, standing_recycler_view, no_standings)
        } else {
            updateUI(true, standing_recycler_view, no_standings)
            standingTableAdapter!!.update(standingsList!!)
            leagueViewModel.updateStandings(league.id, standingsList)
        }
    }

    private fun updateUI(available: Boolean, recyclerView: RecyclerView, noDataView: TextView) {
        recyclerView.visibility = if (available) View.VISIBLE else View.INVISIBLE
        noDataView.visibility = if (available) View.GONE else View.VISIBLE
    }
}