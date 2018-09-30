package life.plank.juna.zone.view.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FeedItem;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.interfaces.OnClickFeedItemListener;
import life.plank.juna.zone.view.fragment.board.fixture.BoardTilesFragment;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static life.plank.juna.zone.util.AppConstants.AUDIO;
import static life.plank.juna.zone.util.AppConstants.IMAGE;
import static life.plank.juna.zone.util.AppConstants.ROOT_COMMENT;
import static life.plank.juna.zone.util.AppConstants.VIDEO;
import static life.plank.juna.zone.util.UIDisplayUtil.getCommentColor;
import static life.plank.juna.zone.util.UIDisplayUtil.getCommentText;
import static life.plank.juna.zone.util.UIDisplayUtil.getDp;
import static life.plank.juna.zone.util.UIDisplayUtil.getSuitableFeedTileSize;
import static life.plank.juna.zone.util.UIDisplayUtil.getScreenSize;

/**
 * Created by plank-prachi on 4/10/2018.
 */
public class BoardMediaAdapter extends RecyclerView.Adapter<BoardMediaAdapter.BoardMediaViewHolder> {

    private final int MEDIA_TILE_WIDTH;
    private final int TEXT_TILE_WIDTH;

    private List<FootballFeed> boardFeed;
    private OnClickFeedItemListener listener;
    private BoardTilesFragment fragment;

    public BoardMediaAdapter(BoardTilesFragment fragment) {
        int screenWidth = (int) (getScreenSize(Objects.requireNonNull(fragment.getActivity()).getWindowManager().getDefaultDisplay())[0] - getDp(8));
        MEDIA_TILE_WIDTH = (int) ((screenWidth / 3) - getDp(2));
        TEXT_TILE_WIDTH = (int) (((screenWidth * 2) / 3) - getDp(1));
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

    /*TODO: Check why every gradient comment is taking two tiles to display the content.
    TODO: Gradients are displayed as squares in the xml. Cant change them to circles. Investigate why they cant be displayed as circles*/
    @Override
    public void onBindViewHolder(BoardMediaViewHolder holder, int position) {
        FeedItem footballFeed = boardFeed.get(position).getFeedItem();
        footballFeed.setTileWidth(Objects.equals(footballFeed.getContentType(), ROOT_COMMENT) ? TEXT_TILE_WIDTH : MEDIA_TILE_WIDTH);
        setItemWidth(holder.itemView, position, footballFeed);
        switch (footballFeed.getContentType()) {
            case AUDIO:
                setVisibility(holder, GONE, VISIBLE, GONE);
                holder.tileImageView.setImageResource(R.drawable.ic_audio);
                break;
            case IMAGE:
                setVisibility(holder, GONE, VISIBLE, GONE);
                if (footballFeed.getThumbnail() != null) {
                    fragment.picasso
                            .load(footballFeed.getThumbnail().getImageUrl())
                            .fit().centerCrop()
                            .placeholder(R.drawable.ic_place_holder)
                            .error(R.drawable.ic_place_holder)
                            .into(holder.tileImageView);
                }
                break;
            case VIDEO:
                setVisibility(holder, GONE, VISIBLE, VISIBLE);
                if (footballFeed.getThumbnail() != null) {
                    fragment.picasso
                            .load(footballFeed.getThumbnail().getImageUrl())
                            .placeholder(R.drawable.ic_place_holder)
                            .error(R.drawable.ic_place_holder)
                            .into(holder.tileImageView);
                }
                break;
            default:
                setVisibility(holder, VISIBLE, GONE, GONE);
                String comment = footballFeed.getTitle().replaceAll("^\"|\"$", "");

                if(comment.startsWith("red")|| comment.startsWith("pink")||
                        comment.startsWith("yellow")||comment.startsWith("green")||comment.startsWith("blue")){
                    holder.commentTextView.setBackgroundColor(Color.GREEN);
                    holder.commentTextView.setText(getCommentText(comment));

                }else{
                    holder.commentTextView.setBackground(getCommentColor(comment));
                    holder.commentTextView.setText(getCommentText(comment));
                }


                break;
        }
    }

    private void setItemWidth(View itemView, int position, FeedItem footballFeed) {
        ViewGroup.LayoutParams params = itemView.getLayoutParams();
        if (Objects.equals(footballFeed.getContentType(), ROOT_COMMENT)) {
            switch (position) {
                case 0:
                    footballFeed.setTileWidth(TEXT_TILE_WIDTH);
                    break;
                case 1:
                case 2:
                    footballFeed.setTileWidth(MEDIA_TILE_WIDTH);
                    break;
                default:
                    footballFeed.setTileWidth(getSuitableFeedTileSize(boardFeed, position, MEDIA_TILE_WIDTH, TEXT_TILE_WIDTH));
                    break;
            }
        } else {
            footballFeed.setTileWidth(MEDIA_TILE_WIDTH);
        }
        params.width = footballFeed.getTileWidth();
        itemView.setLayoutParams(params);
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