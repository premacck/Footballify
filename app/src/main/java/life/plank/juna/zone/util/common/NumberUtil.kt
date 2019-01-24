package life.plank.juna.zone.util.common

import android.os.SystemClock
import java.util.*

/**
 * Method to get a random number between two numbers, inclusive of those numbers
 */
fun getRandomNumberBetween(lowerBound: Long, upperBound: Long): Long {
    if (lowerBound < 0 || upperBound < 0) throw UnsupportedOperationException("Numbers must be positive")

    val a = SystemClock.elapsedRealtimeNanos() % upperBound
    return if (a in lowerBound..upperBound) a else lowerBound + SystemClock.elapsedRealtimeNanos() % (upperBound - lowerBound)
}

object NumberFormatter {
    private val suffixes = TreeMap<Long, String>()

    init {
        suffixes[1_000L] = "K"
        suffixes[1_000_000L] = "M"
        suffixes[1_000_000_000L] = "B"
        suffixes[1_000_000_000_000L] = "T"
    }

    fun format(value: Long): String {
        if (value == java.lang.Long.MIN_VALUE) return format(java.lang.Long.MIN_VALUE + 1)
        if (value < 0) return "-" + format(-value)
        //        deal with easy case
        if (value < 1000) return java.lang.Long.toString(value)

        val e = suffixes.floorEntry(value)
        val divideBy = e.key
        val suffix = e.value

        //        the number part of the output times 10
        val truncated = value / (divideBy!! / 10)
        val hasDecimal = truncated < 100 && truncated / 10.0 != (truncated / 10).toDouble()
        return if (hasDecimal) (truncated / 10.0).toString() + suffix else (truncated / 10).toString() + suffix
    }
}