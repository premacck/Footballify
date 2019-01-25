package life.plank.juna.zone.ui.board.adapter.match

import com.ahamed.multiviewadapter.*
import com.bumptech.glide.RequestManager
import com.prembros.facilis.util.isNullOrEmpty
import life.plank.juna.zone.data.model.football.*
import life.plank.juna.zone.service.CommonDataService.validateAndUpdateList
import life.plank.juna.zone.ui.base.addDataManagerAndRegisterBinder
import life.plank.juna.zone.ui.board.adapter.match.binder.*
import life.plank.juna.zone.ui.board.adapter.match.bindingmodel.*

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