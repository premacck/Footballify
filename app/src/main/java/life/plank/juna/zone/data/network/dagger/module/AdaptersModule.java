package life.plank.juna.zone.data.network.dagger.module;

import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;
import life.plank.juna.zone.data.network.dagger.scope.UiScope;
import life.plank.juna.zone.view.adapter.BoardColorThemeAdapter;
import life.plank.juna.zone.view.adapter.BoardIconAdapter;
import life.plank.juna.zone.view.adapter.GetCoinsAdapter;
import life.plank.juna.zone.view.adapter.LastTransactionsAdapter;
import life.plank.juna.zone.view.adapter.MyBoardsAdapter;

@Module
public class AdaptersModule {

    @UiScope
    @Provides
    public BoardColorThemeAdapter provideBoardColorThemeAdapter() {
        return new BoardColorThemeAdapter();
    }

    @UiScope
    @Provides
    public BoardIconAdapter provideBoardIconAdapter(Picasso picasso) {
        return new BoardIconAdapter(picasso);
    }

    @UiScope
    @Provides
    public GetCoinsAdapter provideGetCoinsAdapter() {
        return new GetCoinsAdapter();
    }

    @UiScope
    @Provides
    public MyBoardsAdapter provideMyBoardsAdapter(Picasso picasso) {
        return new MyBoardsAdapter(picasso);
    }

    @UiScope
    @Provides
    public LastTransactionsAdapter provideLastTransactionsAdapter() {
        return new LastTransactionsAdapter();
    }
}