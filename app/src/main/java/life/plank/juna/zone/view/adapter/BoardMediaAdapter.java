package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

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
    private List<FootballFeed> boardFeeds;
    private Context context;

    public BoardMediaAdapter(Context context, List<FootballFeed> boardFeeds) {
        this.context = context;
        this.boardFeeds = boardFeeds;
    }

    @Override
    public BoardMediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BoardMediaViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_board_grid_row, parent, false ) );

    }

    @Override
    public void onBindViewHolder(BoardMediaViewHolder holder, int position) {
        FootballFeed boardFeeds = this.boardFeeds.get( position );
        if (boardFeeds.getThumbnail() != null) {
            Picasso.with( context )
                    .load( boardFeeds.getThumbnail().getImageUrl() )
                    .fit().centerCrop()
                    .placeholder( R.drawable.ic_place_holder )
                    .transform( new RoundedTransformation( UIDisplayUtil.dpToPx( 8, context ), 0 ) )
                    .error( R.drawable.ic_place_holder )
                    .into( holder.tileImageView );
        } else {
            holder.tileImageView.setImageResource( R.drawable.ic_place_holder );
        }
    }

    @Override
    public int getItemCount() {
        return boardFeeds.size();
    }

    public class BoardMediaViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.feed_image_view)
        ImageView tileImageView;

        public BoardMediaViewHolder(View itemView) {

            super( itemView );
            ButterKnife.bind( this, itemView );
        }
    }
}