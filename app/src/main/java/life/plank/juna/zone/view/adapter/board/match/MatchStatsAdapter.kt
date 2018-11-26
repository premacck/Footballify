package life.plank.juna.zone.view.adapter.board.match

import com.ahamed.multiviewadapter.DataItemManager
import com.ahamed.multiviewadapter.RecyclerAdapter
import com.bumptech.glide.RequestManager
import life.plank.juna.zone.data.model.*
import life.plank.juna.zone.data.model.binder.CommentaryBindingModel
import life.plank.juna.zone.data.model.binder.MatchStatsBindingModel
import life.plank.juna.zone.data.model.binder.StandingsBindingModel
import life.plank.juna.zone.data.model.binder.TeamStatsBindingModel
import life.plank.juna.zone.interfaces.MatchStatsListener
import life.plank.juna.zone.util.AppConstants.MatchTimeVal
import life.plank.juna.zone.util.AppConstants.MatchTimeVal.*
import life.plank.juna.zone.util.DataUtil.validateAndUpdateList
import life.plank.juna.zone.util.DateUtil.getMatchTimeValue
import life.plank.juna.zone.util.facilis.addDataManagerAndRegisterBinder
import life.plank.juna.zone.view.adapter.board.match.binder.CommentaryBinder
import life.plank.juna.zone.view.adapter.board.match.binder.MatchStatsBinder
import life.plank.juna.zone.view.adapter.board.match.binder.StandingsBinder
import life.plank.juna.zone.view.adapter.board.match.binder.TeamStatsBinder


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
        initAndAddCommentaryDataManager()
        initAndAddMatchStatsDataManager()
    }

    private fun preparePostMatchStats() {
        initAndAddStandingsDataManager()
        initAndAddTeamStatsDataManager()
    }
    //endregion

    //region Initializations
    private fun initAndAddCommentaryDataManager() {
        commentaryDataManager = DataItemManager(this, CommentaryBindingModel.from(matchDetails))
//        TODO: modify listener in next pull request
        addDataManagerAndRegisterBinder(commentaryDataManager, CommentaryBinder(null))
    }

    private fun initAndAddMatchStatsDataManager() {
        matchStatsDataManager = DataItemManager(this, MatchStatsBindingModel.from(matchDetails))
        addDataManagerAndRegisterBinder(matchStatsDataManager, MatchStatsBinder(glide))
    }

    private fun initAndAddStandingsDataManager() {
        standingsDataManager = DataItemManager(this, StandingsBindingModel.from(matchDetails))
//        TODO: remove listener in next pull request
        addDataManagerAndRegisterBinder(standingsDataManager, StandingsBinder(null))
    }

    private fun initAndAddTeamStatsDataManager() {
        teamStatsDataManager = DataItemManager(this, TeamStatsBindingModel.from(matchDetails))
        addDataManagerAndRegisterBinder(teamStatsDataManager, TeamStatsBinder(glide))
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
        matchDetails.standingsList = standingsList
        matchDetails.teamStatsList = teamStatsList
        standingsDataManager.setItem(StandingsBindingModel.from(matchDetails))
        teamStatsDataManager.setItem(TeamStatsBindingModel.from(matchDetails))
    }
    //endregion
}