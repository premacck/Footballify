package life.plank.juna.zone.data.network.dagger.module;

import com.google.gson.Gson;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.dagger.component.UiComponent;
import life.plank.juna.zone.data.network.dagger.scope.NetworkScope;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by plank-dhamini on 6/8/2017.
 * Module providing the network objects (e.g., {@link Retrofit}.
 */
@NetworkScope
@Module(subcomponents = {UiComponent.class}, includes = NetworkModule.class)
public class RestServiceModule {

    @NetworkScope
    @Provides
    @Named("default")
    public RestApi provideDefaultRestApi(@Named("default") Retrofit retrofit) {
        return retrofit.create(RestApi.class);
    }

    @NetworkScope
    @Provides
    @Named("footballData")
    public RestApi provideFootballRestApi(@Named("footballData") Retrofit retrofit) {
        return retrofit.create(RestApi.class);
    }

    //todo:combine these two url feed and Football Data
    @NetworkScope
    @Provides
    @Named("default")
    public Retrofit getRetrofit(@Named("header") OkHttpClient okHttpClient, Gson gson, NullOnEmptyConverterFactory nullOnEmptyConverterFactory) {
        return new Retrofit.Builder()
                .baseUrl(ZoneApplication.getContext().getString(R.string.feed_data_base_url))
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(nullOnEmptyConverterFactory)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @NetworkScope
    @Provides
    @Named("footballData")
    public Retrofit getFootballData(@Named("header") OkHttpClient okHttpClient, Gson gson, NullOnEmptyConverterFactory nullOnEmptyConverterFactory) {
        return new Retrofit.Builder()
                .baseUrl(ZoneApplication.getContext().getString(R.string.football_data_base_url))
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(nullOnEmptyConverterFactory)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @NetworkScope
    @Provides
    @Named("azure")
    public Retrofit getAzureRetrofit(@Named("header") OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(ZoneApplication.getContext().getString(R.string.azure_base_url))
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}