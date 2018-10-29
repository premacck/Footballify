package life.plank.juna.zone.view.fragment.board.fixture

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.popup_timeline.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.MatchDetails
import life.plank.juna.zone.data.model.MatchEvent
import life.plank.juna.zone.data.model.MatchFixture
import life.plank.juna.zone.util.AppConstants.*
import life.plank.juna.zone.util.DataUtil.*
import life.plank.juna.zone.util.DateUtil.getTimelineDateHeader
import life.plank.juna.zone.util.FixtureListUpdateTask
import life.plank.juna.zone.util.UIDisplayUtil.*
import life.plank.juna.zone.util.facilis.floatUp
import life.plank.juna.zone.view.adapter.TimelineAdapter
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
    private lateinit var fixture: MatchFixture
    private var adapter: TimelineAdapter? = null

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.hasExtra(getString(R.string.intent_zone_live_data))) {
                setZoneLiveData(intent)
            }
        }
    }

    companion object {
        val TAG: String = TimelinePopup::class.java.simpleName
        fun newInstance(currentMatchId: Long, matchEvents: ArrayList<MatchEvent>, matchDetails: MatchDetails) = TimelinePopup().apply {
            arguments = Bundle().apply {
                putLong(findString(R.string.match_id_string), currentMatchId)
                putParcelableArrayList(findString(R.string.intent_match_event_list), matchEvents)
                putParcelable(findString(R.string.intent_match_fixture), matchDetails)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)
        currentMatchId = arguments?.getLong(getString(R.string.match_id_string), 0)!!
        matchDetails = arguments?.getParcelable(getString(R.string.intent_match_fixture))!!
        fixture = MatchFixture.from(matchDetails)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.popup_timeline, container, false)

    override fun doOnStart() {
        root_card.floatUp()
        initRecyclerView()
        mergeCommentaryAndMatchEvents()
    }

    override fun onResume() {
        super.onResume()
        context?.registerReceiver(mMessageReceiver, IntentFilter(getString(R.string.intent_board)))
    }

    override fun onPause() {
        super.onPause()
        context?.unregisterReceiver(mMessageReceiver)
    }

    override fun getBlurLayout(): BlurLayout? = root_blur_layout

    override fun getDragHandle(): View? = drag_area

    override fun getRootView(): View? = root_card

    override fun getBackgroundLayout(): ViewGroup? = root_blur_layout

    private fun prepareViews() {
        venue_name.text = if (matchDetails.venue != null) matchDetails.venue!!.name else null
        time_status.text = getDisplayTimeStatus(matchDetails.timeStatus!!)
        date.text = getTimelineDateHeader(matchDetails.matchStartTime)
        score.text = getSeparator(fixture, win_pointer, false)

        Glide.with(this)
                .load(matchDetails.homeTeam.logoLink)
                .apply(RequestOptions.overrideOf(getDp(24f).toInt(), getDp(24f).toInt()))
                .into(getEndDrawableTarget(time_status))
        Glide.with(this)
                .load(matchDetails.awayTeam.logoLink)
                .apply(RequestOptions.overrideOf(getDp(24f).toInt(), getDp(24f).toInt()))
                .into(getStartDrawableTarget(date))

        ScrubberLoader.prepare(scrubber, false)
    }

    override fun onDestroy() {
        adapter = null
        FirebaseMessaging.getInstance().unsubscribeFromTopic(getString(R.string.pref_football_match_sub) + currentMatchId)
        super.onDestroy()
    }

    private fun initRecyclerView() {
        adapter = TimelineAdapter(context)
        timeline_recycler_view.adapter = adapter
    }

    private fun setZoneLiveData(intent: Intent) {
        val zoneLiveData = getZoneLiveData(intent, getString(R.string.intent_zone_live_data), gson)
        when (zoneLiveData!!.liveDataType) {
            SCORE_DATA -> {
                val scoreData = zoneLiveData.getScoreData(gson)
                updateScoreLocally(fixture, scoreData)
                FixtureListUpdateTask.update(fixture, scoreData, null, true)
                score.text = getSeparator(fixture, win_pointer, false)
            }
            MATCH_EVENTS -> {
                val matchEventList = zoneLiveData.getMatchEventList(gson)
                if (adapter != null && !isNullOrEmpty(matchEventList)) {
                    adapter!!.updateLiveEvents(matchEventList)
                    timeline_recycler_view.smoothScrollToPosition(0)
                }
            }
            TIME_STATUS -> {
                val timeStatus = zoneLiveData.getLiveTimeStatus(gson)
                if (timeStatus.timeStatus == LIVE && timeStatus.minute == 0 ||
                        timeStatus.timeStatus == HT ||
                        timeStatus.timeStatus == FT) {
                    if (adapter != null) {
                        adapter!!.updateWhistleEvent(timeStatus)
                    }
                }
                updateTimeStatusLocally(fixture, timeStatus)
                FixtureListUpdateTask.update(fixture, null, timeStatus, false)
                time_status.text = getDisplayTimeStatus(fixture.timeStatus!!)
            }
        }
    }

    private fun mergeCommentaryAndMatchEvents() {
        doAsync {
            val matchEvents: List<MatchEvent> = getAllTimelineEvents(matchDetails.commentary, arguments?.getParcelableArrayList(getString(R.string.intent_match_event_list)))
            uiThread {
                adapter!!.updateEvents(matchEvents)
                prepareViews()
                FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.pref_football_match_sub) + currentMatchId)
                progress_bar.visibility = View.GONE
            }
        }
    }
}