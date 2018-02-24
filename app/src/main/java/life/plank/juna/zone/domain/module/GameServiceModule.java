package life.plank.juna.zone.domain.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import life.plank.juna.zone.domain.service.GameService;

/**
 * Created by plank-sobia on 10/13/2017.
 */
@Module
public class GameServiceModule {

    @Provides
    @Singleton
    public GameService getGameService() {
        return new GameService();
    }
}
