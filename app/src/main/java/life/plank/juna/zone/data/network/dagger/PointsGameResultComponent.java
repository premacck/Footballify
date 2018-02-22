package life.plank.juna.zone.data.network.dagger;

import javax.inject.Singleton;

import dagger.Component;
import life.plank.juna.zone.data.network.module.RestServiceModule;
import life.plank.juna.zone.view.activity.PointsGameResultActivity;

/**
 * Created by plank-dhamini on 09/01/18.
 */

@Singleton
@Component(modules = {RestServiceModule.class})
@FunctionalInterface
public interface PointsGameResultComponent {
    void inject(PointsGameResultActivity pointsGameResultActivity);
}