package life.plank.juna.zone.data.network.service;

import life.plank.juna.zone.data.network.model.SampleResponseModel;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by plank-dhamini on 6/8/2017.
 * This class consists the rest service for zone
 */

public class RestService implements RestApi {

    private RestApi api;
    private final String URL = "BaseUrl";

    public RestService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        this.api = retrofit.create(RestApi.class);
    }

    @Override
    public Observable<SampleResponseModel> getCharacters() {
        return this.api.getCharacters();
    }
}
