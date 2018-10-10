package life.plank.juna.zone.data.local.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

import life.plank.juna.zone.data.local.model.LeagueInfo
import life.plank.juna.zone.data.model.PlayerStats
import life.plank.juna.zone.data.model.Standings
import life.plank.juna.zone.data.model.TeamStats

interface LeagueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLeagueInfo(leagueInfo: LeagueInfo)

    @Query("SELECT * FROM LeagueInfo WHERE league_id LIKE :leagueId LIMIT 1")
    fun getLeague(leagueId: Long): LiveData<LeagueInfo>

    @Query("SELECT standingsList FROM LeagueInfo where league_id = :leagueId")
    fun getStandings(leagueId: Long): LiveData<List<Standings>>

    @Query("SELECT teamStatsList FROM LeagueInfo where league_id = :leagueId")
    fun getTeamStats(leagueId: Long): LiveData<List<TeamStats>>

    @Query("SELECT playerStatsList FROM LeagueInfo where league_id = :leagueId")
    fun getPlayerStats(leagueId: Long): LiveData<List<PlayerStats>>

    @Delete
    fun deleteLeagueInfo(leagueInfo: LeagueInfo)
}