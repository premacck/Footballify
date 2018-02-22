package life.plank.juna.zone.data.network.dagger;

import javax.inject.Singleton;

import dagger.Component;
import life.plank.juna.zone.data.network.module.RestServiceModule;
import life.plank.juna.zone.view.activity.SuddenDeathResultActivity;

/**
 * Created by plank-dhamini on 12/01/18.
 */

@Singleton
@Component(modules = {RestServiceModule.class})
@FunctionalInterface
public interface SuddenDeathResultComponent {
    void inject(SuddenDeathResultActivity suddenDeathResultActivity);
}
