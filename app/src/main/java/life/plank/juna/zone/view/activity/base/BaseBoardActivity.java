package life.plank.juna.zone.view.activity.base;

import android.graphics.Bitmap;

import life.plank.juna.zone.interfaces.PeekPreviewContainer;
import life.plank.juna.zone.view.adapter.BoardFeedDetailAdapter;

public abstract class BaseBoardActivity extends StackableCardActivity implements PeekPreviewContainer {

    public static Bitmap boardParentViewBitmap = null;
    protected boolean isTileFullScreenActive;
    protected BoardFeedDetailAdapter boardFeedDetailAdapter;
}