package life.plank.juna.zone.injection.module;

import com.google.gson.Gson;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.api.RestApi;
import life.plank.juna.zone.injection.component.UiComponent;
import life.plank.juna.zone.injection.scope.NetworkScope;
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
    public RestApi provideDefaultRestApi(Retrofit retrofit) {
        return retrofit.create(RestApi.class);
    }

    @NetworkScope
    @Provides
    public Retrofit getRetrofit(@Named("header") OkHttpClient okHttpClient, Gson gson, NullOnEmptyConverterFactory nullOnEmptyConverterFactory) {
        return new Retrofit.Builder()
                .baseUrl(ZoneApplication.getContext().getString(R.string.backend_base_url))
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(nullOnEmptyConverterFactory)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}