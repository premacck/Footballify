package life.plank.juna.zone.view.activity.base;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import life.plank.juna.zone.data.model.FeedEntry;
import life.plank.juna.zone.view.adapter.BoardFeedDetailAdapter;

public abstract class BaseBoardActivity extends AppCompatActivity {

    public static Bitmap boardParentViewBitmap = null;
    protected boolean isTileFullScreenActive;
    protected BoardFeedDetailAdapter boardFeedDetailAdapter;

    public abstract void prepareFullScreenRecyclerView();

    public abstract void updateFullScreenAdapter(List<FeedEntry> feedEntryList);

    public abstract void moveItem(int position, int previousPosition);

    public abstract void setBlurBackgroundAndShowFullScreenTiles(boolean setFlag, int position);
}