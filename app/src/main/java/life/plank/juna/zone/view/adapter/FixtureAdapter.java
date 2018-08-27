package life.plank.juna.zone.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ScoreFixture;
import life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection;
import life.plank.juna.zone.util.BaseRecyclerView;
import life.plank.juna.zone.view.activity.BoardActivity;
import life.plank.juna.zone.view.activity.FixtureActivity;

import static life.plank.juna.zone.util.DataUtil.getSeparator;
import static life.plank.juna.zone.util.UIDisplayUtil.alternateBackgroundColor;

public class FixtureAdapter extends BaseRecyclerView.Adapter<FixtureAdapter.FixtureViewHolder> {

    private List<ScoreFixture> scoreFixtureList;
    private FixtureActivity activity;
    private FixtureSection section;

    FixtureAdapter(List<ScoreFixture> scoreFixtureList, FixtureActivity activity, FixtureSection section) {
        this.scoreFixtureList = scoreFixtureList;
        this.activity = activity;
        this.section = section;
    }

    @Override
    public FixtureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FixtureViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fixture, parent, false),
                this
        );
    }

    @Override
    public int getItemCount() {
        return scoreFixtureList.size();
    }

    static class FixtureViewHolder extends BaseRecyclerView.ViewHolder {

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
        private ScoreFixture scoreFixture;

        FixtureViewHolder(View itemView, FixtureAdapter adapter) {
            super(itemView);
            this.ref = new WeakReference<>(adapter);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind() {
            scoreFixture = ref.get().scoreFixtureList.get(getAdapterPosition());
            alternateBackgroundColor(itemView, getAdapterPosition());
            if (scoreFixture.getHomeTeam().getLogoLink() != null) {
                ref.get().activity.picasso
                        .load(scoreFixture.getHomeTeam().getLogoLink())
                        .fit().centerCrop()
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder)
                        .into(homeTeamLogo);
            }
            if (scoreFixture.getHomeTeam().getLogoLink() != null) {
                ref.get().activity.picasso
                        .load(scoreFixture.getAwayTeam().getLogoLink())
                        .fit().centerCrop()
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder)
                        .into(visitingTeamLogo);
            }

            homeTeamName.setText(scoreFixture.getHomeTeam().getName());
            visitingTeamName.setText(scoreFixture.getAwayTeam().getName());
            separatorView.setText(getSeparator(scoreFixture, ref.get().section, winPointer));
        }

        @OnClick(R.id.root_layout)
        public void onFixtureClicked() {
            BoardActivity.launch(
                    ref.get().activity,
                    scoreFixture.getHomeGoals(),
                    scoreFixture.getAwayGoals(),
                    scoreFixture.getForeignId(),
                    scoreFixture.getHomeTeam().getLogoLink(),
                    scoreFixture.getAwayTeam().getLogoLink(),
                    scoreFixture.getHomeTeam().getName(),
                    scoreFixture.getAwayTeam().getName(),
                    scoreFixture.getMatchDay()
            );
        }
    }
}