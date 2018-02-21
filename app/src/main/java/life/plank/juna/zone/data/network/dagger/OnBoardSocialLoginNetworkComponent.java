package life.plank.juna.zone.data.network.dagger;

import javax.inject.Singleton;

import dagger.Component;
import life.plank.juna.zone.data.network.module.RestServiceModule;
import life.plank.juna.zone.view.activity.OnBoardDialogActivity;

/**
 * Created by plank-hasan on 11/22/2017.
 */
@Singleton
@Component(modules = RestServiceModule.class)
@FunctionalInterface
public interface OnBoardSocialLoginNetworkComponent {
    void inject(OnBoardDialogActivity onBoardDialogActivity);
}
