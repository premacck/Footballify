package life.plank.juna.zone.ui.board.fragment.fixture

import android.os.Bundle
import android.util.Log
import android.view.*
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.prembros.facilis.util.isNullOrEmpty
import kotlinx.android.synthetic.main.fragment_board_info.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.api.*
import life.plank.juna.zone.data.model.football.*
import life.plank.juna.zone.service.CommonDataService.findString
import life.plank.juna.zone.ui.base.fragment.BaseBoardFragment
import life.plank.juna.zone.ui.base.fragment.BaseJunaCard
import life.plank.juna.zone.ui.board.adapter.match.MatchStatsAdapter
import life.plank.juna.zone.ui.football.MatchStatsListener
import life.plank.juna.zone.util.time.DateUtil.getTimeDiffFromNow
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
        ZoneApplication.application.uiComponent.inject(this)
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
        if (parentFragment is BaseJunaCard && !isNullOrEmpty(matchDetails.commentary)) {
            (parentFragment as BaseJunaCard).pushPopup(CommentaryPopup.newInstance((matchDetails.commentary as ArrayList<Commentary>?)!!))
        }
    }

    fun updateCommentary(commentaryList: List<Commentary>) {
        adapter?.updateCommentaries(commentaryList, false)
    }

    fun updateMatchStats(matchStats: MatchStats) {
        adapter?.updateMatchStats(matchStats, 0)
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