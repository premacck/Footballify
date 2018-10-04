package life.plank.juna.zone.view.adapter;

import android.support.annotation.ColorRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import de.hdodenhof.circleimageview.CircleImageView;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.FeedEntry;
import life.plank.juna.zone.data.model.FeedItem;
import life.plank.juna.zone.util.customview.ShimmerRelativeLayout;
import life.plank.juna.zone.view.activity.UserFeedActivity;
import life.plank.juna.zone.view.activity.post.PostDetailActivity;

import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;

public class UserFeedAdapter extends RecyclerView.Adapter<UserFeedAdapter.UserFeedViewHolder> {

    private UserFeedActivity activity;
    private Picasso picasso;
    private List<FeedEntry> userFeed;
    private boolean isUpdated;

    public UserFeedAdapter(UserFeedActivity activity, Picasso picasso) {
        this.activity = activity;
        this.picasso = picasso;
        this.userFeed = new ArrayList<>();
        isUpdated = false;
    }

    @Override
    public UserFeedAdapter.UserFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserFeedAdapter.UserFeedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_grid_row, parent, false), this);
    }

    @Override
    public void onBindViewHolder(UserFeedAdapter.UserFeedViewHolder holder, int position) {
        if (!isNullOrEmpty(userFeed)) {
            updateShimmer(holder, R.color.transparent, false);
            FeedItem feedItem = userFeed.get(position).getFeedItem();
            if (feedItem.getThumbnail() != null) {
                picasso.load(feedItem.getThumbnail().getImageUrl())
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder)
                        .into(holder.tileImageView);
            } else holder.tileImageView.setImageResource(R.drawable.ic_place_holder);

            if (feedItem.getUser() != null) {
                picasso.load(feedItem.getUser().getProfilePictureUrl())
                        .placeholder(R.drawable.ic_default_profile)
                        .error(R.drawable.ic_default_profile)
                        .into(holder.profilePic);
            } else holder.profilePic.setImageResource(R.drawable.ic_default_profile);
        } else {
            if (!isUpdated) {
                updateShimmer(holder, R.color.circle_background_color, true);
            } else {
                updateShimmer(holder, R.color.transparent, false);
            }
        }
    }

    private void updateShimmer(UserFeedAdapter.UserFeedViewHolder holder, @ColorRes int color, boolean isStarted) {
        holder.commentTextView.setBackgroundColor(activity.getResources().getColor(color, null));
        if (isStarted) {
            holder.rootLayout.startShimmerAnimation();
        } else {
            holder.rootLayout.stopShimmerAnimation();
        }
    }

    @Override
    public int getItemCount() {
        return userFeed.isEmpty() ? 12 : userFeed.size();
    }

    public void setUserFeed(List<FeedEntry> userFeed) {
        isUpdated = true;
        this.userFeed.clear();
        this.userFeed = userFeed;
        notifyDataSetChanged();
    }

    static class UserFeedViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tile_image_view)
        ImageView tileImageView;
        @BindView(R.id.comment_text_view)
        TextView commentTextView;
        @BindView(R.id.profile_pic)
        CircleImageView profilePic;
        @BindView(R.id.root_layout)
        ShimmerRelativeLayout rootLayout;

        private final WeakReference<UserFeedAdapter> ref;

        UserFeedViewHolder(View itemView, UserFeedAdapter userFeedAdapter) {
            super(itemView);
            this.ref = new WeakReference<>(userFeedAdapter);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.root_layout)
        public void onFeedEntryClick() {
            PostDetailActivity.launch(ref.get().activity, ref.get().userFeed, null, getAdapterPosition());
        }

        @OnLongClick(R.id.root_layout)
        public boolean onFeedEntryLongClick() {
            ref.get().activity.setBlurBackgroundAndShowFullScreenTiles(true, getAdapterPosition());
            return true;
        }
    }
}