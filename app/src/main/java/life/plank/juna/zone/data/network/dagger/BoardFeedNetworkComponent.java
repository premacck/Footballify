package life.plank.juna.zone.data.network.dagger;

import javax.inject.Singleton;

import dagger.Component;
import life.plank.juna.zone.data.network.module.RestServiceModule;
import life.plank.juna.zone.view.activity.BoardActivity;

/**
 * Created by plank-arfaa on 06/02/18.
 */

@Singleton
@Component(modules = RestServiceModule.class)
@FunctionalInterface
public interface BoardFeedNetworkComponent {
    void inject(BoardActivity boardActivity);
}