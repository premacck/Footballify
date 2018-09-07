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
import life.plank.juna.zone.data.network.model.MatchFixture;
import life.plank.juna.zone.data.network.model.MatchTeamStats;

public class MatchTeamStatsLayout extends FrameLayout {

    @BindView(R.id.teams_logo_layout)
    RelativeLayout teamsLogoLayout;
    @BindView(R.id.match_team_stats_layout)
    LinearLayout matchTeamStatsLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.venue_name)
    TextView venueName;
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
        View rootView = inflate(context, R.layout.item_match_stats, this);
        ButterKnife.bind(this, rootView);
    }

    public void update(MatchTeamStats matchTeamStats, MatchFixture fixture, Picasso picasso) {
        progressBar.setVisibility(GONE);
        noDataTextView.setVisibility(GONE);
        teamsLogoLayout.setVisibility(VISIBLE);
        matchTeamStatsLayout.setVisibility(VISIBLE);

        this.venueName.setText(fixture.getVenue().getName());
        this.homeTeamShots.setText(String.valueOf(matchTeamStats.getHomeShots()));
        this.homeTeamShotsOnTarget.setText(String.valueOf(matchTeamStats.getHomeShotsOnTarget()));
        String homeTeamPossession = matchTeamStats.getHomePossession() + "%";
        this.homeTeamPossession.setText(String.valueOf(homeTeamPossession));
        this.homeTeamFouls.setText(String.valueOf(matchTeamStats.getHomeFouls()));
        this.homeTeamYellowCard.setText(String.valueOf(matchTeamStats.getHomeYellowCards()));
        this.homeTeamRedCard.setText(String.valueOf(matchTeamStats.getHomeRedCards()));
        this.homeTeamOffside.setText(String.valueOf(matchTeamStats.getHomeOffsides()));
        this.homeTeamCorner.setText(String.valueOf(matchTeamStats.getHomeCorners()));

        this.visitingTeamShots.setText(String.valueOf(matchTeamStats.getAwayShots()));
        this.visitingTeamShotsOnTarget.setText(String.valueOf(matchTeamStats.getAwayShotsOnTarget()));
        String visitingTeamPossession = matchTeamStats.getAwayPossession() + "%";
        this.visitingTeamPossession.setText(visitingTeamPossession);
        this.visitingTeamFouls.setText(String.valueOf(matchTeamStats.getAwayFouls()));
        this.visitingTeamYellowCard.setText(String.valueOf(matchTeamStats.getAwayYellowCards()));
        this.visitingTeamRedCard.setText(String.valueOf(matchTeamStats.getAwayRedCards()));
        this.visitingTeamOffside.setText(String.valueOf(matchTeamStats.getAwayOffsides()));
        this.visitingTeamCorner.setText(String.valueOf(matchTeamStats.getAwayCorners()));

        picasso.load(fixture.getHomeTeam().getLogoLink())
                .into(homeTeamLogoImageView);
        picasso.load(fixture.getAwayTeam().getLogoLink())
                .into(visitingTeamLogoImageView);
    }

    //    TODO : remove in next pull request
    public void update(MatchTeamStats matchTeamStats, String homeLogo, String visitingLogo, Picasso picasso) {
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
        teamsLogoLayout.setVisibility(isLoading ? GONE : VISIBLE);
        matchTeamStatsLayout.setVisibility(isLoading ? GONE : VISIBLE);
    }
}