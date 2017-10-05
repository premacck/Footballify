package life.plank.juna.zone.data.network.dagger;

import javax.inject.Singleton;

import dagger.Component;
import life.plank.juna.zone.data.network.module.RestServiceModule;
import life.plank.juna.zone.presentation.activity.SignUpActivity;

/**
 * Created by plank-sobia on 10/3/2017.
 */

@Singleton
@Component(modules = RestServiceModule.class)
public interface RegisterUserNetworkComponent {
    void inject(SignUpActivity signUpActivity);
}
