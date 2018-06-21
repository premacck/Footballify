package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.util.RoundedTransformation;
import life.plank.juna.zone.util.UIDisplayUtil;

/**
 * Created by plank-prachi on 4/10/2018.
 */
public class BoardMediaAdapter extends RecyclerView.Adapter<BoardMediaAdapter.BoardMediaViewHolder> {
    private ArrayList<FootballFeed> boardFeed = new ArrayList<>();
    private Context context;

    public BoardMediaAdapter(Context context, ArrayList<FootballFeed> boardFeed) {
        this.context = context;
        this.boardFeed = boardFeed;
    }

    @Override
    public BoardMediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BoardMediaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_grid_row, parent, false));

    }

    @Override
    public void onBindViewHolder(BoardMediaViewHolder holder, int position) {
        if (boardFeed.get(position).getThumbnail().getImageUrl() != null) {
            holder.titleTextView.setText(boardFeed.get(position).getTitle());
            if (boardFeed.get(position).getContentType().equalsIgnoreCase(context.getString(R.string.audio))) {
                holder.tileImageView.setImageResource(R.drawable.ic_audio);
            } else {
                Picasso.with(context)
                        .load(boardFeed.get(position).getThumbnail().getImageUrl())
                        .fit().centerCrop()
                        .placeholder(R.drawable.ic_place_holder)
                        .transform(new RoundedTransformation(UIDisplayUtil.dpToPx(8, context), 0))
                        .error(R.drawable.ic_place_holder)
                        .into(holder.tileImageView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return boardFeed.size();
    }

    public class BoardMediaViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tile_image_view)
        ImageView tileImageView;
        @BindView(R.id.feed_title_text_view)
        TextView titleTextView;

        public BoardMediaViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}