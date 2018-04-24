package life.plank.juna.zone.data.network.dagger;

import javax.inject.Singleton;

import dagger.Component;
import life.plank.juna.zone.data.network.module.RestServiceModule;
import life.plank.juna.zone.view.fragment.ScoreFixtureFragment;
import life.plank.juna.zone.view.fragment.StandingFragment;

/**
 * Created by plank-prachi on 12/01/18.
 */

@Singleton
@Component(modules = {RestServiceModule.class})
@FunctionalInterface
public interface ScoreFixtureNetworkComponent {
    void inject(ScoreFixtureFragment scoreFixtureFragment);
}
