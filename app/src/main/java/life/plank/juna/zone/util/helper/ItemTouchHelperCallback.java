package life.plank.juna.zone.util.helper;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by plank-niraj on 26-01-2018.
 */

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperInterface itemTouchHelperInterface;
    ArrayList<String> data;

    public ItemTouchHelperCallback(ItemTouchHelperInterface itemTouchHelperInterface, ArrayList<String> data) {
        this.itemTouchHelperInterface = itemTouchHelperInterface;
        this.data = data;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }


    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        final int dragFlags = ItemTouchHelper.START;
        // TODO: 26-01-2018 calculate based on device width and server data
        if (position != data.size() - 1)
            return 0;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return itemTouchHelperInterface.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition(), target);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        itemTouchHelperInterface.clearView(viewHolder.getAdapterPosition());
        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }
}
