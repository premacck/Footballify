package life.plank.juna.zone.presentation.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;

public class GameLaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_launch);
        ButterKnife.bind(this);

        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.windowBackground, typedValue, true);
        if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            int color = typedValue.data;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(color);
            }
        }
    }

    @OnClick(R.id.start_game_image)
    public void startGameClicked() {
        startActivity(new Intent(this, GamePickerActivity.class));
    }
    @OnClick(R.id.exit_zone_image)
    public void exitToZoneClicked() {
        startActivity(new Intent(this, ZoneHomeActivity.class));
    }
}
