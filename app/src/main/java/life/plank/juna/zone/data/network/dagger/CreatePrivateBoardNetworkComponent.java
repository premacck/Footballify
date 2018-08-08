package life.plank.juna.zone.data.network.dagger;

import javax.inject.Singleton;

import dagger.Component;
import life.plank.juna.zone.data.network.module.RestServiceModule;
import life.plank.juna.zone.view.activity.CreatePrivateBoardActivity;

@Singleton
@Component(modules = RestServiceModule.class)
@FunctionalInterface
public interface CreatePrivateBoardNetworkComponent {
    void inject(CreatePrivateBoardActivity createPrivateBoardActivity);
}
