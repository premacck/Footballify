package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

/**
 * Created by plank-dhamini on 18/7/2018.
 */
public class ZoneAdapter extends RecyclerView.Adapter<ZoneAdapter.ZoneViewHolder> {
    private static final String TAG = ZoneAdapter.class.getSimpleName();
    private Context context;

    public ZoneAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ZoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ZoneViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zone_grid, parent, false));

    }

    @Override
    public void onBindViewHolder(ZoneViewHolder holder, int position) {
        holder.zoneImageView.setOnClickListener(view -> {
            if (holder.followTick.getVisibility() == View.VISIBLE) {
                holder.followTick.setVisibility(View.INVISIBLE);
                holder.zoneImageView.setAlpha(255);
            } else {
                holder.followTick.setVisibility(View.VISIBLE);
                holder.zoneImageView.setAlpha(160);
            }
        });
    }

    @Override
    public int getItemCount() {
        //TODO: Replace with actual number of zones
        return 15;
    }

    public class ZoneViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.zone_image_view)
        ImageView zoneImageView;
        @BindView(R.id.zone_title_text_view)
        TextView zoneTitle;
        @BindView(R.id.follow_image_view)
        ImageView followTick;

        ZoneViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}