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
import life.plank.juna.zone.data.network.model.League;
import life.plank.juna.zone.util.GlobalVariable;
import life.plank.juna.zone.view.activity.LeagueInfoActivity;
import life.plank.juna.zone.view.activity.SwipePageActivity;

import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;

public class FootballLeagueAdapter extends RecyclerView.Adapter<FootballLeagueAdapter.FootballFeedViewHolder> {

    private List<League> leagueList;
    private SwipePageActivity activity;

    public FootballLeagueAdapter(SwipePageActivity activity) {
        this.activity = activity;
        this.leagueList = new ArrayList<>();
    }

    @Override
    public FootballFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.football_feed_row, parent, false);
        return new FootballFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FootballFeedViewHolder holder, int position) {
        League league = leagueList.get(position);
        holder.feedTitleTextView.setText(league.getName());
        holder.kickoffTime.setText(R.string.match_status);
        if (!isNullOrEmpty(league.getThumbUrl())) {
            Picasso.with(activity)
                    .load(league.getThumbUrl())
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_place_holder)
                    .into(holder.feedImageView);
        } else {
            holder.feedImageView.setImageResource(R.drawable.ic_place_holder);
        }
        holder.itemView.setOnClickListener(view -> {
            GlobalVariable.getInstance().setTilePosition(position);
            LeagueInfoActivity.launch(activity, activity.gson.toJson(league));
        });
    }

    @Override
    public int getItemCount() {
        return leagueList.size();
    }

    public List<League> getLeagueList() {
        return leagueList;
    }

    public void setLeagueList(List<League> footballFeeds) {
        if (footballFeeds == null) {
            return;
        }
        leagueList.addAll(footballFeeds);
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

