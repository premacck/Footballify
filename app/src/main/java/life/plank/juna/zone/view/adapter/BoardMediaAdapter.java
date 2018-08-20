package life.plank.juna.zone.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.interfaces.OnClickFeedItemListener;

import static life.plank.juna.zone.util.UIDisplayUtil.getCommentColor;
import static life.plank.juna.zone.util.UIDisplayUtil.getCommentText;

/**
 * Created by plank-prachi on 4/10/2018.
 */
public class BoardMediaAdapter extends RecyclerView.Adapter<BoardMediaAdapter.BoardMediaViewHolder> {

    private List<FootballFeed> boardFeed;
    private Picasso picasso;
    private OnClickFeedItemListener listener;

    public BoardMediaAdapter(Picasso picasso) {
        this.picasso = picasso;
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
        if (boardFeed.get(position).getThumbnail() != null) {
            switch (boardFeed.get(position).getContentType()) {
                case "Audio": {
                    holder.commentTextView.setVisibility(View.INVISIBLE);
                    holder.tileImageView.setVisibility(View.VISIBLE);
                    holder.tileImageView.setImageResource(R.drawable.ic_audio);
                    break;
                }
                case "Image": {
                    holder.commentTextView.setVisibility(View.INVISIBLE);
                    holder.tileImageView.setVisibility(View.VISIBLE);
                    picasso
                            .load(boardFeed.get(position).getThumbnail().getImageUrl())
                            .fit().centerCrop()
                            .placeholder(R.drawable.ic_place_holder)
                            .error(R.drawable.ic_place_holder)
                            .into(holder.tileImageView);
                    break;
                }
                case "Video": {
                    holder.commentTextView.setVisibility(View.INVISIBLE);
                    holder.tileImageView.setVisibility(View.VISIBLE);
                    String uri = boardFeed.get(position).getUrl();
                    picasso
                            .load(uri)
                            .placeholder(R.drawable.ic_video)
                            .error(R.drawable.ic_video)
                            .into(holder.tileImageView);
                    break;
                }
            }
        } else {
            if (boardFeed.get(position).getContentType().equals("rootComment")) {
                String comment = boardFeed.get(position).getTitle().replaceAll("^\"|\"$", "");
                holder.tileImageView.setVisibility(View.INVISIBLE);
                holder.commentTextView.setBackgroundColor(getCommentColor(comment));
                holder.commentTextView.setText(getCommentText(comment));

            }
        }
    }

    public void update(List<FootballFeed> boardFeed) {
        this.boardFeed.addAll(boardFeed);
        notifyDataSetChanged();
    }

    public void updateNewPost(FootballFeed footballFeed) {
        boardFeed.add(0, footballFeed);
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
        @BindView(R.id.comment_text_view)
        TextView commentTextView;

        BoardMediaViewHolder(View itemView, OnClickFeedItemListener listener) {
            super(itemView);
            this.listener = listener;
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.root_layout)
        public void onBoardItemClick() {
            listener.onItemClick(getAdapterPosition());
        }
    }
}