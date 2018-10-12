package life.plank.juna.zone.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
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
import life.plank.juna.zone.data.local.model.LeagueInfo;
import life.plank.juna.zone.data.model.FixtureByDate;
import life.plank.juna.zone.data.model.FixtureByMatchDay;
import life.plank.juna.zone.data.model.League;
import life.plank.juna.zone.data.model.MatchFixture;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.util.BoomMenuUtil;
import life.plank.juna.zone.view.activity.base.BaseLeagueActivity;
import life.plank.juna.zone.view.adapter.FixtureAdapter;
import life.plank.juna.zone.view.adapter.PlayerStatsAdapter;
import life.plank.juna.zone.view.adapter.StandingTableAdapter;
import life.plank.juna.zone.view.adapter.TeamStatsAdapter;
import life.plank.juna.zone.view.fragment.base.BaseCard;

import static java.util.Collections.emptyList;
import static life.plank.juna.zone.util.AppConstants.BoomMenuPage.BOOM_MENU_SETTINGS_AND_HOME;
import static life.plank.juna.zone.util.AppConstants.PAST_MATCHES;
import static life.plank.juna.zone.util.AppConstants.PLAYER_STATS;
import static life.plank.juna.zone.util.AppConstants.STANDINGS;
import static life.plank.juna.zone.util.AppConstants.TEAM_STATS;
import static life.plank.juna.zone.util.AppConstants.TODAY_MATCHES;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.DateUtil.getDateDiffFromToday;
import static life.plank.juna.zone.util.FileHandler.getSavedScreenshot;
import static life.plank.juna.zone.util.FileHandler.saveScreenshot;
import static life.plank.juna.zone.util.UIDisplayUtil.loadBitmap;
import static life.plank.juna.zone.util.UIDisplayUtil.setupSwipeGesture;

public class LeagueInfoActivity extends BaseLeagueActivity {
    public static Bitmap matchStatsParentViewBitmap = null;
    String TAG = LeagueInfoActivity.class.getSimpleName();

    @BindView(R.id.root_card)
    CardView rootCard;
    @BindView(R.id.faded_card)
    CardView fadedCard;
    @BindView(R.id.blur_background_image_view)
    ImageView blurBackgroundImageView;
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

    private boolean isDataLocal;
    private League league;
    private FixtureAdapter fixtureAdapter;
    public static List<FixtureByMatchDay> fixtureByMatchDayList;

    public static void launch(Activity fromActivity, League league, View screenshotView) {
        Intent intent = new Intent(fromActivity, LeagueInfoActivity.class);
        saveScreenshot(fromActivity.getLocalClassName(), screenshotView, intent);
        intent.putExtra(fromActivity.getString(R.string.intent_league), league);
        fromActivity.startActivity(intent);
        fromActivity.overridePendingTransition(R.anim.float_up, android.R.anim.fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_info);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        ButterKnife.bind(this);

        BoomMenuUtil.setupBoomMenu(BOOM_MENU_SETTINGS_AND_HOME, this, null, arcMenu);
        Intent intent = getIntent();
        if (intent != null) {
            league = intent.getParcelableExtra(getString(R.string.intent_league));
            blurBg = getSavedScreenshot(intent);
        }

        blurBackgroundImageView.setImageBitmap(blurBg);
        setupSwipeGesture(this, dragArea, rootCard, fadedCard);

        prepareRecyclerViews();
        getLeagueInfoFromRoomDb();

        title.setText(league.getName());
        logo.setImageResource(league.getLeagueLogo());
        rootLayout.setBackgroundColor(getResources().getColor(league.getDominantColor(), null));
    }

