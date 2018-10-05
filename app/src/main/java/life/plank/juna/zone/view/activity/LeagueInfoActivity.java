package life.plank.juna.zone.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bvapp.arcmenulibrary.ArcMenu;
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
import life.plank.juna.zone.data.model.FixtureByDate;
import life.plank.juna.zone.data.model.FixtureByMatchDay;
import life.plank.juna.zone.data.model.League;
import life.plank.juna.zone.data.model.MatchFixture;
import life.plank.juna.zone.data.model.PlayerStats;
import life.plank.juna.zone.data.model.Standings;
import life.plank.juna.zone.data.model.TeamStats;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.util.BoomMenuUtil;
import life.plank.juna.zone.view.activity.base.BaseLeagueActivity;
import life.plank.juna.zone.view.adapter.FixtureAdapter;
import life.plank.juna.zone.view.adapter.PlayerStatsAdapter;
import life.plank.juna.zone.view.adapter.StandingTableAdapter;
import life.plank.juna.zone.view.adapter.TeamStatsAdapter;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.AppConstants.BoomMenuPage.BOOM_MENU_SETTINGS_AND_HOME;
import static life.plank.juna.zone.util.AppConstants.PAST_MATCHES;
import static life.plank.juna.zone.util.AppConstants.PLAYER_STATS;
import static life.plank.juna.zone.util.AppConstants.STANDINGS;
import static life.plank.juna.zone.util.AppConstants.TEAM_STATS;
import static life.plank.juna.zone.util.AppConstants.TODAY_MATCHES;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.DateUtil.getDateDiffFromToday;
import static life.plank.juna.zone.util.UIDisplayUtil.loadBitmap;
import static life.plank.juna.zone.util.UIDisplayUtil.setupSwipeGesture;

public class LeagueInfoActivity extends BaseLeagueActivity {
    public static Bitmap matchStatsParentViewBitmap = null;
    String TAG = LeagueInfoActivity.class.getSimpleName();

