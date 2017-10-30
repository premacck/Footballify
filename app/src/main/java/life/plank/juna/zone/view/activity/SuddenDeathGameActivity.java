package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import life.plank.juna.zone.R;
import life.plank.juna.zone.util.CustomizeStatusBar;

public class SuddenDeathGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudden_death_game);
        CustomizeStatusBar.removeStatusBar(getWindow());
    }

    @Override
    public void onBackPressed() {

    }
}
