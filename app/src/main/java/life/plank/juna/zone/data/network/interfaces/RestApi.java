package life.plank.juna.zone.data.network.interfaces;

import com.google.gson.JsonObject;

import java.util.List;

import life.plank.juna.zone.data.network.model.Arena;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.data.network.model.FootballMatch;
import life.plank.juna.zone.data.network.model.JunaUser;
import life.plank.juna.zone.data.network.model.LineupsModel;
import life.plank.juna.zone.data.network.model.NewsFeed;
import life.plank.juna.zone.data.network.model.PlayerStatsModel;
import life.plank.juna.zone.data.network.model.SampleResponseModel;
import life.plank.juna.zone.data.network.model.ScoreFixtureModel;
import life.plank.juna.zone.data.network.model.SignupModel;
import life.plank.juna.zone.data.network.model.StandingModel;
import life.plank.juna.zone.data.network.model.TeamStatsModel;
import life.plank.juna.zone.data.network.model.UserChoice;
import life.plank.juna.zone.data.network.model.instagramModelClass.InstagramResponse;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @GET("matches/1711146/lineups")
    Observable<Response<LineupsModel>> getLineUpsData();

    @POST("users")
    Observable<Response<SignupModel>> getSignup(@Body SignupModel signupModel );
}