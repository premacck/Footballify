package life.plank.juna.zone.data.network.dagger;

import javax.inject.Singleton;

import dagger.Component;
import life.plank.juna.zone.data.network.module.RestServiceModule;
import life.plank.juna.zone.view.activity.TokenActivity;

@Singleton
@Component(modules = RestServiceModule.class)
@FunctionalInterface
public interface AzureNetworkComponent {
    void inject(TokenActivity tokenActivity);
}

