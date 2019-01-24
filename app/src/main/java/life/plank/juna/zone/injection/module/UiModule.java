package life.plank.juna.zone.injection.module;

import androidx.recyclerview.widget.PagerSnapHelper;
import dagger.Module;
import dagger.Provides;
import life.plank.juna.zone.injection.scope.UiScope;

/**
 * Module providing the view/UI related objects.
 */
@UiScope
@Module
public class UiModule {

    @UiScope
    @Provides
    public PagerSnapHelper providePagerSnapHelper() {
        return new PagerSnapHelper();
    }
}

