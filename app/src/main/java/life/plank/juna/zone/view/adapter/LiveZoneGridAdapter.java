package life.plank.juna.zone.view.adapter;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.Tile;
import life.plank.juna.zone.interfaces.OnItemClickListener;
import life.plank.juna.zone.util.GlobalVariable;
import life.plank.juna.zone.util.UIDisplayUtil;

/**
 * Created by plank-hasan on 1/27/2018.
 */

public class LiveZoneGridAdapter extends RecyclerView.Adapter<LiveZoneGridAdapter.LiveZoneGridViewHolder> {

    private Context context;
    private List<Tile> tileList;
    private int gridViewHeight;
    private OnItemClickListener onItemClickListener;

    public LiveZoneGridAdapter(Context context) {
        this.context = context;
        this.tileList = new ArrayList<>();
    }

    public void addGridItemsToView(int position, Tile data) {
        tileList.add(position, data);
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
        //TODO: tags will be changed once api is ready so hardcoded
        switch (tileList.get(position).getTag()) {
            case "image": {
                holder.tileContentTextView.setVisibility(View.GONE);
                holder.tileImageView.setVisibility(View.VISIBLE);
                holder.tileImageView.setImageResource(tileList.get(position).getImage());
                holder.tileStickerImageView.setVisibility(View.GONE);
                holder.videoImageView.setVisibility(View.GONE);
                break;
            }
            case "text": {
                holder.tileContentTextView.setVisibility(View.VISIBLE);
                holder.liveZoneRelativeLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.grid_item_grey));
                holder.tileStickerImageView.setVisibility(View.GONE);
                holder.tileImageView.setVisibility(View.GONE);
                holder.videoImageView.setVisibility(View.GONE);
                break;
            }
            case "sticker": {
                holder.tileContentTextView.setVisibility(View.GONE);
                holder.tileImageView.setVisibility(View.VISIBLE);
                holder.tileStickerImageView.setVisibility(View.VISIBLE);
                holder.tileImageView.setImageResource(tileList.get(position).getImage());
                holder.tileStickerImageView.setImageResource(tileList.get(position).getSticker());
                holder.videoImageView.setVisibility(View.GONE);
                break;
            }
            case "video": {
                holder.tileContentTextView.setVisibility(View.GONE);
                holder.tileImageView.setVisibility(View.VISIBLE);
                holder.videoImageView.setVisibility(View.VISIBLE);
                holder.tileImageView.setImageResource(tileList.get(position).getImage());
                holder.tileStickerImageView.setVisibility(View.GONE);
                break;
            }
        }
        holder.tileContentTextView.setText(tileList.get(position).getTweet());
        RxView.clicks(holder.itemView)
                .subscribe(v -> onItemClickListener.onItemClicked(position));
    }


    @Override
    public int getItemCount() {
        return this.tileList.size();
    }

    /**
     * Add data to the view
     *
     * @param liveZoneGridViewHeight : gridViewHeight
     */
    public void computeNewDimensions(int liveZoneGridViewHeight) {
        gridViewHeight = liveZoneGridViewHeight;
        setUpData();
        notifyDataSetChanged();
    }

    private void setUpData() {
        //TODO: tags will be changed once api is ready so hardcoded
        tileList.add(new Tile("image", R.drawable.image0, R.drawable.ic_sticker_four, ""));
        tileList.add(new Tile("video", R.drawable.image_dummy_two, 0, ""));
        tileList.add(new Tile("text", 0, 0, "Why would Mourinho do that? Isn't he done with"));
        tileList.add(new Tile("sticker", R.drawable.image1, R.drawable.ic_sticker_four, ""));
        tileList.add(new Tile("image", R.drawable.image3, 0, ""));
        tileList.add(new Tile("image", R.drawable.image_dummy_four, 0, ""));
        tileList.add(new Tile("image", R.drawable.ic_grid_one, 0, ""));
        tileList.add(new Tile("sticker", R.drawable.ic_third_dummy, R.drawable.ic_sticker_one, ""));
        tileList.add(new Tile("text", 0, 0, "Why would Mourinho do that? Isn't he done with"));
        tileList.add(new Tile("image", R.drawable.ic_football_dummy_image, 0, ""));
        tileList.add(new Tile("video", R.drawable.image5, 0, "Why would Mourinho do that? Isn't he done with"));
        tileList.add(new Tile("text", 0, 0, "Why would Mourinho do that? Isn't he done with"));
        tileList.add(new Tile("sticker", R.drawable.ic_club_list_background, R.drawable.ic_sticker_two, ""));
        tileList.add(new Tile("text", 0, 0, "Why would Mourinho do that? Isn't he done with"));
        tileList.add(new Tile("image", R.drawable.football_image_one, 0, "Why would Mourinho do that? Isn't he done with"));
        tileList.add(new Tile("video", R.drawable.image3, 0, "Why would Mourinho do that? Isn't he done with"));

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class LiveZoneGridViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_content)
        RelativeLayout liveZoneRelativeLayout;
        @BindView(R.id.tile_content_text_view)
        TextView tileContentTextView;
        @BindView(R.id.tile_label_text_view)
        TextView tileLabelTextView;
        @BindView(R.id.tile_image_view)
        ImageView tileImageView;
        @BindView(R.id.tile_sticker_image_view)
        ImageView tileStickerImageView;
        @BindView(R.id.video_image_view)
        ImageView videoImageView;

        public LiveZoneGridViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.card_content)
        public void onViewClicked(View view) {
            //TODO this has to be moved to chat fragment, will be moved in next pull request
            //view.getContext().startActivity(new Intent(view.getContext(), CameraActivity.class));
        }

    }
}
