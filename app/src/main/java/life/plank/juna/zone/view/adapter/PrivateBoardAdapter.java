package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FootballFeed;

/**
 * Created by plank-dhamini on 25/7/2018.
 */

public class PrivateBoardAdapter extends RecyclerView.Adapter<PrivateBoardAdapter.PrivateBoardViewHolder> {
    private ArrayList<FootballFeed> boardFeed;
    private Context context;

    public PrivateBoardAdapter(Context context, ArrayList<FootballFeed> boardFeed) {
        this.context = context;
        this.boardFeed = boardFeed;
    }

    @Override
    public PrivateBoardAdapter.PrivateBoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PrivateBoardAdapter.PrivateBoardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_grid_row, parent, false));
    }

    @Override
    public void onBindViewHolder(PrivateBoardAdapter.PrivateBoardViewHolder holder, int position) {
        holder.tileImageView.setBackground(context.getResources().getDrawable(R.drawable.ic_private_screen));
    }

    @Override
    public int getItemCount() {
        return 50;
    }

    public class PrivateBoardViewHolder extends RecyclerView.ViewHolder {
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