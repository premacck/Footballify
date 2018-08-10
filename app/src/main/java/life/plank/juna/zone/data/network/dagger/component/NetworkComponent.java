package life.plank.juna.zone.data.network.dagger.component;

import dagger.Subcomponent;
import life.plank.juna.zone.data.network.dagger.scope.NetworkScope;
import life.plank.juna.zone.data.network.dagger.module.RestServiceModule;

/**
 * This components contains network related objects, like the REST service.
 * <br/><br/>
 * <b>Uses :</b> Overload the "inject" method and include the class name where you want the network object as a parameter.
 */
@NetworkScope
@Subcomponent(modules = RestServiceModule.class)
public interface NetworkComponent {

    @Subcomponent.Builder interface Builder {
        NetworkComponent build();
    }

    UiComponent.Builder viewComponentBuilder();
}