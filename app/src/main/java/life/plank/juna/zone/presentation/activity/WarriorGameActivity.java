package life.plank.juna.zone.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.CustomizeStatusBar;

public class WarriorGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warrior_game);
        ButterKnife.bind(this);
        CustomizeStatusBar.removeStatusBar(getWindow());
    }

    @OnClick({R.id.warrior_background_screen, R.id.warrior_logo})
    public void launchScreenClicked() {
        startActivity(new Intent(this, GameLaunchActivity.class));
    }

    @Override
    public void onBackPressed() {

    }
}
