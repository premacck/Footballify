package life.plank.juna.zone.data.network.dagger;

import javax.inject.Singleton;

import dagger.Component;
import life.plank.juna.zone.data.network.module.RestServiceModule;
import life.plank.juna.zone.domain.module.GameServiceModule;
import life.plank.juna.zone.view.activity.SuddenDeathGameActivity;

/**
 * Created by plank-sobia on 12/7/2017.
 */
@Singleton
@Component(modules = {RestServiceModule.class, GameServiceModule.class})
public interface SuddenDeathGameComponent {
    void inject(SuddenDeathGameActivity suddenDeathGameActivity);
}
