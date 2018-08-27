package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ScoreFixture;
import life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection;
import life.plank.juna.zone.util.BaseRecyclerView;
import life.plank.juna.zone.util.RoundedTransformation;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.view.activity.BoardActivity;

import static life.plank.juna.zone.util.DataUtil.getSeparator;
import static life.plank.juna.zone.util.UIDisplayUtil.alternateBackgroundColor;

public class FixtureAdapter extends BaseRecyclerView.Adapter<FixtureAdapter.FixtureViewHolder> {

    private List<ScoreFixture> scoreFixtureList;
    private Picasso picasso;
    private Context context;
    private FixtureSection fixtureSection;

    FixtureAdapter(List<ScoreFixture> scoreFixtureList, Picasso picasso,
                   Context context, FixtureSection fixtureSection) {
        this.scoreFixtureList = scoreFixtureList;
        this.picasso = picasso;
        this.context = context;
        this.fixtureSection = fixtureSection;
    }

    @Override
    public FixtureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FixtureViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fixture, parent, false),
                this,
                picasso,
                context
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
        private final Picasso picasso;
        private final Context context;
        private ScoreFixture scoreFixture;

        FixtureViewHolder(View itemView, FixtureAdapter adapter, Picasso picasso, Context context) {
            super(itemView);
            this.ref = new WeakReference<>(adapter);
            this.picasso = picasso;
            this.context = context;
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind() {
            scoreFixture = ref.get().scoreFixtureList.get(getAdapterPosition());
            alternateBackgroundColor(itemView, getAdapterPosition());
            if (scoreFixture.getHomeTeam().getLogoLink() != null) {
                picasso
                        .load(scoreFixture.getHomeTeam().getLogoLink())
                        .fit().centerCrop()
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder)
                        .transform(new RoundedTransformation(UIDisplayUtil.dpToPx(8, context), 0))
                        .into(homeTeamLogo);
            }
            if (scoreFixture.getHomeTeam().getLogoLink() != null) {
                picasso
                        .load(scoreFixture.getAwayTeam().getLogoLink())
                        .fit().centerCrop()
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder)
                        .transform(new RoundedTransformation(UIDisplayUtil.dpToPx(8, context), 0))
                        .into(visitingTeamLogo);
            }

            homeTeamName.setText(scoreFixture.getHomeTeam().getName());
            visitingTeamName.setText(scoreFixture.getAwayTeam().getName());
            separatorView.setText(getSeparator(scoreFixture, ref.get().fixtureSection, winPointer));
        }

        @OnClick(R.id.root_layout)
        public void onFixtureClicked() {
            BoardActivity.launch(
                    context,
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