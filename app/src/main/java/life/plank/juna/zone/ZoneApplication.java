package life.plank.juna.zone;

import android.app.Application;
import android.content.Context;

import life.plank.juna.zone.data.network.dagger.CreateArenaNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerCreateArenaNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerLoginUserNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerMultipleUserJoinGameNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerNewsFeedsNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerPointsGameComponent;
import life.plank.juna.zone.data.network.dagger.DaggerRegisterUserNetworkComponent;
import life.plank.juna.zone.data.network.dagger.LoginUserNetworkComponent;
import life.plank.juna.zone.data.network.dagger.MultipleUserJoinGameNetworkComponent;
import life.plank.juna.zone.data.network.dagger.NewsFeedsNetworkComponent;
import life.plank.juna.zone.data.network.dagger.PointsGameComponent;
import life.plank.juna.zone.data.network.dagger.RegisterUserNetworkComponent;
import life.plank.juna.zone.data.network.module.RestServiceModule;
import life.plank.juna.zone.domain.module.GameServiceModule;

/**
 * Created by plank-sobia on 9/19/2017.
 */

public class ZoneApplication extends Application {

    public static Integer roundNumber = 0;
    private static Context context;
    private NewsFeedsNetworkComponent newsFeedsNetworkComponent;
    private LoginUserNetworkComponent loginUserNetworkComponent;
    private RegisterUserNetworkComponent registerUserNetworkComponent;
    private CreateArenaNetworkComponent createArenaNetworkComponent;
    private PointsGameComponent pointsGameComponent;
    private MultipleUserJoinGameNetworkComponent multipleUserJoinGameNetworkComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        newsFeedsNetworkComponent = DaggerNewsFeedsNetworkComponent.builder()
                .restServiceModule(new RestServiceModule())
                .build();

        loginUserNetworkComponent = DaggerLoginUserNetworkComponent.builder()
                .restServiceModule(new RestServiceModule())
                .build();

        registerUserNetworkComponent = DaggerRegisterUserNetworkComponent.builder()
                .restServiceModule(new RestServiceModule())
                .build();

        createArenaNetworkComponent = DaggerCreateArenaNetworkComponent.builder()
                .restServiceModule(new RestServiceModule())
                .build();

        pointsGameComponent = DaggerPointsGameComponent.builder()
                .restServiceModule(new RestServiceModule())
                .gameServiceModule(new GameServiceModule(this))
                .build();

        multipleUserJoinGameNetworkComponent = DaggerMultipleUserJoinGameNetworkComponent.builder()
                .restServiceModule(new RestServiceModule())
                .build();
    }

    public static Context getContext() {
        return context;
    }

    public NewsFeedsNetworkComponent getNewsFeedsNetworkComponent() {
        return newsFeedsNetworkComponent;
    }

    public LoginUserNetworkComponent getLoginNetworkComponent() {
        return loginUserNetworkComponent;
    }

    public RegisterUserNetworkComponent getRegisterNetworkComponent() {
        return registerUserNetworkComponent;
    }

    public CreateArenaNetworkComponent getCreateArenaNetworkComponent() {
        return createArenaNetworkComponent;
    }

    public PointsGameComponent getPointsGameComponent() {
        return pointsGameComponent;
    }

    public MultipleUserJoinGameNetworkComponent getMultipleUserJoinGameNetworkComponent() {
        return multipleUserJoinGameNetworkComponent;
    }
}
