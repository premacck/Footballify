package life.plank.juna.zone.util.common

import android.os.SystemClock

/**
 * Method to get a random number between two numbers, inclusive of those numbers
 */
fun getRandomNumberBetween(lowerBound: Long, upperBound: Long): Long {
    if (lowerBound < 0 || upperBound < 0) throw UnsupportedOperationException("Numbers must be positive")

    val a = SystemClock.elapsedRealtimeNanos() % upperBound
    return if (a in lowerBound..upperBound) a else lowerBound + SystemClock.elapsedRealtimeNanos() % (upperBound - lowerBound)
}
