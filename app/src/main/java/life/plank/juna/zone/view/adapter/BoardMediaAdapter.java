package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

/**
 * Created by plank-prachi on 4/10/2018.
 */
public class BoardMediaAdapter extends RecyclerView.Adapter<BoardMediaAdapter.BoardMediaViewHolder> {
    private Context context;


    public BoardMediaAdapter(Context context) {
        this.context = context;
    }

    @Override
    public BoardMediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BoardMediaViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_board_grid_row, parent, false ) );

    }

    @Override
    public void onBindViewHolder(BoardMediaViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class BoardMediaViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tile_image_view)
        ImageView tileImageView;

        public BoardMediaViewHolder(View itemView) {

            super( itemView );
            ButterKnife.bind( this, itemView );
        }
    }
}