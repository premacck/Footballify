package life.plank.juna.zone.ui.base.fragment

import android.os.Bundle
import androidx.lifecycle.*
import life.plank.juna.zone.data.local.model.LeagueInfo
import life.plank.juna.zone.data.model.football.*
import life.plank.juna.zone.data.viewmodel.LeagueViewModel

abstract class BaseBoardFragment : BaseJunaFragment() {

    private lateinit var leagueViewModel: LeagueViewModel
//    TODO : Change team names to team ID once the change is done in backend
    private lateinit var homeTeamName: String
    private lateinit var visitingTeamName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        leagueViewModel = ViewModelProviders.of(this).get(LeagueViewModel::class.java)
    }

    //    TODO : Change team names to team ID once the change is done in backend
    protected fun getPreMatchData(league: League, homeTeamName: String, visitingTeamName: String) {
        this.homeTeamName = homeTeamName
        this.visitingTeamName = visitingTeamName
        leagueViewModel.leagueInfoLiveData.observe(this, Observer<LeagueInfo>(this::parsePreMatchData))
        leagueViewModel.getLeagueInfoFromDb(league.id)
    }

    private fun parsePreMatchData(leagueInfo: LeagueInfo?) {
        val standingList: MutableList<Standings> = ArrayList()
        val teamStatsList: MutableList<TeamStats> = ArrayList()

        if (leagueInfo != null) {
            leagueInfo.standingsList.forEach { standings ->
                if (standings.teamName == homeTeamName || standings.teamName == visitingTeamName) {
                    standingList.add(standings)
                }
            }
            leagueInfo.teamStatsList.forEach { teamStats ->
                if (teamStats.teamName == homeTeamName || teamStats.teamName == visitingTeamName) {
                    teamStatsList.add(teamStats)
                }
            }
        }

        when (leagueInfo) {
            null -> handlePreMatchData(null)
            else -> handlePreMatchData(standingList to teamStatsList)
        }
    }

    abstract fun handlePreMatchData(pair: Pair<List<Standings>, List<TeamStats>>?)
}