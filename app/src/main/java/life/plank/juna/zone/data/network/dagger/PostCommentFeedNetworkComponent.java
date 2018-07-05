package life.plank.juna.zone.data.network.dagger;

import javax.inject.Singleton;

import dagger.Component;
import life.plank.juna.zone.data.network.module.RestServiceModule;
import life.plank.juna.zone.view.activity.PostCommentActivity;

/**
 * Created by plank-prachi on 12/01/18.
 */

@Singleton
@Component(modules = {RestServiceModule.class})
@FunctionalInterface
public interface PostCommentFeedNetworkComponent {
    void inject(PostCommentActivity postCommentActivity);
}
