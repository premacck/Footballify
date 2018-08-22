package life.plank.juna.zone.util.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

public class TeamStats extends FrameLayout {

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

    public TeamStats(@NonNull Context context) {
        this(context, null);
    }

    public TeamStats(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TeamStats(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TeamStats(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View rootView = inflate(context, R.layout.item_team_stats, this);
        ButterKnife.bind(this, rootView);
    }

    public String getHomeTeamShots() {
        return homeTeamShots.getText().toString();
    }

    /**
     * @return the instance of TeamStats (in all other setters too), useful in method chaining.
     */
    public TeamStats setHomeTeamShots(String homeTeamShots) {
        this.homeTeamShots.setText(homeTeamShots);
        return this;
    }

    public String getHomeTeamShotsOnTarget() {
        return homeTeamShotsOnTarget.getText().toString();
    }

    public TeamStats setHomeTeamShotsOnTarget(String homeTeamShotsOnTarget) {
        this.homeTeamShotsOnTarget.setText(homeTeamShotsOnTarget);
        return this;
    }

    public String getHomeTeamPossession() {
        return homeTeamPossession.getText().toString();
    }

    public TeamStats setHomeTeamPossession(String homeTeamPossession) {
        this.homeTeamPossession.setText(homeTeamPossession);
        return this;
    }

    public String getHomeTeamFouls() {
        return homeTeamFouls.getText().toString();
    }

    public TeamStats setHomeTeamFouls(String homeTeamFouls) {
        this.homeTeamFouls.setText(homeTeamFouls);
        return this;
    }

    public String getHomeTeamYellowCard() {
        return homeTeamYellowCard.getText().toString();
    }

    public TeamStats setHomeTeamYellowCard(String homeTeamYellowCard) {
        this.homeTeamYellowCard.setText(homeTeamYellowCard);
        return this;
    }

    public String getHomeTeamRedCard() {
        return homeTeamRedCard.getText().toString();
    }

    public TeamStats setHomeTeamRedCard(String homeTeamRedCard) {
        this.homeTeamRedCard.setText(homeTeamRedCard);
        return this;
    }

    public String getHomeTeamOffside() {
        return homeTeamOffside.getText().toString();
    }

    public TeamStats setHomeTeamOffside(String homeTeamOffside) {
        this.homeTeamOffside.setText(homeTeamOffside);
        return this;
    }

    public String getHomeTeamCorner() {
        return homeTeamCorner.getText().toString();
    }

    public TeamStats setHomeTeamCorner(String homeTeamCorner) {
        this.homeTeamCorner.setText(homeTeamCorner);
        return this;
    }

    public String getVisitingTeamShots() {
        return visitingTeamShots.getText().toString();
    }

    public TeamStats setVisitingTeamShots(String visitingTeamShots) {
        this.visitingTeamShots.setText(visitingTeamShots);
        return this;
    }

    public String getVisitingTeamShotsOnTarget() {
        return visitingTeamShotsOnTarget.getText().toString();
    }

    public TeamStats setVisitingTeamShotsOnTarget(String visitingTeamShotsOnTarget) {
        this.visitingTeamShotsOnTarget.setText(visitingTeamShotsOnTarget);
        return this;
    }

    public String getVisitingTeamPossession() {
        return visitingTeamPossession.getText().toString();
    }

    public TeamStats setVisitingTeamPossession(String visitingTeamPossession) {
        this.visitingTeamPossession.setText(visitingTeamPossession);
        return this;
    }

    public String getVisitingTeamFouls() {
        return visitingTeamFouls.getText().toString();
    }

    public TeamStats setVisitingTeamFouls(String visitingTeamFouls) {
        this.visitingTeamFouls.setText(visitingTeamFouls);
        return this;
    }

    public String getVisitingTeamYellowCard() {
        return visitingTeamYellowCard.getText().toString();
    }

    public TeamStats setVisitingTeamYellowCard(String visitingTeamYellowCard) {
        this.visitingTeamYellowCard.setText(visitingTeamYellowCard);
        return this;
    }

    public String getVisitingTeamRedCard() {
        return visitingTeamRedCard.getText().toString();
    }

    public TeamStats setVisitingTeamRedCard(String visitingTeamRedCard) {
        this.visitingTeamRedCard.setText(visitingTeamRedCard);
        return this;
    }

    public String getVisitingTeamOffside() {
        return visitingTeamOffside.getText().toString();
    }

    public TeamStats setVisitingTeamOffside(String visitingTeamOffside) {
        this.visitingTeamOffside.setText(visitingTeamOffside);
        return this;
    }

    public String getVisitingTeamCorner() {
        return visitingTeamCorner.getText().toString();
    }

    public TeamStats setVisitingTeamCorner(String visitingTeamCorner) {
        this.visitingTeamCorner.setText(visitingTeamCorner);
        return this;
    }
}