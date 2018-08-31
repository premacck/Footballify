package life.plank.juna.zone.view.fragment.league;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
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
import life.plank.juna.zone.data.network.model.PlayerStatsModel;
import life.plank.juna.zone.data.network.model.StandingModel;
import life.plank.juna.zone.data.network.model.TeamStatsModel;
import life.plank.juna.zone.view.activity.FixtureActivity;
import life.plank.juna.zone.view.activity.MatchResultActivity;
import life.plank.juna.zone.view.activity.MatchResultDetailActivity;
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

public class LeagueInfoFragment extends Fragment {

    private static final String TAG = LeagueInfoFragment.class.getSimpleName();

    @BindView(R.id.match_fixture_result)
    CardView matchFixtureResultLayout;
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
    Picasso picasso;
    @Inject
    Gson gson;

    private StandingTableAdapter standingTableAdapter;
    private PlayerStatsAdapter playerStatsAdapter;
    private TeamStatsAdapter teamStatsAdapter;
    private String seasonName;
    private String leagueName;
    private String countryName;

    public LeagueInfoFragment() {
    }

    public static LeagueInfoFragment newInstance(Context context, String seasonName, String leagueName, String countryName) {
        LeagueInfoFragment fragment = new LeagueInfoFragment();
        Bundle args = new Bundle();
        args.putString(context.getString(R.string.season_name), seasonName);
        args.putString(context.getString(R.string.league_name), leagueName);
        args.putString(context.getString(R.string.country_name), countryName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            seasonName = args.getString(getString(R.string.season_name));
            leagueName = args.getString(getString(R.string.league_name));
            countryName = args.getString(getString(R.string.country_name));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_league_info, container, false);
        ButterKnife.bind(this, rootView);

        ZoneApplication.getApplication().getUiComponent().inject(this);
        prepareRecyclerViews();
        getStandings();
        getPlayerStats();
        getTeamStats();

        return rootView;
    }

    private void prepareRecyclerViews() {
        standingTableAdapter = new StandingTableAdapter(picasso);
        standingRecyclerView.setAdapter(standingTableAdapter);

        playerStatsAdapter = new PlayerStatsAdapter();
        playerStatsRecyclerView.setAdapter(playerStatsAdapter);

        teamStatsAdapter = new TeamStatsAdapter(picasso);
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
        progressBar.setVisibility(View.VISIBLE);
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
        ((MatchResultActivity) Objects.requireNonNull(getActivity())).updateBackgroundBitmap();
        switch (view.getId()) {
            case R.id.see_all_fixtures:
                FixtureActivity.launch(
                        getActivity(),
                        seasonName,
                        leagueName,
                        countryName,
                        matchFixtureResultLayout
                );
                break;
            case R.id.see_all_standings:
                MatchResultDetailActivity.launch(getActivity(), STANDINGS, seasonName, leagueName, countryName,
                        gson.toJson(standingTableAdapter.getStandings()), standingsLayout);
                break;
            case R.id.see_more_team_stats:
                MatchResultDetailActivity.launch(getActivity(), TEAM_STATS, seasonName, leagueName, countryName,
                        gson.toJson(teamStatsAdapter.getTeamStats()), teamStatsLayout);
                break;
            case R.id.see_more_player_stats:
                MatchResultDetailActivity.launch(getActivity(), PLAYER_STATS, seasonName, leagueName, countryName,
                        gson.toJson(playerStatsAdapter.getPlayerStats()), playerStatsLayout);
                break;
        }
    }

    @Override
    public void onDestroy() {
        standingTableAdapter = null;
        teamStatsAdapter = null;
        playerStatsAdapter = null;
        super.onDestroy();
    }
}