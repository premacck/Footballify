package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.UserFeed;

public class UserFeedAdapter extends RecyclerView.Adapter<UserFeedAdapter.UserFeedViewHolder> {
    private Context context;
    private ArrayList<UserFeed> userFeed;

    public UserFeedAdapter(Context context, ArrayList<UserFeed> userFeed) {
        this.context = context;
        this.userFeed = userFeed;
    }

    @Override
    public UserFeedAdapter.UserFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserFeedAdapter.UserFeedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_grid_row, parent, false));
    }

    @Override
    public void onBindViewHolder(UserFeedAdapter.UserFeedViewHolder holder, int position) {

        if (userFeed.get(position).getFeedItem() != null) {
            Picasso.with(context)
                    .load(userFeed.get(position).getFeedItem().getThumbnail().getImageUrl())
                    .fit().centerCrop()
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_place_holder)
                    .into(holder.tileImageView);
        }
    }

    @Override
    public int getItemCount() {
        return userFeed.size();
    }

    class UserFeedViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tile_image_view)
        ImageView tileImageView;

        UserFeedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
