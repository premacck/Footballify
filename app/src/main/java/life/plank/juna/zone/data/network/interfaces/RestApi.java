package life.plank.juna.zone.data.network.interfaces;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Set;

import life.plank.juna.zone.data.network.model.Board;
import life.plank.juna.zone.data.network.model.Commentary;
import life.plank.juna.zone.data.network.model.FixtureByMatchDay;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.data.network.model.Highlights;
import life.plank.juna.zone.data.network.model.Lineups;
import life.plank.juna.zone.data.network.model.LiveTimeStatus;
import life.plank.juna.zone.data.network.model.MatchEvent;
import life.plank.juna.zone.data.network.model.MatchFixture;
import life.plank.juna.zone.data.network.model.MatchTeamStats;
import life.plank.juna.zone.data.network.model.PlayerStatsModel;
import life.plank.juna.zone.data.network.model.SignUpModel;
import life.plank.juna.zone.data.network.model.StandingModel;
import life.plank.juna.zone.data.network.model.TeamStatsModel;
import life.plank.juna.zone.data.network.model.User;
import life.plank.juna.zone.data.network.model.UserFeed;
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
    @GET("/boards/createdBy")
    Observable<Response<List<Board>>> getUserBoards(@Header("Authorization") String authHeader);

    //working
    @POST("/boards/{id}/invite")
    Observable<Response<JsonObject>> inviteUserToJoinBoard(@Body Set<User> user, @Path("id") String boardId, @Header("Authorization") String authHeader);

    //working
    @GET("/boards/feedItems")
    Observable<Response<List<FootballFeed>>> retrieveByBoardId(@Query("id") String boardId, @Header("Authorization") String authHeader);

    //working
    @GET("/users")
    Observable<Response<User>> getUser(@Header("Authorization") String authHeader);

    //working
    @Multipart
    @POST("/activities/upload")
    Observable<Response<JsonObject>> postMediaContentToServer(@Part MultipartBody.Part file,
                                                              @Query("targetId") String targetId,
                                                              @Query("contentType") String contentType,
                                                              @Query("userId") String userId,
                                                              @Query("dateCreated") String dateCreated,
                                                              @Query("feedType") String feedType,
                                                              @Query("description") String description,
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
    Observable<Response<List<FixtureByMatchDay>>> getFixtures(@Query("seasonName") String seasonName,
                                                              @Query("leagueName") String leagueName,
                                                              @Query("countryName") String countryName);

    //working
    @GET("/feedEntries")
    Observable<Response<List<UserFeed>>> getUserFeed(@Header("Authorization") String authHeader);

    //working
    @GET("boards/{id}")
    Observable<Response<Board>> getBoardById(@Path("id") String boardId, @Header("Authorization") String authHeader);

    //working
    @POST("activities/{id}/likes")
    Observable<Response<JsonObject>> postLike(@Path("id") String feedItemId,
                                              @Query("targetId") String boardId,
                                              @Query("target") String target,
                                              @Query("time") String dateCreated,
                                              @Header("Authorization") String authHeader);

    @DELETE("activities/{id}/likes")
    Observable<Response<JsonObject>> deleteLike(@Path("id") String feedItemId,
                                              @Header("Authorization") String authHeader);

    //working
    @POST("activities/{id}/disLikes")
    Observable<Response<JsonObject>> postDisLike(@Path("id") String feedItemId,
                                                 @Query("targetId") String boardId,
                                                 @Query("target") String target,
                                                 @Query("time") String dateCreated,
                                                 @Header("Authorization") String authHeader);

    @DELETE("activities/{id}/disLikes")
    Observable<Response<JsonObject>> deleteDisLike(@Path("id") String feedItemId,
                                                @Header("Authorization") String authHeader);

    //working
    @GET("/users/{displayName}")
    Observable<Response<List<User>>> getSearchedUsers(@Header("Authorization") String authHeader, @Path("displayName") String displayName);

    //working
    @GET("/boards/{boardId}/members")
    Observable<Response<List<User>>> getBoardMembers(@Path("boardId") String boardId, @Header("Authorization") String authHeader);

    @GET("matches/{matchId}/lineups")
    Observable<Response<Lineups>> getLineUpsData(@Path("matchId") long matchId);

    @POST("/ausers")
    Observable<Response<SignUpModel>> createUser(@Body SignUpModel signUpModel);

    @GET("matches/{matchId}/events")
    Observable<Response<List<MatchEvent>>> getMatchEvents(@Path("matchId") long matchId);

    @GET("matches/{matchId}/timestatus")
    Observable<Response<List<LiveTimeStatus>>> getLiveTimeStatus(@Path("matchId") long matchId);

    @GET("highlights/{matchId}")
    Observable<Response<List<Highlights>>> getMatchHighlights(@Path("matchId") long matchId);

    @GET("commentaries/{matchId}")
    Observable<Response<List<Commentary>>> getCommentaries(@Path("matchId") long matchId);

    @GET("matches/{matchId}/stats")
    Observable<Response<MatchTeamStats>> getTeamStatsForMatch(@Path("matchId") long matchId);

    @GET("matches/{matchId}")
    Observable<Response<MatchFixture>> getMatchDetails(@Path("matchId") long matchId);

}