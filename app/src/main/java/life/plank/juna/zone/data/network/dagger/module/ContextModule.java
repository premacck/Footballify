package life.plank.juna.zone.data.network.dagger.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import life.plank.juna.zone.data.network.dagger.component.NetworkComponent;
import life.plank.juna.zone.data.network.dagger.scope.AppScope;

/**
 * Module providing the Context object.
 */
@AppScope
@Module(subcomponents = {NetworkComponent.class})
public class ContextModule {

    private Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @AppScope @Provides
    public Context provideContext() {
        return context;
    }
}