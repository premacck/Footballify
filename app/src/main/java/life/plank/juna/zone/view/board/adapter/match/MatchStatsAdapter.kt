package life.plank.juna.zone.view.board.adapter.match

import com.ahamed.multiviewadapter.*
import com.bumptech.glide.RequestManager
import life.plank.juna.zone.data.model.football.*
import life.plank.juna.zone.service.CommonDataService.validateAndUpdateList
import life.plank.juna.zone.util.common.AppConstants.MatchTimeVal
import life.plank.juna.zone.util.common.AppConstants.MatchTimeVal.*
import life.plank.juna.zone.util.time.DateUtil.getMatchTimeValue
import life.plank.juna.zone.view.base.addDataManagerAndRegisterBinder
import life.plank.juna.zone.view.board.adapter.match.binder.*
import life.plank.juna.zone.view.board.adapter.match.bindingmodel.*
import life.plank.juna.zone.view.football.MatchStatsListener


class MatchStatsAdapter(private val matchDetails: MatchDetails, private val glide: RequestManager, private val listener: MatchStatsListener) : RecyclerAdapter() {

    private lateinit var commentaryDataManager: DataItemManager<CommentaryBindingModel>
    private lateinit var matchStatsDataManager: DataItemManager<MatchStatsBindingModel>
    private lateinit var standingsDataManager: DataItemManager<StandingsBindingModel>
    private lateinit var teamStatsDataManager: DataItemManager<TeamStatsBindingModel>

    init {
        @MatchTimeVal val matchTimeValue = getMatchTimeValue(matchDetails.matchStartTime, true)
        when (matchTimeValue) {
            MATCH_PAST, MATCH_COMPLETED_TODAY, MATCH_LIVE -> preparePostMatchStats()
            MATCH_ABOUT_TO_START, MATCH_ABOUT_TO_START_BOARD_ACTIVE, MATCH_SCHEDULED_TODAY, MATCH_SCHEDULED_LATER -> preparePreMatchStats()
        }
    }

    //region Preparations (choosing what to display)
    private fun preparePreMatchStats() {
        initAndAddStandingsDataManager()
        initAndAddTeamStatsDataManager()
        addScheduledMatchFooter()
    }

    private fun preparePostMatchStats() {
        initAndAddCommentaryDataManager()
        initAndAddMatchStatsDataManager()
    }
    //endregion

    //region Initializations
    private fun initAndAddCommentaryDataManager() {
        commentaryDataManager = DataItemManager(this, CommentaryBindingModel.from(matchDetails))
//        TODO: modify listener in next pull request
        addDataManagerAndRegisterBinder(commentaryDataManager, CommentaryBinder(listener))
    }

    private fun initAndAddMatchStatsDataManager() {
        matchStatsDataManager = DataItemManager(this, MatchStatsBindingModel.from(matchDetails))
        addDataManagerAndRegisterBinder(matchStatsDataManager, MatchStatsBinder(glide))
    }

    private fun initAndAddStandingsDataManager() {
        standingsDataManager = DataItemManager(this, StandingsBindingModel.from(matchDetails))
//        TODO: remove listener in next pull request
        addDataManagerAndRegisterBinder(standingsDataManager, StandingsBinder())
    }

    private fun initAndAddTeamStatsDataManager() {
        teamStatsDataManager = DataItemManager(this, TeamStatsBindingModel.from(matchDetails))
        addDataManagerAndRegisterBinder(teamStatsDataManager, TeamStatsBinder(glide))
    }

    private fun addScheduledMatchFooter() {
        addDataManagerAndRegisterBinder(DataItemManager(this, ""), ScheduledMatchFooterBinder())
    }
    //endregion

    //region Updates
    fun updateCommentaries(commentaryList: List<Commentary>, isError: Boolean) {
        validateAndUpdateList(matchDetails.commentary, commentaryList, isError)
        commentaryDataManager.setItem(CommentaryBindingModel.from(matchDetails))
    }

    fun setMatchStats() {
        matchStatsDataManager.setItem(MatchStatsBindingModel.from(matchDetails))
    }

    fun updateMatchStats(matchStats: MatchStats?, message: Int) {
        if (matchStats != null) {
            matchDetails.matchStats = matchStats
        }
        matchDetails.matchStats?.errorMessage = message
        setMatchStats()
    }

    fun setPreMatchData(standingsList: List<Standings>, teamStatsList: List<TeamStats>) {
        matchDetails.standingsList = standingsList as MutableList<Standings>
        matchDetails.teamStatsList = teamStatsList as MutableList<TeamStats>
        standingsDataManager.setItem(StandingsBindingModel.from(matchDetails))
        teamStatsDataManager.setItem(TeamStatsBindingModel.from(matchDetails))
    }
    //endregion
}