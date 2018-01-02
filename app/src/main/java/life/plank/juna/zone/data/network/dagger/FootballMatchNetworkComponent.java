package life.plank.juna.zone.data.network.dagger;

import javax.inject.Singleton;

import dagger.Component;
import life.plank.juna.zone.data.network.module.RestServiceModule;
import life.plank.juna.zone.view.activity.ClubGameLaunchActivity;
import life.plank.juna.zone.view.activity.ClubWarriorResultActivity;

/**
 * Created by plank-arfaa on 22/12/17.
 */

@Singleton
@Component(modules = RestServiceModule.class)
public interface FootballMatchNetworkComponent {
    void inject(ClubGameLaunchActivity clubGameLaunchActivity);
    void inject(ClubWarriorResultActivity clubWarriorResultActivity);
}
