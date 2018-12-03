package life.plank.juna.zone.data.local.dao

import android.arch.persistence.room.*
import life.plank.juna.zone.data.local.model.LeagueInfo
import life.plank.juna.zone.data.model.FixtureByMatchDay
import life.plank.juna.zone.data.model.PlayerStats
import life.plank.juna.zone.data.model.Standings
import life.plank.juna.zone.data.model.TeamStats

@Dao
interface LeagueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLeagueInfo(leagueInfo: LeagueInfo)

    @Query("SELECT * FROM LeagueInfo WHERE league_id LIKE :leagueId LIMIT 1")
    fun getLeague(leagueId: Long): LeagueInfo?

    @Query("UPDATE LeagueInfo SET fixtureByMatchDayList = :fixtureByMatchDayList where league_id = :leagueId")
    fun updateFixtures(fixtureByMatchDayList: List<FixtureByMatchDay>, leagueId: Long)

    @Query("UPDATE LeagueInfo SET standingsList = :standingsList where league_id = :leagueId")
    fun updateStandings(standingsList: List<Standings>, leagueId: Long)

    @Query("UPDATE LeagueInfo SET teamStatsList = :teamStatsList where league_id = :leagueId")
    fun updateTeamStats(teamStatsList: List<TeamStats>, leagueId: Long)

    @Query("UPDATE LeagueInfo SET playerStatsList = :playerStatsList where league_id = :leagueId")
    fun updatePlayerStats(playerStatsList: List<PlayerStats>, leagueId: Long)

    @Delete
    fun deleteLeagueInfo(leagueInfo: LeagueInfo)
}