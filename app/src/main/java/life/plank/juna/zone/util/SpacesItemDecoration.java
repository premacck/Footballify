package life.plank.juna.zone.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by plank-hasan on 1/29/2018.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.right = space;
        //To remove the padding at the bottom.
        if ((parent.getChildLayoutPosition(view) + 1) % 5 == 0) {
            outRect.bottom = 0;
        } else {
            outRect.bottom = space;
        }
    }
}
