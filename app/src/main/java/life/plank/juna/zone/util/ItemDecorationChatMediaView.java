package life.plank.juna.zone.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemDecorationChatMediaView extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int space = 10;
        outRect.right = space;
        outRect.top = space;
        int position = parent.getChildViewHolder(view).getAdapterPosition();
        if ((position ) % 5 == 0){
            outRect.left = space;
        }
    }
}
