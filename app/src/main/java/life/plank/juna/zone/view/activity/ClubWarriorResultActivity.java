package life.plank.juna.zone.view.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.CustomizeStatusBar;

import static life.plank.juna.zone.util.Font.getFont;

/**
 * Created by plank-dhamini on 20/12/17.
 */

public class ClubWarriorResultActivity extends AppCompatActivity {

    @BindView(R.id.subtitle)
    TextView subtitle;

    @BindView(R.id.home_team_name)
    TextView homeTeamName;

    @BindView(R.id.visiting_team_name)
    TextView visitingTeamName;

    @BindView(R.id.home_team_score)
    TextView home_team_score;

    @BindView(R.id.visiting_team_score)
    TextView visiting_team_score;

    @BindView(R.id.total_score_label)
    TextView totalScoreLabel;

    @BindView(R.id.total_score)
    TextView totalScore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_warrior_result);
        ButterKnife.bind(this);
        CustomizeStatusBar.setTransparentStatusBarColor(getTheme(), getWindow());

        subtitle.setTypeface(getFont(getString(R.string.nexa_bold), getAssets()));
        totalScoreLabel.setTypeface(getFont(getString(R.string.nexa_bold), getAssets()));
        homeTeamName.setTypeface(getFont(getString(R.string.moderne_sans), getAssets()));
        visitingTeamName.setTypeface(getFont(getString(R.string.moderne_sans), getAssets()));
        home_team_score.setTypeface(getFont(getString(R.string.myriad_pro_regular), getAssets()));
        visiting_team_score.setTypeface(getFont(getString(R.string.myriad_pro_regular), getAssets()));
        totalScore.setTypeface(getFont(getString(R.string.myriad_pro_regular), getAssets()));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}

