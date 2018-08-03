package life.plank.juna.zone.data.network.module;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
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
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

    OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();


    @Singleton
    @Provides
    public Gson provideGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new ISO8601DateSerializer());
        return builder.create();
    }

    //todo:combine these two url feed and Football Data
    @Singleton
    @Provides
    @Named("default")
    public Retrofit getRetrofit(Gson gson) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors …

        // add logging as last interceptor
        httpClient.addInterceptor(logging);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
                SharedPreferences azurePref = ZoneApplication.getContext().getSharedPreferences("AzureToken", 0);
                Log.d("Saved value", "" + "Bearer " + azurePref.getString("AzureToken", "NA"));
                azurePref.getString("AzureToken", "NA");

                String token = "Bearer " + azurePref.getString("AzureToken", "NA");
                Log.d("Saved value", "" + token);
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", token)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        return new Retrofit.Builder()
                .baseUrl(ZoneApplication.getContext().getString(R.string.feed_data_base_url))
                .client(HttpClientService.getUnsafeOkHttpClient())
                .client(okHttpClient)
                .client(client)
                .client(httpClient.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }

    @Singleton
    @Provides
    @Named("footballData")
    public Retrofit getFootballData(Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(ZoneApplication.getContext().getString(R.string.football_data_base_url))
                .client(HttpClientService.getUnsafeOkHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
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
