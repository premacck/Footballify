package life.plank.juna.zone.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.model.League;
import life.plank.juna.zone.util.GlobalVariable;
import life.plank.juna.zone.view.activity.base.BaseCardActivity;
import life.plank.juna.zone.view.fragment.football.LeagueInfoFragment;

public class FootballLeagueAdapter extends RecyclerView.Adapter<FootballLeagueAdapter.FootballFeedViewHolder> {

    private List<League> leagueList;
    private BaseCardActivity activity;

    public FootballLeagueAdapter(BaseCardActivity activity) {
        this.activity = activity;
        this.leagueList = new ArrayList<>();
    }

    @NonNull
    @Override
    public FootballFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.football_feed_row, parent, false);
        return new FootballFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FootballFeedViewHolder holder, int position) {
        League league = leagueList.get(position);
        holder.feedTitleTextView.setText(league.getName());
//        TODO: Replace with original time to next match
        holder.kickoffTime.setText(ZoneApplication.getContext().getString(R.string.match_status, "2hrs 13 mins"));

        holder.feedImageView.setImageResource(league.getLeagueLogo());

        holder.itemView.setOnClickListener(view -> {
            GlobalVariable.getInstance().setTilePosition(position);
            activity.pushFragment(LeagueInfoFragment.Companion.newInstance(league), true);
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

