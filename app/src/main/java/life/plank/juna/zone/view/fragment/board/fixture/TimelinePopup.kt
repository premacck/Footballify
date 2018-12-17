package life.plank.juna.zone.view.fragment.board.fixture

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.popup_timeline.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.MatchDetails
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.util.common.DataUtil.*
import life.plank.juna.zone.util.football.FixtureListUpdateTask
import life.plank.juna.zone.util.football.getAllTimelineEvents
import life.plank.juna.zone.util.time.DateUtil.getTimelineDateHeader
import life.plank.juna.zone.util.view.UIDisplayUtil.*
import life.plank.juna.zone.view.adapter.board.match.TimelineAdapter
import life.plank.juna.zone.view.fragment.base.BaseBlurPopup
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*
import javax.inject.Inject

class TimelinePopup : BaseBlurPopup() {

    @Inject
    lateinit var gson: Gson

    private var currentMatchId: Long = 0
    private lateinit var matchDetails: MatchDetails
    private var adapter: TimelineAdapter? = null

    companion object {
        val TAG: String = TimelinePopup::class.java.simpleName
        fun newInstance(currentMatchId: Long, matchDetails: MatchDetails) = TimelinePopup().apply {
            arguments = Bundle().apply {
                putLong(findString(R.string.match_id_string), currentMatchId)
                putParcelable(findString(R.string.intent_match_fixture), matchDetails)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)
        currentMatchId = arguments?.getLong(getString(R.string.match_id_string), 0)!!
        matchDetails = arguments?.getParcelable(getString(R.string.intent_match_fixture))!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.popup_timeline, container, false)

    override fun doOnStart() {
        initRecyclerView()
        prepareViews()
        mergeCommentaryAndMatchEvents()
    }

    override fun getBlurLayout(): BlurLayout? = root_blur_layout

    override fun getDragHandle(): View? = drag_area

    override fun getRootView(): View? = root_card

    override fun getBackgroundLayout(): ViewGroup? = root_blur_layout

    private fun prepareViews() {
        venue_name.text = if (matchDetails.venue != null) matchDetails.venue!!.name else null
        time_status.text = matchDetails.timeStatus?.run { getDisplayTimeStatus(this) } ?: NOT_STARTED
        date.text = getTimelineDateHeader(matchDetails.matchStartTime)
        score.text = getSeparator(matchDetails, win_pointer, false)

        Glide.with(this)
                .load(matchDetails.homeTeam.logoLink)
                .apply(RequestOptions.overrideOf(getDp(24f).toInt(), getDp(24f).toInt()))
                .into(getEndDrawableTarget(time_status))
        Glide.with(this)
                .load(matchDetails.awayTeam.logoLink)
                .apply(RequestOptions.overrideOf(getDp(24f).toInt(), getDp(24f).toInt()))
                .into(getStartDrawableTarget(date))
    }

    override fun onDestroy() {
        adapter = null
        super.onDestroy()
    }

    private fun initRecyclerView() {
        adapter = TimelineAdapter()
        timeline_recycler_view.adapter = adapter
    }

    private fun setZoneLiveData(intent: Intent) {
        val zoneLiveData = getZoneLiveData(intent, getString(R.string.intent_zone_live_data), gson)
        when (zoneLiveData!!.liveDataType) {
            SCORE_DATA -> {
                val scoreData = zoneLiveData.getScoreData(gson)
                updateScoreLocally(matchDetails, scoreData)
                FixtureListUpdateTask.update(matchDetails, scoreData, null, true)
                score.text = getSeparator(matchDetails, win_pointer, false)
            }
            MATCH_EVENTS -> {
                val matchEventList = zoneLiveData.getMatchEventList(gson)
                if (adapter != null && !isNullOrEmpty(matchEventList)) {
                    adapter!!.updateLiveEvents(matchEventList!!)
                    timeline_recycler_view.smoothScrollToPosition(0)
                }
            }
            TIME_STATUS -> {
                val timeStatus = zoneLiveData.getLiveTimeStatus(gson)
                if (timeStatus?.timeStatus == LIVE && timeStatus.minute == 0 ||
                        timeStatus?.timeStatus == HT ||
                        timeStatus?.timeStatus == FT) {
                    if (adapter != null) {
                        adapter!!.updateWhistleEvent(timeStatus)
                    }
                }
                FixtureListUpdateTask.update(matchDetails, null, timeStatus, false)
                time_status.text = getDisplayTimeStatus(matchDetails.timeStatus!!)
            }
        }
    }

    private fun mergeCommentaryAndMatchEvents() {
        if (isNullOrEmpty(matchDetails.commentary) || isNullOrEmpty(matchDetails.matchEvents)) {
            no_data.visibility = View.VISIBLE
            timeline_recycler_view.visibility = View.GONE
            no_data.setText(if (matchDetails.matchStartTime.time <= Date().time) R.string.more_events_will_be_displayed else R.string.match_yet_to_start)
            return
        }

        doAsync {
            val matchEvents = getAllTimelineEvents(matchDetails.commentary!!, matchDetails.matchEvents!!)
            uiThread {
                matchEvents?.run { adapter?.updateEvents(matchEvents) }
            }
        }
    }
}