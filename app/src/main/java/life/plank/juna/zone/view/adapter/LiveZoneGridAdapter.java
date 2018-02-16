package life.plank.juna.zone.view.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
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
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.GlobalVariable;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.view.activity.OpenCamera;

/**
 * Created by plank-hasan on 1/27/2018.
 */

public class LiveZoneGridAdapter extends RecyclerView.Adapter<LiveZoneGridAdapter.LiveZoneGridViewHolder> {

    private Context context;
    private List<Integer> elements;
    private int gridViewHeight;

    public LiveZoneGridAdapter(Context context) {
        this.context = context;
        this.elements = new ArrayList<>();
    }

    public void addData(int position, Integer data) {
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
        holder.liveZoneRelativeLayout.setBackground(context.getResources().getDrawable(elements.get(position)));
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

    private void setUpData() {
        elements.add(R.drawable.ic_grid_three);
        elements.add(R.drawable.ic_grid_two);
        elements.add(R.drawable.ic_grid_one);
        elements.add(R.drawable.ic_grid_four);
        elements.add(R.drawable.ic_grid_five);
        elements.add(R.drawable.ic_grid_six);
        elements.add(R.drawable.ic_grid_three);
        elements.add(R.drawable.ic_grid_two);
        elements.add(R.drawable.ic_grid_one);
        elements.add(R.drawable.ic_grid_four);
        elements.add(R.drawable.ic_grid_five);
        elements.add(R.drawable.meme1);
    }

    private int getRandomBackgroundColor() {
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(context, R.color.Orange));
        colors.add(ContextCompat.getColor(context, R.color.Green));
        colors.add(ContextCompat.getColor(context, R.color.Red));
        colors.add(ContextCompat.getColor(context, R.color.Blue_grey));
        colors.add(ContextCompat.getColor(context, R.color.dark_grey));
        colors.add(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        colors.add(ContextCompat.getColor(context, R.color.orange_end_gradient));
        return colors.get((new Random()).nextInt(colors.size()));
    }

    public class LiveZoneGridViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_content)
        RelativeLayout liveZoneRelativeLayout;

        public LiveZoneGridViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

        @OnClick(R.id.card_content)
        public void onViewClicked(View view) {
            Intent cameraOpen = new Intent(view.getContext(), OpenCamera.class);
            view.getContext().startActivity(cameraOpen);

        }
    }


}
