package life.plank.juna.zone.data.network.interfaces;

import com.google.gson.JsonObject;

import java.util.Date;
import java.util.List;
import java.util.Set;

import life.plank.juna.zone.data.network.model.Board;
import life.plank.juna.zone.data.network.model.FeedItemComment;
import life.plank.juna.zone.data.network.model.FeedItemCommentReply;
import life.plank.juna.zone.data.network.model.FixtureByMatchDay;
import life.plank.juna.zone.data.network.model.FeedEntry;
import life.plank.juna.zone.data.network.model.Lineups;
import life.plank.juna.zone.data.network.model.MatchDetails;
import life.plank.juna.zone.data.network.model.MatchStats;
import life.plank.juna.zone.data.network.model.PlayerStatsModel;
import life.plank.juna.zone.data.network.model.ScrubberData;
import life.plank.juna.zone.data.network.model.SignUpModel;
import life.plank.juna.zone.data.network.model.StandingModel;
import life.plank.juna.zone.data.network.model.TeamStatsModel;
import life.plank.juna.zone.data.network.model.User;
import life.plank.juna.zone.data.network.model.UserFeed;
import life.plank.juna.zone.data.network.model.Zones;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    Observable<Response<Board>> getBoard(@Query("foreignId") Long matchId, @Query("boardType") String boardType, @Header("Authorization") String authHeader);

    //working
    @GET("/boards/createdBy")
    Observable<Response<List<Board>>> getUserBoards(@Header("Authorization") String authHeader);

    //working
    @POST("/boards/{id}/invite")
    Observable<Response<JsonObject>> inviteUserToJoinBoard(@Body Set<User> user, @Path("id") String boardId, @Header("Authorization") String authHeader);

    //working
    @GET("/boards/feedItems")
    Observable<Response<List<FeedEntry>>> getBoardFeedItems(@Query("id") String boardId, @Header("Authorization") String authHeader);

    //working
    @GET("/zones")
    Observable<Response<List<Zones>>> retrieveZones();

    //working
    @DELETE("/boards/{id}/activities/removeUser")
    Observable<Response<JsonObject>> deleteUserFromPrivateBoard(@Path("id") String boardId,
                                                                @Query("boardUserId") String userId,
                                                                @Header("Authorization") String authHeader);

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
    Observable<Response<JsonObject>> postFeedItemOnBoard(@Body String getEditTextValue,
                                                         @Path("id") String boardId,
                                                         @Query("contentType") String contentType,
                                                         @Query("userId") String userId,
                                                         @Query("dateCreated") String dateCreated,
                                                         @Header("Authorization") String authHeader);

    //working
    @Multipart
    @POST("/boards")
    Observable<Response<String>> createPrivateBoard(@Query("boardType") String boardType,
                                                    @Part("name") RequestBody name,
                                                    @Part("zone") RequestBody zone,
                                                    @Part("description") RequestBody description,
                                                    @Part("color") RequestBody color,
                                                    @Part MultipartBody.Part file,
                                                    @Header("Authorization") String authHeader);

    //working
    @Multipart
    @POST("/users/profilePicture")
    Observable<Response<String>> uploadProfilePicture(@Part MultipartBody.Part file,
                                                      @Header("Authorization") String authHeader);

    //working
    @DELETE("/boards/{id}")
    Observable<Response<JsonObject>> deleteBoard(@Path("id") String boardId,
                                                 @Header("Authorization") String authHeader);

    //working
    @GET("seasons/matches")
    Observable<Response<List<FixtureByMatchDay>>> getFixtures(@Query("seasonName") String seasonName,
                                                              @Query("leagueName") String leagueName,
                                                              @Query("countryName") String countryName);

    //working
    @GET("/feedEntries")
    Observable<Response<List<UserFeed>>> getUserFeed(@Header("Authorization") String... authHeader);

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

    //working
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

    //working
    @DELETE("activities/{id}/disLikes")
    Observable<Response<JsonObject>> deleteDisLike(@Path("id") String feedItemId,
                                                   @Header("Authorization") String authHeader);

    //working
    @GET("/users/{displayName}")
    Observable<Response<List<User>>> getSearchedUsers(@Header("Authorization") String authHeader, @Path("displayName") String displayName);

    //working
    @GET("/boards/{boardId}/members")
    Observable<Response<List<User>>> getBoardMembers(@Path("boardId") String boardId, @Header("Authorization") String authHeader);

    //working
    @POST("/boards/{boardId}/activities/follow")
    Observable<Response<JsonObject>> followBoard(@Header("Authorization") String authHeader, @Path("boardId") String boardId);

    //working
    @POST("/zones/follows")
    Observable<Response<JsonObject>> followZones(@Header("Authorization") String authHeader, @Body Zones zones);

    @GET("/boards/following")
    Observable<Response<List<Board>>> getFollowingBoards(@Header("Authorization") String authHeader);

    @GET("matches/{matchId}/lineups")
    Observable<Response<Lineups>> getLineUpsData(@Path("matchId") long matchId);

    @POST("/ausers")
    Observable<Response<SignUpModel>> createUser(@Body SignUpModel signUpModel);

    @GET("matches/{matchId}/stats")
    Observable<Response<MatchStats>> getMatchStatsForMatch(@Path("matchId") long matchId);

    @GET("matches/{matchId}")
    Observable<Response<MatchDetails>> getMatchDetails(@Path("matchId") long matchId);

    @GET("matches/{matchId}/standings")
    Observable<Response<List<StandingModel>>> getMatchStandingsForMatch(@Path("matchId") long matchId);

    @GET("matches/{matchId}/teamStats")
    Observable<Response<List<TeamStatsModel>>> getTeamStatsForMatch(@Path("matchId") long matchId);

    @GET("matches/{matchId}/scrubber/{hour}")
    Observable<Response<List<ScrubberData>>> getScrubberDetails(@Path("matchId") long matchId, @Path("hour") Date currentMatchTime);

    @POST("/activities/{id}/pins")
    Observable<Response<String>> pinFeedItem(@Path("id") String feedItemId,
                                             @Query("target") String target,
                                             @Query("targetId") String boardId,
                                             @Query("time") String dateCreated,
                                             @Header("Authorization") String authHeader);

    @DELETE("/activities/{id}/pins/{pinId}")
    Observable<Response<JsonObject>> unpinFeedItem(@Path("id") String boardId, @Path("pinId") String pinId, @Header("Authorization") String authHeader);

    @GET("/activities/{feedItemId}/comments")
    Observable<Response<List<FeedItemComment>>> getCommentsForFeed(@Path("feedItemId") String feedId,
                                                                   @Header("Authorization") String authHeader);

    @POST("/activities/{feedItemId}/comments")
    Observable<Response<FeedItemComment>> postCommentOnFeedItem(@Body String comment,
                                                     @Path("feedItemId") String feedItemId,
                                                     @Query("boardId") String boardId,
                                                     @Query("time") String time,
                                                     @Header("Authorization") String authHeader);

    @POST("/activities/{feedItemId}/comments/{commentId}")
    Observable<Response<FeedItemCommentReply>> postReplyOnComment(@Body String reply,
                                                                  @Path("feedItemId") String feedItemId,
                                                                  @Path("commentId") String commentId,
                                                                  @Query("boardId") String boardId,
                                                                  @Query("time") String time,
                                                                  @Header("Authorization") String authHeader);
}