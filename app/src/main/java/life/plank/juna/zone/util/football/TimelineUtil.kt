package life.plank.juna.zone.util.football

import kotlinx.android.parcel.RawValue
import life.plank.juna.zone.data.model.Commentary
import life.plank.juna.zone.data.model.LiveTimeStatus
import life.plank.juna.zone.data.model.MatchEvent
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty
import java.util.*

fun getAllTimelineEvents(commentaries: @RawValue List<Commentary>, matchEventList: @RawValue MutableList<MatchEvent>): List<MatchEvent>? {
    if (isNullOrEmpty<Commentary>(commentaries) || isNullOrEmpty<MatchEvent>(matchEventList)) {
        return null
    }

    if (!matchEventList.any { it.eventType == WHISTLE_EVENT }) {
        matchEventList.add(MatchEvent(LiveTimeStatus(LIVE, 0, 0)))
    }
    if (!matchEventList.any { it.liveTimeStatus?.timeStatus == HT } && !matchEventList.any { it.liveTimeStatus?.timeStatus == FT }) {
        for (commentary in commentaries) {
            if (commentary.comment.startsWith(FIRST_HALF_ENDED_)) {
                matchEventList.add(MatchEvent(LiveTimeStatus(HT, commentary.minute.toInt(), commentary.extraMinute.toInt())))
            } else if (commentary.comment.startsWith(SECOND_HALF_ENDED_)) {
                matchEventList.add(MatchEvent(LiveTimeStatus(FT, commentary.minute.toInt(), commentary.extraMinute.toInt())))
            }
        }
    }
    Collections.sort(matchEventList, MatchEventComparator())
    matchEventList.reverse()
    return matchEventList
}

class MatchEventComparator : Comparator<MatchEvent> {
    override fun compare(o1: MatchEvent, o2: MatchEvent): Int {
        return if (o1.minute == o2.minute) {
            Integer.compare(o1.extraMinute, o2.extraMinute)
        } else Integer.compare(o1.minute, o2.minute)
    }
}
