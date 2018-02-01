package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

/**
 * Created by plank-prachi on 1/30/2018.
 */

public class FootballFeedDetailAdapter extends RecyclerView.Adapter<FootballFeedDetailAdapter.FootballFeedDetailViewHolder> {
    String[] playersDetails = {"TEAMS", "LEAGUES/CUPS", "PUNDITS", "LIVEZONE", "TEAMS", "LEAGUES/CUPS", "PUNDITS", "LIVEZONE"};
    int[] images = {R.drawable.ic_third_dummy, R.drawable.ic_second_dummy, R.drawable.ic_fourth_dummy, R.drawable.ic_football_dummy_image, R.drawable.ic_third_dummy, R.drawable.ic_second_dummy, R.drawable.ic_fourth_dummy, R.drawable.ic_football_dummy_image};

    private Context context;
    private LayoutInflater mInflater;

    public class FootballFeedDetailViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.feed_image)
        ImageView footballFeedImage;
        @BindView(R.id.feed_content)
        TextView footballFeedContent;

        public FootballFeedDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public FootballFeedDetailAdapter(Context context, String[] playersDetails) {
        this.mInflater = LayoutInflater.from(context);
        this.playersDetails = playersDetails;

    }

    @Override
    public FootballFeedDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.football_feed_detail_row, parent, false);
        context = parent.getContext();
        return new FootballFeedDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FootballFeedDetailViewHolder holder, int position) {
        holder.footballFeedContent.setText(playersDetails[position]);
        holder.footballFeedImage.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return playersDetails.length;
    }


}
