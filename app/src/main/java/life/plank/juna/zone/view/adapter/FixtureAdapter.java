package life.plank.juna.zone.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.MatchFixture;
import life.plank.juna.zone.util.BaseRecyclerView;
import life.plank.juna.zone.util.DataUtil;
import life.plank.juna.zone.view.activity.MatchBoardActivity;
import life.plank.juna.zone.view.activity.base.BaseLeagueActivity;

import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_LIVE;
import static life.plank.juna.zone.util.DataUtil.getSeparator;
import static life.plank.juna.zone.util.DateUtil.getMatchTimeValue;
import static life.plank.juna.zone.util.UIDisplayUtil.alternateBackgroundColor;
import static life.plank.juna.zone.util.UIDisplayUtil.getDp;
import static life.plank.juna.zone.util.UIDisplayUtil.getEndDrawableTarget;
import static life.plank.juna.zone.util.UIDisplayUtil.getStartDrawableTarget;

public class FixtureAdapter extends BaseRecyclerView.Adapter<BaseRecyclerView.ViewHolder> {

    private static final int FIXTURE_LIVE = 0;
    private static final int FIXTURE_NOT_LIVE = 1;

    private List<MatchFixture> matchFixtureList;
    private BaseLeagueActivity activity;

    public FixtureAdapter(List<MatchFixture> matchFixtureList, BaseLeagueActivity activity) {
        this.matchFixtureList = matchFixtureList;
        this.activity = activity;
        if (this.matchFixtureList == null) {
            this.matchFixtureList = new ArrayList<>();
        }
    }

    @Override
    public BaseRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FIXTURE_LIVE) {
            return new LiveFixtureViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fixture_league, parent, false), this);
        } else {
            return new NotLiveFixtureViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fixture, parent, false), this);
        }
    }

    @Override
    public int getItemCount() {
        return matchFixtureList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getMatchTimeValue(matchFixtureList.get(position).getMatchStartTime(), false) == MATCH_LIVE ? FIXTURE_LIVE : FIXTURE_NOT_LIVE;
    }

    public void update(List<MatchFixture> matchFixtureList) {
        this.matchFixtureList.addAll(matchFixtureList);
        notifyDataSetChanged();
    }

    public static class NotLiveFixtureViewHolder extends BaseRecyclerView.ViewHolder {

        @BindView(R.id.home_team_name)
        TextView homeTeamName;
        @BindView(R.id.home_team_logo)
        ImageView homeTeamLogo;
        @BindView(R.id.date_schedule)
        TextView separatorView;
        @BindView(R.id.win_pointer)
        ImageView winPointer;
        @BindView(R.id.visiting_team_logo)
        ImageView visitingTeamLogo;
        @BindView(R.id.visiting_team_name)
        TextView visitingTeamName;

        private final WeakReference<FixtureAdapter> ref;
        private MatchFixture scoreFixture;

        NotLiveFixtureViewHolder(View itemView, FixtureAdapter adapter) {
            super(itemView);
            this.ref = new WeakReference<>(adapter);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind() {
            scoreFixture = ref.get().matchFixtureList.get(getAdapterPosition());
            alternateBackgroundColor(itemView, getAdapterPosition());
            if (scoreFixture.getHomeTeam().getLogoLink() != null) {
                ref.get().activity.getPicasso()
                        .load(scoreFixture.getHomeTeam().getLogoLink())
                        .fit().centerCrop()
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder)
                        .into(homeTeamLogo);
            }
            if (scoreFixture.getHomeTeam().getLogoLink() != null) {
                ref.get().activity.getPicasso()
                        .load(scoreFixture.getAwayTeam().getLogoLink())
                        .fit().centerCrop()
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder)
                        .into(visitingTeamLogo);
            }

            homeTeamName.setText(scoreFixture.getHomeTeam().getName());
            visitingTeamName.setText(scoreFixture.getAwayTeam().getName());
            separatorView.setText(getSeparator(scoreFixture, winPointer, false));
        }

        @OnClick(R.id.root_layout)
        public void onFixtureClicked() {
            MatchBoardActivity.launch(ref.get().activity, scoreFixture, ref.get().activity.getLeague());
        }
    }

    static class LiveFixtureViewHolder extends BaseRecyclerView.ViewHolder {

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

        private final WeakReference<FixtureAdapter> ref;
        private MatchFixture scoreFixture;

        LiveFixtureViewHolder(View itemView, FixtureAdapter adapter) {
            super(itemView);
            ref = new WeakReference<>(adapter);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind() {
            scoreFixture = ref.get().matchFixtureList.get(getAdapterPosition());
            alternateBackgroundColor(itemView, getAdapterPosition());
            if (scoreFixture.getHomeTeam().getLogoLink() != null) {
                ref.get().activity.getPicasso()
                        .load(scoreFixture.getHomeTeam().getLogoLink())
                        .resize((int) getDp(12), (int) getDp(12))
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder)
                        .into(getEndDrawableTarget(homeTeamNameTextView));
            }
            if (scoreFixture.getHomeTeam().getLogoLink() != null) {
                ref.get().activity.getPicasso()
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
            DataUtil.ScrubberLoader.prepare(scrubber, false);
        }

        @OnClick(R.id.root_layout)
        public void onFixtureClicked() {
            MatchBoardActivity.launch(ref.get().activity, scoreFixture, ref.get().activity.getLeague());
        }
    }
}