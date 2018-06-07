package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.Arena;
import life.plank.juna.zone.util.CustomizeStatusBar;
import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class JoinGameActivity extends AppCompatActivity {

    @Inject
    @Named("default")
    Retrofit retrofit;

    @BindView(R.id.league_name)
    TextView leagueName;

    private String gameType;
    private RestApi restApi;
    private Arena arena;
    public static final String TAG = "JoinGameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        ButterKnife.bind(this);
        CustomizeStatusBar.setTransparentStatusBarColor(getTheme(), getWindow());
        gameType = getIntent().getStringExtra(getString(R.string.game_type));

        ((ZoneApplication) getApplication()).getJoinGameNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        arena = Arena.getNullArena();
        Typeface sansFredianoFont = Typeface.createFromAsset(getAssets(), getString(R.string.sans_frediano));
        leagueName.setTypeface(sansFredianoFont);
    }

    @OnClick(R.id.button_join_game)
    public void joinGame() {

        getArenaByInvitationCode();

    }

    private void getArenaByInvitationCode() {
        restApi.getArenaByInvitationCode(getIntent().getStringExtra("invitationCode"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Arena>() {

                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "In getArenaByInvitationCode onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "In getArenaByInvitationCode onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(Arena responseArena) {
                        arena.copyArena(responseArena);

                        if (gameType.equals(getString(R.string.points_game))) {
                            startActivity(new Intent(getApplicationContext(), PointsGameActivity.class));
                        } else if (gameType.equals(getString(R.string.sudden_death_game))) {
                            startActivity(new Intent(getApplicationContext(), SuddenDeathGameActivity.class));
                        }
                    }
                });
    }


    @OnClick(R.id.home_icon)
    public void exitGame() {
        startActivity(new Intent(this, GameLaunchActivity.class));
    }

    @Override
    public void onBackPressed() {

    }
}
