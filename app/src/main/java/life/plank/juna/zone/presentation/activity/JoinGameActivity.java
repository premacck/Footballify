package life.plank.juna.zone.presentation.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.CustomizeStatusBar;

public class JoinGameActivity extends AppCompatActivity {

    @BindView(R.id.league_name)
    TextView leagueName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        ButterKnife.bind(this);
        CustomizeStatusBar.setTransparentStatusBarColor(getTheme(), getWindow());

        Typeface moderneSansFont = Typeface.createFromAsset(getAssets(), "font/san-frediano.ttf");
        leagueName.setTypeface(moderneSansFont);
    }

    @OnClick(R.id.button_join_game)
    public void joinPointsGame() {
        startActivity(new Intent(this, PointsMatchActivity.class));
    }

    @OnClick(R.id.home_icon)
    public void exitPointsGame() {
        startActivity(new Intent(this, GameLaunchActivity.class));
    }
}
