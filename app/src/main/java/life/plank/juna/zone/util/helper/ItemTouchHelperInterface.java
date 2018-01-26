package life.plank.juna.zone.util.helper;

import android.support.v7.widget.RecyclerView;

/**
 * Created by plank-niraj on 26-01-2018.
 */

public interface ItemTouchHelperInterface {
    boolean onItemMove(int fromPosition, int toPosition, RecyclerView.ViewHolder target);
}
