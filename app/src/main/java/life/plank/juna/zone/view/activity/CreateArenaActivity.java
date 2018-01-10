package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.builder.JunaUserBuilder;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.Arena;
import life.plank.juna.zone.data.network.model.Player;
import life.plank.juna.zone.util.CustomizeStatusBar;
import retrofit2.Retrofit;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreateArenaActivity extends AppCompatActivity {

    @Inject
    @Named("default")
    Retrofit retrofit;

    @BindView(R.id.text_secret_code)
    TextView secretCodeText;
    @BindView(R.id.text_share_secret_code)
    TextView shareSecretCodeText;
    @BindView(R.id.user_list)
    ListView userListView;
    @BindView(R.id.secret_code)
    TextView secretCode;
    @BindView(R.id.button_start_playing)
    ImageView startPlayingButton;
    @BindView(R.id.create_arena_relative_layout)
    RelativeLayout relativeLayout;

    private static final String TAG = CreateArenaActivity.class.getSimpleName();
    private AVLoadingIndicatorView spinner;
    private Subscription subscription;
    private RestApi restApi;
    private List<String> playerList = new ArrayList<>();
    private Arena arena;
    private String gameType;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_arena);
        ButterKnife.bind(this);
        CustomizeStatusBar.setTransparentStatusBarColor(getTheme(), getWindow());
        gameType = getIntent().getStringExtra(getString(R.string.game_type));
        Typeface moderneSansFont = Typeface.createFromAsset(getAssets(), getString(R.string.moderne_sans));
        secretCodeText.setTypeface(moderneSansFont);
        secretCode.setTypeface(moderneSansFont);
        Typeface arsenalFont = Typeface.createFromAsset(getAssets(), getString(R.string.arsenal_regular));
        shareSecretCodeText.setTypeface(arsenalFont);

        spinner = (AVLoadingIndicatorView) findViewById(R.id.spinner);
        spinner.show();
        startPlayingButton.setEnabled(false);

        ((ZoneApplication) getApplication()).getCreateArenaNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        arena = Arena.getNullArena();
        sharedPreferences = getSharedPreferences(getString(R.string.login_pref), MODE_PRIVATE);
        arena.setCreator(JunaUserBuilder.getInstance()
                .withUserName(sharedPreferences.getString(getString(R.string.shared_pref_username), ""))
                .build());
        arena.setGameType(gameType);
        createArena();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    @OnClick(R.id.button_start_playing)
    public void startPlayingPointsGame() {
        if (gameType.equals(getString(R.string.points_game))) {
            Intent intent = new Intent(this, JoinGameActivity.class);
            intent.putExtra(getString(R.string.game_type), getString(R.string.points_game));
            intent.putExtra("invitationCode", arena.getInvitationCode());
            startActivity(intent);
            subscription.unsubscribe();
        } else if (gameType.equals(getString(R.string.sudden_death_game))) {
            Intent intent = new Intent(this, JoinGameActivity.class);
            intent.putExtra(getString(R.string.game_type), getString(R.string.sudden_death_game));
            intent.putExtra("invitationCode", arena.getInvitationCode());
            startActivity(intent);
            subscription.unsubscribe();
        }
    }

    private void createArena() {
        subscription = restApi.getArena(arena)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Arena>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "In onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "In onError: " + e.getMessage());
                        Snackbar snackbar = Snackbar.make(relativeLayout, "Arena Creation Failed", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Retry sign-in", v -> {
                                    startActivity(new Intent(getApplication(), SocialLoginActivity.class));
                                    sharedPreferences.edit()
                                            .clear()
                                            .apply();
                                });
                        snackbar.show();
                    }

                    @Override
                    public void onNext(Arena responseArena) {
                        Log.d(TAG, "In onNext");
                        arena.copyArena(responseArena);
                        secretCode.setText(arena.getInvitationCode());
                        spinner.hide();
                        getArenaByInvitationCode();
                    }
                });
    }

    private void getArenaByInvitationCode() {
        subscription = restApi.getArenaByInvitationCode(arena.getInvitationCode())
                .repeatWhen(completed -> completed.delay(2000, TimeUnit.MILLISECONDS))
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
                        Log.d(TAG, "In getArenaByInvitationCode onNext");
                        arena.copyArena(responseArena);
                        playerList.clear();
                        for (Player player : arena.getPlayers()) {
                            playerList.add(player.getDisplayName());

                        }
                        updatePlayerListAdapter();
                        enableStartPlayingButton();
                    }
                });
    }

    private void enableStartPlayingButton() {
        startPlayingButton.setEnabled(true);
        startPlayingButton.setAlpha(1f);
    }

    public void updatePlayerListAdapter() {
        userListView.setAdapter(new ArrayAdapter<>(
                this, R.layout.user_list_view_row,
                R.id.user_name, playerList));
    }

    @OnClick(R.id.home_icon)
    public void homeIconClicked() {
        startActivity(new Intent(this, GameLaunchActivity.class));
    }

    @Override
    public void onBackPressed() {

    }
}
