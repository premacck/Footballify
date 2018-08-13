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
import life.plank.juna.zone.data.network.model.ScoreFixtureModel;
import life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection;
import life.plank.juna.zone.util.BaseRecyclerView;
import life.plank.juna.zone.util.RoundedTransformation;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.view.activity.BoardActivity;

import static life.plank.juna.zone.util.DataUtil.getSeparator;
import static life.plank.juna.zone.util.DateUtil.getFormattedDate;

public class FixtureAndResultSectionAdapter extends BaseRecyclerView.Adapter<FixtureAndResultSectionAdapter.ViewHolder> {

    private List<ScoreFixtureModel> scoreFixtureModelList;
    private Picasso picasso;
    private Context context;
    private FixtureSection fixtureSection;

    FixtureAndResultSectionAdapter(List<ScoreFixtureModel> scoreFixtureModelList, Picasso picasso,
                                   Context context, FixtureSection fixtureSection) {
        this.scoreFixtureModelList = scoreFixtureModelList;
        this.picasso = picasso;
        this.context = context;
        this.fixtureSection = fixtureSection;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match_fixture, parent, false),
                this,
                picasso,
                context
        );
    }

    @Override
    public int getItemCount() {
        return scoreFixtureModelList.size();
    }

    static class ViewHolder extends BaseRecyclerView.ViewHolder {

        @BindView(R.id.home_team_logo)
        ImageView homeTeamLogo;
        @BindView(R.id.away_team_logo)
        ImageView awayTeamLogo;
        @BindView(R.id.date_schedule)
        TextView dateView;
        @BindView(R.id.team_names)
        TextView teamNamesView;

        private ScoreFixtureModel scoreFixture;
        private final WeakReference<FixtureAndResultSectionAdapter> ref;
        private final Picasso picasso;
        private final Context context;

        ViewHolder(View itemView, FixtureAndResultSectionAdapter adapter, Picasso picasso, Context context) {
            super(itemView);
            this.ref = new WeakReference<>(adapter);
            this.picasso = picasso;
            this.context = context;
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind() {
            scoreFixture = ref.get().scoreFixtureModelList.get(getAdapterPosition());
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
                        .into(awayTeamLogo);
            }

            String teamNameText = scoreFixture.getHomeTeam().getName() +
                    getSeparator(context, scoreFixture, ref.get().fixtureSection) +
                    scoreFixture.getAwayTeam().getName();
            teamNamesView.setText(teamNameText);
            dateView.setText(getFormattedDate(context, scoreFixture));
        }

        @OnClick(R.id.root_layout)
        public void onFixtureClicked() {
            BoardActivity.launch(
                    context,
                    scoreFixture.getHomeGoals(),
                    scoreFixture.getAwayGoals(),
                    scoreFixture.getForeignId(),
                    scoreFixture.getHomeTeam().getLogoLink(),
                    scoreFixture.getAwayTeam().getLogoLink()
            );
        }
    }
}