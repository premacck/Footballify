package life.plank.juna.zone.data.network.dagger;

import javax.inject.Singleton;

import dagger.Component;
import life.plank.juna.zone.data.network.module.RestServiceModule;
import life.plank.juna.zone.presentation.activity.PointsGameActivity;

/**
 * Created by plank-sobia on 10/10/2017.
 */
@Singleton
@Component(modules = RestServiceModule.class)
public interface PostUserChoiceNetworkComponent {
    void inject(PointsGameActivity pointsGameActivity);
}
