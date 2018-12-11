package life.plank.juna.zone.util.common

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM
import android.support.annotation.DrawableRes
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.Commentary
import life.plank.juna.zone.data.model.MatchEvent
import life.plank.juna.zone.data.model.ScrubberData
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.util.football.getAllTimelineEvents
import life.plank.juna.zone.util.time.DateUtil.FUTURE_DATE_FORMAT
import life.plank.juna.zone.util.view.UIDisplayUtil.findColor
import life.plank.juna.zone.util.view.UIDisplayUtil.findDrawable
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*
import kotlin.collections.ArrayList

//region Dummy Data for Scrubber. TODO : remove this region after getting the data from backend.
private val dummyEvents = arrayOf(
        GOAL,
        SUBSTITUTION,
        YELLOW_CARD,
        RED_CARD,
        YELLOW_RED,
        FOUL, FOUL, FOUL, FOUL,
        FOUL, FOUL, FOUL, FOUL,
        FOUL, FOUL, FOUL, FOUL,
        FOUL, FOUL, FOUL, FOUL,
        FOUL, FOUL, FOUL, FOUL,
        FOUL, FOUL, FOUL, FOUL
)
private val random = Random()

private fun getRandomEvent(): String = dummyEvents[random.nextInt(dummyEvents.size - 1)]

private fun getRandomBoolean(): Boolean = random.nextBoolean()

private fun getRandomInteraction(): Int = random.nextInt(50)

private fun getInteractionaround(): Int = random.nextInt()

fun getRandomDummyScrubberData(): List<ScrubberData> {
    val scrubberDataList = ArrayList<ScrubberData>()
    var i = 0
    while (i < 100) {
        when (i) {
            0 -> scrubberDataList.add(ScrubberData(0, getRandomInteraction().toLong(), LIVE, false))
            40 -> scrubberDataList.add(ScrubberData(40, getRandomInteraction().toLong(), HT, true))
            98 -> scrubberDataList.add(ScrubberData(98, getRandomInteraction().toLong(), FT, true))
            else -> scrubberDataList.add(ScrubberData(i.toLong(), getRandomInteraction().toLong(), getRandomEvent(), getRandomBoolean()))
        }
        i += 2
    }
    return scrubberDataList
}

private fun getDefinedDummyScrubberData(): List<ScrubberData> {
    val scrubberDataList = ArrayList<ScrubberData>()
    scrubberDataList.add(ScrubberData(0, 18, LIVE, false))
    scrubberDataList.add(ScrubberData(2, 22, FOUL, true))
    scrubberDataList.add(ScrubberData(4, 26, GOAL, true))
    scrubberDataList.add(ScrubberData(6, 28, SUBSTITUTION, false))
    scrubberDataList.add(ScrubberData(8, 30, FOUL, true))
    scrubberDataList.add(ScrubberData(12, 32, YELLOW_CARD, false))
    scrubberDataList.add(ScrubberData(15, 34, SUBSTITUTION, true))
    scrubberDataList.add(ScrubberData(18, 38, FOUL, true))
    scrubberDataList.add(ScrubberData(20, 44, YELLOW_CARD, true))
    scrubberDataList.add(ScrubberData(21, 48, GOAL, false))
    scrubberDataList.add(ScrubberData(22, 46, FOUL, true))
    scrubberDataList.add(ScrubberData(24, 42, FOUL, true))
    scrubberDataList.add(ScrubberData(28, 36, SUBSTITUTION, false))
    scrubberDataList.add(ScrubberData(32, 32, FOUL, true))
    scrubberDataList.add(ScrubberData(38, 34, RED_CARD, true))
    scrubberDataList.add(ScrubberData(42, 30, FOUL, true))
    scrubberDataList.add(ScrubberData(45, 34, HT, true))
    scrubberDataList.add(ScrubberData(46, 38, FOUL, true))
    scrubberDataList.add(ScrubberData(48, 44, GOAL, true))
    scrubberDataList.add(ScrubberData(52, 42, FOUL, true))
    scrubberDataList.add(ScrubberData(53, 36, SUBSTITUTION, true))
    scrubberDataList.add(ScrubberData(54, 34, SUBSTITUTION, false))
    scrubberDataList.add(ScrubberData(58, 38, FOUL, true))
    scrubberDataList.add(ScrubberData(62, 44, GOAL, true))
    scrubberDataList.add(ScrubberData(64, 40, FOUL, true))
    scrubberDataList.add(ScrubberData(66, 38, RED_CARD, false))
    scrubberDataList.add(ScrubberData(71, 36, FOUL, true))
    scrubberDataList.add(ScrubberData(77, 32, SUBSTITUTION, true))
    scrubberDataList.add(ScrubberData(82, 26, FOUL, true))
    scrubberDataList.add(ScrubberData(85, 34, GOAL, true))
    scrubberDataList.add(ScrubberData(89, 36, FOUL, true))
    scrubberDataList.add(ScrubberData(90, 40, YELLOW_RED, true))
    scrubberDataList.add(ScrubberData(92, 46, FOUL, true))
    scrubberDataList.add(ScrubberData(94, 48, FT, true))
    return scrubberDataList
}
//endregion

