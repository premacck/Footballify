package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.CustomizeStatusBar;

/**
 * Created by plank-arfaa on 20/12/17.
 */

public class ClubPointsActivity  extends AppCompatActivity implements  View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_points_game);
        ButterKnife.bind(this);
        CustomizeStatusBar.setTransparentStatusBarColor(getTheme(), getWindow());
        Typeface alironBoldFont = Typeface.createFromAsset(getAssets(), getString(R.string.aileron_bold));
//        pickClubLabel.setTypeface(alironBoldFont);

    }

    @OnClick(R.id.home_icon)
    public void homeIconClicked() {
        startActivity(new Intent(this, GameLaunchActivity.class));
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, ClubGameLaunchActivity.class);
        intent.putExtra(getString(R.string.club_image_name), String.valueOf(view.getTag()));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}
