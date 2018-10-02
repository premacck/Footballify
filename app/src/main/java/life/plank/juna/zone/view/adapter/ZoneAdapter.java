package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.Zones;
import life.plank.juna.zone.interfaces.OnClickZoneItemListener;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.PreferenceManager.getToken;

/**
 * Created by plank-dhamini on 18/7/2018.
 */
public class ZoneAdapter extends RecyclerView.Adapter<ZoneAdapter.ZoneViewHolder> {
    private static final String TAG = ZoneAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<Zones> zones;
    private OnClickZoneItemListener onClickZoneItemListener;

    public ZoneAdapter(Context context, ArrayList<Zones> zones, OnClickZoneItemListener onClickZoneItemListener) {
        this.context = context;
        this.zones = zones;
        this.onClickZoneItemListener = onClickZoneItemListener;
    }

    @Override
    public ZoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ZoneViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zone_grid, parent, false));

    }

    @Override
    public void onBindViewHolder(ZoneViewHolder holder, int position) {

        holder.zoneTitle.setText(zones.get(position).name);

        holder.zoneImageView.setOnClickListener(view -> {
            if (holder.followTick.getVisibility() == View.VISIBLE) {
                holder.followTick.setVisibility(View.INVISIBLE);
                holder.zoneImageView.setAlpha(context.getResources().getInteger(R.integer.opaque));
                holder.zoneTitle.setVisibility(View.VISIBLE);
                holder.followersCount.setVisibility(View.VISIBLE);

                ViewGroup.LayoutParams params = holder.zoneImageView.getLayoutParams();
                params.width = holder.zoneImageView.getMeasuredWidth() + context.getResources().getInteger(R.integer.zone_grid_layout_param);
                params.height = holder.zoneImageView.getMeasuredWidth() + context.getResources().getInteger(R.integer.zone_grid_layout_param);
                holder.zoneImageView.setLayoutParams(params);
                onClickZoneItemListener.onItemClick(zones.get(position).id,false);

            } else {
                holder.followTick.setVisibility(View.VISIBLE);
                holder.zoneImageView.setAlpha(context.getResources().getInteger(R.integer.visiblilty_160));
                holder.zoneTitle.setVisibility(View.INVISIBLE);
                holder.followersCount.setVisibility(View.INVISIBLE);
                ViewGroup.LayoutParams params = holder.zoneImageView.getLayoutParams();

                params.width = holder.zoneImageView.getMeasuredWidth() - context.getResources().getInteger(R.integer.zone_grid_layout_param);
                params.height = holder.zoneImageView.getMeasuredWidth() - context.getResources().getInteger(R.integer.zone_grid_layout_param);
                holder.zoneImageView.setLayoutParams(params);
                onClickZoneItemListener.onItemClick(zones.get(position).id,true);
            }
        });
    }


    @Override
    public int getItemCount() {
        return zones.size();
    }

    public class ZoneViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.zone_image_view)
        ImageView zoneImageView;
        @BindView(R.id.zone_title_text_view)
        TextView zoneTitle;
        @BindView(R.id.followers_count_layout)
        LinearLayout followersCount;
        @BindView(R.id.follow_image_view)
        ImageView followTick;

        ZoneViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}