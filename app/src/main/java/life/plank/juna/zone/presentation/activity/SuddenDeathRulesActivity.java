package life.plank.juna.zone.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.CustomizeStatusBar;

public class SuddenDeathRulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudden_death_rules);
        ButterKnife.bind(this);
        CustomizeStatusBar.setTransparentStatusBarColor(getTheme(), getWindow());
    }

    @OnClick(R.id.close_icon)
    public void exitRulesScreen() {
        startActivity(new Intent(this, GamePickerActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
