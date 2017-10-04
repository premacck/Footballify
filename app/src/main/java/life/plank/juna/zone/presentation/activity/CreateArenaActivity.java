package life.plank.juna.zone.presentation.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.Arena;
import life.plank.juna.zone.data.network.model.ArenaCreationData;
import life.plank.juna.zone.data.network.model.Creator;
import life.plank.juna.zone.util.CustomizeStatusBar;
import life.plank.juna.zone.util.PreferenceManager;
import retrofit2.Retrofit;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreateArenaActivity extends AppCompatActivity {

    @Inject
    Retrofit retrofit;

    @BindView(R.id.text_secret_code)
    TextView secretCodeText;
    @BindView(R.id.text_share_secret_code)
    TextView shareSecretCodeText;
    @BindView(R.id.user_list)
    ListView userListView;
    @BindView(R.id.secret_code)
    TextView secretCode;
    @BindView(R.id.create_arena_relative_layout)
    RelativeLayout relativeLayout;

    private AVLoadingIndicatorView spinner;
    private static final String TAG = CreateArenaActivity.class.getSimpleName();
    private Subscription subscription;
    private RestApi restApi;
    private Creator creator = new Creator();

    //Todo: Remove dummy data once usernames are retrieved from database on arena creation
    String[] usersForArena = {
            "Safari",
            "Global",
            "FireFox",
            "UC Browser",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_arena);
        ButterKnife.bind(this);
        CustomizeStatusBar.setTransparentStatusBarColor(getTheme(), getWindow());

        Typeface moderneSansFont = Typeface.createFromAsset(getAssets(), "font/moderne_sans.ttf");
        secretCodeText.setTypeface(moderneSansFont);
        secretCode.setTypeface(moderneSansFont);
        Typeface arsenalFont = Typeface.createFromAsset(getAssets(), "font/arsenal_regular.otf");
        shareSecretCodeText.setTypeface(arsenalFont);

        userListView.setAdapter(new ArrayAdapter<>(
                this, R.layout.user_list_view_row,
                R.id.user_name, usersForArena));

        PreferenceManager prefManager = new PreferenceManager(this);
        creator.setUsername(prefManager.getPreference(getString(R.string.shared_pref_username)));

        spinner = (AVLoadingIndicatorView) findViewById(R.id.spinner);
        spinner.show();

        ((ZoneApplication) getApplication()).getCreateArenaNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        createArena();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    private void createArena() {
        subscription = restApi.getArena(ArenaCreationData.getInstance()
                .withCreator(creator)
                .withGameType(getString(R.string.points_game)))
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
                                .setAction("Retry sign-in", v -> startActivity(new Intent(getApplication(), LoginActivity.class)));
                        snackbar.show();
                    }

                    @Override
                    public void onNext(Arena arena) {
                        Log.d(TAG, "In onNext");
                        secretCode.setText(arena.getInvitationCode());
                        spinner.hide();
                    }
                });
    }
}
