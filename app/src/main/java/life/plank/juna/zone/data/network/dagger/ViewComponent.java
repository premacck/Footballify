package life.plank.juna.zone.data.network.dagger;

import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Component;
import life.plank.juna.zone.data.network.module.ViewModule;

@Singleton
@Component(modules = {ViewModule.class})
public interface ViewComponent {
    Picasso getPicasso();
//    void inject(CreateBoardActivity createBoardActivity);
}