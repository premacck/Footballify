package life.plank.juna.zone.ui.board.fragment.fixture

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_board_info.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.api.*
import life.plank.juna.zone.data.model.football.MatchDetails
import life.plank.juna.zone.data.model.football.MatchEvent
import life.plank.juna.zone.service.CommonDataService.findString
import life.plank.juna.zone.ui.base.fragment.BaseJunaFragment
import life.plank.juna.zone.ui.board.adapter.match.LineupsAdapter
import life.plank.juna.zone.util.common.errorToast
import java.net.HttpURLConnection
import javax.inject.Inject

class LineupFragment : BaseJunaFragment() {

    @Inject
    lateinit var restApi: RestApi
    @Inject
    lateinit var gson: Gson

    lateinit var matchDetails: MatchDetails
    private var adapter: LineupsAdapter? = null

    companion object {
        private val TAG = LineupFragment::class.java.simpleName
        fun newInstance(matchDetails: MatchDetails) = LineupFragment().apply { arguments = Bundle().apply { putParcelable(findString(R.string.match_id_string), matchDetails) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.application.uiComponent.inject(this)
        arguments?.run { matchDetails = getParcelable(getString(R.string.match_id_string))!! }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_board_info, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prepareRecyclerView()
        getLineupFormation(false)
        swipe_refresh_layout.setOnRefreshListener { getLineupFormation(true) }
    }

    private fun prepareRecyclerView() {
        if (!isAdded) return

        adapter = LineupsAdapter(matchDetails, Glide.with(this))
        list_board_info.adapter = adapter
    }

    fun updateMatchEvents(matchEventList: List<MatchEvent>) {
        adapter?.updateMatchEventsAndSubstitutions(matchEventList, false)
    }

    /**
     * Method for fetching lineups live. invoked by receiving the FootballLiveData's lineup broadcast
     */
    fun getLineupFormation(isRefreshing: Boolean) {
        if (!isAdded) return

        updateUi(false, R.string.loading_lineups)
        restApi.getLineUpsData(matchDetails.matchId)
                .onTerminate { if (isRefreshing) swipe_refresh_layout.isRefreshing = false }
                .setObserverThreadsAndSmartSubscribe({
                    if (!isAdded) return@setObserverThreadsAndSmartSubscribe

                    Log.e(TAG, "getLineupFormation(): ", it)
                    errorToast(R.string.line_ups_not_available, it)
                    updateUi(false, R.string.stay_tuned_for_the_lineups)
                }, {
                    if (!isAdded) return@setObserverThreadsAndSmartSubscribe

                    when (it.code()) {
                        HttpURLConnection.HTTP_OK -> {
                            val lineups = it.body()
//                                Updating the adapter only if live lineups fetch is successful
                            if (lineups != null) {
                                if (adapter != null) {
                                    adapter?.setLineups(lineups)
                                } else {
                                    matchDetails.lineups = lineups
                                    prepareRecyclerView()
                                }
                                updateUi(true)
                            } else
                                updateUi(false, R.string.stay_tuned_for_the_lineups)
                        }
                        else -> {
                            errorToast(R.string.line_ups_not_available, it)
                            updateUi(false, R.string.stay_tuned_for_the_lineups)
                        }
                    }
                }, this)
    }

    private fun updateUi(isDataAvailable: Boolean, @StringRes message: Int = 0) {
        list_board_info.visibility = if (isDataAvailable) View.VISIBLE else View.GONE
        no_data.visibility = if (isDataAvailable) View.GONE else View.VISIBLE
        no_data.text = if (message == 0) null else getString(message)
    }

    override fun onDetach() {
        adapter = null
        super.onDetach()
    }
}