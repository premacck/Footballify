package life.plank.juna.zone.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.FixtureByDate;
import life.plank.juna.zone.data.network.model.FixtureByMatchDay;
import life.plank.juna.zone.data.network.model.MatchFixture;
import life.plank.juna.zone.data.network.model.PlayerStatsModel;
import life.plank.juna.zone.data.network.model.StandingModel;
import life.plank.juna.zone.data.network.model.TeamStatsModel;
import life.plank.juna.zone.interfaces.PublicBoardHeaderListener;
import life.plank.juna.zone.view.adapter.FixtureLeagueAdapter;
import life.plank.juna.zone.view.adapter.PlayerStatsAdapter;
import life.plank.juna.zone.view.adapter.StandingTableAdapter;
import life.plank.juna.zone.view.adapter.TeamStatsAdapter;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.AppConstants.PAST_MATCHES;
import static life.plank.juna.zone.util.AppConstants.PLAYER_STATS;
import static life.plank.juna.zone.util.AppConstants.STANDINGS;
import static life.plank.juna.zone.util.AppConstants.TEAM_STATS;
import static life.plank.juna.zone.util.AppConstants.TODAY_MATCHES;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.DateUtil.getDateDiffFromToday;
import static life.plank.juna.zone.util.UIDisplayUtil.loadBitmap;

public class LeagueInfoActivity extends AppCompatActivity implements PublicBoardHeaderListener {
    public static Bitmap matchStatsParentViewBitmap = null;
    String TAG = LeagueInfoActivity.class.getSimpleName();

