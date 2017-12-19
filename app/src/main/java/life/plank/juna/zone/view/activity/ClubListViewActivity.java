package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.CustomizeStatusBar;

/**
 * Created by plank-arfaa on 19/12/17.
 */

public class ClubListViewActivity extends AppCompatActivity {


    @BindView(R.id.pick_club_label)
    TextView pickClubLabel;

    private static final String TAG = ClubListViewActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_list_view);
        ButterKnife.bind(this);
        CustomizeStatusBar.setTransparentStatusBarColor(getTheme(), getWindow());
        Typeface alironBoldFont = Typeface.createFromAsset(getAssets(), getString(R.string.aileron_bold));
        pickClubLabel.setTypeface(alironBoldFont);
    }

    @OnClick(R.id.home_icon)
    public void homeIconClicked() {
        startActivity(new Intent(this, GameLaunchActivity.class));
    }

    @OnClick(R.id.club_chelsea)
    public void clubChelseaButtonClicked() {
        Log.d(TAG, "Chelsea clicked");
    }

    @OnClick(R.id.club_man_united)
    public void clubManUnitedButtonClicked() {
        Log.d(TAG, "Man U clicked");
    }

    @OnClick(R.id.club_arsenal)
    public void clubArsenalButtonClicked() {
        Log.d(TAG, "Arsenal clicked");
    }

    @OnClick(R.id.club_tottenham)
    public void clubTottenhamButtonClicked() {
        Log.d(TAG, "Tottenham clicked");
    }

    @OnClick(R.id.club_leicester)
    public void clubLeicesterButtonClicked() {
        Log.d(TAG, "Leicester clicked");
    }

    @OnClick(R.id.club_liverpool)
    public void clubLiverpoolButtonClicked() {
        Log.d(TAG, "Liverpool clicked");
    }

    @OnClick(R.id.club_man_city)
    public void clubManCityButtonClicked() {
        Log.d(TAG, "ManCity clicked");
    }

    @OnClick(R.id.club_everton)
    public void clubEvertonButtonClicked() {
        Log.d(TAG, "Everton clicked");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
