package life.plank.juna.zone.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;

public class WarriorGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warrior_game);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @OnClick({R.id.warrior_background_screen, R.id.warrior_logo})
    public void launchScreenClicked() {
        startActivity(new Intent(this, GameLaunchActivity.class));
    }
}
