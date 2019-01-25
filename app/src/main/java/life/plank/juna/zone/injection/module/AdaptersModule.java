package life.plank.juna.zone.injection.module;

import dagger.Module;
import dagger.Provides;
import life.plank.juna.zone.injection.scope.UiScope;
import life.plank.juna.zone.ui.board.adapter.creation.BoardColorThemeAdapter;
import life.plank.juna.zone.ui.board.adapter.creation.BoardIconAdapter;
import life.plank.juna.zone.ui.user.adapter.GetCoinsAdapter;
import life.plank.juna.zone.ui.user.adapter.LastTransactionsAdapter;

@Module
public class AdaptersModule {

    @UiScope
    @Provides
    public BoardColorThemeAdapter provideBoardColorThemeAdapter() {
        return new BoardColorThemeAdapter();
    }

    @UiScope
    @Provides
    public BoardIconAdapter provideBoardIconAdapter() {
        return new BoardIconAdapter();
    }

    @UiScope
    @Provides
    public GetCoinsAdapter provideGetCoinsAdapter() {
        return new GetCoinsAdapter();
    }

    @UiScope
    @Provides
    public LastTransactionsAdapter provideLastTransactionsAdapter() {
        return new LastTransactionsAdapter();
    }
}