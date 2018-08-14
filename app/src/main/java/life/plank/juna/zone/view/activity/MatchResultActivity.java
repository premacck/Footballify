package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import static life.plank.juna.zone.util.UIDisplayUtil.loadBitmap;

public class MatchResultActivity extends AppCompatActivity {
    public static Bitmap matchStatsParentViewBitmap = null;
    String TAG = MatchResultActivity.class.getSimpleName();
    @Inject
    @Named("footballData")
    Retrofit retrofit;
    @BindView(R.id.stats_parent_view)
    CardView statsParentView;
    @BindView(R.id.standing_recycler_view)
    RecyclerView standingRecyclerView;
    @BindView(R.id.team_stats_recycler_view)
    RecyclerView teamStatsRecyclerView;
    @BindView(R.id.player_stats_recycler_view)
    RecyclerView playerStatsRecyclerView;
    @BindView(R.id.frame_layout_container)
    FrameLayout frameLayoutGraph;
    @BindView(R.id.layout_board_engagement)
    RelativeLayout layoutBoardEngagement;
    @BindView(R.id.layout_info_tiles)
    RelativeLayout layoutInfoTiles;
    @BindView(R.id.following_text_view)
    TextView followingTextVIew;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    List<StandingModel> standingModel;
    List<PlayerStatsModel> playerStatsModelList;
    List<TeamStatsModel> teamStatsModelList;
    private StandingTableAdapter standingTableAdapter;
    private PlayerStatsAdapter playerStatsAdapter;
    private TeamStatsAdapter teamStatsAdapter;
    private RestApi restApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_result);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        ButterKnife.bind(this);

        getStandings(AppConstants.LEAGUE_NAME);
        getPlayerStats(AppConstants.SEASON_NAME);
        populateStandingRecyclerView();
        populatePlayerStatsRecyclerView();
        populateTeamStatsRecyclerView();
        getTeamStats(AppConstants.SEASON_NAME);

        layoutBoardEngagement.setBackgroundColor(getColor(R.color.transparent_white_one));
        layoutInfoTiles.setBackgroundColor(getColor(R.color.transparent_white_two));
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
                        Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<List<StandingModel>> response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                populateStandingRecyclerView(response.body());
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(MatchResultActivity.this, R.string.failed_to_get_standings, Toast.LENGTH_SHORT).show();
                            default:
                                Toast.makeText(MatchResultActivity.this, R.string.failed_to_get_standings, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getApplicationContext(), R.string.failed_to_get_team_stats, Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), R.string.failed_to_get_team_stats, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
    }

    @OnClick({R.id.see_all, R.id.following_text_view, R.id.see_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.see_all:
                if (GlobalVariable.getInstance().getTilePosition() != 3) {
//                    TODO : replace this with values retrieved from the API call.
                    FixtureAndResultActivity.launch(MatchResultActivity.this, AppConstants.SEASON_NAME, AppConstants.LEAGUE_NAME, AppConstants.COUNTRY_NAME);
                    break;
                } else {
                    Intent intent = new Intent(this, WorldCupFixtureActivity.class);
                    startActivity(intent);
                    break;
                }
            case R.id.following_text_view: {
                if (followingTextVIew.getText().toString().equalsIgnoreCase(getString(R.string.follow))) {
                    followingTextVIew.setText(R.string.follow);
                } else {
                    followingTextVIew.setText(R.string.unfollow);
                }
                break;
            }
            case R.id.see_complete:
                matchStatsParentViewBitmap = loadBitmap(statsParentView, statsParentView, this);
                Intent intent = new Intent(this, MatchResultDetailActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}

