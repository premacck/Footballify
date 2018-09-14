package life.plank.juna.zone.data;

import android.util.Pair;

import java.util.List;

import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.Board;
import life.plank.juna.zone.data.network.model.Lineups;
import life.plank.juna.zone.data.network.model.MatchDetails;
import life.plank.juna.zone.data.network.model.MatchStats;
import life.plank.juna.zone.data.network.model.StandingModel;
import life.plank.juna.zone.data.network.model.TeamStatsModel;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Class for Aggregating multiple API calls.
 */
public class RestApiAggregator {

    /**
     * Method for combining the retrieveBoard() and getMatchDetails() methods of restApi and footballRestApi respectively
     *
     * @return {@link Pair} containing {@link Board} and {@link MatchDetails}
     */
    public static Observable<Pair<Board, MatchDetails>> getBoardAndMatchDetails(RestApi restApi, RestApi footballRestApi, long matchId) {
//        TODO : un-comment this after updating MatchFixture class
        /*return afterSubscribingAndObservingOn(
                Observable.zip(
                        restApi.retrieveBoard(matchId, AppConstants.BOARD_TYPE, getToken(getContext())),
                        footballRestApi.getMatchDetails(matchId),
                        ((boardResponse, matchDetailsResponse) -> {
                            if (boardResponse.code() == HTTP_OK && matchDetailsResponse.code() == HTTP_OK) {
                                return new Pair<>(boardResponse.body(), matchDetailsResponse.body());
                            } else
                                return null;
                        })));*/
        return null;
    }

    /**
     * Method for combining the retrieveBoard() and getMatchDetails() methods of restApi and footballRestApi respectively
     *
     * @return {@link MatchDetails} containing {@link MatchStats} and {@link Lineups}
     */
    public static Observable<MatchDetails> getPostMatchBoardData(MatchDetails matchDetails, RestApi restApi, long matchId) {
        return afterSubscribingAndObservingOn(
                Observable.zip(
                        restApi.getMatchStatsForMatch(matchId),
                        restApi.getLineUpsData(matchId),
                        (((matchStatsResponse, lineupsResponse) -> {
                            if (matchStatsResponse.code() == HTTP_OK && lineupsResponse.code() == HTTP_OK) {
                                matchDetails.setMatchStats(matchStatsResponse.body());
                                matchDetails.setLineups(lineupsResponse.body());
                                return matchDetails;
                            } else
                                return null;
                        }))));
    }

    /**
     * Method for combining the retrieveBoard() and getMatchDetails() methods of restApi and footballRestApi respectively
     *
     * @return {@link MatchDetails} containing {@link List<StandingModel>} and {@link List<TeamStatsModel>}
     */
    public static Observable<MatchDetails> getPreMatchBoardData(MatchDetails matchDetails, RestApi restApi, long matchId) {
        return afterSubscribingAndObservingOn(
                Observable.zip(
                        restApi.getMatchStandingsForMatch(matchId),
                        restApi.getTeamStatsForMatch(matchId),
                        (((standingsResponse, teamStatsResponse) -> {
                            if (standingsResponse.code() == HTTP_OK && teamStatsResponse.code() == HTTP_OK) {
                                matchDetails.setStandingsList(standingsResponse.body());
                                matchDetails.setTeamStatsList(teamStatsResponse.body());
                                return matchDetails;
                            } else
                                return null;
                        }))));
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