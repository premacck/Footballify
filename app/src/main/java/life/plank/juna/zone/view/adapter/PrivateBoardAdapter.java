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
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.FeedItem;

import static life.plank.juna.zone.util.UIDisplayUtil.getCommentColor;
import static life.plank.juna.zone.util.UIDisplayUtil.getCommentText;

/**
 * Created by plank-dhamini on 25/7/2018.
 */

public class PrivateBoardAdapter extends RecyclerView.Adapter<PrivateBoardAdapter.PrivateBoardViewHolder> {
    private ArrayList<FeedItem> boardFeed;
    private Picasso picasso;

    public PrivateBoardAdapter(Picasso picasso) {
        this.picasso = picasso;
        this.boardFeed = new ArrayList<>();
    }

    @Override
    public PrivateBoardAdapter.PrivateBoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PrivateBoardAdapter.PrivateBoardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_grid_row, parent, false));
    }

    @Override
    public void onBindViewHolder(PrivateBoardViewHolder holder, int position) {
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

                holder.commentTextView.setBackground(getCommentColor(comment));
                holder.commentTextView.setText(getCommentText(comment));

            }
        }
    }

    @Override
    public int getItemCount() {
        return boardFeed.size();
    }

    public void update(List<FeedItem> boardFeed) {
        this.boardFeed.addAll(boardFeed);
        notifyDataSetChanged();
    }

    public void updateNewPost(FeedItem feedItem) {
        boardFeed.add(0, feedItem);
        notifyItemInserted(0);
    }

    static class PrivateBoardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tile_image_view)
        ImageView tileImageView;
        @BindView(R.id.comment_text_view)
        TextView commentTextView;

        PrivateBoardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}