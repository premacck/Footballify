package life.plank.juna.zone.data.network.interfaces;

import com.google.gson.JsonObject;

import java.util.List;

import life.plank.juna.zone.data.network.model.Board;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.data.network.model.LineupsModel;
import life.plank.juna.zone.data.network.model.MatchSummaryModel;
import life.plank.juna.zone.data.network.model.PlayerStatsModel;
import life.plank.juna.zone.data.network.model.ScoreFixtureModel;
import life.plank.juna.zone.data.network.model.SignInModel;
import life.plank.juna.zone.data.network.model.SignUpModel;
import life.plank.juna.zone.data.network.model.StandingModel;
import life.plank.juna.zone.data.network.model.TeamStatsModel;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by plank-dhamini on 6/8/2017.
 * This class consists of all the rest api calls for zone
 */

public interface RestApi {

    //working
    @GET("seasons/standings")
    Observable<Response<List<StandingModel>>> getStandings(@Query("leagueName") String leagueName,
                                                           @Query("seasonName") String seasonName,
                                                           @Query("countryName") String countryName);

    //working
    @GET("seasons/playerstats")
    Observable<Response<List<PlayerStatsModel>>> getPlayerStats(@Query("leagueName") String leagueName,
                                                                @Query("seasonName") String seasonName,
                                                                @Query("countryName") String countryName);

    //working
    @GET("teams/stats")
    Observable<Response<List<TeamStatsModel>>> getTeamStats(@Query("leagueName") String leagueName,
                                                            @Query("seasonName") String seasonName,
                                                            @Query("countryName") String countryName);


    //working
    @GET("/boards")
    Observable<Response<Board>> retrieveBoard(@Query("foreignId") Long foreignId, @Query("boardType") String boardType, @Header("Authorization") String authHeader);

    //working
    @GET("/boards/feedItems")
    Observable<Response<List<FootballFeed>>> retrieveByBoardId(@Query("id") String boardId, @Header("Authorization") String authHeader);

    //working
    @GET("/users")
    Observable<Response<SignInModel>> getUser(@Header("Authorization") String authHeader);

    //working
    @Multipart
    @POST("/activities/upload")
    Observable<Response<JsonObject>> postMediaContentToServer(@Part MultipartBody.Part file, @Query("targetId")
            String targetId, @Query("contentType") String contentType,
                                                              @Query("userId") String userId,
                                                              @Query("dateCreated") String dateCreated,
                                                              @Query("feedType") String feedType,
                                                              @Header("Authorization") String authHeader);

    //working
    @POST("boards/{id}/feedItems")
    Observable<Response<JsonObject>> postCommentOnBoardFeed(@Body String getEditTextValue,
                                                            @Path("id") String boardId,
                                                            @Query("contentType") String contentType,
                                                            @Query("userId") String userId,
                                                            @Query("dateCreated") String dateCreated,
                                                            @Header("Authorization") String authHeader);

    //working
    @POST("/boards")
    Observable<Response<String>> createPrivateBoard(@Query("boardType") String boardType, @Body Board privateBoard,
                                                        @Header("Authorization") String authHeader);

    //working
    @GET("seasons/matches")
    Observable<Response<List<ScoreFixtureModel>>> getScoresAndFixtures(@Query("seasonName") String seasonName,
                                                                       @Query("leagueName") String leagueName,
                                                                       @Query("countryName") String countryName);

    //yet to verify
    @GET("api/feeditems")
    Observable<Response<List<FootballFeed>>> getFootballFeed(@Header("newsfeed-continuation-token") String header);

    @GET("matches/{matchId}/lineups")
    Observable<Response<LineupsModel>> getLineUpsData(@Path("matchId") long matchId);

    @POST("/ausers")
    Observable<Response<SignUpModel>> createUser(@Body SignUpModel signUpModel);

    @POST("feedItems/{id}/likes")
    Observable<Response<FootballFeed>> getLikedFeedItem(@Path("id") String id, @Query("userId") String userId);

    @GET("matches/{matchId}/matchsummary")
    Observable<Response<MatchSummaryModel>> getMatchSummary(@Path("matchId") long matchId);


    @POST("feedItems/{id}/shares")
    Observable<Response<FootballFeed>> shareBoardFeedItem(@Path("id") String id, @Query("shareTo") String shareTo, @Query("boardId") String boardId, @Query("userId") String userId);

    @DELETE("feedItems/{id}/likes")
    Observable<Response<JsonObject>> unlikeBoardItem(@Path("id") String id, @Query("userId") String userId);

    @POST("feedItems/{id}/comments")
    Observable<Response<JsonObject>> postCommentOnFeeditem(@Body String getEditTextValue, @Path("id") String feedItemId, @Query("userId") String userId, @Query("boardId") String boardId, @Query("time") String time);
}

