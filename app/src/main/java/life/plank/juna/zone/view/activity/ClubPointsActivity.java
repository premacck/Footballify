package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.Arena;
import life.plank.juna.zone.data.network.model.FootballMatch;
import life.plank.juna.zone.domain.service.GameService;
import life.plank.juna.zone.util.Cursor;
import life.plank.juna.zone.util.CustomizeStatusBar;
import life.plank.juna.zone.util.GlobalVariable;
import life.plank.juna.zone.util.TeamNameMap;

import static life.plank.juna.zone.util.Font.getFont;

/**
 * Created by plank-arfaa on 20/12/17.
 */

public class ClubPointsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ClubPointsActivity.class.getSimpleName();

    @BindView(R.id.league_name)
    TextView leagueName;

    @BindView(R.id.home_team_name)
    TextView homeTeamName;

    @BindView(R.id.visiting_team_name)
    TextView visitingTeamName;

    @BindView(R.id.vs)
    TextView vs;

    @BindView(R.id.home_team_score)
    EditText homeTeamScore;

    @BindView(R.id.visiting_team_score)
    EditText visitingTeamScore;

    @BindView(R.id.home_team_logo)
    ImageView homeTeamLogo;

    @BindView(R.id.visiting_team_logo)
    ImageView visitingTeamLogo;

    private String winnerName;
    private Integer winnerScore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_points_game);
        ButterKnife.bind(this);
        CustomizeStatusBar.setTransparentStatusBarColor(getTheme(), getWindow());

        homeTeamName.setTypeface(getFont(getString(R.string.moderne_sans), getAssets()));
        visitingTeamName.setTypeface(getFont(getString(R.string.moderne_sans), getAssets()));
        leagueName.setTypeface(getFont(getString(R.string.myriad_pro_regular), getAssets()));
        vs.setTypeface(getFont(getString(R.string.myriad_pro_regular), getAssets()));

        RxTextView.textChangeEvents(homeTeamScore)
                .subscribe(event -> Cursor.shiftCursorFocus(homeTeamScore));

        RxTextView.textChangeEvents(visitingTeamScore)
                .subscribe(event -> Cursor.shiftCursorFocus(visitingTeamScore));

        setInfo();
        TeamNameMap.setTeamMap(getApplicationContext());

        homeTeamLogo.setImageDrawable(TeamNameMap.getTeamNameMap()
                .get(FootballMatch.getInstance().getHomeTeam().getName()));
        visitingTeamLogo.setImageDrawable(TeamNameMap.getTeamNameMap()
                .get(FootballMatch.getInstance().getVisitingTeam().getName()));

    }

    private void setInfo() {

        homeTeamName.setText(FootballMatch.getInstance().getHomeTeam().getName());
        visitingTeamName.setText(FootballMatch.getInstance().getVisitingTeam().getName());
        Integer year = FootballMatch.getInstance().getLeagueYearStart();
        leagueName.setText("FA Carling Premiership" + "\n" + year + "/" + (year + 1));

    }

    @OnClick(R.id.home_icon)
    public void homeIconClicked() {
        startActivity(new Intent(this, GameLaunchActivity.class));
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, ClubGameLaunchActivity.class);
        intent.putExtra(getString(R.string.club_image_name), String.valueOf(view.getTag()));
        startActivity(intent);
    }

    @OnClick(R.id.submit_button)
    public void onClickSubmitButton() {
        postUserChoice();
    }

    @Override
    public void onBackPressed() {

    }

    public void postUserChoice() {

        if (Integer.parseInt(homeTeamScore.getText().toString()) > Integer.parseInt(visitingTeamScore.getText().toString())) {
            winnerName = homeTeamName.getText().toString();

        } else {
            winnerName = visitingTeamName.getText().toString();
            winnerScore = Integer.valueOf(visitingTeamScore.getText().toString());

        }

        computeClubPoints(winnerName, winnerScore);
    }

    public void computeClubPoints(String teamName, Integer teamScore) {
        String winnerName;
        Integer winnerScore;
        if (GlobalVariable.getInstance().getClubPointsGameRound() <= 20) {
            if (FootballMatch.getInstance().getHomeTeamScore() > FootballMatch.getInstance().getVisitingTeamScore()) {

                winnerName = FootballMatch.getInstance().getHomeTeam().getName();
                winnerScore = FootballMatch.getInstance().getHomeTeamScore();

            } else {

                winnerName = FootballMatch.getInstance().getVisitingTeam().getName();
                winnerScore = FootballMatch.getInstance().getVisitingTeamScore();

            }

            if (teamName.equals(winnerName)) {
                if (teamScore.equals(winnerScore)) {

                    GlobalVariable.getInstance().setClubPointsGameScore(GlobalVariable.getInstance().getClubPointsGameScore() + 2);

                } else {

                    GlobalVariable.getInstance().setClubPointsGameScore(GlobalVariable.getInstance().getClubPointsGameScore() + 1);
                    GlobalVariable.getInstance().setClubPointsWinner(true);

                }
            } else {

                GlobalVariable.getInstance().setClubPointsWinner(false);
                GlobalVariable.getInstance().setClubGamesDraw(false);

            }
        } else {

            if (GlobalVariable.getInstance().getClubPointsGameScore() == 40) {
                //TODO: Navigate to bonus view
            } else {
                //TODO: Navigate to club game leaderboard
            }

        }
    }
}
