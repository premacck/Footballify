package life.plank.juna.zone.data.network.interfaces;

import com.google.gson.JsonObject;

import java.util.List;

import life.plank.juna.zone.data.network.model.Arena;
import life.plank.juna.zone.data.network.model.BoardCreationModel;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.data.network.model.FootballMatch;
import life.plank.juna.zone.data.network.model.JunaUser;
import life.plank.juna.zone.data.network.model.LineupsModel;
import life.plank.juna.zone.data.network.model.MatchSummaryModel;
import life.plank.juna.zone.data.network.model.NewsFeed;
import life.plank.juna.zone.data.network.model.PlayerStatsModel;
import life.plank.juna.zone.data.network.model.SampleResponseModel;
import life.plank.juna.zone.data.network.model.ScoreFixtureModel;
import life.plank.juna.zone.data.network.model.SignInModel;
import life.plank.juna.zone.data.network.model.SignUpModel;
import life.plank.juna.zone.data.network.model.StandingModel;
import life.plank.juna.zone.data.network.model.TeamStatsModel;
import life.plank.juna.zone.data.network.model.UserChoice;
import life.plank.juna.zone.data.network.model.instagramModelClass.InstagramResponse;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by plank-dhamini on 6/8/2017.
 * This class consists of all the rest api calls for zone
 */

public interface RestApi {

    @GET("sampleUrl/")
    Observable<SampleResponseModel> getCharacters();

    @GET("rssFeeds/{date}/?limit=50")
    Observable<List<NewsFeed>> getNewsFeed(@Path("date") String date);

    @POST("authentication/register/")
    Observable<Response<Void>> registerUser(@Body JunaUser junaUser);

    @POST("authentication/login/")
    Observable<Response<Void>> loginUser(@Body JunaUser junaUser);

    @POST("authentication/register/")
    Observable<Response<Void>> socialSignUp(@Body JunaUser junaUser);

    @POST("authentication/login/")
    Observable<Response<Void>> socialSignIn(@Body JunaUser junaUser);

    @POST("arenas/")
    Observable<Arena> getArena(@Body Arena arena);

    @GET("arenas/")
    Observable<Arena> getArenaByInvitationCode(@Query("invitationcode") String invitationcode);

    @POST("rounds/{roundId}/userChoices")
    Observable<Response<Void>> postUserChoice(@Path("roundId") Integer roundId, @Body UserChoice userChoice);

    @GET("rounds/{roundId}/userChoices")
    Observable<List<UserChoice>> getUserChoice(@Path("roundId") Integer roundId);

    @PUT("arenas/{secretCode}/players")
    Observable<Response<Void>> putJoinArena(@Path("secretCode") String secretCode, @Body JunaUser junaUser);

    @GET("v1/users/self")
    Observable<InstagramResponse> getInstagramUserData(@Query("access_token") String accessToken);

    @GET("/footballMatches")
    Observable<FootballMatch> getRandomFootballMatchByName(@Query("team") String team);

    @GET("api/feeditems")
    Observable<Response<List<FootballFeed>>> getFootballFeed(@Header("newsfeed-continuation-token") String header);

    @GET("seasons/current/standings")
    Observable<Response<List<StandingModel>>> getStandings(@Query("leagueName") String leagueName);

    @GET("seasons/matches")
    Observable<Response<List<ScoreFixtureModel>>> getScoresAndFixtures(@Query("seasonName") String seasonName);

    @GET("seasons/playerstats")
    Observable<Response<List<PlayerStatsModel>>> getPlayerStats(@Query("seasonName") String seasonName);

    @GET("teams/stats")
    Observable<Response<List<TeamStatsModel>>> getTeamStats(@Query("seasonName") String seasonName);

    @GET("matches/{matchId}/lineups")
    Observable<Response<LineupsModel>> getLineUpsData(@Path("matchId") long matchId);

    @POST("/users")
    Observable<Response<SignUpModel>> createUser(@Body SignUpModel signUpModel);

    @GET("/users")
    Observable<Response<SignInModel>> getUser(@Query("emailAddress") String emailAddress);

    @POST("feedItems/f87f341d-2c7d-41f6-ba44-7e9f4920f533/likes")
    Observable<Response<JsonObject>> getLikedFeedItem(@Query("userId") String userId);

    @GET("matches/{matchId}/matchsummary")
    Observable<Response<MatchSummaryModel>> getMatchSummary(@Path("matchId") long matchId);

    @Multipart
    @POST("feedItems/upload")
    Observable<Response<JsonObject>> postImageFromGallery(@Part MultipartBody.Part file, @Query("targetId") String targetId, @Query("targetType") String targetType, @Query("contentType") String contentType, @Query("userId") String userId, @Query("dateCreated") String dateCreated);

    @Multipart
    @POST("feedItems/upload")
    Observable<Response<JsonObject>> postAudioFile(@Part MultipartBody.Part file, @Query("targetId") String targetId, @Query("targetType") String targetType, @Query("contentType") String contentType, @Query("userId") String userId, @Query("dateCreated") String dateCreated);

    @GET("api/feeditems")
    Observable<Response<List<FootballFeed>>> getBoardFeed(@Header("newsfeed-continuation-token") String header);

    @POST("boards/{boardId}/activities/enter")
    Observable<Response<JsonObject>> enterBoard(@Path( "boardId" ) String boardId, @Query("userId") String userId);

    @GET("/boards")
    Observable<Response<BoardCreationModel>> retrieveBoard(@Query("foreignId") Long foreignId, @Query("boardType") String boardType);
}

