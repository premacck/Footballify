package life.plank.juna.zone.data.network.module;

import android.content.Context;

import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = ContextModule.class)
public class ViewModule {

    @Singleton @Provides
    public Picasso providePicasso(Context context) {
        return Picasso.with(context);
    }
}

