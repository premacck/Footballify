package life.plank.juna.zone.data.api

import android.util.*
import com.google.gson.JsonObject
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.board.Board
import life.plank.juna.zone.data.model.board.poll.Poll
import life.plank.juna.zone.data.model.football.*
import life.plank.juna.zone.sharedpreference.IdToken
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.ui.board.fragment.user.PrivateBoardFragment
import retrofit2.Response
import rx.Observable
import java.net.HttpURLConnection.HTTP_OK
import java.util.*

/**
 * Aggregator for multiple API calls.
 */
object RestApiAggregator {

    /**
     * Method for combining the getBoard() and getMatchDetails() methods of restApi and footballRestApi respectively
     *
     * @return [Pair] containing [Board] and [MatchDetails]
     */
    fun getBoardAndMatchDetails(restApi: RestApi, matchId: Long): Observable<Pair<Board?, MatchDetails?>> {
        return Observable.zip<Response<Board>, Response<MatchDetails>, Pair<Board?, MatchDetails?>>(
                restApi.getBoard(matchId, AppConstants.BOARD_TYPE, IdToken),
                restApi.getMatchDetails(matchId)
        ) { boardResponse, matchDetailsResponse ->
            if (boardResponse.code() != HTTP_OK || matchDetailsResponse.code() != HTTP_OK) {
                Log.e("getBoardAndMatchDetails", "boardResponse : " + boardResponse.code() + " : " + boardResponse.message())
                Log.e("getBoardAndMatchDetails", "matchDetailsResponse : " + matchDetailsResponse.code() + " : " + matchDetailsResponse.message())
            }
            Pair(boardResponse.body(), matchDetailsResponse.body())
        }
    }

    /**
     * Method for combining the getBoard() and getMatchDetails() methods of restApi and footballRestApi respectively
     *
     * @return [MatchDetails] containing [MatchStats] and [Lineups]
     */
    fun getPostMatchBoardData(matchDetails: MatchDetails, restApi: RestApi): Observable<MatchDetails> {
        return Observable.zip<Response<MatchStats>, Response<Lineups>, MatchDetails>(
                restApi.getMatchStatsForMatch(matchDetails.matchId),
                restApi.getLineUpsData(matchDetails.matchId)
        ) { matchStatsResponse, lineupsResponse ->
            if (matchStatsResponse.code() == HTTP_OK) {
                matchDetails.matchStats = matchStatsResponse.body()
            } else {
                Log.e("getPostMatchBoardData", "matchStatsResponse : " + matchStatsResponse.code() + " : " + matchStatsResponse.message())
            }
            if (lineupsResponse.code() == HTTP_OK) {
                matchDetails.lineups = lineupsResponse.body()
            } else {
                Log.e("getPostMatchBoardData", "lineupsResponse : " + lineupsResponse.code() + " : " + lineupsResponse.message())
            }
            matchDetails
        }
    }

    fun getLeagueStats(league: League, restApi: RestApi): Observable<Pair<MutableList<TeamStats>, MutableList<PlayerStats>>> {
        return Observable.zip<Response<MutableList<TeamStats>>, Response<MutableList<PlayerStats>>, Pair<MutableList<TeamStats>, MutableList<PlayerStats>>>(
                restApi.getTeamStats(league.name, league.seasonName!!, league.countryName!!),
                restApi.getPlayerStats(league.name, league.seasonName!!, league.countryName!!)
        ) { teamStatsResponse, playerStatsResponse ->
            if (teamStatsResponse.code() != HTTP_OK) {
                Log.e("getLeagueInfo", "teamStatsResponse : " + teamStatsResponse.code() + " : " + teamStatsResponse.message())
                errorToast(R.string.failed_to_get_team_stats, teamStatsResponse)
            }
            if (playerStatsResponse.code() != HTTP_OK) {
                Log.e("getLeagueInfo", "playerStatsResponse : " + playerStatsResponse.code() + " : " + playerStatsResponse.message())
                errorToast(R.string.failed_to_get_player_stats, teamStatsResponse)
            }
            val teamStats = teamStatsResponse.body() ?: Collections.emptyList()
            val playerStats = playerStatsResponse.body() ?: Collections.emptyList()
            Pair(teamStats, playerStats)
        }
    }

    /**
     * Method to get and Follow the Private board while opening the [PrivateBoardFragment]
     */
    fun getPrivateBoardToOpen(boardId: String, restApi: RestApi): Observable<Board> {
        return Observable.zip<Response<Board>, Response<JsonObject>, Board>(
                restApi.getBoardById(boardId, IdToken),
                restApi.followBoard(boardId, IdToken)
        ) { boardResponse, jsonObjectResponse ->
            if (jsonObjectResponse.code() != HTTP_OK) {
                val error = "FollowBoard: " + jsonObjectResponse.code() + " : " + jsonObjectResponse.message()
                Log.e("getPrivateBoardToOpen", error)
            }
            when (boardResponse.code()) {
                HTTP_OK -> return@zip boardResponse.body()
                else -> {
                    val error = "Error in boardResponse: " + boardResponse.code() + " : " + boardResponse.message()
                    Log.e("getPrivateBoardToOpen", error)
                    boardResponse.body()
                }
            }
        }
    }

    fun getPoll(restApi: RestApi, boardId: String): Observable<Poll> {
        return restApi.getBoardPoll(boardId)
                .flatMap({ pollResponse ->
                    val poll = pollResponse.body()
                    if (pollResponse.code() != HTTP_OK || poll == null) {
                        errorLog("getPoll()", R.string.failed_to_get_poll, pollResponse)
                        return@flatMap restApi.getBoardPollAnswer(0, IdToken)
                    }
                    return@flatMap restApi.getBoardPollAnswer(poll.id, IdToken)
                }, { pollResponse, pollAnswerResponse ->
                    val poll = pollResponse.body()
                    if (pollResponse.code() != HTTP_OK) {
                        errorLog("getPoll()", R.string.failed_to_get_poll, pollResponse)
                        return@flatMap poll
                    }
                    if (pollAnswerResponse.code() != HTTP_OK) {
                        errorLog("getPoll()", R.string.failed_to_get_poll, pollAnswerResponse)
                        return@flatMap poll
                    }
                    val pollAnswer = pollAnswerResponse.body()
                    if (poll != null && pollAnswer != null) {
                        poll.totalVotes = pollAnswer.totalVotes
                        poll.userSelection = pollAnswer.userSelection
                        poll.choices = pollAnswer.choices
                    }
                    return@flatMap poll
                })
    }
}