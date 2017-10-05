package life.plank.juna.zone.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.CustomizeStatusBar;

public class GamePickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_picker);
        ButterKnife.bind(this);
        CustomizeStatusBar.setTransparentStatusBarColor(getTheme(), getWindow());
    }

    @OnClick(R.id.home_icon)
    public void homeIconClicked() {
        startActivity(new Intent(this, GameLaunchActivity.class));
    }

    @OnClick(R.id.points_game_image)
    public void startPointsGame() {
        startActivity(new Intent(this, CreateArenaActivity.class));
    }

    @OnClick(R.id.points_game_rules_help)
    public void pointsGameHelpClicked() {
        startActivity(new Intent(this, PointsGameRulesActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @OnClick(R.id.sudden_death_game_rules)
    public void suddenDeathGameHelpClicked() {
        startActivity(new Intent(this, SuddenDeathRulesActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
