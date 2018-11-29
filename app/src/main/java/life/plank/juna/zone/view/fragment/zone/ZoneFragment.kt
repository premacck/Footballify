package life.plank.juna.zone.view.fragment.zone

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import com.bumptech.glide.Glide
import com.facebook.FacebookSdk.getApplicationContext
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_zone.*
import kotlinx.android.synthetic.main.search_people_bottom_sheet.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.League
import life.plank.juna.zone.data.model.User
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.interfaces.OnItemClickListener
import life.plank.juna.zone.util.*
import life.plank.juna.zone.util.AppConstants.BoomMenuPage.BOOM_MENU_SETTINGS_AND_HOME
import life.plank.juna.zone.util.DataUtil.getStaticLeagues
import life.plank.juna.zone.util.PreferenceManager.Auth.getToken
import life.plank.juna.zone.view.LatestMatch.LeagueModel
import life.plank.juna.zone.view.LatestMatch.MultiListAdapter
import life.plank.juna.zone.view.adapter.common.SearchViewAdapter
import life.plank.juna.zone.view.fragment.base.BaseFragment
import org.jetbrains.anko.support.v4.find
import java.net.HttpURLConnection
import javax.inject.Inject

class ZoneFragment : BaseFragment(), SearchView.OnQueryTextListener, OnItemClickListener {

    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var picasso: Picasso
    @Inject
    lateinit var restApi: RestApi

    lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    lateinit var adapter: MultiListAdapter
    lateinit var searchViewAdapter: SearchViewAdapter
    internal var userList = ArrayList<User>()

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
        setupBoomMenu(BOOM_MENU_SETTINGS_AND_HOME, activity!!, null, arc_menu, null)
        initBottomSheetRecyclerView()
        search_view.queryHint = getString(R.string.search_query_hint)

        bottomSheetBehavior = BottomSheetBehavior.from<View>(bottom_sheet)
        bottomSheetBehavior.peekHeight = 0

        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    UIDisplayUtil.hideSoftKeyboard(find(android.R.id.content))
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })
        arc_menu.setupWith(football_feed_recycler_view)

    }

    private fun setUpData() {
        if (NetworkStatus.isNetworkAvailable(parent_layout, context)) {
            getUpcomingMatches()
        } else {
            customToast(R.string.no_internet_connection)
        }
    }

    private fun initRecyclerView() {

        adapter = MultiListAdapter(activity)
        football_feed_recycler_view.layoutManager = LinearLayoutManager(getApplicationContext())
        football_feed_recycler_view.adapter = adapter

        progress_bar!!.visibility = View.GONE

        adapter.addLeague(LeagueModel("Flower sdgvd gdfdhfc"))

    }

    private fun initBottomSheetRecyclerView() {
        recycler_view.layoutManager = GridLayoutManager(context, 5)
        searchViewAdapter = SearchViewAdapter(userList, Glide.with(this))
        recycler_view.adapter = searchViewAdapter
        search_view.setOnQueryTextListener(this)

    }

    override fun onResume() {
        super.onResume()
        arc_menu.menuIn()
    }

    override fun onPause() {
        super.onPause()
        arc_menu.menuOut()
    }

    private fun getSearchedUsers(displayName: String) {
        restApi.getSearchedUsers(getToken(), displayName).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "getSearchedUsers() : onError: ", it)
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_OK -> searchViewAdapter.update(it.body() as ArrayList)
                HttpURLConnection.HTTP_NOT_FOUND -> {
                    userList.clear()
                    searchViewAdapter.notifyDataSetChanged()
                }
                else -> Log.e(TAG, it.message())
            }
        })
    }

    private fun getUpcomingMatches() {
        restApi.getUpcomingMatches(getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "getUpcomingMatches() : onError: ", it)
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_OK -> {
                    it.body()
                    val leagueList = getStaticLeagues()

                    val topThreeMatch = ArrayList<League>()
                    for (i in 0..2) {
                        topThreeMatch.add(leagueList[i])
                    }
                    adapter.addTopMatch(topThreeMatch)

                    val lowerMatch = ArrayList<League>()
                    for (i in 3 until leagueList.size) {
                        lowerMatch.add(leagueList[i])
                    }
                    adapter.addLowerMatch(lowerMatch)
                    adapter.notifyDataSetChanged()

                }
                HttpURLConnection.HTTP_NOT_FOUND -> {
                }
                else -> Log.e(TAG, it.message())
            }
        }
        )
    }

    override fun onQueryTextSubmit(s: String): Boolean {
        return true
    }

    override fun onQueryTextChange(s: String): Boolean {
        if (!s.isEmpty()) {
            getSearchedUsers(s)
        } else {
            userList.clear()
            searchViewAdapter.notifyDataSetChanged()
        }
        return true
    }

    override fun onItemClicked(objectId: String, isSelected: Boolean?) {
        //TODO: handle on item click
    }
}