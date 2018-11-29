package life.plank.juna.zone.view.LatestMatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahamed.multiviewadapter.BaseViewHolder;
import com.ahamed.multiviewadapter.ItemBinder;

import life.plank.juna.zone.R;

class LeagueBinder extends ItemBinder<LeagueModel, LeagueBinder.LeagueViewHolder> {

    @Override
    public LeagueViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        return new LeagueViewHolder(inflater.inflate(R.layout.item_leagues, parent, false));
    }

    @Override
    public void bind(LeagueViewHolder holder, LeagueModel item) {
        // Bind the data here
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof LeagueModel;
    }

    class LeagueViewHolder extends BaseViewHolder<LeagueModel> {

        LeagueViewHolder(View itemView) {
            super(itemView);
        }
        // Normal ViewHolder code
    }
}
