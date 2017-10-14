package life.plank.juna.zone.data.network.interfaces;

import java.util.List;

import life.plank.juna.zone.data.network.model.Arena;
import life.plank.juna.zone.data.network.model.JunaUser;
import life.plank.juna.zone.data.network.model.NewsFeed;
import life.plank.juna.zone.data.network.model.SampleResponseModel;
import life.plank.juna.zone.data.network.model.UserChoice;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
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

    @POST("arenas/")
    Observable<Arena> getArena(@Body Arena arena);

    @GET("arenas/")
    Observable<Arena> getArenaByInvitationCode(@Query("invitationcode") String invitationcode);

    @POST("rounds/{roundId}/userChoices")
    Observable<Response<Void>> postUserChoice(@Path("roundId") Integer roundId, @Body UserChoice userChoice);

    @PUT("arenas/{secretCode}/players")
    Observable<Response<Void>> putJoinArena(@Path("secretCode") String secretCode, @Body JunaUser junaUser);
}