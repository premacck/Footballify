package life.plank.juna.zone;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import io.alterac.blurkit.BlurKit;
import io.fabric.sdk.android.Fabric;
import life.plank.juna.zone.injection.component.AppComponent;
import life.plank.juna.zone.injection.component.DaggerAppComponent;
import life.plank.juna.zone.injection.component.DaggerViewModelComponent;
import life.plank.juna.zone.injection.component.NetworkComponent;
import life.plank.juna.zone.injection.component.UiComponent;
import life.plank.juna.zone.injection.component.ViewModelComponent;
import life.plank.juna.zone.injection.module.ContextModule;

public class ZoneApplication extends Application {

    private static ZoneApplication zoneApplication;

    private AppComponent appComponent;
    private UiComponent uiComponent;
    private NetworkComponent networkComponent;
    private ViewModelComponent viewModelComponent;

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
        BlurKit.init(this);

        appComponent = DaggerAppComponent.builder()
                .contextModule(new ContextModule(getApplicationContext()))
                .build();
        networkComponent = appComponent.networkComponentBuilder()
                .build();
        uiComponent = networkComponent.viewComponentBuilder()
                .build();
        viewModelComponent = DaggerViewModelComponent.builder().build();
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

    public ViewModelComponent getViewModelComponent() {
        return viewModelComponent;
    }
}