package life.plank.juna.zone.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FeedEntry;

public class UserFeedAdapter extends RecyclerView.Adapter<UserFeedAdapter.UserFeedViewHolder> {
    private Picasso picasso;
    private List<FeedEntry> userFeed;

    public UserFeedAdapter(Picasso picasso) {
        this.picasso = picasso;
        this.userFeed = new ArrayList<>();
    }

    @Override
    public UserFeedAdapter.UserFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserFeedAdapter.UserFeedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_grid_row, parent, false));
    }

    @Override
    public void onBindViewHolder(UserFeedAdapter.UserFeedViewHolder holder, int position) {
        if (userFeed.get(position).getFeedItem() != null) {
            picasso.load(userFeed.get(position).getFeedItem().getThumbnail().getImageUrl())
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_place_holder)
                    .into(holder.tileImageView);
        }
    }

    @Override
    public int getItemCount() {
        return userFeed.size();
    }

    public void setUserFeed(List<FeedEntry> userFeed) {
        this.userFeed.clear();
        this.userFeed = userFeed;
        notifyDataSetChanged();
    }

    static class UserFeedViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tile_image_view)
        ImageView tileImageView;

        UserFeedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
