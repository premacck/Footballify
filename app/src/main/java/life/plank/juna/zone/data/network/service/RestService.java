package life.plank.juna.zone.data.network.service;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import life.plank.juna.zone.data.network.interfaces.RestApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by plank-dhamini on 6/8/2017.
 * This class consists the rest service for zone
 */

public class RestService {
    private static RestService restService = null;

    private RestService() {
    }

    public static synchronized RestService getInstance() {
        if (restService == null) {
            restService = new RestService();
        }
        return restService;
    }

    public RestApi getRestApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("Base URL")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RestApi.class);
    }
}
