package life.plank.juna.zone.view.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.PlayerStatsModel;
import life.plank.juna.zone.data.network.model.StandingModel;
import life.plank.juna.zone.data.network.model.TeamStatsModel;
import life.plank.juna.zone.view.adapter.PlayerStatsAdapter;
import life.plank.juna.zone.view.adapter.StandingTableAdapter;
import life.plank.juna.zone.view.adapter.TeamStatsAdapter;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.AppConstants.PLAYER_STATS;
import static life.plank.juna.zone.util.AppConstants.STANDINGS;
import static life.plank.juna.zone.util.AppConstants.TEAM_STATS;
import static life.plank.juna.zone.util.UIDisplayUtil.setSharedElementTransitionDuration;
import static life.plank.juna.zone.util.UIDisplayUtil.setupSwipeGesture;
import static life.plank.juna.zone.view.activity.MatchResultActivity.matchStatsParentViewBitmap;

public class MatchResultDetailActivity extends AppCompatActivity {

    private static final String TAG = MatchResultDetailActivity.class.getSimpleName();
    @Inject
    @Named("footballData")
    RestApi restApi;
    @Inject
    Picasso picasso;
    @BindView(R.id.standing_recycler_view)
    RecyclerView standingRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.standing_header_layout)
    RelativeLayout standingsHeader;
    @BindView(R.id.team_stats_header_layout)
    RelativeLayout teamStatsHeader;
    @BindView(R.id.player_stats_header)
    RelativeLayout playerStatsHeader;
    @BindView(R.id.header)
    TextView headerTextView;
    @BindView(R.id.no_data)
    TextView noDataTextView;

    private StandingTableAdapter standingTableAdapter;
    private TeamStatsAdapter teamStatsAdapter;
    private PlayerStatsAdapter playerStatsAdapter;
    private String seasonName;
    private String leagueName;
    private String countryName;

    public static void launch(Activity fromActivity, String viewToLoad, String seasonName, String leagueName, String countryName, View fromView) {
        Intent intent = new Intent(fromActivity, MatchResultDetailActivity.class);
        intent.putExtra(fromActivity.getString(R.string.season_name), seasonName);
        intent.putExtra(fromActivity.getString(R.string.league_name), leagueName);
        intent.putExtra(fromActivity.getString(R.string.country_name), countryName);
        intent.putExtra(fromActivity.getString(R.string.intent_load_view), viewToLoad);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(fromActivity, Pair.create(fromView, fromActivity.getString(R.string.pref_match)));
        fromActivity.startActivity(intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_result_detail);
        ButterKnife.bind(this);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        getWindow().getDecorView().setBackground(new BitmapDrawable(getResources(), matchStatsParentViewBitmap));
        setSharedElementTransitionDuration(this, getResources().getInteger(R.integer.shared_element_animation_duration));
        setupSwipeGesture(this, headerTextView);

        Intent intent = getIntent();
        if (intent != null) {
            seasonName = intent.getStringExtra(getString(R.string.season_name));
            leagueName = intent.getStringExtra(getString(R.string.league_name));
            countryName = intent.getStringExtra(getString(R.string.country_name));
            String viewToLoad = intent.getStringExtra(getString(R.string.intent_load_view));
            headerTextView.setText(viewToLoad);
            switch (viewToLoad) {
                case STANDINGS:
                    standingTableAdapter = new StandingTableAdapter(picasso);
                    prepareRecyclerView(standingTableAdapter);
                    getStandings();
                    toggleStatsHeaderVisibility(LinearLayout.VISIBLE, LinearLayout.GONE, LinearLayout.GONE);
                    break;
                case TEAM_STATS:
                    teamStatsAdapter = new TeamStatsAdapter(picasso);
                    prepareRecyclerView(teamStatsAdapter);
                    getTeamStats();
                    toggleStatsHeaderVisibility(LinearLayout.GONE, LinearLayout.VISIBLE, LinearLayout.GONE);
                    break;
                case PLAYER_STATS:
                    playerStatsAdapter = new PlayerStatsAdapter();
                    prepareRecyclerView(playerStatsAdapter);
                    getPlayerStats();
                    toggleStatsHeaderVisibility(LinearLayout.GONE, LinearLayout.GONE, LinearLayout.VISIBLE);
                    break;
            }
        }
    }

    private void toggleStatsHeaderVisibility(int standingsHeaderVisibility, int teamStatsHeaderVisibility, int playerStatsHeaderVisibility) {
        standingsHeader.setVisibility(standingsHeaderVisibility);
        teamStatsHeader.setVisibility(teamStatsHeaderVisibility);
        playerStatsHeader.setVisibility(playerStatsHeaderVisibility);
    }

    private void prepareRecyclerView(RecyclerView.Adapter adapter) {
        standingRecyclerView.setAdapter(adapter);
    }

    public void getStandings() {
        progressBar.setVisibility(View.VISIBLE);
        restApi.getStandings(leagueName, seasonName, countryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<StandingModel>>>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.VISIBLE);
                        Log.e(TAG, "onError: " + e);
                        onRecyclerViewContentChanged(false, R.string.something_went_wrong);
                    }

                    @Override
                    public void onNext(Response<List<StandingModel>> response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                onRecyclerViewContentChanged(true, 0);
                                standingTableAdapter.update(response.body());
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                onRecyclerViewContentChanged(false, R.string.failed_to_get_standings);
                            default:
                                onRecyclerViewContentChanged(false, R.string.failed_to_get_standings);
                                break;
                        }
                    }
                });
    }

    public void getTeamStats() {
        progressBar.setVisibility(View.VISIBLE);
        restApi.getTeamStats(leagueName, seasonName, countryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<TeamStatsModel>>>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, " Error: " + e);
                        onRecyclerViewContentChanged(false, R.string.something_went_wrong);
                    }

                    @Override
                    public void onNext(Response<List<TeamStatsModel>> response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                onRecyclerViewContentChanged(true, 0);
                                teamStatsAdapter.update(response.body());
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                onRecyclerViewContentChanged(false, R.string.failed_to_get_team_stats);
                                break;
                            default:
                                onRecyclerViewContentChanged(false, R.string.failed_to_get_team_stats);
                                break;
                        }
                    }
                });
    }

    public void getPlayerStats() {
        restApi.getPlayerStats(leagueName, seasonName, countryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<PlayerStatsModel>>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, " Error" + e);
                        onRecyclerViewContentChanged(false, R.string.something_went_wrong);
                    }

                    @Override
                    public void onNext(Response<List<PlayerStatsModel>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                onRecyclerViewContentChanged(true, 0);
                                playerStatsAdapter.update(response.body());
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                onRecyclerViewContentChanged(false, R.string.failed_to_get_player_stats);
                                break;
                            default:
                                onRecyclerViewContentChanged(false, R.string.failed_to_get_player_stats);
                                break;
                        }
                    }
                });
    }

    private void onRecyclerViewContentChanged(boolean isDataAvailable, @StringRes int message) {
        progressBar.setVisibility(View.GONE);
        standingRecyclerView.setVisibility(isDataAvailable ? View.VISIBLE : View.INVISIBLE);
        noDataTextView.setVisibility(isDataAvailable ? View.GONE : View.VISIBLE);
        String messageString = message != 0 ? getString(message) : null;
        noDataTextView.setText(messageString);
    }

    @Override
    protected void onDestroy() {
        standingTableAdapter = null;
        teamStatsAdapter = null;
        playerStatsAdapter = null;
        super.onDestroy();
    }
}