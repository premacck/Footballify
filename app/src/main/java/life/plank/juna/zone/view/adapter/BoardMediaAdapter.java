package life.plank.juna.zone.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.interfaces.OnClickFeedItemListener;
import life.plank.juna.zone.view.fragment.board.fixture.BoardTilesFragment;

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

    private List<FootballFeed> boardFeed;
    private OnClickFeedItemListener listener;
    private BoardTilesFragment fragment;

    public BoardMediaAdapter(BoardTilesFragment fragment) {
        this.fragment = fragment;
        this.boardFeed = new ArrayList<>();
    }

    @Override
    public BoardMediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BoardMediaViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_tile, parent, false),
                listener
        );
    }

    @Override
    public void onBindViewHolder(BoardMediaViewHolder holder, int position) {
        switch (boardFeed.get(position).getFeedItem().getContentType()) {
            case AUDIO:
                setVisibility(holder, GONE, VISIBLE, GONE);
                holder.tileImageView.setImageResource(R.drawable.ic_audio);
                break;
            case IMAGE:
                setVisibility(holder, GONE, VISIBLE, GONE);
                if (boardFeed.get(position).getFeedItem().getThumbnail() != null) {
                    fragment.picasso
                            .load(boardFeed.get(position).getFeedItem().getThumbnail().getImageUrl())
                            .fit().centerCrop()
                            .placeholder(R.drawable.ic_place_holder)
                            .error(R.drawable.ic_place_holder)
                            .into(holder.tileImageView);
                }
                break;
            case VIDEO:
                setVisibility(holder, GONE, VISIBLE, VISIBLE);
                if (boardFeed.get(position).getFeedItem().getThumbnail() != null) {
                    fragment.picasso
                            .load(boardFeed.get(position).getFeedItem().getThumbnail().getImageUrl())
                            .placeholder(R.drawable.ic_place_holder)
                            .error(R.drawable.ic_place_holder)
                            .into(holder.tileImageView);
                }
                break;
            default:
                setVisibility(holder, VISIBLE, GONE, GONE);
                String comment = boardFeed.get(position).getFeedItem().getTitle().replaceAll("^\"|\"$", "");
                holder.commentTextView.setBackgroundColor(getCommentColor(comment));
                holder.commentTextView.setText(getCommentText(comment));
                break;
        }
    }

    private void setVisibility(BoardMediaViewHolder holder, int commentTextViewVisibility, int tileImageViewVisibility, int playBtnVisibility) {
        holder.commentTextView.setVisibility(commentTextViewVisibility);
        holder.tileImageView.setVisibility(tileImageViewVisibility);
        holder.playBtn.setVisibility(playBtnVisibility);
    }


    public void update(List<FootballFeed> boardFeed) {
        if (!this.boardFeed.isEmpty()) {
            this.boardFeed.clear();
        }
        this.boardFeed.addAll(boardFeed);
        notifyDataSetChanged();
    }

    public void updateNew(List<FootballFeed> boardFeed) {
        int previousSize = this.boardFeed.size();
        this.boardFeed.addAll(boardFeed);
        notifyItemRangeInserted(previousSize, boardFeed.size());
    }


    public void updateNewPost(FootballFeed feedItem) {
        boardFeed.add(0, feedItem);
        notifyItemInserted(0);
    }

    public List<FootballFeed> getBoardFeed() {
        return boardFeed;
    }

    public void setOnClickFeedItemListener(OnClickFeedItemListener onClickFeedItemListener) {
        this.listener = onClickFeedItemListener;
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

        BoardMediaViewHolder(View itemView, OnClickFeedItemListener listener) {
            super(itemView);
            this.listener = listener;
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.root_layout)
        public void onBoardItemClick() {
            listener.onItemClick(getAdapterPosition(), itemView);
        }

        @OnLongClick(R.id.root_layout)
        public boolean onBoardItemLongClick() {
            listener.onItemClick(getAdapterPosition(), itemView);
            return true;
        }
    }
}