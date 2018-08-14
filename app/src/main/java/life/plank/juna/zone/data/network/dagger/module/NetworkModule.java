package life.plank.juna.zone.data.network.dagger.module;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import life.plank.juna.zone.data.network.dagger.scope.NetworkScope;
import life.plank.juna.zone.util.helper.ISO8601DateSerializer;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Module providing the network objects (e.g., {@link OkHttpClient}.
 */
@NetworkScope
@Module
public class NetworkModule {

    /**
     * Provides 10MB cache
     */
    @NetworkScope
    @Provides
    Cache provideCache(File directory) {
        return new Cache(directory, 10 * 1024 * 1024);
    }

    @Named("header")
    @NetworkScope
    @Provides
    HttpLoggingInterceptor provideHeaderHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS);
    }

    @Named("body")
    @NetworkScope
    @Provides
    HttpLoggingInterceptor provideBodyHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @NetworkScope
    @Provides
    @Named("header")
    public OkHttpClient provideDefaultOkHttpClient(@Named("header") HttpLoggingInterceptor httpLoggingInterceptor,
                                                   SSLSocketFactory sslSocketFactory, X509TrustManager x509TrustManager) {
        return new OkHttpClient().newBuilder()
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .sslSocketFactory(sslSocketFactory, x509TrustManager)
                .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                .build();
    }

    @NetworkScope
    @Provides
    @Named("body")
    public OkHttpClient provideOkHttpClient(@Named("body") HttpLoggingInterceptor httpLoggingInterceptor,
                                            SSLSocketFactory sslSocketFactory, X509TrustManager x509TrustManager) {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .sslSocketFactory(sslSocketFactory, x509TrustManager)
                .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                .build();
    }

    @NetworkScope
    @Provides
    public X509TrustManager provideX509TrustManager() {
        return new X509TrustManager() {
            @SuppressLint("TrustAllX509TrustManager")
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                //Do nothing
            }

            @SuppressLint("TrustAllX509TrustManager")
            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                //Do nothing
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }
        };
    }

    @NetworkScope
    @Provides
    public TrustManager[] provideTrustManagers(X509TrustManager x509TrustManager) {
        return new TrustManager[]{x509TrustManager};
    }

    @NetworkScope
    @Provides
    public SSLSocketFactory provideSSLSocketFactory(TrustManager[] trustAllCerts) {
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @NetworkScope
    @Provides
    public Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new ISO8601DateSerializer())
                .setLenient()
                .create();
    }

    @NetworkScope
    @Provides
    public NullOnEmptyConverterFactory provideNullOnEmptyConverterFactory() {
        return new NullOnEmptyConverterFactory();
    }
}