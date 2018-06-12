package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.FootballMatch;
import life.plank.juna.zone.util.CustomizeStatusBar;
import life.plank.juna.zone.util.GlobalVariable;
import life.plank.juna.zone.util.TeamNameMap;
import retrofit2.Retrofit;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.Font.getFont;

/**
 * Created by plank-dhamini on 20/12/17.
 */

public class ClubWarriorResultActivity extends AppCompatActivity {

    @Inject
    @Named("default")
    Retrofit retrofit;

    @BindView(R.id.title)
    ImageView title;

    @BindView(R.id.subtitle)
    TextView subtitle;

    @BindView(R.id.home_team_logo)
    ImageView homeTeamLogo;

    @BindView(R.id.visiting_team_logo)
    ImageView visitingTeamLogo;

    @BindView(R.id.home_team_name)
    TextView homeTeamName;

    @BindView(R.id.visiting_team_name)
    TextView visitingTeamName;

    @BindView(R.id.home_team_score)
    TextView homeTeamScore;

    @BindView(R.id.visiting_team_score)
    TextView visitingTeamScore;

    @BindView(R.id.total_score_label)
    TextView totalScoreLabel;

    @BindView(R.id.total_score)
    TextView totalScore;
    private Subscription subscription;
    private RestApi restApi;

    private static final String TAG = ClubWarriorResultActivity.class.getSimpleName();
    GlobalVariable globalVariable = GlobalVariable.getInstance();
    private FootballMatch footballMatch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_warrior_result);
        ButterKnife.bind(this);
        CustomizeStatusBar.setTransparentStatusBarColor(getTheme(), getWindow());

        subtitle.setTypeface(getFont(getString(R.string.nexa_bold), getAssets()));
        totalScoreLabel.setTypeface(getFont(getString(R.string.nexa_bold), getAssets()));
        homeTeamName.setTypeface(getFont(getString(R.string.moderne_sans), getAssets()));
        visitingTeamName.setTypeface(getFont(getString(R.string.moderne_sans), getAssets()));
        homeTeamScore.setTypeface(getFont(getString(R.string.myriad_pro_regular), getAssets()));
        visitingTeamScore.setTypeface(getFont(getString(R.string.myriad_pro_regular), getAssets()));
        totalScore.setTypeface(getFont(getString(R.string.myriad_pro_regular), getAssets()));

        ((ZoneApplication) getApplication()).getfootballMatchNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        footballMatch = FootballMatch.getNullFootballMatch();

        homeTeamLogo.setImageDrawable(TeamNameMap.getTeamNameMap()
                .get(getIntent().getStringExtra("homeTeamName")));
        visitingTeamLogo.setImageDrawable(TeamNameMap.getTeamNameMap()
                .get(getIntent().getStringExtra("visitingTeamName")));

        homeTeamName.setText(getIntent().getStringExtra("homeTeamName"));
        visitingTeamName.setText(getIntent().getStringExtra("visitingTeamName"));

        homeTeamScore.setText(getIntent().getStringExtra("homeTeamScore"));
        visitingTeamScore.setText(getIntent().getStringExtra("visitingTeamScore"));

        if (getIntent().getStringExtra("isWinner").equals("1")){
            title.setImageResource(R.drawable.ic_oh);
            subtitle.setText("That was so close !");
        }else  if (getIntent().getStringExtra("isWinner").equals("2")){
            title.setImageResource(R.drawable.ic_oops);
            subtitle.setText("You're Wrong");
        }

        totalScore.setText(getIntent().getStringExtra("score"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.result_view)
    public void onClickView() {

        subscription = restApi.getRandomFootballMatchByName(globalVariable.getTeamName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FootballMatch>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "In onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "In onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(FootballMatch footballMatchResponse) {
                        Log.d(TAG, "In  onNext");
                        footballMatch.copyFootballMatch(footballMatchResponse);
                        Log.d(TAG, "Match name---------" + footballMatch.getHomeTeam().getName());
                        Intent intent = new Intent(ClubWarriorResultActivity.this, ClubPointsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}

