package life.plank.juna.zone.data.network.interfaces;


import life.plank.juna.zone.data.network.model.SampleResponseModel;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by plank-dhamini on 6/8/2017.
 * This class consists of all the rest api calls for zone
 */

public interface RestApi {

    @GET("sampleUrl/")
    Observable<SampleResponseModel> getCharacters();

}
