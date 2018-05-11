package life.plank.juna.zone.data.network.dagger;

import javax.inject.Singleton;

import dagger.Component;
import life.plank.juna.zone.data.network.module.RestServiceModule;
import life.plank.juna.zone.view.activity.FootballFeedDetailActivity;
import life.plank.juna.zone.view.adapter.FootballFeedDetailAdapter;

/**
 * Created by plank-prachi on 12/01/18.
 */

@Singleton
@Component(modules = {RestServiceModule.class})
@FunctionalInterface
public interface LikeFeedNetworkComponent {
    void inject(FootballFeedDetailAdapter footballFeedDetailAdapter);
}
