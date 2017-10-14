package life.plank.juna.zone.domain.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import life.plank.juna.zone.domain.service.GameService;

/**
 * Created by plank-sobia on 10/13/2017.
 */
@Module
public class GameServiceModule {
    private Context context;

    public GameServiceModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public GameService getGameService() {
        return new GameService(context);
    }
}
