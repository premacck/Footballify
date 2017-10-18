package life.plank.juna.zone.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.CustomizeStatusBar;

public class GameLaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_launch);
        ButterKnife.bind(this);
        CustomizeStatusBar.setTransparentStatusBarColor(getTheme(), getWindow());
    }

    @OnClick(R.id.start_game_image)
    public void startGameClicked() {
        startActivity(new Intent(this, GamePickerActivity.class));
    }

    @OnClick(R.id.join_game_image)
    public void multipleUserJoinGame() {
        startActivity(new Intent(this, MultipleUserJoinGameActivity.class));
    }

    @OnClick(R.id.exit_zone_image)
    public void exitToZoneClicked() {
        startActivity(new Intent(this, ZoneHomeActivity.class));
    }

    @Override
    public void onBackPressed() {

    }
}
