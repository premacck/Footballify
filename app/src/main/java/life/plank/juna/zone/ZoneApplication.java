package life.plank.juna.zone;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import io.fabric.sdk.android.Fabric;
import life.plank.juna.zone.data.network.dagger.component.AppComponent;
import life.plank.juna.zone.data.network.dagger.component.DaggerAppComponent;
import life.plank.juna.zone.data.network.dagger.component.NetworkComponent;
import life.plank.juna.zone.data.network.dagger.component.UiComponent;
import life.plank.juna.zone.data.network.dagger.module.ContextModule;

/**
 * Created by plank-sobia on 9/19/2017.
 */
public class ZoneApplication extends Application {

    private static ZoneApplication zoneApplication;

    private AppComponent appComponent;
    private UiComponent uiComponent;
    private NetworkComponent networkComponent;

    public static ZoneApplication getApplication() {
        return zoneApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override public void onCreate() {
        super.onCreate();
        zoneApplication = this;
        Fabric.with(this, new Crashlytics());

        appComponent = DaggerAppComponent.builder()
                .contextModule(new ContextModule(getApplicationContext()))
                .build();
        networkComponent = appComponent.networkComponentBuilder()
                .build();
        uiComponent = networkComponent.viewComponentBuilder()
                .build();
    }

    public static Gson getGson() {
        return zoneApplication.networkComponent.getGson();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public NetworkComponent getNetworkComponent() {
        return networkComponent;
    }

    public UiComponent getUiComponent() {
        return uiComponent;
    }
}