    @BindView(R.id.root_layout)
    LinearLayout rootLayout;
    @BindView(R.id.league_toolbar)
    LinearLayout toolbar;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.match_fixture_result)
    CardView MatchFixtureResultLayout;
    @BindView(R.id.fixture_progress_bar)
    ProgressBar fixtureProgressBar;
    @BindView(R.id.team_stats_progress_bar)
    ProgressBar teamStatsProgressBar;
    @BindView(R.id.player_stats_progress_bar)
    ProgressBar playerStatsProgressBar;
    @BindView(R.id.standings_progress_bar)
    ProgressBar standingsProgressBar;
    @BindView(R.id.fixture_no_data)
    TextView fixtureNoData;
    @BindView(R.id.fixtures_section_list)
    RecyclerView fixtureRecyclerView;
    @BindView(R.id.standing_recycler_view)
    RecyclerView standingRecyclerView;
    @BindView(R.id.team_stats_recycler_view)
    RecyclerView teamStatsRecyclerView;
    @BindView(R.id.player_stats_recycler_view)
    RecyclerView playerStatsRecyclerView;
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
    @BindView(R.id.arc_menu)
    ArcMenu arcMenu;
    @BindView(R.id.drag_area)
    TextView dragArea;

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

    private League league;
    private FixtureAdapter fixtureAdapter;
    public static List<FixtureByMatchDay> fixtureByMatchDayList;

    public static void launch(Activity fromActivity, League league) {
        Intent intent = new Intent(fromActivity, LeagueInfoActivity.class);
        intent.putExtra(fromActivity.getString(R.string.intent_league), league);
        fromActivity.startActivity(intent);
        fromActivity.overridePendingTransition(R.anim.float_up, R.anim.sink_up);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_info);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        ButterKnife.bind(this);
        setupSwipeGesture(this, dragArea);

        BoomMenuUtil.setupBoomMenu(BOOM_MENU_SETTINGS_AND_HOME, this, null, arcMenu);
        Intent intent = getIntent();
        if (intent != null) {
            league = intent.getParcelableExtra(getString(R.string.intent_league));
        }

        prepareRecyclerViews();
        getFixtures();
        getStandings();
        getTeamStats();
        getPlayerStats();

        title.setText(league.getName());
        logo.setImageResource(league.getLeagueLogo());
        rootLayout.setBackgroundColor(getResources().getColor(league.getDominantColor(), null));
    }

    public void updateBackgroundBitmap() {
        matchStatsParentViewBitmap = loadBitmap(getWindow().getDecorView(), getWindow().getDecorView(), this);
    }

    private void prepareRecyclerViews() {
        fixtureAdapter = new FixtureAdapter(null, this);
        fixtureRecyclerView.setAdapter(fixtureAdapter);

        standingTableAdapter = new StandingTableAdapter(picasso);
        standingRecyclerView.setAdapter(standingTableAdapter);

        playerStatsAdapter = new PlayerStatsAdapter();
        playerStatsRecyclerView.setAdapter(playerStatsAdapter);

        teamStatsAdapter = new TeamStatsAdapter(picasso);
        teamStatsRecyclerView.setAdapter(teamStatsAdapter);
    }

    public void getStandings() {
        restApi.getStandings(league.getName(), league.getSeasonName(), league.getCountryName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> standingsProgressBar.setVisibility(View.VISIBLE))
                .doOnTerminate(() -> standingsProgressBar.setVisibility(View.GONE))
                .subscribe(new Subscriber<Response<List<Standings>>>() {
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
                    public void onNext(Response<List<Standings>> response) {
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
        restApi.getPlayerStats(league.getName(), league.getSeasonName(), league.getCountryName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> playerStatsProgressBar.setVisibility(View.VISIBLE))
                .doOnTerminate(() -> playerStatsProgressBar.setVisibility(View.GONE))
                .subscribe(new Subscriber<Response<List<PlayerStats>>>() {
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
                    public void onNext(Response<List<PlayerStats>> response) {
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
        restApi.getTeamStats(league.getName(), league.getSeasonName(), league.getCountryName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> teamStatsProgressBar.setVisibility(View.VISIBLE))
                .doOnTerminate(() -> teamStatsProgressBar.setVisibility(View.GONE))
                .subscribe(new Subscriber<Response<List<TeamStats>>>() {
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
                    public void onNext(Response<List<TeamStats>> response) {
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
                    FixtureActivity.launch(this, league);
                }
                break;
            case R.id.see_all_standings:
                LeagueInfoDetailActivity.launch(this, STANDINGS, (ArrayList<? extends Parcelable>) standingTableAdapter.getStandings(), standingsLayout);
                break;
            case R.id.see_more_team_stats:
                LeagueInfoDetailActivity.launch(this, TEAM_STATS, (ArrayList<? extends Parcelable>) teamStatsAdapter.getTeamStats(), teamStatsLayout);
                break;
            case R.id.see_more_player_stats:
                LeagueInfoDetailActivity.launch(this, PLAYER_STATS, (ArrayList<? extends Parcelable>) playerStatsAdapter.getPlayerStats(), playerStatsLayout);
                break;
        }
    }

    public void getFixtures() {
        restApi.getFixtures(league.getSeasonName(), league.getName(), league.getCountryName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> fixtureProgressBar.setVisibility(View.VISIBLE))
                .doOnTerminate(() -> fixtureProgressBar.setVisibility(View.GONE))
                .subscribe(new Subscriber<Response<List<FixtureByMatchDay>>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error: " + e);
                        updateUI(false, fixtureRecyclerView, seeAllFixtures, fixtureNoData);
                    }

                    @Override
                    public void onNext(Response<List<FixtureByMatchDay>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                fixtureByMatchDayList = response.body();
                                if (!isNullOrEmpty(fixtureByMatchDayList)) {
                                    UpdateFixtureAdapterTask.parse(LeagueInfoActivity.this);
                                    updateUI(true, fixtureRecyclerView, seeAllFixtures, fixtureNoData);
                                } else
                                    updateUI(false, fixtureRecyclerView, seeAllFixtures, fixtureNoData);
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                updateUI(false, fixtureRecyclerView, seeAllFixtures, fixtureNoData);
                            default:
                                updateUI(false, fixtureRecyclerView, seeAllFixtures, fixtureNoData);
                                break;
                        }
                    }
                });
    }

    @Override
    public Picasso getPicasso() {
        return picasso;
    }

    @Override
    public Gson getGson() {
        return gson;
    }

    @Override
    public League getLeague() {
        return league;
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
            boolean isPastMatches = true;
            if (!isNullOrEmpty(fixtureByMatchDayList)) {
                for (FixtureByMatchDay matchDay : fixtureByMatchDayList) {
                    try {
                        if (Objects.equals(matchDay.getDaySection(), PAST_MATCHES)) {
                            isPastMatches = true;
                            recyclerViewScrollIndex = fixtureByMatchDayList.indexOf(matchDay);
                        } else if (Objects.equals(matchDay.getDaySection(), TODAY_MATCHES)) {
                            isPastMatches = false;
                            recyclerViewScrollIndex = fixtureByMatchDayList.indexOf(matchDay);
                        }
                    } catch (Exception e) {
                        Log.e("FixtureAdapterTask", "doInBackground: recyclerViewScrollIndex ", e);
                    }
                }
                List<MatchFixture> matchFixtures = new ArrayList<>();
                getMatchesToShow(matchFixtures, isPastMatches);
                return matchFixtures.size() >= 4 ? matchFixtures.subList(0, 4) : matchFixtures;
            }
            return null;
        }

        private void getMatchesToShow(List<MatchFixture> matchFixtures, boolean isPastMatches) {
            List<FixtureByDate> fixtureByDateList = fixtureByMatchDayList.get(recyclerViewScrollIndex).getFixtureByDateList();
            for (FixtureByDate fixtureByDate : fixtureByDateList) {
                for (MatchFixture matchFixture : fixtureByDate.getFixtures()) {
                    try {
                        if (isPastMatches && getDateDiffFromToday(matchFixture.getMatchStartTime()) <= 0) {
                            matchFixtures.add(matchFixture);
                        } else if (getDateDiffFromToday(matchFixture.getMatchStartTime()) <= 1) {
                                matchFixtures.add(matchFixture);
                        }
                    } catch (Exception e) {
                        Log.e("FixtureAdapterTask", "doInBackground: getDateDiffFromToday() ", e);
                    }
                }
            }
            if (isPastMatches) {
                Collections.reverse(matchFixtures);
            }
        }

        @Override
        protected void onPostExecute(List<MatchFixture> matchFixtures) {
            if (ref.get() != null) {
                if (!isNullOrEmpty(matchFixtures)) {
                    if (ref.get().fixtureAdapter != null) {
                        ref.get().fixtureAdapter.update(matchFixtures);
                    }
                    ref.get().fixtureRecyclerView.scrollToPosition(recyclerViewScrollIndex);
                    ref.get().seeAllFixtures.setEnabled(true);
                    ref.get().seeAllFixtures.setClickable(true);
                }
                ref.get().fixtureProgressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroy() {
        fixtureAdapter = null;
        standingTableAdapter = null;
        teamStatsAdapter = null;
        playerStatsAdapter = null;
        if (!isNullOrEmpty(fixtureByMatchDayList)) {
            fixtureByMatchDayList.clear();
            fixtureByMatchDayList = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.float_down, R.anim.sink_down);
    }
}