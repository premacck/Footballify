package life.plank.juna.zone.view.activity

import android.os.Bundle
import android.support.v7.widget.CardView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_onboarding.*
import kotlinx.android.synthetic.main.custom_search_view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.FootballTeam
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.interfaces.OnClickZoneItemListener
import life.plank.juna.zone.util.DataUtil
import life.plank.juna.zone.util.PreferenceManager
import life.plank.juna.zone.util.common.launch
import life.plank.juna.zone.util.errorToast
import life.plank.juna.zone.util.facilis.BaseCard
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.view.activity.zone.ZoneActivity
import life.plank.juna.zone.view.adapter.OnboardingAdapter
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import java.net.HttpURLConnection
import javax.inject.Inject

class OnboardingActivity : BaseCard(), OnClickZoneItemListener {

    override fun getBackgroundBlurLayout(): ViewGroup? = blur_layout

    override fun getRootCard(): CardView? = onboarding_root_card

    override fun getDragHandle(): View? = onboarding_drag_area

    companion object {
        private val TAG = OnboardingActivity::class.java.simpleName
        fun newInstance(): OnboardingActivity = OnboardingActivity()
    }

    override fun onItemClick(id: String?, isSelected: Boolean?) {
        if (isSelected!!) {
            teamSet.add(id!!)
        } else {
            teamSet.remove(id)
        }
    }

    @Inject
    lateinit var restApi: RestApi
    private var onBoardingAdapter: OnboardingAdapter? = null
    private var teamList = ArrayList<FootballTeam>()
    var teamSet: MutableSet<String> = HashSet()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.activity_onboarding, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)

        initBottomSheetRecyclerView()
        prepareSearchEditText()
        next.onDebouncingClick { postTeamPref(getString(R.string.football)) }
        getPopularTeams()

    }

    private fun initBottomSheetRecyclerView() {
        onBoardingAdapter = OnboardingAdapter(context, teamList, this)
        onboarding_recycler_view.adapter = onBoardingAdapter
    }

    private fun prepareSearchEditText() {
        search_edit_text.textChangedListener {
            onTextChanged { charSequence, _, _, _ ->
                if (!DataUtil.isNullOrEmpty(charSequence.toString())) {
                    getFootballTeams(charSequence.toString())
                } else {
                    teamList.clear()
                    onBoardingAdapter?.notifyDataSetChanged()
                }
            }
        }
    }

    private fun getPopularTeams() {
        if (DataUtil.isNullOrEmpty(PreferenceManager.Auth.getToken()))
            return
        restApi.getPopularTeams(PreferenceManager.Auth.getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e(OnboardingActivity.TAG, "Popular Team details: ", it)
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_OK -> {
                    onBoardingAdapter?.setTeamList(it.body())
                }
                else -> errorToast(R.string.popular_team_not_found, it)
            }
        })
    }

    private fun postTeamPref(zone: String) {
        if (!teamSet.isEmpty()) {
            restApi.postTeamPreferences(zone, teamSet, PreferenceManager.Auth.getToken()).setObserverThreadsAndSmartSubscribe({
                Log.e(OnboardingActivity.TAG, "Team Preference details: ", it)
            }, {
                when (it.code()) {
                    HttpURLConnection.HTTP_NO_CONTENT -> {
                        (activity!!.launch<ZoneActivity>())
                    }
                    else -> errorToast(R.string.team_pref_not_found, it)
                }
            })
        } else {
            Toast.makeText(context, getString(R.string.select_team), Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFootballTeams(teamName: String) {
        restApi.getSearchedFootballTeams(teamName, PreferenceManager.Auth.getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e(OnboardingActivity.TAG, "getFootballTeamDetails: ", it)
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
        })
    }
}
