package life.plank.juna.zone.data.network.dagger.module;

import android.content.Context;
import android.support.v7.widget.PagerSnapHelper;

import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;
import life.plank.juna.zone.data.network.dagger.scope.UiScope;

/**
 * Module providing the view/UI related objects (e.g., {@link Picasso}.
 */
@UiScope
@Module
public class UiModule {

    @UiScope
    @Provides
    public Picasso providePicasso(Context context) {
        return Picasso.with(context);
    }

    @UiScope
    @Provides
    public PagerSnapHelper providePagerSnapHelper() {
        return new PagerSnapHelper();
    }
}

