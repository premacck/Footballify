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

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.MatchTeamStats;

public class MatchTeamStatsLayout extends FrameLayout {

    @BindView(R.id.teams_logo_layout)
    RelativeLayout teamsLogoLayout;
    @BindView(R.id.match_team_stats_layout)
    LinearLayout matchTeamStatsLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.no_data)
    TextView noDataTextView;

    @BindView(R.id.home_team_logo)
    ImageView homeTeamLogoImageView;
    @BindView(R.id.home_team_shots)
    TextView homeTeamShots;
    @BindView(R.id.home_team_shots_on_target)
    TextView homeTeamShotsOnTarget;
    @BindView(R.id.home_team_possession)
    TextView homeTeamPossession;
    @BindView(R.id.home_team_fouls)
    TextView homeTeamFouls;
    @BindView(R.id.home_team_yellow_card)
    TextView homeTeamYellowCard;
    @BindView(R.id.home_team_red_card)
    TextView homeTeamRedCard;
    @BindView(R.id.home_team_offside)
    TextView homeTeamOffside;
    @BindView(R.id.home_team_corner)
    TextView homeTeamCorner;

    @BindView(R.id.visiting_team_logo)
    ImageView visitingTeamLogoImageView;
    @BindView(R.id.visiting_team_shots)
    TextView visitingTeamShots;
    @BindView(R.id.visiting_team_shots_on_target)
    TextView visitingTeamShotsOnTarget;
    @BindView(R.id.visiting_team_possession)
    TextView visitingTeamPossession;
    @BindView(R.id.visiting_team_fouls)
    TextView visitingTeamFouls;
    @BindView(R.id.visiting_team_yellow_card)
    TextView visitingTeamYellowCard;
    @BindView(R.id.visiting_team_red_card)
    TextView visitingTeamRedCard;
    @BindView(R.id.visiting_team_offside)
    TextView visitingTeamOffside;
    @BindView(R.id.visiting_team_corner)
    TextView visitingTeamCorner;

    public MatchTeamStatsLayout(@NonNull Context context) {
        this(context, null);
    }

    public MatchTeamStatsLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MatchTeamStatsLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MatchTeamStatsLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View rootView = inflate(context, R.layout.item_team_stats, this);
        ButterKnife.bind(this, rootView);
    }

    public void update(MatchTeamStats matchTeamStats, String homeLogo, String visitingLogo, Picasso picasso) {
        progressBar.setVisibility(GONE);
        noDataTextView.setVisibility(GONE);
        teamsLogoLayout.setVisibility(VISIBLE);
        matchTeamStatsLayout.setVisibility(VISIBLE);
        setHomeTeamShots(matchTeamStats.getHomeShots())
                .setVisitingTeamShots(matchTeamStats.getAwayShots())
                .setHomeTeamShotsOnTarget(matchTeamStats.getHomeShotsOnTarget())
                .setVisitingTeamShotsOnTarget(matchTeamStats.getAwayShotsOnTarget())
                .setHomeTeamPossession(matchTeamStats.getHomePossession())
                .setVisitingTeamPossession(matchTeamStats.getAwayPossession())
                .setHomeTeamFouls(matchTeamStats.getHomeFouls())
                .setVisitingTeamFouls(matchTeamStats.getAwayFouls())
                .setHomeTeamYellowCard(matchTeamStats.getHomeYellowCards())
                .setVisitingTeamYellowCard(matchTeamStats.getAwayYellowCards())
                .setHomeTeamRedCard(matchTeamStats.getHomeRedCards())
                .setVisitingTeamRedCard(matchTeamStats.getAwayRedCards())
                .setHomeTeamOffside(matchTeamStats.getHomeOffsides())
                .setVisitingTeamOffside(matchTeamStats.getAwayOffsides())
                .setHomeTeamCorner(matchTeamStats.getHomeCorners())
                .setVisitingTeamCorner(matchTeamStats.getAwayCorners());

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

    public String getHomeTeamShots() {
        return homeTeamShots.getText().toString();
    }

    /**
     * @return the instance of TeamStats (in all other setters too), useful in method chaining.
     */
    public MatchTeamStatsLayout setHomeTeamShots(int homeTeamShots) {
        this.homeTeamShots.setText(String.valueOf(homeTeamShots));
        return this;
    }

    public String getHomeTeamShotsOnTarget() {
        return homeTeamShotsOnTarget.getText().toString();
    }

    public MatchTeamStatsLayout setHomeTeamShotsOnTarget(int homeTeamShotsOnTarget) {
        this.homeTeamShotsOnTarget.setText(String.valueOf(homeTeamShotsOnTarget));
        return this;
    }

    public String getHomeTeamPossession() {
        return homeTeamPossession.getText().toString();
    }

    public MatchTeamStatsLayout setHomeTeamPossession(int homeTeamPossession) {
        String possession = homeTeamPossession + "%";
        this.homeTeamPossession.setText(String.valueOf(possession));
        return this;
    }

    public String getHomeTeamFouls() {
        return homeTeamFouls.getText().toString();
    }

    public MatchTeamStatsLayout setHomeTeamFouls(int homeTeamFouls) {
        this.homeTeamFouls.setText(String.valueOf(homeTeamFouls));
        return this;
    }

    public String getHomeTeamYellowCard() {
        return homeTeamYellowCard.getText().toString();
    }

    public MatchTeamStatsLayout setHomeTeamYellowCard(int homeTeamYellowCard) {
        this.homeTeamYellowCard.setText(String.valueOf(homeTeamYellowCard));
        return this;
    }

    public String getHomeTeamRedCard() {
        return homeTeamRedCard.getText().toString();
    }

    public MatchTeamStatsLayout setHomeTeamRedCard(int homeTeamRedCard) {
        this.homeTeamRedCard.setText(String.valueOf(homeTeamRedCard));
        return this;
    }

    public String getHomeTeamOffside() {
        return homeTeamOffside.getText().toString();
    }

    public MatchTeamStatsLayout setHomeTeamOffside(int homeTeamOffside) {
        this.homeTeamOffside.setText(String.valueOf(homeTeamOffside));
        return this;
    }

    public String getHomeTeamCorner() {
        return homeTeamCorner.getText().toString();
    }

    public MatchTeamStatsLayout setHomeTeamCorner(int homeTeamCorner) {
        this.homeTeamCorner.setText(String.valueOf(homeTeamCorner));
        return this;
    }

    public String getVisitingTeamShots() {
        return visitingTeamShots.getText().toString();
    }

    public MatchTeamStatsLayout setVisitingTeamShots(int visitingTeamShots) {
        this.visitingTeamShots.setText(String.valueOf(visitingTeamShots));
        return this;
    }

    public String getVisitingTeamShotsOnTarget() {
        return visitingTeamShotsOnTarget.getText().toString();
    }

    public MatchTeamStatsLayout setVisitingTeamShotsOnTarget(int visitingTeamShotsOnTarget) {
        this.visitingTeamShotsOnTarget.setText(String.valueOf(visitingTeamShotsOnTarget));
        return this;
    }

    public String getVisitingTeamPossession() {
        return visitingTeamPossession.getText().toString();
    }

    public MatchTeamStatsLayout setVisitingTeamPossession(int visitingTeamPossession) {
        String possession = visitingTeamPossession + "%";
        this.visitingTeamPossession.setText(possession);
        return this;
    }

    public String getVisitingTeamFouls() {
        return visitingTeamFouls.getText().toString();
    }

    public MatchTeamStatsLayout setVisitingTeamFouls(int visitingTeamFouls) {
        this.visitingTeamFouls.setText(String.valueOf(visitingTeamFouls));
        return this;
    }

    public String getVisitingTeamYellowCard() {
        return visitingTeamYellowCard.getText().toString();
    }

    public MatchTeamStatsLayout setVisitingTeamYellowCard(int visitingTeamYellowCard) {
        this.visitingTeamYellowCard.setText(String.valueOf(visitingTeamYellowCard));
        return this;
    }

    public String getVisitingTeamRedCard() {
        return visitingTeamRedCard.getText().toString();
    }

    public MatchTeamStatsLayout setVisitingTeamRedCard(int visitingTeamRedCard) {
        this.visitingTeamRedCard.setText(String.valueOf(visitingTeamRedCard));
        return this;
    }

    public String getVisitingTeamOffside() {
        return visitingTeamOffside.getText().toString();
    }

    public MatchTeamStatsLayout setVisitingTeamOffside(int visitingTeamOffside) {
        this.visitingTeamOffside.setText(String.valueOf(visitingTeamOffside));
        return this;
    }

    public String getVisitingTeamCorner() {
        return visitingTeamCorner.getText().toString();
    }

    public void setVisitingTeamCorner(int visitingTeamCorner) {
        this.visitingTeamCorner.setText(String.valueOf(visitingTeamCorner));
    }
}