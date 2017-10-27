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

    private String gameType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        ButterKnife.bind(this);
        CustomizeStatusBar.setTransparentStatusBarColor(getTheme(), getWindow());
        gameType = getIntent().getStringExtra(getString(R.string.game_type));

        Typeface sansFredianoFont = Typeface.createFromAsset(getAssets(), getString(R.string.sans_frediano));
        leagueName.setTypeface(sansFredianoFont);
    }

    @OnClick(R.id.button_join_game)
    public void joinGame() {
        if (gameType.equals(getString(R.string.points_game))) {
            startActivity(new Intent(this, PointsGameActivity.class));
        } else if (gameType.equals(getString(R.string.sudden_death_game))) {
            startActivity(new Intent(this, SuddenDeathGameActivity.class));
        }
    }

    @OnClick(R.id.home_icon)
    public void exitGame() {
        startActivity(new Intent(this, GameLaunchActivity.class));
    }

    @Override
    public void onBackPressed() {

    }
}
