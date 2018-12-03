package life.plank.juna.zone.view.fragment.board.fixture

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_board_info.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.MatchDetails
import life.plank.juna.zone.data.model.ZoneLiveData
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.AppConstants.LINEUPS_DATA
import life.plank.juna.zone.util.common.AppConstants.MATCH_EVENTS
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.util.common.errorToast
import life.plank.juna.zone.util.common.onSubscribe
import life.plank.juna.zone.util.common.onTerminate
import life.plank.juna.zone.util.common.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.view.adapter.board.match.LineupsAdapter
import life.plank.juna.zone.view.fragment.base.BaseFragment
import java.net.HttpURLConnection
import javax.inject.Inject

class LineupFragment : BaseFragment() {

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
        ZoneApplication.getApplication().uiComponent.inject(this)
        arguments?.run { matchDetails = getParcelable(getString(R.string.match_id_string))!! }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_board_info, container, false)
        ButterKnife.bind(this, rootView)
        prepareRecyclerView()
        return rootView
    }

    private fun prepareRecyclerView() {
        if (!isAdded) return

        adapter = LineupsAdapter(matchDetails, Glide.with(this))
        list_board_info.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLineupFormation(false)
        swipe_refresh_layout.setOnRefreshListener { getLineupFormation(true) }
    }

    fun updateZoneLiveData(zoneLiveData: ZoneLiveData) {
        when (zoneLiveData.liveDataType) {
            MATCH_EVENTS -> {
                val matchEventList = zoneLiveData.getMatchEventList(gson)
                if (matchEventList != null) {
                    adapter!!.updateMatchEventsAndSubstitutions(matchEventList, false)
                }
            }
            LINEUPS_DATA -> getLineupFormation(false)
        }
    }

    /**
     * Method for fetching lineups live. invoked by receiving the ZoneLiveData's lineup broadcast
     */
    private fun getLineupFormation(isRefreshing: Boolean) {
        if (!isAdded) return

        restApi.getLineUpsData(matchDetails.matchId)
                .onSubscribe { startLoading() }
                .onTerminate { if (isRefreshing) swipe_refresh_layout.isRefreshing = false }
                .setObserverThreadsAndSmartSubscribe({
                    if (!isAdded) return@setObserverThreadsAndSmartSubscribe

                    Log.e(TAG, "getLineupFormation(): ", it)
                    errorToast(R.string.line_ups_not_available, it)
                    updateUi(false)
                }, {
                    if (!isAdded) return@setObserverThreadsAndSmartSubscribe

                    when (it.code()) {
                        HttpURLConnection.HTTP_OK -> {
                            val lineups = it.body()
                            //                                Updating the adapter only if live lineups fetch is successful
                            if (lineups != null) {
                                if (adapter != null) {
                                    adapter!!.setLineups(lineups)
                                } else {
                                    matchDetails.lineups = lineups
                                    prepareRecyclerView()
                                }
                                updateUi(true)
                            } else
                                updateUi(false)
                        }
                        else -> {
                            errorToast(R.string.line_ups_not_available, it)
                            updateUi(false)
                        }
                    }
                })
    }

    private fun updateUi(isDataAvailable: Boolean) {
        list_board_info.visibility = if (isDataAvailable) View.VISIBLE else View.INVISIBLE
        no_data.visibility = if (isDataAvailable) View.INVISIBLE else View.VISIBLE
        no_data.setText(R.string.stay_tuned_for_the_lineups)
    }

    private fun startLoading() {
        list_board_info.visibility = View.INVISIBLE
        no_data.visibility = View.INVISIBLE
    }

    override fun onDetach() {
        adapter = null
        super.onDetach()
    }
}