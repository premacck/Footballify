package life.plank.juna.zone.data.network.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.service.HttpClientService;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by plank-dhamini on 6/8/2017.
 * This class consists the rest service for zone
 */

@Module
public class RestServiceModule {

    @Singleton
    @Provides
    public Gson provideGson() {
        GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }

    @Singleton
    @Provides
    @Named("default")
    public Retrofit getRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(ZoneApplication.getContext().getString(R.string.base_url))
                .client(HttpClientService.getUnsafeOkHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Singleton
    @Provides
    @Named("instagram")
    public Retrofit getInstagramRetrofitService() {
        return new Retrofit.Builder()
                .baseUrl(ZoneApplication.getContext().getString(R.string.instagram_base_url))
                .client(HttpClientService.getUnsafeOkHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    @Named("azure")
    public Retrofit getAzureRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(ZoneApplication.getContext().getString(R.string.azure_base_url))
                .client(HttpClientService.getUnsafeOkHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
