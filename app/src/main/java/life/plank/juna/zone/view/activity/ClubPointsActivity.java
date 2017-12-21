package life.plank.juna.zone.view.activity;

import android.app.Service;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.util.CustomizeStatusBar;

/**
 * Created by plank-arfaa on 20/12/17.
 */

public class ClubPointsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.league_name)
    TextView leagueName;

    @BindView(R.id.home_team_name)
    TextView homeTeamName;

    @BindView(R.id.visiting_team_name)
    TextView visitingTeamName;

    @BindView(R.id.vs)
    TextView vs;

    @BindView(R.id.home_team_score)
    EditText homeTeamScore;

    @BindView(R.id.visiting_team_score)
    EditText visitingTeamScore;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_points_game);
        ButterKnife.bind(this);
        CustomizeStatusBar.setTransparentStatusBarColor(getTheme(), getWindow());

        Typeface moderneSansFont = Typeface.createFromAsset(getAssets(), getString(R.string.moderne_sans));
        homeTeamName.setTypeface(moderneSansFont);
        visitingTeamName.setTypeface(moderneSansFont);

        Typeface myriadProFont = Typeface.createFromAsset(getAssets(), getString(R.string.myriad_pro_regular));
        leagueName.setTypeface(myriadProFont);

        Typeface newsGothicFont = Typeface.createFromAsset(getAssets(), getString(R.string.news_gothic_mt));
        vs.setTypeface(newsGothicFont);

        RxTextView.textChangeEvents(homeTeamScore)
                .subscribe(event -> shiftCursorFocus(homeTeamScore));

        RxTextView.textChangeEvents(visitingTeamScore)
                .subscribe(event -> shiftCursorFocus(visitingTeamScore));
    }

    private void shiftCursorFocus(EditText editText) {
        if (editText.length() == 1) {
            View next = editText.focusSearch(View.FOCUS_RIGHT);
            if (next == null) {
                InputMethodManager inputMethodManager = (InputMethodManager) ZoneApplication.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            } else next.requestFocus();
        }
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

    @OnClick(R.id.submit_button)
    public void onClickSubmitButton() {
        startActivity(new Intent(this, ClubWarriorResultActivity.class));
    }

    @Override
    public void onBackPressed() {

    }
}
