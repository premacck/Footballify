package life.plank.juna.zone.view.adapter;

import android.app.Activity;
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
import life.plank.juna.zone.interfaces.OnClickFeedItemListener;
import life.plank.juna.zone.interfaces.PinFeedListener;
import life.plank.juna.zone.util.GlobalVariable;
import life.plank.juna.zone.view.activity.LeagueInfoActivity;

public class FootballFeedAdapter extends RecyclerView.Adapter<FootballFeedAdapter.FootballFeedViewHolder> {

    private List<FootballFeed> footballFeedList;
    private PinFeedListener pinFeedListener;
    private OnClickFeedItemListener onClickFeedItemListener;
    private Activity activity;

    public FootballFeedAdapter(Activity activity) {
        this.activity = activity;
        this.footballFeedList = new ArrayList<>();
        if (activity instanceof PinFeedListener) {
            pinFeedListener = (PinFeedListener) activity;
        }
        if (activity instanceof OnClickFeedItemListener) {
            onClickFeedItemListener = (OnClickFeedItemListener) activity;
        }
    }

    @Override
    public FootballFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.football_feed_row, parent, false);
        return new FootballFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FootballFeedViewHolder holder, int position) {
        FootballFeed footballFeed = footballFeedList.get(position);
        holder.feedTitleTextView.setText(footballFeed.getTitle());
        holder.kickoffTime.setText(R.string.match_status);
        if (footballFeed.getThumbnail() != null) {
            Picasso.with(activity)
                    .load(footballFeed.getThumbnail().getImageUrl())
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_place_holder)
                    .into(holder.feedImageView);
        } else {
            holder.feedImageView.setImageResource(R.drawable.ic_place_holder);
        }
        holder.itemView.setOnClickListener(view -> {
            GlobalVariable.getInstance().setTilePosition(position);
            LeagueInfoActivity.launch(
                    activity,
                    footballFeed.getSeasonName(),
                    footballFeed.getTitle(),
                    footballFeed.getCountryName(),
                    footballFeed.getThumbnail().getImageUrl()
            );
        });
    }

    @Override
    public int getItemCount() {
        return footballFeedList.size();
    }

    public List<FootballFeed> getFootballFeedList() {
        return footballFeedList;
    }

    public void setFootballFeedList(List<FootballFeed> footballFeeds) {
        if (footballFeeds == null) {
            return;
        }
        footballFeedList.addAll(footballFeeds);
        notifyDataSetChanged();
    }

    static class FootballFeedViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.feed_title_text_view)
        TextView feedTitleTextView;
        @BindView(R.id.kickoff_time)
        TextView kickoffTime;
        @BindView(R.id.feed_image_view)
        ImageView feedImageView;

        FootballFeedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

