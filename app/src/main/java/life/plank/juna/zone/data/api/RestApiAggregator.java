package life.plank.juna.zone.data.api;

import android.util.Log;
import android.util.Pair;

import java.util.List;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.local.model.LeagueInfo;
import life.plank.juna.zone.data.model.board.Board;
import life.plank.juna.zone.data.model.board.poll.Poll;
import life.plank.juna.zone.data.model.board.poll.PollAnswerResponse;
import life.plank.juna.zone.data.model.football.FixtureByMatchDay;
import life.plank.juna.zone.data.model.football.League;
import life.plank.juna.zone.data.model.football.Lineups;
import life.plank.juna.zone.data.model.football.MatchDetails;
import life.plank.juna.zone.data.model.football.MatchFixture;
import life.plank.juna.zone.data.model.football.MatchFixtureKt;
import life.plank.juna.zone.data.model.football.MatchStats;
import life.plank.juna.zone.data.model.football.PlayerStats;
import life.plank.juna.zone.data.model.football.Standings;
import life.plank.juna.zone.data.model.football.TeamStats;
import life.plank.juna.zone.util.common.AppConstants;
import life.plank.juna.zone.view.board.fragment.user.PrivateBoardFragment;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.prembros.facilis.util.DataUtilKt.isNullOrEmpty;
import static java.net.HttpURLConnection.HTTP_OK;
import static life.plank.juna.zone.sharedpreference.AuthPrefsKt.getIdToken;
import static life.plank.juna.zone.util.common.ToastUtilKt.errorLog;

// todo: delete unused methods //
/**
 * Class for Aggregating multiple API calls.
 */
public class RestApiAggregator {

    /**
     * Method for combining the getBoard() and getMatchDetails() methods of restApi and footballRestApi respectively
     *
     * @return {@link Pair} containing {@link Board} and {@link MatchDetails}
     */
    public static Observable<Pair<Board, MatchDetails>> getBoardAndMatchDetails(RestApi restApi, long matchId) {
        return Observable.zip(
                restApi.getBoard(matchId, AppConstants.BOARD_TYPE, getIdToken()),
                restApi.getMatchDetails(matchId),
                ((boardResponse, matchDetailsResponse) -> {
                    if (boardResponse.code() != HTTP_OK || matchDetailsResponse.code() != HTTP_OK) {
                        Log.e("getBoardAndMatchDetails", "boardResponse : " + boardResponse.code() + " : " + boardResponse.message());
                        Log.e("getBoardAndMatchDetails", "matchDetailsResponse : " + matchDetailsResponse.code() + " : " + matchDetailsResponse.message());
                    }
                    return new Pair<>(boardResponse.body(), matchDetailsResponse.body());
                }));
    }

    /**
     * Method for combining the getBoard() and getMatchDetails() methods of restApi and footballRestApi respectively
     *
     * @return {@link MatchDetails} containing {@link MatchStats} and {@link Lineups}
     */
    public static Observable<MatchDetails> getPostMatchBoardData(MatchDetails matchDetails, RestApi restApi) {
        return Observable.zip(
                restApi.getMatchStatsForMatch(matchDetails.getMatchId()),
                restApi.getLineUpsData(matchDetails.getMatchId()),
                ((matchStatsResponse, lineupsResponse) -> {
                    if (matchStatsResponse.code() == HTTP_OK) {
                        matchDetails.setMatchStats(matchStatsResponse.body());
                    } else {
                        Log.e("getPostMatchBoardData", "matchStatsResponse : " + matchStatsResponse.code() + " : " + matchStatsResponse.message());
                    }
                    if (lineupsResponse.code() == HTTP_OK) {
                        matchDetails.setLineups(lineupsResponse.body());
                    } else {
                        Log.e("getPostMatchBoardData", "lineupsResponse : " + lineupsResponse.code() + " : " + lineupsResponse.message());
                    }
                    return matchDetails;
                }));
    }

