package life.plank.juna.zone.view.adapter.board.info

import android.app.Activity
import android.view.View
import com.ahamed.multiviewadapter.DataItemManager
import com.ahamed.multiviewadapter.ItemBinder
import com.ahamed.multiviewadapter.ItemViewHolder
import com.ahamed.multiviewadapter.RecyclerAdapter
import life.plank.juna.zone.data.model.*
import life.plank.juna.zone.data.model.binder.HighlightsBindingModel
import life.plank.juna.zone.data.model.binder.ScrubberBindingModel
import life.plank.juna.zone.util.AppConstants.MatchTimeVal
import life.plank.juna.zone.util.AppConstants.MatchTimeVal.*
import life.plank.juna.zone.util.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.DateUtil.getMatchTimeValue
import life.plank.juna.zone.view.adapter.board.info.binder.MatchHighlightsBinder
import life.plank.juna.zone.view.adapter.board.info.binder.ScrubberBinder
import java.util.*

class BoardMediaAdapter(private val matchDetails: MatchDetails, private val activity: Activity, private val listener: BoardMediaAdapterListener) : RecyclerAdapter() {

    private var scrubberDataManager: DataItemManager<ScrubberBindingModel>? = null
    private var highlightsDataManager: DataItemManager<HighlightsBindingModel>? = null

    init {


        @MatchTimeVal val matchTimeValue = getMatchTimeValue(matchDetails.matchStartTime, true)
        when (matchTimeValue) {
            MATCH_PAST, MATCH_COMPLETED_TODAY, MATCH_LIVE -> preparePastOrLiveMatchAdapter()
            MATCH_ABOUT_TO_START -> prepareRecentMatchAdapter()
            MATCH_ABOUT_TO_START_BOARD_ACTIVE -> prepareRecentMatchAdapterWhenBoardIsActive()
            MATCH_SCHEDULED_TODAY, MATCH_SCHEDULED_LATER -> {
            }
        }
    }

    /**
     * Method for populating components of a past or live match
     * <br></br>Consists of:
     * <br></br>Scrubber, from [ScrubberBinder]
     * <br></br>Scrubber, from [MatchHighlightsBinder]
     */
    private fun preparePastOrLiveMatchAdapter() {
        initAndAddScrubberDataManager()
        initAndAddHighlightsDataManager()
    }

    /**
     * Method for populating components of a match which is about to start in an hour
     * <br></br>Consists of:
     * <br></br>Scrubber, from [ScrubberBinder]
     */
    private fun prepareRecentMatchAdapter() {
        initAndAddScrubberDataManager()
    }

    private fun prepareRecentMatchAdapterWhenBoardIsActive() {
        initAndAddScrubberDataManager()
    }

    private fun initAndAddScrubberDataManager() {
        scrubberDataManager = DataItemManager(this, ScrubberBindingModel.from(matchDetails))
        addDataManagerAndRegisterBinder(scrubberDataManager!!, ScrubberBinder(listener))
    }

    private fun initAndAddHighlightsDataManager() {
        highlightsDataManager = DataItemManager(this, HighlightsBindingModel.from(matchDetails))
        addDataManagerAndRegisterBinder(highlightsDataManager!!, MatchHighlightsBinder(activity))
    }

    private fun <BM> addDataManagerAndRegisterBinder(dataManager: DataItemManager<BM>, binderToRegister: ItemBinder<BM, out ItemViewHolder<BM>>) {
        addDataManager(dataManager)
        registerBinder(binderToRegister)
    }

    //region Methods to update live match data
    fun setScrubber() {
        if (scrubberDataManager != null) {
            scrubberDataManager!!.setItem(ScrubberBindingModel.from(matchDetails))
        }
    }

    fun updateScrubber(scrubberDataList: List<ScrubberData>, isError: Boolean) {
        validateAndUpdateList(matchDetails.scrubberDataList as MutableList<ScrubberData>?, scrubberDataList, isError)
        setScrubber()
    }

    fun updateMatchStats(matchStats: MatchStats?, message: Int) {
        if (matchStats != null) {
            matchDetails.matchStats = matchStats
        }
        matchDetails.matchStats!!.errorMessage = message
    }

    fun updateLineups(lineups: Lineups?) {
        if (lineups != null) {
            matchDetails.lineups = lineups
        }
        matchDetails.lineups!!.errorMessage = 0

    }

    fun updateHighlights(highlightsList: List<Highlights>, isError: Boolean) {
        validateAndUpdateList(this.matchDetails.highlights as MutableList<Highlights>?, highlightsList, isError)
        if (highlightsDataManager != null) {
            highlightsDataManager!!.setItem(HighlightsBindingModel.from(matchDetails))
        }
    }

    //endregion

    private fun <T> validateAndUpdateList(originalList: MutableList<T>?, newList: List<T>, isError: Boolean) {
        var originalList = originalList
        if (!isError) {
            if (originalList == null) {
                originalList = ArrayList()
            }
            if (!isNullOrEmpty(newList)) {
                originalList.addAll(newList)
            }
        }
    }

    interface BoardMediaAdapterListener {
        fun onScrubberClick(fromView: View)
    }
}