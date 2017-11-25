package life.plank.juna.zone.data.network.service;

import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    public static RestApi getRetrofitService() {
        return new Retrofit.Builder()
                .baseUrl(ZoneApplication.getContext().getString(R.string.instagram_base_url))
                .client(HttpClientService.getUnsafeOkHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RestApi.class);
    }
}