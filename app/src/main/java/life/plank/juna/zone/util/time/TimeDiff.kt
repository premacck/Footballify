package life.plank.juna.zone.util.time

import android.util.Log
import life.plank.juna.zone.util.time.DateUtil.getCommentDateAndTimeFormat
import java.util.*

private const val SECOND_MILLIS = 1000L
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS
private const val MOMENTS_AGO = "Now"
private const val A_MINUTE_AGO = "A minute ago"
private const val MINUTES_AGO = "minutes ago"
private const val AN_HOUR_AGO = "An hour ago"
private const val HOURS_AGO = "hours ago"
private const val YESTERDAY = "Yesterday"
private const val DAYS_AGO = "days ago"

fun getTimeAgo(date: Date): String {
    var time = date.time
    try {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000
        }

        val now = System.currentTimeMillis()
        if (time > now || time <= 0) {
            return ""
        }

        val diff = now - time
        return when {
            diff < MINUTE_MILLIS -> MOMENTS_AGO
            diff < 2 * MINUTE_MILLIS -> A_MINUTE_AGO
            diff < 50 * MINUTE_MILLIS -> "${diff / MINUTE_MILLIS} $MINUTES_AGO"
            diff < 90 * MINUTE_MILLIS -> AN_HOUR_AGO
            diff < 24 * HOUR_MILLIS -> "${diff / HOUR_MILLIS} $HOURS_AGO"
            diff < 48 * HOUR_MILLIS -> YESTERDAY
            else -> "${diff / DAY_MILLIS} $DAYS_AGO"
        }
    } catch (e: Exception) {
        Log.e("getTimeAgo()", e.message, e)
        return getCommentDateAndTimeFormat(date)
    }
}

fun getTimeInHourMinuteFormat(timeStamp: Long): String {
    val floatHourDiff = timeStamp.toFloat() / HOUR_MILLIS
    val intHourDiff = floatHourDiff.toInt()
    val minuteDiff = ((floatHourDiff - intHourDiff) * 60).toInt()
    return "$intHourDiff hrs $minuteDiff mins"
}

fun getFootballTimeElapsed(timeStamp: Long): String {
    val floatMinuteDiff = timeStamp.toFloat() / MINUTE_MILLIS
    val intMinuteDiff = floatMinuteDiff.toInt()
    val secondDiff = ((floatMinuteDiff - intMinuteDiff) * 60).toInt()
    return "$intMinuteDiff : $secondDiff"
}