package life.plank.juna.zone.data.network.dagger;

import javax.inject.Singleton;

import dagger.Component;
import life.plank.juna.zone.data.network.module.RestServiceModule;
import life.plank.juna.zone.view.activity.CreateBoardActivity;

@Singleton
@Component(modules = RestServiceModule.class)
@FunctionalInterface
public interface CreatePrivateBoardNetworkComponent {
    void inject(CreateBoardActivity createBoardActivity);
}
