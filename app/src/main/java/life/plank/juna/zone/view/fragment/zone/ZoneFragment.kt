package life.plank.juna.zone.view.fragment.zone

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_zone.*
import kotlinx.android.synthetic.main.search_people_bottom_sheet.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.User
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.interfaces.OnItemClickListener
import life.plank.juna.zone.util.common.AppConstants.BoomMenuPage.BOOM_MENU_SETTINGS_AND_HOME
import life.plank.juna.zone.util.common.DataUtil.getStaticLeagues
import life.plank.juna.zone.util.common.customToast
import life.plank.juna.zone.util.common.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.util.network.NetworkStatus
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.view.UIDisplayUtil
import life.plank.juna.zone.util.view.setupBoomMenu
import life.plank.juna.zone.util.view.setupWith
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.adapter.common.SearchViewAdapter
import life.plank.juna.zone.view.adapter.league.FootballLeagueAdapter
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
    lateinit var adapter: FootballLeagueAdapter
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
            getFootballFeed()
        } else {
            customToast(R.string.no_internet_connection)
        }
    }

    private fun initRecyclerView() {
        adapter = FootballLeagueAdapter(activity as BaseCardActivity?)
        football_feed_recycler_view.adapter = adapter
        football_feed_recycler_view.setHasFixedSize(true)
    }

    private fun initBottomSheetRecyclerView() {
        recycler_view.layoutManager = GridLayoutManager(context, 5)
        searchViewAdapter = SearchViewAdapter(userList, Glide.with(this))
        recycler_view.adapter = searchViewAdapter
        search_view.setOnQueryTextListener(this)

    }

    private fun getFootballFeed() {
        progress_bar!!.visibility = View.GONE
        adapter.leagueList = getStaticLeagues()
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
        }, this)
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