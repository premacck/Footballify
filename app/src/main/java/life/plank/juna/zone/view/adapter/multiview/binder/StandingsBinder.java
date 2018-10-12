package life.plank.juna.zone.view.adapter.multiview.binder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahamed.multiviewadapter.ItemBinder;
import com.ahamed.multiviewadapter.ItemViewHolder;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.binder.StandingsBindingModel;
import life.plank.juna.zone.view.adapter.StandingTableAdapter;
import life.plank.juna.zone.view.adapter.multiview.BoardInfoAdapter;

import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.UIDisplayUtil.getDp;

public class StandingsBinder extends ItemBinder<StandingsBindingModel, StandingsBinder.StandingsViewHolder> {

    private Picasso picasso;
    private BoardInfoAdapter.BoardInfoAdapterListener listener;

    public StandingsBinder(Picasso picasso, BoardInfoAdapter.BoardInfoAdapterListener listener) {
        this.picasso = picasso;
        this.listener = listener;
    }

    @Override
    public StandingsViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        return new StandingsViewHolder(inflater.inflate(R.layout.item_standings, parent, false), this);
    }

    @Override
    public void bind(StandingsViewHolder holder, StandingsBindingModel item) {
        holder.standingsProgressBar.setVisibility(View.GONE);
        if (item.getErrorMessage() != null || isNullOrEmpty(item.getStandingsList())) {
            holder.noStandings.setVisibility(View.VISIBLE);
            holder.noStandings.setText(item.getErrorMessage());
            holder.standingsHeader.setVisibility(View.INVISIBLE);
            holder.seeAllStandings.setVisibility(View.INVISIBLE);
            holder.standingRecyclerView.setVisibility(View.INVISIBLE);
            return;
        }

        holder.noStandings.setVisibility(View.INVISIBLE);
        holder.standingsHeader.setVisibility(View.VISIBLE);
        holder.seeAllStandings.setVisibility(View.VISIBLE);
        holder.standingRecyclerView.setVisibility(View.VISIBLE);
        StandingTableAdapter adapter = new StandingTableAdapter(picasso);
        holder.standingRecyclerView.setAdapter(adapter);
        adapter.update(item.getStandingsList());
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.standingRecyclerView.getLayoutParams();
        params.height = (int) getDp(65);
        holder.standingRecyclerView.setLayoutParams(params);
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof StandingsBindingModel;
    }

    static class StandingsViewHolder extends ItemViewHolder<StandingsBindingModel> {

        @BindView(R.id.standing_header_layout)
        RelativeLayout standingsHeader;
        @BindView(R.id.standing_recycler_view)
        RecyclerView standingRecyclerView;
        @BindView(R.id.no_standings)
        TextView noStandings;
        @BindView(R.id.see_all_standings)
        TextView seeAllStandings;
        @BindView(R.id.standings_progress_bar)
        ProgressBar standingsProgressBar;

        private final WeakReference<StandingsBinder> ref;

        StandingsViewHolder(View itemView, StandingsBinder standingsBinder) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ref = new WeakReference<>(standingsBinder);
        }

        @OnClick(R.id.see_all_standings)
        public void onSeeAllStandingsClick() {
            ref.get().listener.onSeeAllStandingsClick(itemView);
        }
    }
}
