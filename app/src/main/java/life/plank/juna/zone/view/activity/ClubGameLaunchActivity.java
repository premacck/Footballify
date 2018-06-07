package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

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
import retrofit2.Retrofit;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by plank-arfaa on 19/12/17.
 */

public class ClubGameLaunchActivity extends AppCompatActivity {

    @Inject
    @Named("default")
    Retrofit retrofit;

    @BindView(R.id.club_image)
    ImageView clubImage;

    private Subscription subscription;
    private RestApi restApi;
    private static final String TAG = ClubGameLaunchActivity.class.getSimpleName();
    GlobalVariable globalVariable = GlobalVariable.getInstance();

    private FootballMatch footballMatch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_game_launch);
        ButterKnife.bind(this);
        CustomizeStatusBar.setTransparentStatusBarColor(getTheme(), getWindow());
        ((ZoneApplication) getApplication()).getfootballMatchNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        footballMatch = FootballMatch.getNullFootballMatch();
        clubImage.setImageResource(getResources().getIdentifier(getIntent().getStringExtra(getString(R.string.club_image_name)), getString(R.string.drawable), getPackageName()));
    }

    private void getRandomFootballMatchByName() {
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
                        Intent intent = new Intent(ClubGameLaunchActivity.this, ClubPointsActivity.class);
                        startActivity(intent);
                    }
                });
    }

    @OnClick(R.id.home_icon)
    public void homeIconClicked() {
        startActivity(new Intent(this, GameLaunchActivity.class));
    }

    @OnClick(R.id.club_warrior_view)
    public void onClickView() {
        getRandomFootballMatchByName();
    }
}
