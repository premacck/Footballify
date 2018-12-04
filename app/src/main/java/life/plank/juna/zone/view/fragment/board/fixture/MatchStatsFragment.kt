package life.plank.juna.zone.view.fragment.board.fixture

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_board_info.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.RestApiAggregator
import life.plank.juna.zone.data.model.*
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.interfaces.MatchStatsListener
import life.plank.juna.zone.util.common.AppConstants.COMMENTARY_DATA
import life.plank.juna.zone.util.common.AppConstants.MATCH_STATS_DATA
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.common.onSubscribe
import life.plank.juna.zone.util.common.onTerminate
import life.plank.juna.zone.util.common.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.util.facilis.BaseCard
import life.plank.juna.zone.util.time.DateUtil.getTimeDiffFromNow
import life.plank.juna.zone.view.adapter.board.match.MatchStatsAdapter
import life.plank.juna.zone.view.fragment.base.BaseBoardFragment
import java.util.*
import javax.inject.Inject

class MatchStatsFragment : BaseBoardFragment(), MatchStatsListener {

    @Inject
    lateinit var restApi: RestApi
    @Inject
    lateinit var gson: Gson

    lateinit var matchDetails: MatchDetails
    private var adapter: MatchStatsAdapter? = null

    companion object {
        private val TAG = MatchStatsFragment::class.java.simpleName
        fun newInstance(matchDetails: MatchDetails) = MatchStatsFragment().apply { arguments = Bundle().apply { putParcelable(findString(R.string.match_id_string), matchDetails) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)
        arguments?.run { matchDetails = getParcelable(getString(R.string.match_id_string))!! }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_board_info, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prepareRecyclerView()
        getBoardInfoData(false)
        swipe_refresh_layout.setOnRefreshListener { getBoardInfoData(true) }
    }

    private fun prepareRecyclerView() {
        adapter = MatchStatsAdapter(matchDetails, Glide.with(this), this)
        list_board_info?.adapter = adapter
    }

    private fun getBoardInfoData(isRefreshing: Boolean) {
        val timeDiffOfMatchFromNow = getTimeDiffFromNow(matchDetails.matchStartTime)
        if (timeDiffOfMatchFromNow > 0) {
            if (matchDetails.league != null) {
                if (isRefreshing) swipe_refresh_layout.isRefreshing = false
                getPreMatchData(
                        matchDetails.league!!,
                        Objects.requireNonNull<String>(matchDetails.homeTeam.name),
                        Objects.requireNonNull<String>(matchDetails.awayTeam.name)
                )
            }
        } else {
            getPostMatchData(isRefreshing)
        }
    }

    override fun onCommentarySeeAllClick(fromView: View) {
        if (parentFragment is BaseCard && !isNullOrEmpty(matchDetails.commentary)) {
            (parentFragment as BaseCard).pushPopup(CommentaryPopup.newInstance((matchDetails.commentary as ArrayList<Commentary>?)!!))
        }
    }

    fun updateZoneLiveData(zoneLiveData: ZoneLiveData) {
        when (zoneLiveData.liveDataType) {
            COMMENTARY_DATA -> {
                val commentaryList = zoneLiveData.getCommentaryList(gson)
                if (commentaryList != null) {
                    adapter?.updateCommentaries(commentaryList, false)
                }
            }
            MATCH_STATS_DATA -> adapter?.updateMatchStats(zoneLiveData.getMatchStats(gson), 0)
        }
    }

    override fun handlePreMatchData(pair: Pair<List<Standings>, List<TeamStats>>?) {
        if (adapter != null && pair != null) {
            adapter?.setPreMatchData(pair.first, pair.second)
        }
    }

    private fun getPostMatchData(isRefreshing: Boolean) {
        RestApiAggregator.getPostMatchBoardData(matchDetails, restApi)
                .onSubscribe { if (isRefreshing) swipe_refresh_layout?.isRefreshing = true }
                .onTerminate { if (isRefreshing) swipe_refresh_layout?.isRefreshing = false }
                .setObserverThreadsAndSmartSubscribe({
                    Log.e(TAG, "getPostMatchData : " + it.message, it)
                    adapter?.setMatchStats()
                }, {
                    adapter?.setMatchStats()
                }, this)
    }

    override fun onDetach() {
        adapter = null
        super.onDetach()
    }
}