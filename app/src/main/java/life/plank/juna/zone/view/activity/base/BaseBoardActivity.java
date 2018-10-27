package life.plank.juna.zone.view.activity.base;

import android.graphics.Bitmap;

import life.plank.juna.zone.interfaces.FeedEntryContainer;
import life.plank.juna.zone.view.adapter.BoardFeedDetailAdapter;

public abstract class BaseBoardActivity extends StackableCardActivity implements FeedEntryContainer {

    public static Bitmap boardParentViewBitmap = null;
    protected boolean isTileFullScreenActive;
    protected BoardFeedDetailAdapter boardFeedDetailAdapter;
}