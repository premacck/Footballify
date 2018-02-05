package life.plank.juna.zone.view.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.GlobalVariable;
import life.plank.juna.zone.util.UIDisplayUtil;

/**
 * Created by plank-hasan on 1/27/2018.
 */

public class LiveZoneGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<String> elements;
    private int gridViewHeight;
    private ArrayList<Integer> images;

    public LiveZoneGridAdapter(Context context) {
        this.context = context;
        this.elements = new ArrayList<>();
    }

    @Override
    public LiveZoneGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(this.context).inflate(R.layout.item_livezone_grid, parent, false);
        return new LiveZoneGridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        LiveZoneGridViewHolder liveZoneGridViewHolder = (LiveZoneGridViewHolder) holder;
        int data = (int) context.getResources().getDimension(R.dimen.cardview_compat_inset_shadow);
        liveZoneGridViewHolder.liveZoneRelativeLayout.getLayoutParams().width = (UIDisplayUtil.getDisplayMetricsData(context, GlobalVariable.getInstance().getDisplayWidth()) / 4) - data;
        liveZoneGridViewHolder.liveZoneRelativeLayout.getLayoutParams().height = (gridViewHeight / 5);
        if ("text".contentEquals(elements.get(position))) {
            liveZoneGridViewHolder.liveZoneRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.grid_item_gray));
        } else {
            liveZoneGridViewHolder.liveZoneRelativeLayout.setBackground(context.getResources().getDrawable(images.get((new Random()).nextInt(images.size()))));
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.elements.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    /**
     * Add data to the view
     *
     * @param liveZoneGridViewHeight : gridViewHeight
     */
    public void addData(int liveZoneGridViewHeight) {
        gridViewHeight = liveZoneGridViewHeight;
        setUpData();
        setUpImages();
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
        ArrayList<String> data = new ArrayList<>();
        data.add("text");
        data.add("text");
        data.add("image");
        data.add("image");
        data.add("image");
        data.add("image");
        data.add("text");
        data.add("image");
        data.add("text");
        data.add("image");
        data.add("text");
        data.add("image");
        data.add("text");
        data.add("image");
        data.add("text");
        data.add("image");
        data.add("text");
        data.add("image");
        data.add("text");
        data.add("image");
        data.add("text");
        data.add("image");
        data.add("image");
        elements.addAll(data);
        elements.addAll(data);
    }

    private void setUpImages() {
        images = new ArrayList<>();
        images.add(R.drawable.ic_grid_one);
        images.add(R.drawable.ic_grid_two);
        images.add(R.drawable.ic_grid_three);
        images.add(R.drawable.ic_grid_four);
        images.add(R.drawable.ic_grid_five);
        images.add(R.drawable.ic_grid_six);
    }

}