/**
 * Method for converting the [<] list to the [LineChart] compatible, customized [LineDataSet].
 *
 * @param scrubberDataList the [<] list
 * @return the [LineDataSet] object for use by the lineChart.
 */
internal fun getLineDataSet(scrubberDataList: List<ScrubberData>): LineDataSet {
    val entries = ArrayList<Entry>()
    for (scrubberData in scrubberDataList) {
        entries.add(Entry(
                scrubberData.millisecondsX.toFloat(),
                scrubberData.interactionY.toFloat(),
                getSuitableScrubberIcon(scrubberData.event.eventType, scrubberData.event.isHomeTeam)
        ))
    }
    val dataSet = LineDataSet(entries, findString(R.string.scrubber))
    dataSet.setDrawIcons(true)
    dataSet.setDrawCircles(false)
    dataSet.setDrawValues(false)
    dataSet.color = findColor(R.color.mainGradientEnd)
    dataSet.lineWidth = 1f
    dataSet.setDrawFilled(true)
    dataSet.fillDrawable = GradientDrawable(
            TOP_BOTTOM,
            intArrayOf(findColor(R.color.mainGradientTranslucentStart), findColor(R.color.mainGradientTranslucentEnd))
    )
    return dataSet
}

/**
 * Method for getting icons for scrubber events.<br></br>
 * **Note** : Icons will only be shown for **YELLOW_CARD, RED_CARD, YELLOW_RED, GOAL, SUBSTITUTION, LIVE, HT and FT events.**
 */
private fun getSuitableScrubberIcon(eventType: String, isHomeTeam: Boolean): Drawable? {
    @DrawableRes val drawableIcon: Int = when (eventType) {
        YELLOW_CARD -> if (isHomeTeam)
            R.drawable.yellow_left
        else
            R.drawable.yellow_right
        RED_CARD -> if (isHomeTeam)
            R.drawable.red_left
        else
            R.drawable.red_right
        YELLOW_RED -> R.drawable.yellow_red
        GOAL -> if (isHomeTeam)
            R.drawable.ic_goal_left
        else
            R.drawable.ic_goal_right
        SUBSTITUTION -> if (isHomeTeam)
            R.drawable.ic_sub_left
        else
            R.drawable.ic_sub_right
        LIVE, HT, FT -> R.drawable.ic_whistle
        else -> 0
    }
    return if (drawableIcon != 0) findDrawable(drawableIcon) else null
}

/**
 * Customizing the [LineChart] instance according to our needs.
 *
 * @param lineChart the [LineChart] instance to customize.
 */
private fun prepareScrubber(lineChart: LineChart?) {
    lineChart?.setPinchZoom(true)
    lineChart?.axisLeft?.isEnabled = false
    lineChart?.axisRight?.isEnabled = false
    lineChart?.description?.isEnabled = false
    lineChart?.setDrawGridBackground(false)
    lineChart?.legend?.isEnabled = false
    lineChart?.xAxis?.position = XAxis.XAxisPosition.BOTTOM
    lineChart?.xAxis?.setValueFormatter { value, _ -> getDateForScrubber(value.toLong()) }
}

fun getDateForScrubber(milliSeconds: Long): String {
    return try {
        FUTURE_DATE_FORMAT.format(Date(milliSeconds))
    } catch (e: Exception) {
        milliSeconds.toString()
    }
}

fun loadScrubber(lineChart: LineChart, isRandom: Boolean) = loadScrubber(lineChart, if (isRandom) getRandomDummyScrubberData() else getDefinedDummyScrubberData())

fun loadScrubber(lineChart: LineChart?, scrubberDataList: List<ScrubberData>) {
    prepareScrubber(lineChart)
    ZoneApplication.getContext().doAsync {
        val lineDataSets1 = ArrayList<ILineDataSet>()
        lineDataSets1.add(getLineDataSet(scrubberDataList))
        uiThread {
            lineChart?.data = LineData(lineDataSets1)
            lineChart?.invalidate()
        }
    }
}

fun loadScrubber(lineChart: LineChart?, commentaryList: List<Commentary>, matchEventList: MutableList<MatchEvent>) {
    ZoneApplication.getContext().doAsync {
        val scrubberDataList: MutableList<ScrubberData> = ArrayList()
        getAllTimelineEvents(commentaryList, matchEventList)?.run {
            if (isNotEmpty()) {
                forEach {
                    scrubberDataList.add(ScrubberData(
                            it.minute.toLong(),
                            getRandomNumberBetween(
                                    it.minute + it.extraMinute - 4L,
                                    it.minute + it.extraMinute + 4L
                            ),
                            it
                    ))
                }
            }
        }
        uiThread { loadScrubber(lineChart, scrubberDataList) }
    }
}