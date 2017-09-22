package life.plank.juna.zone.data.network.interfaces;

import java.util.List;

import life.plank.juna.zone.data.network.model.NewsFeed;
import life.plank.juna.zone.data.network.model.SampleResponseModel;
import retrofit2.http.GET;
import retrofit2.http.Path;
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
}