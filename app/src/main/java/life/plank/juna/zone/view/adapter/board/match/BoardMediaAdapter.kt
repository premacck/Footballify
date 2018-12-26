package life.plank.juna.zone.view.adapter.board.match

import com.ahamed.multiviewadapter.*
import life.plank.juna.zone.data.model.*
import life.plank.juna.zone.data.model.binder.HighlightsBindingModel
import life.plank.juna.zone.util.common.AppConstants.MatchTimeVal
import life.plank.juna.zone.util.common.AppConstants.MatchTimeVal.*
import life.plank.juna.zone.util.common.JunaDataUtil.validateAndUpdateList
import life.plank.juna.zone.util.time.DateUtil.getMatchTimeValue
import life.plank.juna.zone.util.view.addDataManagerAndRegisterBinder
import life.plank.juna.zone.view.adapter.board.match.binder.MatchHighlightsBinder

class BoardMediaAdapter(private val matchDetails: MatchDetails, private val screenWidth: Int) : RecyclerAdapter() {

    private var highlightsDataManager: DataItemManager<HighlightsBindingModel>? = null

    init {
        @MatchTimeVal val matchTimeValue = getMatchTimeValue(matchDetails.matchStartTime, true)
        when (matchTimeValue) {
            MATCH_PAST, MATCH_COMPLETED_TODAY, MATCH_LIVE -> initAndAddHighlightsDataManager()
            MATCH_ABOUT_TO_START, MATCH_ABOUT_TO_START_BOARD_ACTIVE, MATCH_SCHEDULED_TODAY, MATCH_SCHEDULED_LATER -> {
            }
        }
    }

    private fun initAndAddHighlightsDataManager() {
        highlightsDataManager = DataItemManager(this, HighlightsBindingModel.from(matchDetails))
        addDataManagerAndRegisterBinder(highlightsDataManager!!, MatchHighlightsBinder(screenWidth))
    }

    fun updateHighlights(highlightsList: List<Highlights>, isError: Boolean) {
        validateAndUpdateList(this.matchDetails.highlights, highlightsList, isError)
        if (highlightsDataManager != null) {
            highlightsDataManager!!.setItem(HighlightsBindingModel.from(matchDetails))
        }
    }
}