package life.plank.juna.zone.view.activity.base;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.View;

import java.util.List;

import life.plank.juna.zone.data.model.FeedEntry;
import life.plank.juna.zone.util.OnSwipeTouchListener;
import life.plank.juna.zone.view.adapter.BoardFeedDetailAdapter;

public abstract class BaseBoardActivity extends StackableCardActivity {

    public static Bitmap boardParentViewBitmap = null;
    protected boolean isTileFullScreenActive;
    protected BoardFeedDetailAdapter boardFeedDetailAdapter;

    public abstract void dismissFullScreenRecyclerView();

    public abstract void prepareFullScreenRecyclerView();

    public abstract void updateFullScreenAdapter(List<FeedEntry> feedEntryList);

    public abstract void moveItem(int position, int previousPosition);

    public abstract void setBlurBackgroundAndShowFullScreenTiles(boolean setFlag, int position);

    @SuppressLint("ClickableViewAccessibility")
    protected void setupFullScreenRecyclerViewSwipeGesture(View recyclerViewDragArea, View boardTilesFullRecyclerView) {
        recyclerViewDragArea.setOnTouchListener(new OnSwipeTouchListener(this, recyclerViewDragArea, boardTilesFullRecyclerView) {
            @Override
            public void onSwipeDown() {
                dismissFullScreenRecyclerView();
            }
        });
    }
}