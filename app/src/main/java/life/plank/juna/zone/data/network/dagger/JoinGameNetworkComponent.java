package life.plank.juna.zone.data.network.dagger;

import javax.inject.Singleton;

import dagger.Component;
import life.plank.juna.zone.data.network.module.RestServiceModule;
import life.plank.juna.zone.view.activity.JoinGameActivity;

/**
 * Created by plank-arfaa on 10/01/18.
 */

@Singleton
@Component(modules = RestServiceModule.class)
@FunctionalInterface
public interface JoinGameNetworkComponent {
    void inject(JoinGameActivity joinGameActivity);
}