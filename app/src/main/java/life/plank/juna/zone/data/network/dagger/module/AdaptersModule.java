package life.plank.juna.zone.data.network.dagger.module;

import dagger.Module;
import dagger.Provides;
import life.plank.juna.zone.data.network.dagger.scope.UiScope;
import life.plank.juna.zone.view.adapter.board.creation.BoardColorThemeAdapter;
import life.plank.juna.zone.view.adapter.board.creation.BoardIconAdapter;
import life.plank.juna.zone.view.adapter.user.GetCoinsAdapter;
import life.plank.juna.zone.view.adapter.user.LastTransactionsAdapter;

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