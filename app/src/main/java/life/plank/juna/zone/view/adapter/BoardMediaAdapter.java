package life.plank.juna.zone.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.FeedEntry;
import life.plank.juna.zone.data.model.FeedItem;
import life.plank.juna.zone.interfaces.OnClickFeedItemListener;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static life.plank.juna.zone.util.AppConstants.AUDIO;
import static life.plank.juna.zone.util.AppConstants.IMAGE;
import static life.plank.juna.zone.util.AppConstants.VIDEO;
import static life.plank.juna.zone.util.UIDisplayUtil.getCommentColor;
import static life.plank.juna.zone.util.UIDisplayUtil.getCommentText;

/**
 * Created by plank-prachi on 4/10/2018.
 */
public class BoardMediaAdapter extends RecyclerView.Adapter<BoardMediaAdapter.BoardMediaViewHolder> {

    private final RequestManager glide;
    private List<FeedEntry> boardFeed;
    private OnClickFeedItemListener listener;

    public BoardMediaAdapter(RequestManager glide, OnClickFeedItemListener listener) {
        this.glide = glide;
        this.listener = listener;
        this.boardFeed = new ArrayList<>();
    }

    @NonNull
    @Override
    public BoardMediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BoardMediaViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_tile, parent, false),
                listener
        );
    }

    @Override
    public void onBindViewHolder(@NonNull BoardMediaViewHolder holder, int position) {
        FeedItem feedItem = boardFeed.get(position).getFeedItem();

        //TODO: remove this null check after the backend returns the user profile picture
        if (feedItem.getUser() != null) {
            glide.load(feedItem.getUser().getProfilePictureUrl())
                    .apply(RequestOptions.centerCropTransform()
                            .placeholder(R.drawable.ic_default_profile)
                            .error(R.drawable.ic_default_profile))
                    .into(holder.profilePictureImageView);
        }

        if (feedItem.getContentType() == null) return;

        switch (feedItem.getContentType()) {
            case AUDIO:
                setVisibility(holder, GONE, VISIBLE, GONE);
                holder.tileImageView.setImageResource(R.drawable.ic_mic_white);
                break;
            case IMAGE:
                setVisibility(holder, GONE, VISIBLE, GONE);
                if (feedItem.getThumbnail() != null) {
                    glide.load(feedItem.getThumbnail().getImageUrl())
                            .apply(RequestOptions.centerCropTransform().placeholder(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder))
                            .into(holder.tileImageView);
                }
                break;
            case VIDEO:
                setVisibility(holder, GONE, VISIBLE, VISIBLE);
                if (feedItem.getThumbnail() != null) {
                    glide.load(feedItem.getThumbnail().getImageUrl())
                            .apply(RequestOptions.centerCropTransform().placeholder(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder))
                            .into(holder.tileImageView);
                }
                break;
            default:
                setVisibility(holder, VISIBLE, GONE, GONE);
                if (feedItem.getTitle() == null) break;
                String comment = feedItem.getTitle().replaceAll("^\"|\"$", "");
                holder.commentTextView.setBackground(getCommentColor(comment));
                holder.commentTextView.setText(getCommentText(comment));
                break;
        }
    }

    private void setVisibility(BoardMediaViewHolder holder, int commentTextViewVisibility, int tileImageViewVisibility, int playBtnVisibility) {
        holder.commentTextView.setVisibility(commentTextViewVisibility);
        holder.tileImageView.setVisibility(tileImageViewVisibility);
        holder.playBtn.setVisibility(playBtnVisibility);
    }

    public void update(List<FeedEntry> boardFeed) {
        if (! this.boardFeed.isEmpty()) {
            this.boardFeed.clear();
        }
        this.boardFeed.addAll(boardFeed);
        notifyDataSetChanged();
    }

    public void updateNew(List<FeedEntry> boardFeed) {
        int previousSize = this.boardFeed.size();
        this.boardFeed.addAll(boardFeed);
        notifyItemRangeInserted(previousSize, boardFeed.size());
    }

    public void updateNewPost(FeedEntry feedItem) {
        boardFeed.add(0, feedItem);
        notifyItemInserted(0);
    }

    public List<FeedEntry> getBoardFeed() {
        return boardFeed;
    }

    @Override
    public int getItemCount() {
        return boardFeed.size();
    }

    static class BoardMediaViewHolder extends RecyclerView.ViewHolder {

        private final OnClickFeedItemListener listener;

        @BindView(R.id.tile_image_view)
        ImageView tileImageView;
        @BindView(R.id.play_btn)
        ImageView playBtn;
        @BindView(R.id.comment_text_view)
        TextView commentTextView;
        @BindView(R.id.profile_picture)
        ImageView profilePictureImageView;

        BoardMediaViewHolder(View itemView, OnClickFeedItemListener listener) {
            super(itemView);
            this.listener = listener;
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.root_layout)
        void onBoardItemClick() {
            listener.onItemClick(getAdapterPosition());
        }

        @OnLongClick(R.id.root_layout)
        boolean onBoardItemLongClick() {
            listener.onItemLongClick(getAdapterPosition());
            return true;
        }
    }
}