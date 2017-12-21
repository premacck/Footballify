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

        Typeface nexaBoldFont = Typeface.createFromAsset(getAssets(), getString(R.string.nexa_bold));
        subtitle.setTypeface(nexaBoldFont);
        totalScoreLabel.setTypeface(nexaBoldFont);

        Typeface moderneSansFont = Typeface.createFromAsset(getAssets(), getString(R.string.moderne_sans));
        homeTeamName.setTypeface(moderneSansFont);
        visitingTeamName.setTypeface(moderneSansFont);

        Typeface myriadProFont = Typeface.createFromAsset(getAssets(), getString(R.string.myriad_pro_regular));
        home_team_score.setTypeface(myriadProFont);
        visiting_team_score.setTypeface(myriadProFont);
        totalScore.setTypeface(myriadProFont);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}

