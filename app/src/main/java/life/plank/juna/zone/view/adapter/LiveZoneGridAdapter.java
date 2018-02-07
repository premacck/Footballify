package life.plank.juna.zone.view.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.GlobalVariable;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.viewmodel.LiveZoneGridModel;

/**
 * Created by plank-hasan on 1/27/2018.
 */

public class LiveZoneGridAdapter extends RecyclerView.Adapter<LiveZoneGridAdapter.LiveZoneGridViewHolder> {

    private Context context;
    private List<LiveZoneGridModel> elements;
    private int gridViewHeight;

    public LiveZoneGridAdapter(Context context) {
        this.context = context;
        this.elements = new ArrayList<>();
    }

    public void addData(int position, LiveZoneGridModel data) {
        elements.add(position, data);
        notifyItemInserted(position);

    }

    @Override
    public LiveZoneGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(this.context).inflate(R.layout.item_livezone_grid, parent, false);
        return new LiveZoneGridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LiveZoneGridAdapter.LiveZoneGridViewHolder holder, final int position) {
        int data = (int) context.getResources().getDimension(R.dimen.cardview_compat_inset_shadow);
        holder.liveZoneRelativeLayout.getLayoutParams().width = (UIDisplayUtil.getDisplayMetricsData(context,
                GlobalVariable.getInstance().getDisplayWidth()) / 4) - data;
        holder.liveZoneRelativeLayout.getLayoutParams().height = (gridViewHeight / 5);
        if ("text".contentEquals(elements.get(position).getData())) {
            holder.liveZoneRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.grid_item_grey));
        } else {
            holder.liveZoneRelativeLayout.setBackground(context.getResources().getDrawable(elements.get(position).getImage()));
        }

    }


    @Override
    public int getItemCount() {
        return this.elements.size();
    }


    /**
     * Add data to the view
     *
     * @param liveZoneGridViewHeight : gridViewHeight
     */
    public void addData(int liveZoneGridViewHeight) {
        gridViewHeight = liveZoneGridViewHeight;
        setUpData();
        notifyDataSetChanged();
    }

    public class LiveZoneGridViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_content)
        RelativeLayout liveZoneRelativeLayout;

        public LiveZoneGridViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private void setUpData() {
        elements.addAll(LiveZoneGridModel.getLiveZoneData());
        elements.addAll(LiveZoneGridModel.getLiveZoneData());
    }


}
