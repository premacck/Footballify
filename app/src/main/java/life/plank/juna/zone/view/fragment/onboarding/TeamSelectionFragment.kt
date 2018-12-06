package life.plank.juna.zone.view.fragment.onboarding

import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_team_selection.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.FootballTeam
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.DataUtil
import life.plank.juna.zone.util.common.errorToast
import life.plank.juna.zone.util.common.launch
import life.plank.juna.zone.util.common.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.sharedpreference.PreferenceManager
import life.plank.juna.zone.view.activity.zone.ZoneActivity
import life.plank.juna.zone.view.adapter.onboarding.TeamSelectionAdapter
import life.plank.juna.zone.view.fragment.base.SearchableCard
import rx.Subscription
import java.net.HttpURLConnection
import javax.inject.Inject

class TeamSelectionFragment : SearchableCard() {

    @Inject
    lateinit var restApi: RestApi
    private var onBoardingAdapter: TeamSelectionAdapter? = null
    private var teamList = ArrayList<FootballTeam>()
    private var searchSubscription: Subscription? = null

    companion object {
        private val TAG = TeamSelectionFragment::class.java.simpleName
        fun newInstance(): TeamSelectionFragment = TeamSelectionFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_team_selection, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)

        initBottomSheetRecyclerView()
        next.onDebouncingClick { postTeamPref(getString(R.string.football)) }
        getPopularTeams()
    }

    override fun getBackgroundBlurLayout(): ViewGroup? = blur_layout

    override fun getRootView(): CardView? = root_card

    override fun getDragView(): View? = drag_area

    override fun searchView(): EditText = search_view

    override fun searchedList(): MutableList<*> = teamList

    override fun searchAdapter(): RecyclerView.Adapter<*>? = onBoardingAdapter

    override fun searchAction(searchString: String) = getFootballTeams(searchString)

    override fun searchDelay(): Long = 500

    override fun searchSubscription(): Subscription? = searchSubscription

    private fun initBottomSheetRecyclerView() {
        onBoardingAdapter = TeamSelectionAdapter(Glide.with(this), teamList)
        onboarding_recycler_view.adapter = onBoardingAdapter
    }

    private fun getPopularTeams() {
        if (DataUtil.isNullOrEmpty(PreferenceManager.Auth.getToken()))
            return
        restApi.getPopularTeams(PreferenceManager.Auth.getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "Popular Team details: ", it)
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_OK -> {
                    onBoardingAdapter?.setTeamList(it.body())
                }
                else -> errorToast(R.string.popular_team_not_found, it)
            }
        }, this)
    }

    private fun postTeamPref(zone: String) {
        if (onBoardingAdapter?.selectedTeamNames?.isEmpty() == false) {
            restApi.postTeamPreferences(zone, onBoardingAdapter?.selectedTeamNames, PreferenceManager.Auth.getToken()).setObserverThreadsAndSmartSubscribe({
                Log.e(TAG, "Team Preference details: ", it)
            }, {
                when (it.code()) {
                    HttpURLConnection.HTTP_NO_CONTENT -> {
                        (activity!!.launch<ZoneActivity>())
                    }
                    else -> errorToast(R.string.team_pref_not_found, it)
                }
            }, this)
        } else {
            Toast.makeText(context, getString(R.string.select_team), Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFootballTeams(teamName: String) {
        searchSubscription = restApi.getSearchedFootballTeams(teamName, PreferenceManager.Auth.getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "getFootballTeamDetails: ", it)
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_OK -> {
                    onBoardingAdapter?.setTeamList(it.body())
                }
                HttpURLConnection.HTTP_NOT_FOUND -> {
                    teamList.clear()
                    onBoardingAdapter?.notifyDataSetChanged()
                }
                else -> errorToast(R.string.team_not_found, it)
            }
        }, this)
    }
}
