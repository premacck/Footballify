package life.plank.juna.zone.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
import static life.plank.juna.zone.util.UIDisplayUtil.loadBitmap;

public class MatchResultActivity extends AppCompatActivity {
    public static Bitmap matchStatsParentViewBitmap = null;
    String TAG = MatchResultActivity.class.getSimpleName();
    @Inject
    @Named("footballData")
    Retrofit retrofit;
    @Inject
    Picasso picasso;
    @BindView(R.id.stats_parent_view)
    CardView statsParentView;
    @BindView(R.id.match_fixture_result)
    CardView matchFixtureResultLayout;
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
    @BindView(R.id.no_standings)
    TextView noStandingsTextView;
    @BindView(R.id.no_team_stats)
    TextView noTeamStatsTextView;
    @BindView(R.id.no_player_stats)
    TextView noPlayerStatsTextView;
    @BindView(R.id.see_all_fixtures)
    TextView seeAllFixtures;
    @BindView(R.id.see_all_standings)
    TextView seeAllStandings;
    @BindView(R.id.see_more_team_stats)
    TextView seeMoreTeamStats;
    @BindView(R.id.see_more_player_stats)
    TextView seeMorePlayerStats;
    @BindView(R.id.league_logo)
    ImageView leagueLogoImageView;
    @BindView(R.id.league_name)
    TextView leagueNameTextView;

    private StandingTableAdapter standingTableAdapter;
    private PlayerStatsAdapter playerStatsAdapter;
    private TeamStatsAdapter teamStatsAdapter;
    private RestApi restApi;
    private String seasonName;
    private String leagueName;
    private String countryName;
    private String leagueLogo;