    /**
     * Method for combining the getBoard() and getMatchDetails() methods of restApi and footballRestApi respectively
     *
     * @return {@link MatchDetails} containing {@link List<Standings>} and {@link List<TeamStats>}
     */
    public static Observable<MatchDetails> getPreMatchBoardData(MatchDetails matchDetails, RestApi restApi) {
        return afterSubscribingAndObservingOn(
                Observable.zip(
                        restApi.getMatchStandingsForMatch(matchDetails.getMatchId()),
                        restApi.getTeamStatsForMatch(matchDetails.getMatchId()),
                        restApi.getLineUpsData(matchDetails.getMatchId()),
                        ((standingsResponse, teamStatsResponse, lineupsResponse) -> {
                            if (standingsResponse.code() == HTTP_OK) {
                                matchDetails.setStandingsList(standingsResponse.body());
                            } else {
                                Log.e("getPreMatchBoardData", "standingsResponse : " + standingsResponse.code() + " : " + standingsResponse.message());
                            }
                            if (teamStatsResponse.code() == HTTP_OK) {
                                matchDetails.setTeamStatsList(teamStatsResponse.body());
                            } else {
                                Log.e("getPreMatchBoardData", "teamStatsResponse : " + teamStatsResponse.code() + " : " + teamStatsResponse.message());
                            }
                            if (lineupsResponse.code() == HTTP_OK) {
                                matchDetails.setLineups(lineupsResponse.body());
                            } else {
                                Log.e("getPreMatchBoardData", "lineupsResponse : " + lineupsResponse.code() + " : " + lineupsResponse.message());
                            }
                            return matchDetails;
                        })));
    }

    public static Observable<LeagueInfo> getLeagueInfo(League league, RestApi restApi) {
        return afterSubscribingAndObservingOn(
                Observable.zip(
                        restApi.getFixtures(league.getSeasonName(), league.getName(), league.getCountryName()),
                        restApi.getStandings(league.getName(), league.getSeasonName(), league.getCountryName()),
                        restApi.getTeamStats(league.getName(), league.getSeasonName(), league.getCountryName()),
                        restApi.getPlayerStats(league.getName(), league.getSeasonName(), league.getCountryName()),
                        ((fixtureResponse, standingsResponse, teamStatsResponse, playerStatsResponse) -> {
                            LeagueInfo leagueInfo = new LeagueInfo();
                            leagueInfo.setId(league.getId());
                            leagueInfo.setLeague(league);
                            if (fixtureResponse.code() == HTTP_OK) {
                                List<MatchFixture> matchFixtureList = fixtureResponse.body();
                                if (matchFixtureList != null) {
                                    List<FixtureByMatchDay> fixtureByMatchDayList = MatchFixtureKt.convertToFixtureByMatchDayList(matchFixtureList);
                                    if (!isNullOrEmpty(fixtureByMatchDayList)) {
                                        leagueInfo.setFixtureByMatchDayList(fixtureByMatchDayList);
                                    }
                                }
                            } else {
                                Log.e("getLeagueInfo", "fixtureResponse : " + fixtureResponse.code() + " : " + fixtureResponse.message());
                            }
                            if (standingsResponse.code() == HTTP_OK) {
                                List<Standings> standingsList = standingsResponse.body();
                                if (!isNullOrEmpty(standingsList)) {
                                    leagueInfo.setStandingsList(standingsList);
                                }
                            } else {
                                Log.e("getLeagueInfo", "standingsResponse : " + standingsResponse.code() + " : " + standingsResponse.message());
                            }
                            if (teamStatsResponse.code() == HTTP_OK) {
                                List<TeamStats> teamStatsList = teamStatsResponse.body();
                                if (!isNullOrEmpty(teamStatsList)) {
                                    leagueInfo.setTeamStatsList(teamStatsList);
                                }
                            } else {
                                Log.e("getLeagueInfo", "teamStatsResponse : " + teamStatsResponse.code() + " : " + teamStatsResponse.message());
                            }
                            if (playerStatsResponse.code() == HTTP_OK) {
                                List<PlayerStats> playerStatsList = playerStatsResponse.body();
                                if (!isNullOrEmpty(playerStatsList)) {
                                    leagueInfo.setPlayerStatsList(playerStatsList);
                                }
                            } else {
                                Log.e("getLeagueInfo", "playerStatsResponse : " + playerStatsResponse.code() + " : " + playerStatsResponse.message());
                            }
                            return leagueInfo;
                        })
                )
        );
    }

