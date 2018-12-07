package life.plank.juna.zone.view.fragment.zone

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.FacebookSdk.getApplicationContext
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.follow_league_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_team_selection.*
import kotlinx.android.synthetic.main.fragment_zone.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.League
import life.plank.juna.zone.data.model.NextMatch
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.interfaces.OnItemClickListener
import life.plank.juna.zone.util.common.AppConstants.BoomMenuPage.BOOM_MENU_SETTINGS_AND_HOME
import life.plank.juna.zone.util.common.DataUtil
import life.plank.juna.zone.util.common.customToast
import life.plank.juna.zone.util.common.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.util.network.NetworkStatus
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.view.boomMenu
import life.plank.juna.zone.util.view.setupBoomMenu
import life.plank.juna.zone.util.view.setupWith
import life.plank.juna.zone.view.adapter.LeagueSelectionAdapter
import life.plank.juna.zone.view.fragment.base.BaseFragment
import life.plank.juna.zone.view.latestMatch.LeagueModel
import life.plank.juna.zone.view.latestMatch.MultiListAdapter
import java.net.HttpURLConnection
import javax.inject.Inject

class ZoneFragment : BaseFragment(), OnItemClickListener {

    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var picasso: Picasso
    @Inject
    lateinit var restApi: RestApi
    lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    lateinit var adapter: MultiListAdapter
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

        adapter = MultiListAdapter(activity, this)
        football_feed_recycler_view.layoutManager = LinearLayoutManager(getApplicationContext())
        football_feed_recycler_view.adapter = adapter

        progress_bar!!.visibility = View.GONE

        adapter.addLeague(LeagueModel("Flower sdgvd gdfdhfc"))
    }

    private fun initBottomSheetRecyclerView() {
        onboarding_recycler_view.layoutManager = GridLayoutManager(context, 3)
        leagueSelectionAdapter = LeagueSelectionAdapter(leagueList)
        onboarding_recycler_view.adapter = leagueSelectionAdapter

    }

    override fun onResume() {
        super.onResume()
        boomMenu().menuIn()
    }

    override fun onPause() {
        super.onPause()
        boomMenu().menuOut()
    }

    private fun getUpcomingMatches() {
        restApi.getUpcomingMatches(getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "getUpcomingMatches() : onError: ", it)
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_OK -> {
                    val matchList = it.body()
                    if (matchList != null) {
                        val topThreeMatch = ArrayList<NextMatch>()
                        for (i in 0..2) {
                            topThreeMatch.add(matchList[i])
                        }
                        adapter.addTopMatch(topThreeMatch)

                        val lowerMatch = ArrayList<NextMatch>()
                        for (i in 3 until matchList.size) {
                            lowerMatch.add(matchList[i])
                        }
                        adapter.addLowerMatch(lowerMatch)
                        adapter.notifyDataSetChanged()
                    }
                }
                HttpURLConnection.HTTP_NOT_FOUND -> {
                }
                else -> Log.e(TAG, it.message())
            }
        })
    }

    override fun onItemClicked() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}