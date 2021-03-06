package life.plank.juna.zone.injection.component;

import com.google.gson.Gson;

import dagger.Subcomponent;
import life.plank.juna.zone.injection.module.RestServiceModule;
import life.plank.juna.zone.injection.scope.NetworkScope;

/**
 * This components contains network related objects, like the REST service.
 * <br/><br/>
 * <b>Uses :</b> Overload the "inject" method and include the class name where you want the network object as a parameter.
 */
@NetworkScope
@Subcomponent(modules = RestServiceModule.class)
public interface NetworkComponent {

    Gson getGson();

    UiComponent.Builder viewComponentBuilder();

    @Subcomponent.Builder
    interface Builder {
        NetworkComponent build();
    }
}