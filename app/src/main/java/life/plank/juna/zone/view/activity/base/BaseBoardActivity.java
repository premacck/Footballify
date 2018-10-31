package life.plank.juna.zone.view.activity.base;

import life.plank.juna.zone.interfaces.FeedEntryContainer;
import life.plank.juna.zone.view.adapter.BoardFeedDetailAdapter;

public abstract class BaseBoardActivity extends BaseCardActivity implements FeedEntryContainer {

    protected boolean isTileFullScreenActive;
    protected BoardFeedDetailAdapter boardFeedDetailAdapter;
}