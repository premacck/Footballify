package life.plank.juna.zone.data.network.dagger.component;

import android.content.Context;

import dagger.Component;
import life.plank.juna.zone.data.network.dagger.module.ContextModule;
import life.plank.juna.zone.data.network.dagger.scope.AppScope;

/**
 * This is the base Application Component. The objects that are to be used throughout the application (like ApplicationContext) should be placed here.
 * <br/><br/>
 * If any component is using any {@link Context}, then make that component either a subComponent, or a dependent component of {@link AppComponent}
 * <br/><br/>
 * For more information on SubComponents and dependent Components,
 * see https://proandroiddev.com/dagger-2-part-ii-custom-scopes-component-dependencies-subcomponents-697c1fa1cfc?gi=75b4ef193467
 */
@AppScope
@Component(modules = {ContextModule.class})
public interface AppComponent {

    /**
     * Release the ApplicationContext for use.
     */
    Context getAppContext();

    /**
     * SubComponent getters (must be defined for all SubComponents of {@link AppComponent}
     */
    NetworkComponent.Builder networkComponentBuilder();
}