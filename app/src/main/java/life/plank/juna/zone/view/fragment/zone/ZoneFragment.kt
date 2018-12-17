package life.plank.juna.zone.view.fragment.zone

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import kotlinx.android.synthetic.main.follow_league_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_team_selection.*
import kotlinx.android.synthetic.main.fragment_zone.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.League
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.interfaces.OnItemClickListener
import life.plank.juna.zone.util.common.AppConstants.BoomMenuPage.BOOM_MENU_SETTINGS_AND_HOME
import life.plank.juna.zone.util.common.DataUtil
import life.plank.juna.zone.util.common.customToast
import life.plank.juna.zone.util.common.errorToast
import life.plank.juna.zone.util.common.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.util.network.NetworkStatus
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.CurrentUser.getUserPreferences
import life.plank.juna.zone.util.view.boomMenu
import life.plank.juna.zone.util.view.setupBoomMenu
import life.plank.juna.zone.util.view.setupWith
import life.plank.juna.zone.view.adapter.LeagueSelectionAdapter
import life.plank.juna.zone.view.fragment.base.BaseFragment
import life.plank.juna.zone.view.latestMatch.FootballZoneAdapter
import java.net.HttpURLConnection
import javax.inject.Inject

class ZoneFragment : BaseFragment(), OnItemClickListener {

    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var restApi: RestApi
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    lateinit var adapter: FootballZoneAdapter
    private var leagueSelectionAdapter: LeagueSelectionAdapter? = null
    private var leagueList = ArrayList<League>()

    companion object {
        private val TAG = ZoneFragment::class.java.simpleName
        fun newInstance() = ZoneFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_zone, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()
        setUpData()
        setupBoomMenu(BOOM_MENU_SETTINGS_AND_HOME, activity!!, null, null)
        initBottomSheetRecyclerView()
        leagueSelectionAdapter?.setLeagueList(DataUtil.getStaticLeagues())
        search_view.visibility = View.GONE
        title.visibility = View.GONE
        next.visibility = View.GONE

        bottomSheetBehavior = BottomSheetBehavior.from<View>(bottom_sheet)
        bottomSheetBehavior.peekHeight = 0

        boomMenu().setupWith(football_feed_recycler_view)
    }

    private fun setUpData() {
        if (NetworkStatus.isNetworkAvailable(parent_layout, context)) {
            getUpcomingMatches()
        } else {
            customToast(R.string.no_internet_connection)
        }
    }

    private fun initRecyclerView() {
        adapter = FootballZoneAdapter(activity!!, restApi, this)
        football_feed_recycler_view.adapter = adapter

        progress_bar.visibility = View.GONE
    }

    private fun initBottomSheetRecyclerView() {
        onboarding_recycler_view.layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 3)
        leagueSelectionAdapter = LeagueSelectionAdapter(activity!!, leagueList)
        onboarding_recycler_view.adapter = leagueSelectionAdapter
    }

    private fun getUpcomingMatches() {
        restApi.getNextMatches(getUserPreferences()[0].zonePreferences?.leagues, getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "getUpcomingMatches() : onError: ", it)
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_OK -> it.body()?.run { adapter.setMatches(this) }
                HttpURLConnection.HTTP_NOT_FOUND -> {
                    errorToast(R.string.user_pref_not_found, it)
                }
                else -> errorToast(R.string.next_match_not_found, it)

            }
        })
    }

    override fun onItemClicked() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}