    @Override
    public View getScreenshotLayout() {
        return rootCard;
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

    private void getLeagueInfoFromRoomDb() {
        isDataLocal = true;
        leagueViewModel.getLeagueInfoLiveData().observe(this, this::handleLeagueInfoData);
        leagueViewModel.getLeagueInfoFromDb(league.getId());
    }

    private void getLeagueInfoFromRestApi() {
        if (isDataLocal) {
            isDataLocal = false;
            leagueViewModel.getLeagueInfoFromRestApi(league, restApi);
        }
    }

    private void handleLeagueInfoData(LeagueInfo leagueInfo) {
        if (leagueInfo != null) {
//            Update new data in DB
            if (!isDataLocal) {
                leagueViewModel.getLeagueRepository().insertLeagueInfo(leagueInfo);
            }

            fixtureProgressBar.setVisibility(View.GONE);
            if (Objects.equals(leagueInfo.getFixtureByMatchDayList(), emptyList()) || isNullOrEmpty(leagueInfo.getFixtureByMatchDayList())) {
                updateUI(false, fixtureRecyclerView, seeAllFixtures, fixtureNoData);
            } else {
                fixtureByMatchDayList = leagueInfo.getFixtureByMatchDayList();
                UpdateFixtureAdapterTask.parse(LeagueInfoActivity.this);
                updateUI(true, fixtureRecyclerView, seeAllFixtures, fixtureNoData);
            }

            standingsProgressBar.setVisibility(View.GONE);
            if (Objects.equals(leagueInfo.getStandingsList(), emptyList()) || isNullOrEmpty(leagueInfo.getStandingsList())) {
                updateUI(false, standingRecyclerView, seeAllStandings, noStandingsTextView);
            } else {
                updateUI(true, standingRecyclerView, seeAllStandings, noStandingsTextView);
                standingTableAdapter.update(leagueInfo.getStandingsList());
            }

            teamStatsProgressBar.setVisibility(View.GONE);
            if (Objects.equals(leagueInfo.getTeamStatsList(), emptyList()) || isNullOrEmpty(leagueInfo.getTeamStatsList())) {
                updateUI(false, teamStatsRecyclerView, seeMoreTeamStats, noTeamStatsTextView);
            } else {
                updateUI(true, teamStatsRecyclerView, seeMoreTeamStats, noTeamStatsTextView);
                teamStatsAdapter.update(leagueInfo.getTeamStatsList());
            }

            playerStatsProgressBar.setVisibility(View.GONE);
            if (Objects.equals(leagueInfo.getPlayerStatsList(), emptyList()) || isNullOrEmpty(leagueInfo.getPlayerStatsList())) {
                updateUI(false, playerStatsRecyclerView, seeMorePlayerStats, noPlayerStatsTextView);
            } else {
                updateUI(true, playerStatsRecyclerView, seeMorePlayerStats, noPlayerStatsTextView);
                playerStatsAdapter.update(leagueInfo.getPlayerStatsList());
            }
            getLeagueInfoFromRestApi();
        } else {
            getLeagueInfoFromRestApi();
        }
    }

    private void updateUI(boolean available, RecyclerView recyclerView, TextView seeMoreView, TextView noDataView) {
        recyclerView.setVisibility(available ? View.VISIBLE : View.INVISIBLE);
        seeMoreView.setVisibility(available ? View.VISIBLE : View.GONE);
        noDataView.setVisibility(available ? View.GONE : View.VISIBLE);
    }

    @OnClick({R.id.see_all_fixtures, R.id.see_all_standings, R.id.see_more_team_stats, R.id.see_more_player_stats})
    public void onItemClick(View view) {
        updateBackgroundBitmap();
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

    @NonNull
    @Override
    public Picasso getPicasso() {
        return picasso;
    }

    @NonNull
    @Override
    public Gson getGson() {
        return gson;
    }

    @NonNull
    @Override
    public League getLeague() {
        return league;
    }

    @Override
    public void pushCard(BaseCard card) {

    }

    @Override
    public void popCard(BaseCard card) {

    }

    @Override
    public void setBlurBg(Bitmap blurBg) {

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
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}