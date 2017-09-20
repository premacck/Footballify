package life.plank.juna.zone.data.network.dagger;

import android.app.Application;

import life.plank.juna.zone.data.network.module.RestServiceModule;

/**
 * Created by plank-sobia on 9/19/2017.
 */

public class NewsFeedsCustomApplication extends Application {

    private NewsFeedsNetworkComponent newsFeedsNetworkComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        newsFeedsNetworkComponent = DaggerNewsFeedsNetworkComponent.builder()
                .restServiceModule(new RestServiceModule())
                .build();
    }

    public NewsFeedsNetworkComponent getNetworkComponent() {
        return newsFeedsNetworkComponent;
    }
}
