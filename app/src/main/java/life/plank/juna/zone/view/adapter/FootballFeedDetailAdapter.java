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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.util.GlobalVariable;

/**
 * Created by plank-prachi on 1/30/2018.
 */

public class FootballFeedDetailAdapter extends RecyclerView.Adapter<FootballFeedDetailAdapter.FootballFeedDetailViewHolder> {
    private Context context;
    private LayoutInflater mInflater;
    private List<FootballFeed> footballFeedsList = new ArrayList<>();

    public class FootballFeedDetailViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.feed_image)
        ImageView footballFeedImage;
        @BindView(R.id.feed_content)
        TextView footballFeedContent;
        @BindView(R.id.feed_content_details)
        TextView footballFeedContentDetails;

        public FootballFeedDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public FootballFeedDetailAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        footballFeedsList = GlobalVariable.getInstance().getFootballFeeds();
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
        holder.footballFeedContent.setText(footballFeedsList.get(position).getHeadline());
        holder.footballFeedContentDetails.setText(footballFeedsList.get(position).getSummary());
        if (footballFeedsList.get(position).getThumbnail() != null) {
            Picasso.with(context)
                    .load(footballFeedsList.get(position).getThumbnail().getImageUrl())
                    .fit()
                    .into(holder.footballFeedImage);
        } else {
            holder.footballFeedImage.setImageResource(R.drawable.ic_third_dummy);
        }
    }

    @Override
    public int getItemCount() {
        return footballFeedsList.size();
    }
}
