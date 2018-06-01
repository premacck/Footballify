package life.plank.juna.zone.data.network.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.service.HttpClientService;
import life.plank.juna.zone.util.helper.ISO8601DateSerializer;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
        builder.registerTypeAdapter( Date.class, new ISO8601DateSerializer());
        return builder.create();
    }
    @Singleton
    @Provides
    @Named("default")
    public Retrofit getRetrofit(Gson gson) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor( );
        httpLoggingInterceptor.setLevel( HttpLoggingInterceptor.Level.BODY );
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor( httpLoggingInterceptor );
        builder.connectTimeout( 60, TimeUnit.SECONDS );
        builder.readTimeout( 60, TimeUnit.SECONDS );
        OkHttpClient okHttpClient = builder.build();
        return new Retrofit.Builder()
                .baseUrl(ZoneApplication.getContext().getString(R.string.base_url))
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(new NullOnEmptyConverterFactory())
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
