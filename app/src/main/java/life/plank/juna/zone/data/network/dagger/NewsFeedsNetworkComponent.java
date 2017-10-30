package life.plank.juna.zone.data.network.dagger;

import javax.inject.Singleton;

import dagger.Component;
import life.plank.juna.zone.data.network.module.RestServiceModule;
import life.plank.juna.zone.view.fragment.NewsFeedsFragment;

/**
 * Created by plank-sobia on 9/19/2017.
 */

@Singleton
@Component(modules = RestServiceModule.class)
public interface NewsFeedsNetworkComponent {
    void inject(NewsFeedsFragment newsFeedsFragment);
}
