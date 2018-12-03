package life.plank.juna.zone.view.adapter.board.match

import com.ahamed.multiviewadapter.DataItemManager
import com.ahamed.multiviewadapter.RecyclerAdapter
import com.bumptech.glide.RequestManager
import life.plank.juna.zone.data.model.Lineups
import life.plank.juna.zone.data.model.MatchDetails
import life.plank.juna.zone.data.model.MatchEvent
import life.plank.juna.zone.data.model.binder.LineupsBindingModel
import life.plank.juna.zone.data.model.binder.SubstitutionBindingModel
import life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.common.DataUtil.validateAndUpdateList
import life.plank.juna.zone.util.facilis.addDataManagerAndRegisterBinder
import life.plank.juna.zone.view.adapter.board.match.binder.BenchDataBinder
import life.plank.juna.zone.view.adapter.board.match.binder.LineupsBinder

class LineupsAdapter(private val matchDetails: MatchDetails, private val glide: RequestManager) : RecyclerAdapter() {

    private lateinit var lineupsDataManager: DataItemManager<LineupsBindingModel>
    private lateinit var substitutionDataManager: DataItemManager<SubstitutionBindingModel>

    init {
        initAndAddLineupsDataManager()
        initAndAddSubstitutionDataManager()
    }

    private fun initAndAddLineupsDataManager() {
        lineupsDataManager = DataItemManager(this, LineupsBindingModel.from(matchDetails))
        addDataManagerAndRegisterBinder(lineupsDataManager, LineupsBinder(glide))
    }

    private fun initAndAddSubstitutionDataManager() {
        if (!isNullOrEmpty(matchDetails.matchEvents)) {
            substitutionDataManager = DataItemManager(this, SubstitutionBindingModel.from(matchDetails))
            addDataManagerAndRegisterBinder(substitutionDataManager, BenchDataBinder(glide))
        }
    }

    fun setLineups(lineups: Lineups) {
        matchDetails.lineups = lineups
        lineupsDataManager.setItem(LineupsBindingModel.from(matchDetails))
    }

    fun updateMatchEventsAndSubstitutions(matchEventList: List<MatchEvent>, isError: Boolean) {
        validateAndUpdateList(matchDetails.matchEvents, matchEventList, isError)
        substitutionDataManager.setItem(SubstitutionBindingModel.from(matchDetails))
    }
}