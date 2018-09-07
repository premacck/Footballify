package life.plank.juna.zone.util.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.TeamStatsModel;

public class TeamStatsLayout extends FrameLayout {

    @BindView(R.id.teams_logo_layout)
    RelativeLayout teamsLogoLayout;
    @BindView(R.id.match_team_stats_layout)
    LinearLayout matchTeamStatsLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.league_name)
    TextView leagueName;
    @BindView(R.id.no_data)
    TextView noDataTextView;

    @BindView(R.id.home_team_logo)
    ImageView homeTeamLogoImageView;
    @BindView(R.id.home_team_win)
    TextView homeTeamWin;
    @BindView(R.id.home_team_loss)
    TextView homeTeamLoss;
    @BindView(R.id.home_team_goal)
    TextView homeTeamGoals;
    @BindView(R.id.home_team_passes)
    TextView homeTeamPasses;
    @BindView(R.id.home_team_shots)
    TextView homeTeamShots;
    @BindView(R.id.home_team_yellow_card)
    TextView homeTeamYellowCard;
    @BindView(R.id.home_team_red_card)
    TextView homeTeamRedCard;

    @BindView(R.id.visiting_team_logo)
    ImageView visitingTeamLogoImageView;
    @BindView(R.id.visiting_team_win)
    TextView visitingTeamWin;
    @BindView(R.id.visiting_team_loss)
    TextView visitingTeamLoss;
    @BindView(R.id.visiting_team_goal)
    TextView visitingTeamGoals;
    @BindView(R.id.visiting_team_passes)
    TextView visitingTeamPasses;
    @BindView(R.id.visiting_team_shots)
    TextView visitingTeamShots;
    @BindView(R.id.visiting_team_yellow_card)
    TextView visitingTeamYellowCard;
    @BindView(R.id.visiting_team_red_card)
    TextView visitingTeamRedCard;

    public TeamStatsLayout(@NonNull Context context) {
        this(context, null);
    }

    public TeamStatsLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TeamStatsLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TeamStatsLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View rootView = inflate(context, R.layout.item_team_stats, this);
        ButterKnife.bind(this, rootView);
    }

    public void update(List<TeamStatsModel> teamStatModels, String homeLogo, String visitingLogo, Picasso picasso) {
        progressBar.setVisibility(GONE);
        noDataTextView.setVisibility(GONE);
        teamsLogoLayout.setVisibility(VISIBLE);
        matchTeamStatsLayout.setVisibility(VISIBLE);
//        TODO : set actual league name in next pull request.
        setLeagueName("")
                .setHomeTeamWin((int) teamStatModels.get(0).getWin())
                .setHomeTeamLoss((int) teamStatModels.get(0).getLoss())
                .setHomeTeamGoals((int) teamStatModels.get(0).getGoal())
                .setHomeTeamPasses((int) teamStatModels.get(0).getPass())
                .setHomeTeamShots((int) teamStatModels.get(0).getShot())
                .setHomeTeamYellowCard((int) teamStatModels.get(0).getYellowCard())
                .setHomeTeamRedCard((int) teamStatModels.get(0).getRedCard())
                .setVisitingTeamWin((int) teamStatModels.get(1).getWin())
                .setVisitingTeamLoss((int) teamStatModels.get(1).getLoss())
                .setVisitingTeamGoals((int) teamStatModels.get(1).getGoal())
                .setVisitingTeamPasses((int) teamStatModels.get(1).getPass())
                .setVisitingTeamShots((int) teamStatModels.get(1).getShot())
                .setVisitingTeamYellowCard((int) teamStatModels.get(1).getYellowCard())
                .setVisitingTeamRedCard((int) teamStatModels.get(1).getRedCard());

        picasso.load(homeLogo)
                .into(homeTeamLogoImageView);
        picasso.load(visitingLogo)
                .into(visitingTeamLogoImageView);
    }

    public void notAvailable(@StringRes int message) {
        noDataTextView.setText(message);
        progressBar.setVisibility(GONE);
        noDataTextView.setVisibility(VISIBLE);
        teamsLogoLayout.setVisibility(INVISIBLE);
        matchTeamStatsLayout.setVisibility(INVISIBLE);
    }

    public void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? VISIBLE : GONE);
        teamsLogoLayout.setVisibility(isLoading ? INVISIBLE : VISIBLE);
        matchTeamStatsLayout.setVisibility(isLoading ? INVISIBLE : VISIBLE);
    }

    public TeamStatsLayout setLeagueName(String leagueName) {
        this.leagueName.setText(leagueName);
        return this;
    }

    /**
     * @return the instance of TeamStats (in all other setters too), useful in method chaining.
     */
    public TeamStatsLayout setHomeTeamShots(int homeTeamShots) {
        this.homeTeamShots.setText(String.valueOf(homeTeamShots));
        return this;
    }

    public TeamStatsLayout setHomeTeamWin(int homeTeamWin) {
        this.homeTeamWin.setText(String.valueOf(homeTeamWin));
        return this;
    }

    public TeamStatsLayout setHomeTeamLoss(int homeTeamLoss) {
        String possession = homeTeamLoss + "%";
        this.homeTeamLoss.setText(String.valueOf(possession));
        return this;
    }

    public TeamStatsLayout setHomeTeamGoals(int homeTeamGoals) {
        this.homeTeamGoals.setText(String.valueOf(homeTeamGoals));
        return this;
    }

    public TeamStatsLayout setHomeTeamYellowCard(int homeTeamYellowCard) {
        this.homeTeamYellowCard.setText(String.valueOf(homeTeamYellowCard));
        return this;
    }

    public TeamStatsLayout setHomeTeamRedCard(int homeTeamRedCard) {
        this.homeTeamRedCard.setText(String.valueOf(homeTeamRedCard));
        return this;
    }

    public TeamStatsLayout setHomeTeamPasses(int homeTeamPasses) {
        this.homeTeamPasses.setText(String.valueOf(homeTeamPasses));
        return this;
    }

    public TeamStatsLayout setVisitingTeamShots(int visitingTeamShots) {
        this.visitingTeamShots.setText(String.valueOf(visitingTeamShots));
        return this;
    }

    public TeamStatsLayout setVisitingTeamWin(int visitingTeamWin) {
        this.visitingTeamWin.setText(String.valueOf(visitingTeamWin));
        return this;
    }

    public TeamStatsLayout setVisitingTeamLoss(int visitingTeamLoss) {
        String possession = visitingTeamLoss + "%";
        this.visitingTeamLoss.setText(possession);
        return this;
    }

    public TeamStatsLayout setVisitingTeamGoals(int visitingTeamGoals) {
        this.visitingTeamGoals.setText(String.valueOf(visitingTeamGoals));
        return this;
    }

    public TeamStatsLayout setVisitingTeamPasses(int visitingTeamPasses) {
        this.visitingTeamPasses.setText(String.valueOf(visitingTeamPasses));
        return this;
    }

    public TeamStatsLayout setVisitingTeamYellowCard(int visitingTeamYellowCard) {
        this.visitingTeamYellowCard.setText(String.valueOf(visitingTeamYellowCard));
        return this;
    }

    public void setVisitingTeamRedCard(int visitingTeamRedCard) {
        this.visitingTeamRedCard.setText(String.valueOf(visitingTeamRedCard));
    }
}