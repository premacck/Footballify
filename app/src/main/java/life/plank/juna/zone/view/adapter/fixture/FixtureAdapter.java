package life.plank.juna.zone.view.adapter.fixture;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.github.mikephil.charting.charts.LineChart;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.MatchFixture;
import life.plank.juna.zone.interfaces.LeagueContainer;
import life.plank.juna.zone.util.view.BaseRecyclerView;

import static life.plank.juna.zone.util.common.AppConstants.MatchTimeVal.MATCH_LIVE;
import static life.plank.juna.zone.util.common.DataUtil.getSeparator;
import static life.plank.juna.zone.util.common.ScrubberUtilKt.loadScrubber;
import static life.plank.juna.zone.util.time.DateUtil.getMatchTimeValue;
import static life.plank.juna.zone.util.view.UIDisplayUtil.alternateBackgroundColor;
import static life.plank.juna.zone.util.view.UIDisplayUtil.getDp;
import static life.plank.juna.zone.util.view.UIDisplayUtil.getEndDrawableTarget;
import static life.plank.juna.zone.util.view.UIDisplayUtil.getStartDrawableTarget;

public class FixtureAdapter extends BaseRecyclerView.Adapter<BaseRecyclerView.ViewHolder> {

    private static final int FIXTURE_LIVE = 0;
    private static final int FIXTURE_NOT_LIVE = 1;

    private List<MatchFixture> matchFixtureList;
    private LeagueContainer leagueContainer;

    FixtureAdapter(List<MatchFixture> matchFixtureList, LeagueContainer leagueContainer) {
        this.matchFixtureList = matchFixtureList;
        this.leagueContainer = leagueContainer;
        if (this.matchFixtureList == null) {
            this.matchFixtureList = new ArrayList<>();
        }
    }

    @NonNull
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
        this.matchFixtureList = matchFixtureList;
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

            ref.get().leagueContainer.getGlide()
                    .load(scoreFixture.getHomeTeam().getLogoLink())
                    .apply(RequestOptions.centerCropTransform().placeholder(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder))
                    .into(homeTeamLogo);

            ref.get().leagueContainer.getGlide()
                    .load(scoreFixture.getAwayTeam().getLogoLink())
                    .apply(RequestOptions.centerCropTransform().placeholder(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder))
                    .into(visitingTeamLogo);

            homeTeamName.setText(scoreFixture.getHomeTeam().getName());
            visitingTeamName.setText(scoreFixture.getAwayTeam().getName());
            separatorView.setText(getSeparator(scoreFixture.toMatchDetails(), winPointer, false));
        }

        @OnClick(R.id.root_layout)
        void onFixtureClicked() {
            ref.get().leagueContainer.onFixtureSelected(scoreFixture, ref.get().leagueContainer.getTheLeague());
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

            ref.get().leagueContainer.getGlide()
                    .load(scoreFixture.getHomeTeam().getLogoLink())
                    .apply(RequestOptions.centerCropTransform()
                            .placeholder(R.drawable.ic_place_holder)
                            .error(R.drawable.ic_place_holder)
                            .override((int) getDp(12), (int) getDp(12)))
                    .into(getEndDrawableTarget(homeTeamNameTextView));

            ref.get().leagueContainer.getGlide()
                    .load(scoreFixture.getAwayTeam().getLogoLink())
                    .apply(RequestOptions.centerCropTransform()
                            .placeholder(R.drawable.ic_place_holder)
                            .error(R.drawable.ic_place_holder)
                            .override((int) getDp(12), (int) getDp(12)))
                    .into(getStartDrawableTarget(visitingTeamNameTextView));

            homeTeamNameTextView.setText(scoreFixture.getHomeTeam().getName());
            visitingTeamNameTextView.setText(scoreFixture.getAwayTeam().getName());
            scoreTextView.setText(getSeparator(scoreFixture.toMatchDetails(), winPointer, false));
            timeStatusTextView.setText(scoreFixture.getTimeStatus());
            loadScrubber(scrubber, false);
        }

        @OnClick(R.id.root_layout)
        void onFixtureClicked() {
            ref.get().leagueContainer.onFixtureSelected(scoreFixture, ref.get().leagueContainer.getTheLeague());
        }
    }
}