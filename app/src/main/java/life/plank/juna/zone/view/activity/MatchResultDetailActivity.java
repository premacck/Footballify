package life.plank.juna.zone.view.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.ArrayList;
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
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.view.adapter.PlayerStatsAdapter;
import life.plank.juna.zone.view.adapter.StandingTableAdapter;
import life.plank.juna.zone.view.adapter.TeamStatsAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.AppConstants.LOAD_VIEW;
import static life.plank.juna.zone.util.AppConstants.PLAYER_STATS;
import static life.plank.juna.zone.util.AppConstants.STANDINGS;
import static life.plank.juna.zone.util.AppConstants.TEAM_STATS;
import static life.plank.juna.zone.view.activity.MatchResultActivity.matchStatsParentViewBitmap;

public class MatchResultDetailActivity extends AppCompatActivity {

    private static final String TAG = MatchResultDetailActivity.class.getSimpleName();
    @Inject
    @Named("footballData")
    Retrofit retrofit;
    @Inject
    Picasso picasso;
    @BindView(R.id.standing_recycler_view)
    RecyclerView standingRecyclerView;
    @BindView(R.id.blur_background_image_view)
    ImageView blurBackgroundImageView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.standing_header_layout)
    LinearLayout standingsHeader;
    @BindView(R.id.team_stats_header_layout)
    LinearLayout teamStatsHeader;
    @BindView(R.id.player_stats_header)
    LinearLayout playerStatsHeader;

    List<StandingModel> standingModel;
    List<TeamStatsModel> teamStatsModel;
    List<PlayerStatsModel> playerStatsModel;
    private StandingTableAdapter standingTableAdapter;
    private TeamStatsAdapter teamStatsAdapter;
    private PlayerStatsAdapter playerStatsAdapter;
    private RestApi restApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_result_detail);
        ButterKnife.bind(this);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        blurBackgroundImageView.setBackground(new BitmapDrawable(getResources(), matchStatsParentViewBitmap));
        restApi = retrofit.create(RestApi.class);

        switch (getIntent().getStringExtra(LOAD_VIEW)) {
            case STANDINGS:
                initStandingRecyclerView();
                getStandings(AppConstants.LEAGUE_NAME, AppConstants.SEASON_NAME, AppConstants.COUNTRY_NAME);
                toggleStatsHeaderVisibility(LinearLayout.VISIBLE, LinearLayout.GONE, LinearLayout.GONE);
                break;
            case TEAM_STATS:
                initTeamStatsRecyclerView();
                getTeamStats(AppConstants.LEAGUE_NAME, AppConstants.SEASON_NAME, AppConstants.COUNTRY_NAME);
                toggleStatsHeaderVisibility(LinearLayout.GONE, LinearLayout.VISIBLE, LinearLayout.GONE);
                break;
            case PLAYER_STATS:
                initPlayerStatsRecyclerView();
                getPlayerStats(AppConstants.LEAGUE_NAME, AppConstants.SEASON_NAME, AppConstants.COUNTRY_NAME);
                toggleStatsHeaderVisibility(LinearLayout.GONE, LinearLayout.GONE, LinearLayout.VISIBLE);
                break;
        }
    }

    private void toggleStatsHeaderVisibility(int standingsHeaderVisibility, int teamStatsHeaderVisibility, int playerStatsHeaderVisibility) {
        standingsHeader.setVisibility(standingsHeaderVisibility);
        teamStatsHeader.setVisibility(teamStatsHeaderVisibility);
        playerStatsHeader.setVisibility(playerStatsHeaderVisibility);
    }

    public void initStandingRecyclerView() {
        standingModel = new ArrayList<>();
        standingTableAdapter = new StandingTableAdapter(picasso);
        standingRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        standingRecyclerView.setAdapter(standingTableAdapter);
    }

    public void initTeamStatsRecyclerView() {
        teamStatsModel = new ArrayList<>();
        teamStatsAdapter = new TeamStatsAdapter(picasso);
        standingRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        standingRecyclerView.setAdapter(teamStatsAdapter);
    }

    public void initPlayerStatsRecyclerView() {
        playerStatsModel = new ArrayList<>();
        playerStatsAdapter = new PlayerStatsAdapter();
        standingRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        standingRecyclerView.setAdapter(playerStatsAdapter);
    }

    public void populateStandingRecyclerView(List<StandingModel> standingModels) {
        standingModel.addAll(standingModels);
        standingTableAdapter.notifyDataSetChanged();
    }

    public void populateTeamStatsRecyclerView(List<TeamStatsModel> teamStatsModels) {
        teamStatsModel.addAll(teamStatsModels);
        teamStatsAdapter.notifyDataSetChanged();
    }

    public void populatePlayerStatsRecyclerView(List<PlayerStatsModel> playerStatsModels) {
        playerStatsModel.addAll(playerStatsModels);
        playerStatsAdapter.notifyDataSetChanged();
    }

    public void getStandings(String leagueName, String seasonName, String countryName) {
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
                        Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<List<StandingModel>> response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                Log.d(TAG, "Data from Backend: " + response.body());
                                populateStandingRecyclerView(response.body());
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(MatchResultDetailActivity.this, R.string.failed_to_get_standings, Toast.LENGTH_SHORT).show();
                            default:
                                Toast.makeText(MatchResultDetailActivity.this, R.string.failed_to_get_standings, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    public void getTeamStats(String leagueName, String seasonName, String countryName) {
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
                    }

                    @Override
                    public void onNext(Response<List<TeamStatsModel>> response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                populateTeamStatsRecyclerView(response.body());
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(getApplicationContext(), R.string.failed_to_get_team_stats, Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), R.string.failed_to_get_team_stats, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
    }

    public void getPlayerStats(String leagueName, String seasonName, String countryName) {
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
                        Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<List<PlayerStatsModel>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                populatePlayerStatsRecyclerView(response.body());
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(getApplicationContext(), R.string.failed_to_get_player_stats, Toast.LENGTH_LONG).show();
                                break;
                            default:
                                populatePlayerStatsRecyclerView(response.body());
                                Toast.makeText(getApplicationContext(), R.string.failed_to_get_player_stats, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
    }
}