    public static void launch(Activity fromActivity, String seasonName, String leagueName, String countryName, String leagueLogo) {
        Intent intent = new Intent(fromActivity, MatchResultActivity.class);
        intent.putExtra(fromActivity.getString(R.string.season_name), seasonName);
        intent.putExtra(fromActivity.getString(R.string.league_name), leagueName);
        intent.putExtra(fromActivity.getString(R.string.country_name), countryName);
        intent.putExtra(fromActivity.getString(R.string.league_logo), leagueLogo);
        fromActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_result);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            seasonName = intent.getStringExtra(getString(R.string.season_name));
            leagueName = intent.getStringExtra(getString(R.string.league_name));
            countryName = intent.getStringExtra(getString(R.string.country_name));
            leagueLogo = intent.getStringExtra(getString(R.string.league_logo));
        }

        prepareLeagueInfo();
        prepareRecyclerViews();

        getStandings();
        getPlayerStats();
        getTeamStats();

        layoutBoardEngagement.setBackgroundColor(getColor(R.color.transparent_white_one));
        layoutInfoTiles.setBackgroundColor(getColor(R.color.transparent_white_two));
    }

    private void prepareLeagueInfo() {
        leagueNameTextView.setText(leagueName);
        picasso.load(leagueLogo)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(leagueLogoImageView);
    }

    private void prepareRecyclerViews() {
//        TODO: changing these parameters to fix build, will revert back in next pull request
        standingTableAdapter = new StandingTableAdapter(this, new ArrayList<>());
        standingRecyclerView.setAdapter(standingTableAdapter);

        playerStatsAdapter = new PlayerStatsAdapter(this, new ArrayList<>());
        playerStatsRecyclerView.setAdapter(playerStatsAdapter);

        teamStatsAdapter = new TeamStatsAdapter(this, new ArrayList<>());
        teamStatsRecyclerView.setAdapter(teamStatsAdapter);
    }

    public void getStandings() {
        progressBar.setVisibility(View.VISIBLE);
        restApi.getStandings(leagueName, seasonName, countryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<StandingModel>>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        onStandingsChanged(false);
                        Log.e(TAG, "onError: " + e);
                    }

                    @Override
                    public void onNext(Response<List<StandingModel>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                onStandingsChanged(true);
//                                TODO : commenting this to fix build. will revert back in next pull request
//                                standingTableAdapter.update(response.body());
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                onStandingsChanged(false);
                            default:
                                onStandingsChanged(false);
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
                        onPlayerStatsChanged(false);
                        Log.e(TAG, " Error" + e);
                    }

                    @Override
                    public void onNext(Response<List<PlayerStatsModel>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                onPlayerStatsChanged(true);
//                                TODO : commenting this to fix build. will revert back in next pull request
//                                playerStatsAdapter.update(response.body());
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                onPlayerStatsChanged(false);
                                break;
                            default:
                                onPlayerStatsChanged(false);
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
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        onTeamStatsChanged(false);
                        Log.e(TAG, " Error: " + e);
                    }

                    @Override
                    public void onNext(Response<List<TeamStatsModel>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                onTeamStatsChanged(true);
//                                TODO : commenting this to fix build. will revert back in next pull request
//                                teamStatsAdapter.update(response.body());
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                onTeamStatsChanged(false);
                                break;
                            default:
                                onTeamStatsChanged(false);
                                break;
                        }
                    }
                });
    }

    private void onStandingsChanged(boolean available) {
        progressBar.setVisibility(View.INVISIBLE);
        standingRecyclerView.setVisibility(available ? View.VISIBLE : View.INVISIBLE);
        seeAllStandings.setVisibility(available ? View.VISIBLE : View.GONE);
        noStandingsTextView.setVisibility(available ? View.GONE : View.VISIBLE);
    }

    private void onTeamStatsChanged(boolean available) {
        progressBar.setVisibility(View.INVISIBLE);
        teamStatsRecyclerView.setVisibility(available ? View.VISIBLE : View.INVISIBLE);
        seeMoreTeamStats.setVisibility(available ? View.VISIBLE : View.GONE);
        noTeamStatsTextView.setVisibility(available ? View.GONE : View.VISIBLE);
    }

    private void onPlayerStatsChanged(boolean available) {
        progressBar.setVisibility(View.INVISIBLE);
        playerStatsRecyclerView.setVisibility(available ? View.VISIBLE : View.INVISIBLE);
        seeMorePlayerStats.setVisibility(available ? View.VISIBLE : View.INVISIBLE);
        noPlayerStatsTextView.setVisibility(available ? View.GONE : View.VISIBLE);
    }

    @OnClick({R.id.see_all_fixtures, R.id.following_text_view, R.id.see_all_standings, R.id.see_more_team_stats, R.id.see_more_player_stats})
    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.see_all_fixtures:
                matchStatsParentViewBitmap = loadBitmap(statsParentView, statsParentView, this);
                FixtureActivity.launch(
                        MatchResultActivity.this,
                        seasonName,
                        leagueName,
                        countryName,
                        matchFixtureResultLayout
                );
                break;
            case R.id.following_text_view:
                if (followingTextVIew.getText().toString().equalsIgnoreCase(getString(R.string.follow))) {
                    followingTextVIew.setText(R.string.follow);
                } else {
                    followingTextVIew.setText(R.string.unfollow);
                }
                break;
            case R.id.see_all_standings:
                matchStatsParentViewBitmap = loadBitmap(statsParentView, statsParentView, this);
                Intent intent = new Intent(this, MatchResultDetailActivity.class);
                intent.putExtra(LOAD_VIEW, STANDINGS);
                startActivity(intent);
                break;
            case R.id.see_more_team_stats:
                matchStatsParentViewBitmap = loadBitmap(statsParentView, statsParentView, this);
                Intent teamIntent = new Intent(this, MatchResultDetailActivity.class);
                teamIntent.putExtra(LOAD_VIEW, TEAM_STATS);
                startActivity(teamIntent);
                break;
            case R.id.see_more_player_stats:
                matchStatsParentViewBitmap = loadBitmap(statsParentView, statsParentView, this);
                Intent playerIntent = new Intent(this, MatchResultDetailActivity.class);
                playerIntent.putExtra(LOAD_VIEW, PLAYER_STATS);
                startActivity(playerIntent);
                break;
        }
    }
}