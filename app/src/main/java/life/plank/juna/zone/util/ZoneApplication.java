package life.plank.juna.zone.util;

import android.app.Application;
import android.content.Context;

import life.plank.juna.zone.data.network.dagger.DaggerNewsFeedsNetworkComponent;
import life.plank.juna.zone.data.network.dagger.NewsFeedsNetworkComponent;
import life.plank.juna.zone.data.network.module.RestServiceModule;

/**
 * Created by plank-sobia on 9/19/2017.
 */

public class ZoneApplication extends Application {

    private static Context context;
    private NewsFeedsNetworkComponent newsFeedsNetworkComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        newsFeedsNetworkComponent = DaggerNewsFeedsNetworkComponent.builder()
                .restServiceModule(new RestServiceModule())
                .build();
    }

    public static Context getContext() {
        return context;
    }

    public NewsFeedsNetworkComponent getNetworkComponent() {
        return newsFeedsNetworkComponent;
    }
}