    public static Observable<Pair<List<TeamStats>, List<PlayerStats>>> getLeagueStats(League league, RestApi restApi) {
        return afterSubscribingAndObservingOn(
                Observable.zip(
                        restApi.getTeamStats(league.getName(), league.getSeasonName(), league.getCountryName()),
                        restApi.getPlayerStats(league.getName(), league.getSeasonName(), league.getCountryName()),
                        (teamStatsResponse, playerStatsResponse) -> {
                            if (teamStatsResponse.code() != HTTP_OK) {
                                Log.e("getLeagueInfo", "teamStatsResponse : " + teamStatsResponse.code() + " : " + teamStatsResponse.message());
                                life.plank.juna.zone.util.common.ToastUtilKt.errorToast(R.string.failed_to_get_team_stats, teamStatsResponse);
                            }
                            if (playerStatsResponse.code() != HTTP_OK) {
                                Log.e("getLeagueInfo", "playerStatsResponse : " + playerStatsResponse.code() + " : " + playerStatsResponse.message());
                                life.plank.juna.zone.util.common.ToastUtilKt.errorToast(R.string.failed_to_get_player_stats, teamStatsResponse);
                            }
                            return new Pair<>(teamStatsResponse.body(), playerStatsResponse.body());
                        }
                )
        );
    }

    /**
     * Method to get and Follow the Private board while opening the {@link PrivateBoardFragment}
     */
    public static Observable<Board> getPrivateBoardToOpen(String boardId, RestApi restApi) {
        return Observable.zip(
                restApi.getBoardById(boardId, getIdToken()),
                restApi.followBoard(boardId, getIdToken()),
                ((boardResponse, jsonObjectResponse) -> {
                    if (jsonObjectResponse.code() != HTTP_OK) {
                        String error = "FollowBoard: " + jsonObjectResponse.code() + " : " + jsonObjectResponse.message();
                        Log.e("getPrivateBoardToOpen", error);
                    }
                    switch (boardResponse.code()) {
                        case HTTP_OK:
                            return boardResponse.body();
                        default:
                            String error = "Error in boardResponse: " + boardResponse.code() + " : " + boardResponse.message();
                            Log.e("getPrivateBoardToOpen", error);
                            return boardResponse.body();
                    }
                })
        );
    }

    public static Observable<Poll> getPoll(RestApi restApi, String boardId) {
        return restApi.getBoardPoll(boardId, getIdToken())
                .flatMap(pollResponse -> {
                    Poll poll = pollResponse.body();
                    if (pollResponse.code() != HTTP_OK || poll == null) {
                        errorLog("getPoll()", R.string.failed_to_get_poll, pollResponse);
                        return restApi.getBoardPollAnswer(0, getIdToken());
                    }
                    return restApi.getBoardPollAnswer(poll.getId(), getIdToken());
                }, (pollResponse, pollAnswerResponse) -> {
                    Poll poll = pollResponse.body();
                    if (pollResponse.code() != HTTP_OK) {
                        errorLog("getPoll()", R.string.failed_to_get_poll, pollResponse);
                        return poll;
                    }
                    if (pollAnswerResponse.code() != HTTP_OK) {
                        errorLog("getPoll()", R.string.failed_to_get_poll, pollAnswerResponse);
                        return poll;
                    }
                    PollAnswerResponse pollAnswer = pollAnswerResponse.body();
                    if (poll != null && pollAnswer != null) {
                        poll.setTotalVotes(pollAnswer.getTotalVotes());
                        poll.setUserSelection(pollAnswer.getUserSelection());
                        poll.setChoices(pollAnswer.getChoices());
                    }
                    return poll;
                });
    }

    /**
     * Common utility method for subscribing on {@code Schedulers.io()} and observing on {@code AndroidSchedulers.mainThread()}
     */
    private static <T> Observable<T> afterSubscribingAndObservingOn(Observable<T> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}