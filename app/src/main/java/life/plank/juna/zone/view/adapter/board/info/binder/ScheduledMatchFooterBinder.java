package life.plank.juna.zone.view.adapter.board.info.binder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahamed.multiviewadapter.ItemBinder;
import com.ahamed.multiviewadapter.ItemViewHolder;

import life.plank.juna.zone.R;

public class ScheduledMatchFooterBinder extends ItemBinder<String, ScheduledMatchFooterBinder.ScheduledMatchFooterViewHolder> {

    @Override
    public ScheduledMatchFooterViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        return new ScheduledMatchFooterViewHolder(inflater.inflate(R.layout.item_scheduled_match_footer, parent, false));
    }

    @Override
    public void bind(ScheduledMatchFooterViewHolder holder, String item) {
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof String;
    }

    static class ScheduledMatchFooterViewHolder extends ItemViewHolder<String> {

        ScheduledMatchFooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}