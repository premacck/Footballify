package life.plank.juna.zone.view.adapter.board.match

import android.app.Activity
import android.view.View
import com.ahamed.multiviewadapter.DataItemManager
import com.ahamed.multiviewadapter.ItemBinder
import com.ahamed.multiviewadapter.ItemViewHolder
import com.ahamed.multiviewadapter.RecyclerAdapter
import life.plank.juna.zone.data.model.Highlights
import life.plank.juna.zone.data.model.MatchDetails
import life.plank.juna.zone.data.model.binder.HighlightsBindingModel
import life.plank.juna.zone.util.AppConstants.MatchTimeVal
import life.plank.juna.zone.util.AppConstants.MatchTimeVal.*
import life.plank.juna.zone.util.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.DateUtil.getMatchTimeValue
import life.plank.juna.zone.view.adapter.board.match.binder.MatchHighlightsBinder
import life.plank.juna.zone.view.adapter.board.match.binder.ScrubberBinder
import java.util.*

class BoardMediaAdapter(private val matchDetails: MatchDetails, private val activity: Activity) : RecyclerAdapter() {

    private var highlightsDataManager: DataItemManager<HighlightsBindingModel>? = null

    init {
        @MatchTimeVal val matchTimeValue = getMatchTimeValue(matchDetails.matchStartTime, true)
        when (matchTimeValue) {
            MATCH_PAST, MATCH_COMPLETED_TODAY, MATCH_LIVE -> preparePastOrLiveMatchAdapter()
            MATCH_ABOUT_TO_START, MATCH_ABOUT_TO_START_BOARD_ACTIVE, MATCH_SCHEDULED_TODAY, MATCH_SCHEDULED_LATER -> {
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
        initAndAddHighlightsDataManager()
    }

    private fun initAndAddHighlightsDataManager() {
        highlightsDataManager = DataItemManager(this, HighlightsBindingModel.from(matchDetails))
        addDataManagerAndRegisterBinder(highlightsDataManager!!, MatchHighlightsBinder(activity))
    }

    private fun <BM> addDataManagerAndRegisterBinder(dataManager: DataItemManager<BM>, binderToRegister: ItemBinder<BM, out ItemViewHolder<BM>>) {
        addDataManager(dataManager)
        registerBinder(binderToRegister)
    }

    fun updateHighlights(highlightsList: List<Highlights>, isError: Boolean) {
        validateAndUpdateList(this.matchDetails.highlights as MutableList<Highlights>?, highlightsList, isError)
        if (highlightsDataManager != null) {
            highlightsDataManager!!.setItem(HighlightsBindingModel.from(matchDetails))
        }
    }

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