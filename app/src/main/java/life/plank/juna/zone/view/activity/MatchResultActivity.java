package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.PlayerStatsModel;
import life.plank.juna.zone.data.network.model.StandingModel;
import life.plank.juna.zone.data.network.model.TeamStatsModel;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.GlobalVariable;
import life.plank.juna.zone.view.adapter.PlayerStatsAdapter;
import life.plank.juna.zone.view.adapter.StandingTableAdapter;
import life.plank.juna.zone.view.adapter.TeamStatsAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MatchResultActivity extends AppCompatActivity {
    String TAG = MatchResultActivity.class.getSimpleName();
    @Inject
    @Named("footballData")
    Retrofit retrofit;
    @BindView(R.id.standing_recycler_view)
    RecyclerView standingRecyclerView;
    @BindView(R.id.team_stats_recycler_view)
    RecyclerView teamStatsRecyclerView;
    @BindView(R.id.player_stats_recycler_view)
    RecyclerView playerStatsRecyclerView;
    @BindView(R.id.tap_for_score_and_fixtures)
    TextView tapForScoreAndFixture;
    @BindView(R.id.frame_layout_container)
    FrameLayout frameLayoutGraph;
    @BindView(R.id.following)
    TextView followingTextVIew;
    List<StandingModel> standingModel;
    List<PlayerStatsModel> playerStatsModelList;
    List<TeamStatsModel> teamStatsModelList;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private StandingTableAdapter standingTableAdapter;
    private RestApi restApi;
    private PlayerStatsAdapter playerStatsAdapter;
    private TeamStatsAdapter teamStatsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_result);
        ((ZoneApplication) getApplication()).getStandingNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        ButterKnife.bind(this);

        getStandings(AppConstants.LEAGUE_NAME);
        getPlayerStats(AppConstants.SEASON_NAME);
        populateStandingRecyclerView();
        populatePlayerStatsRecyclerView();
        populateTeamStatsRecyclerView();
        getTeamStats(AppConstants.SEASON_NAME);
    }

    public void populateStandingRecyclerView() {
        standingModel = new ArrayList<>();
        standingTableAdapter = new StandingTableAdapter(this, standingModel);
        standingRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        standingRecyclerView.setAdapter(standingTableAdapter);
    }

    public void populatePlayerStatsRecyclerView() {
        playerStatsModelList = new ArrayList<>();
        playerStatsAdapter = new PlayerStatsAdapter(this, playerStatsModelList);
        playerStatsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        playerStatsRecyclerView.setAdapter(playerStatsAdapter);
    }

    public void populateStandingRecyclerView(List<StandingModel> standingModels) {
        standingModel.addAll(standingModels);
        standingTableAdapter.notifyDataSetChanged();
    }

    public void populatePlayerStatsRecyclerView(List<PlayerStatsModel> playerStatsModels) {
        playerStatsModelList.addAll(playerStatsModels);
        playerStatsAdapter.notifyDataSetChanged();
    }

    public void populateTeamStatsRecyclerView(List<TeamStatsModel> teamStatsModels) {
        teamStatsModelList.addAll(teamStatsModels);
        teamStatsAdapter.notifyDataSetChanged();
    }

    public void populateTeamStatsRecyclerView() {
        teamStatsModelList = new ArrayList<>();
        teamStatsAdapter = new TeamStatsAdapter(this, teamStatsModelList);
        teamStatsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        teamStatsRecyclerView.setAdapter(teamStatsAdapter);
    }


    public void getStandings(String leagueName) {
        progressBar.setVisibility(View.VISIBLE);
        restApi.getStandings(leagueName)
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
                        Toast.makeText(getApplicationContext(), "Something went wrong. Try again later", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<List<StandingModel>> response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                populateStandingRecyclerView(response.body());
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(MatchResultActivity.this, "Failed to get standings", Toast.LENGTH_SHORT).show();
                            default:
                                Toast.makeText(MatchResultActivity.this, "Failed to get standings", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    public void getPlayerStats(String seasonName) {
        restApi.getPlayerStats(seasonName)
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
                        Toast.makeText(getApplicationContext(), "Something went wrong. Try again later", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<List<PlayerStatsModel>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                populatePlayerStatsRecyclerView(response.body());
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(getApplicationContext(), "Failed to get player stats", Toast.LENGTH_LONG).show();
                                break;
                            default:
                                populatePlayerStatsRecyclerView(response.body());
                                Toast.makeText(getApplicationContext(), "Failed to get player stats", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
    }

    public void getTeamStats(String seasonName) {
        progressBar.setVisibility(View.VISIBLE);
        restApi.getTeamStats(seasonName)
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
                                Toast.makeText(getApplicationContext(), "Failed to get team stats", Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), "Failed to get team stats", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
    }

    @OnClick({R.id.tap_for_score_and_fixtures, R.id.following})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tap_for_score_and_fixtures:
                if (GlobalVariable.getInstance().getTilePosition() != 3) {
                    Intent intent = new Intent(this, FixtureAndResultActivity.class);
                    startActivity(intent);
                    break;
                } else {
                    Intent intent = new Intent(this, WorldCupFixtureActivity.class);
                    startActivity(intent);
                    break;
                }
            case R.id.following: {
                if (followingTextVIew.getText().toString().equalsIgnoreCase(getString(R.string.follow))) {
                    followingTextVIew.setText(R.string.follow);
                } else {
                    followingTextVIew.setText(R.string.unfollow);
                }
                break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}

