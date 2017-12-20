package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

public class ClubListViewActivity extends AppCompatActivity implements  View.OnClickListener {


    @BindView(R.id.pick_club_label)
    TextView pickClubLabel;
    @BindView(R.id.club_chelsea)
    ImageView chelsea;
    @BindView(R.id.club_man_united)
    ImageView manUnited;
    @BindView(R.id.club_arsenal)
    ImageView arsenal;
    @BindView(R.id.club_tottenham)
    ImageView tottenham;
    @BindView(R.id.club_leicester)
    ImageView leicester;
    @BindView(R.id.club_liverpool)
    ImageView liverpool;
    @BindView(R.id.club_man_city)
    ImageView manCity;
    @BindView(R.id.club_everton)
    ImageView everton;

    private static final String TAG = ClubListViewActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_list_view);
        ButterKnife.bind(this);
        CustomizeStatusBar.setTransparentStatusBarColor(getTheme(), getWindow());
        Typeface alironBoldFont = Typeface.createFromAsset(getAssets(), getString(R.string.aileron_bold));
        pickClubLabel.setTypeface(alironBoldFont);

        initializeButtons();

    }

    private void initializeButtons() {
        chelsea.setOnClickListener(this);
        manUnited.setOnClickListener(this);
        arsenal.setOnClickListener(this);
        tottenham.setOnClickListener(this);
        leicester.setOnClickListener(this);
        liverpool.setOnClickListener(this);
        manCity.setOnClickListener(this);
        everton.setOnClickListener(this);
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
        super.onBackPressed();
    }

}
