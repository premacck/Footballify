package life.plank.juna.zone.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.MatchFixture;
import life.plank.juna.zone.util.BaseRecyclerView;
import life.plank.juna.zone.util.DataUtil.ScrubberLoader;

import static life.plank.juna.zone.util.DataUtil.getSeparator;
import static life.plank.juna.zone.util.UIDisplayUtil.alternateBackgroundColor;
import static life.plank.juna.zone.util.UIDisplayUtil.getDp;
import static life.plank.juna.zone.util.UIDisplayUtil.getEndDrawableTarget;
import static life.plank.juna.zone.util.UIDisplayUtil.getStartDrawableTarget;

public class FixtureLeagueAdapter extends BaseRecyclerView.Adapter<FixtureLeagueAdapter.FixtureLeagueViewHolder> {

    private Picasso picasso;
    private List<MatchFixture> matchFixtureList;

    public FixtureLeagueAdapter(Picasso picasso) {
        this.picasso = picasso;
        matchFixtureList = new ArrayList<>();
    }

    @Override
    public FixtureLeagueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FixtureLeagueViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fixture_league, parent, false), this);
    }

    @Override
    public int getItemCount() {
        return matchFixtureList.size();
    }

    public void update(List<MatchFixture> matchFixtureList) {
        this.matchFixtureList.addAll(matchFixtureList);
        notifyDataSetChanged();
    }

    static class FixtureLeagueViewHolder extends BaseRecyclerView.ViewHolder {

        @BindView(R.id.home_team_name)
        TextView homeTeamNameTextView;
        @BindView(R.id.score)
        TextView scoreTextView;
        @BindView(R.id.win_pointer)
        ImageView winPointer;
        @BindView(R.id.visiting_team_name)
        TextView visitingTeamNameTextView;
        @BindView(R.id.time_status)
        TextView timeStatusTextView;
        @BindView(R.id.scrubber)
        LineChart scrubber;

        private final WeakReference<FixtureLeagueAdapter> ref;

        FixtureLeagueViewHolder(View itemView, FixtureLeagueAdapter adapter) {
            super(itemView);
            ref = new WeakReference<>(adapter);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind() {
            MatchFixture scoreFixture = ref.get().matchFixtureList.get(getAdapterPosition());
            alternateBackgroundColor(itemView, getAdapterPosition());
            if (scoreFixture.getHomeTeam().getLogoLink() != null) {
                ref.get().picasso
                        .load(scoreFixture.getHomeTeam().getLogoLink())
                        .resize((int) getDp(12), (int) getDp(12))
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder)
                        .into(getEndDrawableTarget(homeTeamNameTextView));
            }
            if (scoreFixture.getHomeTeam().getLogoLink() != null) {
                ref.get().picasso
                        .load(scoreFixture.getAwayTeam().getLogoLink())
                        .resize((int) getDp(12), (int) getDp(12))
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder)
                        .into(getStartDrawableTarget(visitingTeamNameTextView));
            }

            homeTeamNameTextView.setText(scoreFixture.getHomeTeam().getName());
            visitingTeamNameTextView.setText(scoreFixture.getAwayTeam().getName());
            scoreTextView.setText(getSeparator(scoreFixture, winPointer, false));
            timeStatusTextView.setText(scoreFixture.getTimeStatus());
            ScrubberLoader.prepare(scrubber, false);
        }
    }
}