    @BindView(R.id.stats_parent_view)
    CardView statsParentView;
    @BindView(R.id.league_toolbar)
    LinearLayout toolbar;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.match_fixture_result)
    CardView matchFixtureResultLayout;
    @BindView(R.id.fixture_progress_bar)
    ProgressBar fixtureProgressBar;
    @BindView(R.id.fixtures_section_list)
    RecyclerView fixtureRecyclerView;
    @BindView(R.id.standing_recycler_view)
    RecyclerView standingRecyclerView;
    @BindView(R.id.team_stats_recycler_view)
    RecyclerView teamStatsRecyclerView;
    @BindView(R.id.player_stats_recycler_view)
    RecyclerView playerStatsRecyclerView;
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
    @BindView(R.id.standings_layout)
    CardView standingsLayout;
    @BindView(R.id.team_stats_layout)
    CardView teamStatsLayout;
    @BindView(R.id.player_stats_layout)
    CardView playerStatsLayout;

    @Inject
    @Named("footballData")
    RestApi restApi;
    @Inject
    public Picasso picasso;
    @Inject
    public Gson gson;

    private StandingTableAdapter standingTableAdapter;
    private PlayerStatsAdapter playerStatsAdapter;
    private TeamStatsAdapter teamStatsAdapter;
    private String seasonName;
    private String leagueName;
    private String countryName;

    private String leagueLogo;
    private FixtureLeagueAdapter fixtureLeagueAdapter;
    public static List<FixtureByMatchDay> fixtureByMatchDayList;

    public static void launch(Activity fromActivity, String seasonName, String leagueName, String countryName, String leagueLogo) {
        Intent intent = new Intent(fromActivity, LeagueInfoActivity.class);
        intent.putExtra(fromActivity.getString(R.string.season_name), seasonName);
        intent.putExtra(fromActivity.getString(R.string.league_name), leagueName);
        intent.putExtra(fromActivity.getString(R.string.country_name), countryName);
        intent.putExtra(fromActivity.getString(R.string.league_logo), leagueLogo);
        fromActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_info);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            seasonName = intent.getStringExtra(getString(R.string.season_name));
            leagueName = intent.getStringExtra(getString(R.string.league_name));
            countryName = intent.getStringExtra(getString(R.string.country_name));
            leagueLogo = intent.getStringExtra(getString(R.string.league_logo));
        }

        prepareRecyclerViews();
        getFixtures();
        getStandings();
        getTeamStats();
        getPlayerStats();

        title.setText(leagueName);
        picasso.load(leagueLogo)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(logo);
    }

    public void updateBackgroundBitmap() {
        matchStatsParentViewBitmap = loadBitmap(getWindow().getDecorView(), getWindow().getDecorView(), this);
    }

    @Override
    public void followClicked(TextView followBtn) {
        if (followBtn.getText().toString().equalsIgnoreCase(getString(R.string.follow))) {
            followBtn.setText(R.string.follow);
        } else {
            followBtn.setText(R.string.unfollow);
        }
    }

    private void prepareRecyclerViews() {
        fixtureLeagueAdapter = new FixtureLeagueAdapter(picasso);
        fixtureRecyclerView.setAdapter(fixtureLeagueAdapter);

        standingTableAdapter = new StandingTableAdapter(picasso);
        standingRecyclerView.setAdapter(standingTableAdapter);

        playerStatsAdapter = new PlayerStatsAdapter();
        playerStatsRecyclerView.setAdapter(playerStatsAdapter);

        teamStatsAdapter = new TeamStatsAdapter(picasso);
        teamStatsRecyclerView.setAdapter(teamStatsAdapter);
    }

    public void getStandings() {
        restApi.getStandings(leagueName, seasonName, countryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<StandingModel>>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: getStandings()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        updateUI(false, standingRecyclerView, seeAllStandings, noStandingsTextView);
                        Log.e(TAG, "onError: " + e);
                    }

                    @Override
                    public void onNext(Response<List<StandingModel>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                updateUI(true, standingRecyclerView, seeAllStandings, noStandingsTextView);
                                standingTableAdapter.update(response.body());
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                updateUI(false, standingRecyclerView, seeAllStandings, noStandingsTextView);
                            default:
                                updateUI(false, standingRecyclerView, seeAllStandings, noStandingsTextView);
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
                        Log.i(TAG, "onCompleted: getPlayerStats()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        updateUI(false, playerStatsRecyclerView, seeMorePlayerStats, noPlayerStatsTextView);
                        Log.e(TAG, " Error" + e);
                    }

                    @Override
                    public void onNext(Response<List<PlayerStatsModel>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                updateUI(true, playerStatsRecyclerView, seeMorePlayerStats, noPlayerStatsTextView);
                                playerStatsAdapter.update(response.body());
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                updateUI(false, playerStatsRecyclerView, seeMorePlayerStats, noPlayerStatsTextView);
                                break;
                            default:
                                updateUI(false, playerStatsRecyclerView, seeMorePlayerStats, noPlayerStatsTextView);
                                break;
                        }
                    }
                });
    }

    public void getTeamStats() {
        restApi.getTeamStats(leagueName, seasonName, countryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<TeamStatsModel>>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: getTeamStats()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        updateUI(false, teamStatsRecyclerView, seeMoreTeamStats, noTeamStatsTextView);
                        Log.e(TAG, " Error: " + e);
                    }

                    @Override
                    public void onNext(Response<List<TeamStatsModel>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                updateUI(true, teamStatsRecyclerView, seeMoreTeamStats, noTeamStatsTextView);
                                teamStatsAdapter.update(response.body());
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                updateUI(false, teamStatsRecyclerView, seeMoreTeamStats, noTeamStatsTextView);
                                break;
                            default:
                                updateUI(false, teamStatsRecyclerView, seeMoreTeamStats, noTeamStatsTextView);
                                break;
                        }
                    }
                });
    }

    private void updateUI(boolean available, RecyclerView recyclerView, TextView seeMoreView, TextView noDataView) {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(available ? View.VISIBLE : View.INVISIBLE);
        seeMoreView.setVisibility(available ? View.VISIBLE : View.GONE);
        noDataView.setVisibility(available ? View.GONE : View.VISIBLE);
    }

    @OnClick({R.id.see_all_fixtures, R.id.see_all_standings, R.id.see_more_team_stats, R.id.see_more_player_stats})
    public void onItemClick(View view) {
        Objects.requireNonNull(this).updateBackgroundBitmap();
        switch (view.getId()) {
            case R.id.see_all_fixtures:
                if (!isNullOrEmpty(fixtureByMatchDayList)) {
                    FixtureActivity.launch(this, matchFixtureResultLayout);
                }
                break;
            case R.id.see_all_standings:
                LeagueInfoDetailActivity.launch(this, STANDINGS, seasonName, leagueName, countryName,
                        gson.toJson(standingTableAdapter.getStandings()), standingsLayout);
                break;
            case R.id.see_more_team_stats:
                LeagueInfoDetailActivity.launch(this, TEAM_STATS, seasonName, leagueName, countryName,
                        gson.toJson(teamStatsAdapter.getTeamStats()), teamStatsLayout);
                break;
            case R.id.see_more_player_stats:
                LeagueInfoDetailActivity.launch(this, PLAYER_STATS, seasonName, leagueName, countryName,
                        gson.toJson(playerStatsAdapter.getPlayerStats()), playerStatsLayout);
                break;
        }
    }

    public void getFixtures() {
        restApi.getFixtures(seasonName, leagueName, countryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> progressBar.setVisibility(View.VISIBLE))
                .doOnTerminate(() -> progressBar.setVisibility(View.GONE))
                .subscribe(new Observer<Response<List<FixtureByMatchDay>>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error: " + e);
                    }

                    @Override
                    public void onNext(Response<List<FixtureByMatchDay>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                fixtureByMatchDayList = response.body();
                                if (!isNullOrEmpty(fixtureByMatchDayList)) {
                                    UpdateFixtureAdapterTask.parse(LeagueInfoActivity.this);
                                }
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                            default:

                                break;
                        }
                    }
                });
    }

    private static class UpdateFixtureAdapterTask extends AsyncTask<Void, Void, List<MatchFixture>> {

        private WeakReference<LeagueInfoActivity> ref;
        private int recyclerViewScrollIndex = 0;

        private static void parse(LeagueInfoActivity activity) {
            new UpdateFixtureAdapterTask(activity).execute();
        }

        private UpdateFixtureAdapterTask(LeagueInfoActivity activity) {
            this.ref = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            ref.get().fixtureProgressBar.setVisibility(View.VISIBLE);
            ref.get().seeAllFixtures.setEnabled(false);
            ref.get().seeAllFixtures.setClickable(false);
        }

        @Override
        protected List<MatchFixture> doInBackground(Void... voids) {
            if (!isNullOrEmpty(fixtureByMatchDayList)) {
                for (FixtureByMatchDay matchDay : fixtureByMatchDayList) {
                    if (Objects.equals(matchDay.getDaySection(), PAST_MATCHES) || Objects.equals(matchDay.getDaySection(), TODAY_MATCHES)) {
                        recyclerViewScrollIndex = fixtureByMatchDayList.indexOf(matchDay);
                    }
                }
                List<MatchFixture> matchFixtures = new ArrayList<>();
                List<FixtureByDate> fixtureByDateList = fixtureByMatchDayList.get(recyclerViewScrollIndex).getFixtureByDateList();
                for (FixtureByDate fixtureByDate : fixtureByDateList) {
                    for (MatchFixture matchFixture : fixtureByDate.getFixtures()) {
                        if (getDateDiffFromToday(matchFixture.getMatchStartTime()) <= 0) {
                            matchFixtures.add(matchFixture);
                        }
                    }
                }
                Collections.reverse(matchFixtures);
                return matchFixtures.size() >= 2 ? matchFixtures.subList(0, 2) : matchFixtures;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<MatchFixture> matchFixtures) {
            if (!isNullOrEmpty(matchFixtures) && ref.get() != null) {
                if (ref.get().fixtureLeagueAdapter != null) {
                    ref.get().fixtureLeagueAdapter.update(matchFixtures);
                }
                ref.get().fixtureProgressBar.setVisibility(View.GONE);
                ref.get().fixtureRecyclerView.scrollToPosition(recyclerViewScrollIndex);
                ref.get().seeAllFixtures.setEnabled(true);
                ref.get().seeAllFixtures.setClickable(true);
            }
        }
    }

    @Override
    public void onDestroy() {
        fixtureLeagueAdapter = null;
        standingTableAdapter = null;
        teamStatsAdapter = null;
        playerStatsAdapter = null;
        if (!isNullOrEmpty(fixtureByMatchDayList)) {
            fixtureByMatchDayList.clear();
            fixtureByMatchDayList = null;
        }
        super.onDestroy();
    }
}