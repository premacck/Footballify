package life.plank.juna.zone.ui.board.adapter.match

import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.prembros.facilis.util.isNullOrEmpty
import kotlinx.android.synthetic.main.item_timeline.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.football.*
import life.plank.juna.zone.service.CommonDataService.findString
import life.plank.juna.zone.service.MatchDataService
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.util.view.UIDisplayUtil.getDp
import java.util.*

class TimelineAdapter : RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>() {

    private val matchEventList: MutableList<MatchEvent>

    init {
        matchEventList = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder =
            TimelineViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_timeline, parent, false))

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        holder.run {
            val event = matchEventList[position]

            if (event.liveTimeStatus != null) {
                setLayout(true)
                onWhistleEvent()
            } else {
                setLayout(false)
                placeTimelineLayout()

                when (event.eventType) {
                    GOAL -> onGoalEvent(false)
                    YELLOW_CARD, RED_CARD, YELLOW_RED -> onCardEvent()
                    SUBSTITUTION -> onSubstitutionEvent()
                    PENALTY -> onPenaltyEvent(false)
                    MISSED_PENALTY -> onPenaltyEvent(true)
                    OWN_GOAL -> onGoalEvent(true)
                    else -> {
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = matchEventList.size

    fun updateEvents(matchEventList: List<MatchEvent>) {
        this.matchEventList.addAll(matchEventList)
        notifyDataSetChanged()
    }

    private fun TimelineViewHolder.setLayout(isWhistleEvent: Boolean) {
        itemView.run {
            whistle_event_layout.visibility = if (isWhistleEvent) View.VISIBLE else View.GONE
            timeline_layout.visibility = if (isWhistleEvent) View.GONE else View.VISIBLE
            minute.visibility = if (isWhistleEvent) View.GONE else View.VISIBLE
        }
    }

    private fun TimelineViewHolder.placeTimelineLayout() {
        itemView.run {
            val params = timeline_layout.layoutParams as RelativeLayout.LayoutParams
            params.addRule(if (event.isHomeTeam) RelativeLayout.ALIGN_PARENT_START else RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
            params.removeRule(if (event.isHomeTeam) RelativeLayout.ALIGN_PARENT_END else RelativeLayout.ALIGN_PARENT_START)
            params.addRule(if (event.isHomeTeam) RelativeLayout.START_OF else RelativeLayout.END_OF, R.id.center_space)
            params.removeRule(if (event.isHomeTeam) RelativeLayout.END_OF else RelativeLayout.START_OF)
            params.rightMargin = getDp((if (event.isHomeTeam) 22 else 0).toFloat()).toInt()
            params.leftMargin = getDp((if (event.isHomeTeam) 0 else 22).toFloat()).toInt()
            timeline_event_up.gravity = Gravity.CENTER_VERTICAL or if (event.isHomeTeam) Gravity.END else Gravity.START
            timeline_event_down.gravity = Gravity.CENTER_VERTICAL or if (event.isHomeTeam) Gravity.END else Gravity.START
            val eventParams = timeline_event_up.layoutParams as LinearLayout.LayoutParams
            eventParams.gravity = Gravity.CENTER_VERTICAL or if (event.isHomeTeam) Gravity.END else Gravity.START
            timeline_event_up.layoutParams = eventParams
            timeline_event_down.layoutParams = eventParams
            timeline_layout.layoutParams = params
        }
    }

    private fun TimelineViewHolder.onWhistleEvent() {
        itemView.run {
            whistle.visibility = View.VISIBLE
            center_space.visibility = View.INVISIBLE
            minute.visibility = View.GONE
            whistle_event_up.text = getTimedEventString()
            whistle_event_down.visibility = View.VISIBLE
            whistle_event_down.text = getTimedEventExtraString()
        }
    }

    private fun TimelineViewHolder.onCardEvent() {
        itemView.run {
            setCenterLayout()
            timeline_event_down.compoundDrawablePadding = getDp(10f).toInt()
            val suitableCardDrawable = if (event.eventType.contains(findString(R.string.red)))
                if (event.isHomeTeam)
                    R.drawable.red_left
                else
                    R.drawable.red_right
            else if (event.isHomeTeam)
                R.drawable.yellow_left
            else
                R.drawable.yellow_right
            if (event.isHomeTeam) {
                timeline_event_up.setCompoundDrawablesWithIntrinsicBounds(0, 0, suitableCardDrawable, 0)
            } else {
                timeline_event_up.setCompoundDrawablesWithIntrinsicBounds(suitableCardDrawable, 0, 0, 0)
            }
            timeline_event_up.text = event.playerName
            timeline_event_down.visibility = View.GONE
        }
    }

    private fun TimelineViewHolder.onSubstitutionEvent() {
        itemView.run {
            setCenterLayout()
            timeline_event_down.compoundDrawablePadding = getDp(10f).toInt()
            if (event.isHomeTeam) {
                timeline_event_up.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_substitute_in, 0)
                timeline_event_down.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_substitute_out, 0)
            } else {
                timeline_event_up.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_substitute_in, 0, 0, 0)
                timeline_event_down.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_substitute_out, 0, 0, 0)
            }
            timeline_event_up.text = event.playerName
            timeline_event_down.visibility = View.VISIBLE
            timeline_event_down.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            timeline_event_down.text = event.relatedPlayerName
        }
    }

    private fun TimelineViewHolder.onGoalEvent(isOwnGoal: Boolean) {
        itemView.run {
            setCenterLayout()
            timeline_event_down.compoundDrawablePadding = getDp(4f).toInt()
            timeline_event_up.setCompoundDrawablesWithIntrinsicBounds(
                    if (event.isHomeTeam) 0 else R.drawable.ic_goal_right,
                    0,
                    if (event.isHomeTeam) R.drawable.ic_goal_left else 0,
                    0
            )
            timeline_event_up.text = event.playerName
            timeline_event_down.visibility = View.VISIBLE
            timeline_event_down.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
            val timelineEventDownText = StringBuilder()
            if (isOwnGoal && event.isHomeTeam) {
                timelineEventDownText.append(OWN_GOAL_LOWERCASE).append(COMMA)
            }
            if (event.relatedPlayerName != null) {
                timeline_event_down.setCompoundDrawablesWithIntrinsicBounds(
                        if (event.isHomeTeam) R.drawable.ic_assist else 0,
                        0,
                        if (event.isHomeTeam) 0 else R.drawable.ic_assist,
                        0
                )
                timelineEventDownText.append(if (event.isHomeTeam) event.relatedPlayerName else event.result)
                        .append(WIDE_SPACE)
                        .append(if (event.isHomeTeam) event.result else event.relatedPlayerName)
            } else {
                timeline_event_down.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                timelineEventDownText.append(event.result)
            }
            if (isOwnGoal && !event.isHomeTeam) {
                timelineEventDownText.append(COMMA).append(OWN_GOAL_LOWERCASE)
            }
            timeline_event_down.text = timelineEventDownText
        }
    }

    private fun TimelineViewHolder.onPenaltyEvent(isMissed: Boolean) {
        itemView.run {
            setCenterLayout()
            timeline_event_down.visibility = View.VISIBLE
            timeline_event_up.visibility = View.VISIBLE
            timeline_event_down.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
            timeline_event_up.setCompoundDrawablesWithIntrinsicBounds(
                    if (isMissed) 0 else if (event.isHomeTeam) 0 else R.drawable.ic_goal_right,
                    0,
                    if (isMissed) 0 else if (event.isHomeTeam) R.drawable.ic_goal_left else 0,
                    0
            )
            timeline_event_down.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            timeline_event_up.text = event.playerName
            val penaltyText = StringBuilder()
            if (isMissed) {
                penaltyText.append(findString(R.string.missed_penalty))
            } else {
                penaltyText.append(if (event.isHomeTeam) findString(R.string.penalty) else event.result)
                        .append(COMMA)
                        .append(if (event.isHomeTeam) event.result else findString(R.string.penalty))
            }
            timeline_event_down.text = penaltyText
        }
    }

    private fun TimelineViewHolder.setCenterLayout() {
        itemView.run {
            whistle.visibility = View.GONE
            center_space.visibility = View.VISIBLE
            minute.visibility = View.VISIBLE
            setMinute()
        }
    }

    private fun TimelineViewHolder.setMinute() {
        itemView.run {
            val minuteText = event.minute.toString() + "'"
            minute.text = minuteText
        }
    }

    private fun TimelineViewHolder.getTimedEventString(): String {
        itemView.run {
            return when (Objects.requireNonNull<LiveTimeStatus>(event.liveTimeStatus).timeStatus) {
                LIVE -> KICK_OFF
                HT -> HALF_TIME
                else -> FULL_TIME
            }
        }
    }

    private fun TimelineViewHolder.getTimedEventExtraString(): String {
        itemView.run {
            return when (Objects.requireNonNull<LiveTimeStatus>(event.liveTimeStatus).timeStatus) {
                LIVE -> LIVE_TIME
                else -> MatchDataService.getFormattedExtraMinutes(event.liveTimeStatus!!.extraMinute)
            }
        }
    }

    /**
     * Adding the element at its respective position.
     * Best case runtime : O(1)
     * Worst case runtime : O(n)
     * Live case runtime : O(1) - because live events are going to be coming "live", so it will always insert at 0th position.
     */
    fun updateLiveEvents(matchEventList: List<MatchEvent>) {
        if (!isNullOrEmpty(matchEventList) && !isNullOrEmpty(matchEventList[0].playerName)) {
            var positionToInsert = 0
            for (matchEvent in this.matchEventList) {
                if (matchEvent.minute > matchEventList[0].minute) {
                    positionToInsert = this.matchEventList.indexOf(matchEvent)
                } else
                    break
            }
            this.matchEventList.addAll(positionToInsert, matchEventList)
            notifyItemRangeInserted(positionToInsert, matchEventList.size)
        }
    }

    /**
     * Adding the whistle event at its respective position.
     */
    fun updateWhistleEvent(timeStatus: LiveTimeStatus) {
        var positionToInsert = 0
        for (matchEvent in matchEventList) {
            if (matchEvent.minute > timeStatus.minute) {
                positionToInsert = this.matchEventList.indexOf(matchEvent)
            } else
                break
        }
        matchEventList.add(positionToInsert, MatchEvent(timeStatus))
        notifyItemInserted(positionToInsert)
    }

    class TimelineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var event: MatchEvent
    }
}