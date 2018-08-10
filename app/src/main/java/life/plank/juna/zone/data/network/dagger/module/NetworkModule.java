package life.plank.juna.zone.data.network.dagger.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import life.plank.juna.zone.data.network.dagger.scope.NetworkScope;
import life.plank.juna.zone.util.helper.ISO8601DateSerializer;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Module providing the network objects (e.g., {@link OkHttpClient}.
 */
@NetworkScope
@Module
public class NetworkModule {

    /**
     * Provides 10MB cache
     */
    @NetworkScope @Provides
    Cache getCache(File directory) {
        return new Cache(directory, 10 * 1024 * 1024);
    }

    @NetworkScope @Provides
    public OkHttpClient provideOkHttpClient() {
        return new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    @NetworkScope @Provides
    public Gson provideGson() {
        return new GsonBuilder().registerTypeAdapter(Date.class, new ISO8601DateSerializer()).create();
    }
}