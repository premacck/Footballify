package life.plank.juna.zone.data.network.dagger.module;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.openid.appauth.AuthorizationService;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.dagger.scope.NetworkScope;
import life.plank.juna.zone.util.time.ISO8601DateSerializer;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Module providing the network objects (e.g., {@link OkHttpClient}.
 */
@NetworkScope
@Module
public class NetworkModule {

    public static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Date.class, new ISO8601DateSerializer())
            .setDateFormat(DateFormat.FULL, DateFormat.FULL)
            .setLenient()
            .create();

    /**
     * Provides 50MB cache
     */
    @NetworkScope
    @Provides
    Cache provideCache(File directory) {
        return new Cache(directory, 50 * 1024 * 1024);
    }

    @Named("header")
    @NetworkScope
    @Provides
    HttpLoggingInterceptor provideHeaderHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS);
    }

    @NetworkScope
    @Provides
    @Named("header")
    public OkHttpClient provideDefaultOkHttpClient(@Named("header") HttpLoggingInterceptor httpLoggingInterceptor, Interceptor subscriptionInterceptor) {
        return new OkHttpClient().newBuilder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(subscriptionInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                .build();
    }

    @NetworkScope
    @Provides
    public Gson provideGson() {
        return GSON;
    }

    @NetworkScope
    @Provides
    public ISO8601DateSerializer provideISO8601DateSerializer() {
        return new ISO8601DateSerializer();
    }

    @NetworkScope
    @Provides
    public NullOnEmptyConverterFactory provideNullOnEmptyConverterFactory() {
        return new NullOnEmptyConverterFactory();
    }

    @NetworkScope
    @Provides
    public AuthorizationService provideAuthorizationService(Context context) {
        return new AuthorizationService(context);
    }

    @NetworkScope
    @Provides
    public Interceptor provideInterceptor(Context context) {
        return chain -> {
            Request request = chain.request();
            return chain.proceed(
                    request.newBuilder()
                            .header(context.getString(R.string.subscription_key_key), context.getString(R.string.subscription_key_value))
                            .build()
            );
        };
    }
}