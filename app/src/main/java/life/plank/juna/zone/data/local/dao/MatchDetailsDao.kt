package life.plank.juna.zone.data.local.dao

import androidx.room.*
import life.plank.juna.zone.data.model.*

@Dao
interface MatchDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMatchDetail(matchDetails: MatchDetails)

    @Query("SELECT * FROM MatchDetails where matchId LIKE :matchId LIMIT 1")
    fun getMatchDetails(matchId: Long): MatchDetails

    @Query("UPDATE MatchDetails SET timeStatus = :timeStatus WHERE matchId LIKE :matchId")
    fun updateTimeStatus(timeStatus: String, matchId: Long)

    @Query("UPDATE MatchDetails SET homeGoals = :homeGoals WHERE matchId LIKE :matchId")
    fun updateHomeGoals(homeGoals: Int, matchId: Long)

    @Query("UPDATE MatchDetails SET awayGoals = :awayGoals WHERE matchId LIKE :matchId")
    fun updateAwayGoals(awayGoals: Int, matchId: Long)

    @Query("UPDATE MatchDetails SET homeTeamPenaltyScore = :homeTeamPenaltyScore WHERE matchId LIKE :matchId")
    fun updateHomeTeamPenaltyScore(homeTeamPenaltyScore: Int, matchId: Long)

    @Query("UPDATE MatchDetails SET awayTeamPenaltyScore = :awayTeamPenaltyScore WHERE matchId LIKE :matchId")
    fun updateAwayTeamPenaltyScore(awayTeamPenaltyScore: Int, matchId: Long)

    @Query("UPDATE MatchDetails SET highlights = :highlights WHERE matchId LIKE :matchId")
    fun updateHighlights(highlights: List<Highlights>, matchId: Long)

    @Query("UPDATE MatchDetails SET matchEvents = :matchEvents WHERE matchId LIKE :matchId")
    fun updateMatchEvents(matchEvents: List<MatchEvent>, matchId: Long)

    @Query("UPDATE MatchDetails SET commentary = :commentaries WHERE matchId LIKE :matchId")
    fun updateCommentaries(commentaries: List<Commentary>, matchId: Long)

    @Query("UPDATE MatchDetails SET standingsList = :standingsList WHERE matchId LIKE :matchId")
    fun update(standingsList: List<Standings>, matchId: Long)

    @Query("UPDATE MatchDetails SET teamStatsList = :teamStatsList WHERE matchId LIKE :matchId")
    fun updateTeamStats(teamStatsList: List<TeamStats>, matchId: Long)

    @Query("UPDATE MatchDetails SET scrubberDataList = :scrubberDataList WHERE matchId LIKE :matchId")
    fun updateScrubberData(scrubberDataList: List<ScrubberData>, matchId: Long)

    @Query("UPDATE MatchDetails SET lineups_homeTeamFormation = :homeTeamFormation, lineups_awayTeamFormation = :awayTeamFormation WHERE matchId LIKE :matchId")
    fun updateLineups(homeTeamFormation: List<FormationList>, awayTeamFormation: List<FormationList>, matchId: Long)

    @Query("UPDATE MatchDetails SET matchStats_homeShots = :homeShots WHERE matchId LIKE :matchId")
    fun updateMatchStatsHomeShots(homeShots: Int, matchId: Long)

    @Query("UPDATE MatchDetails SET matchStats_homeShotsOnTarget = :homeShotsOnTarget WHERE matchId LIKE :matchId")
    fun updateMatchStatsHomeShotsOnTarget(homeShotsOnTarget: Int, matchId: Long)

    @Query("UPDATE MatchDetails SET matchStats_homePossession = :homePossession WHERE matchId LIKE :matchId")
    fun updateMatchStatsHomePossession(homePossession: Int, matchId: Long)

    @Query("UPDATE MatchDetails SET matchStats_homeFouls = :homeFouls WHERE matchId LIKE :matchId")
    fun updateMatchStatsHomeFouls(homeFouls: Int, matchId: Long)

    @Query("UPDATE MatchDetails SET matchStats_homeRedCards = :homeRedCards WHERE matchId LIKE :matchId")
    fun updateMatchStatsHomeRedCards(homeRedCards: Int, matchId: Long)

    @Query("UPDATE MatchDetails SET matchStats_homeYellowCards = :homeYellowCards WHERE matchId LIKE :matchId")
    fun updateMatchStatsHomeYellowCards(homeYellowCards: Int, matchId: Long)

    @Query("UPDATE MatchDetails SET matchStats_homeOffsides = :homeOffsides WHERE matchId LIKE :matchId")
    fun updateMatchStatsHomeOffsides(homeOffsides: Int, matchId: Long)

    @Query("UPDATE MatchDetails SET matchStats_homeCorners = :homeCorners WHERE matchId LIKE :matchId")
    fun updateMatchStatsHomeCorners(homeCorners: Int, matchId: Long)

    @Query("UPDATE MatchDetails SET matchStats_awayShots = :awayShots WHERE matchId LIKE :matchId")
    fun updateMatchStatsAwayShots(awayShots: Int, matchId: Long)

    @Query("UPDATE MatchDetails SET matchStats_awayShotsOnTarget = :awayShotsOnTarget WHERE matchId LIKE :matchId")
    fun updateMatchStatsAwayShotsOnTarget(awayShotsOnTarget: Int, matchId: Long)

    @Query("UPDATE MatchDetails SET matchStats_awayPossession = :awayPossession WHERE matchId LIKE :matchId")
    fun updateMatchStatsAwayPossession(awayPossession: Int, matchId: Long)

    @Query("UPDATE MatchDetails SET matchStats_awayFouls = :awayFouls WHERE matchId LIKE :matchId")
    fun updateMatchStatsAwayFouls(awayFouls: Int, matchId: Long)

    @Query("UPDATE MatchDetails SET matchStats_awayRedCards = :awayRedCards WHERE matchId LIKE :matchId")
    fun updateMatchStatsAwayRedCards(awayRedCards: Int, matchId: Long)

    @Query("UPDATE MatchDetails SET matchStats_awayYellowCards = :awayYellowCards WHERE matchId LIKE :matchId")
    fun updateMatchStatsAwayYellowCards(awayYellowCards: Int,matchId: Long)

    @Query("UPDATE MatchDetails SET matchStats_awayOffsides = :awayOffsides WHERE matchId LIKE :matchId")
    fun updateMatchStatsAwayOffsides(awayOffsides: Int, matchId: Long)

    @Query("UPDATE MatchDetails SET matchStats_awayCorners = :awayCorners WHERE matchId LIKE :matchId")
    fun updateMatchStatsAwayCorners(awayCorners: Int, matchId: Long)

    @Delete
    fun deleteMatchDetails(matchDetails: MatchDetails)
}