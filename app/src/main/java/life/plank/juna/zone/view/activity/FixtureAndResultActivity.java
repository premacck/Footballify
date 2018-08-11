package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.ScoreFixtureModel;
import life.plank.juna.zone.domain.service.FootballFixtureClassifierService;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.view.adapter.LiveMatchesAdapter;
import life.plank.juna.zone.view.adapter.PastMatchAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FixtureAndResultActivity extends AppCompatActivity {
    @Inject
    @Named("footballData")
    Retrofit retrofit;
    // @Inject
    // todo: Make this a dagger component
    FootballFixtureClassifierService footballFixtureClassifierService = new FootballFixtureClassifierService();
    @BindView(R.id.fixtures_list) RecyclerView pastMatchRecyclerView;
    @BindView(R.id.fixtures_list_live) RecyclerView liveMatchRecyclerView;
    private LiveMatchesAdapter liveMatchesAdapter;
//    private TomorrowsMatchesAdapter tomorrowsMatchesAdapter;
//    private ScheduledMatchesAdapter scheduledMatchesAdapter;
    private PastMatchAdapter pastMatchAdapter;
    private RestApi restApi;
    private String TAG = FixtureAndResultActivity.class.getSimpleName();
    private SparseArray<List<ScoreFixtureModel>> matchDayMap;
    private HashMap<FootballFixtureClassifierService.FixtureClassification, List<ScoreFixtureModel>> classifiedMatchesMap;
//    @BindView(R.id.progress_bar)
//    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixture_and_result);
        ButterKnife.bind(this);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        populatePastMatchFixtureRecyclerView();
        populateLiveMatchScoreRecyclerView();
        getScoreFixture(AppConstants.SEASON_NAME);
    }

    public void populateLiveMatchScoreRecyclerView() {
        liveMatchesAdapter = new LiveMatchesAdapter(this);
        liveMatchRecyclerView.setAdapter(liveMatchesAdapter);
//        DividerItemDecoration itemDecor = new DividerItemDecoration(this, VERTICAL);
//        currentMatchRecyclerView.addItemDecoration(itemDecor);

    }

//    public void populateTomorowMatchFixtureRecyclerView() {
//        tomorrowsMatchesAdapter = new TomorrowsMatchesAdapter(this);
//        tomorrowMatchRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        tomorrowMatchRecyclerView.setAdapter(tomorrowsMatchesAdapter);
//        DividerItemDecoration itemDecor = new DividerItemDecoration(this, VERTICAL);
//        tomorrowMatchRecyclerView.addItemDecoration(itemDecor);
//
//    }

    public void populatePastMatchFixtureRecyclerView() {
        pastMatchAdapter = new PastMatchAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        pastMatchRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        pastMatchRecyclerView.setAdapter(pastMatchAdapter);
    }

//    public void populateScheduledScoreFixtureRecyclerView() {
//        scheduledMatchesAdapter = new ScheduledMatchesAdapter(this, classifiedMatchesMap);
//        upcomingMatchRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        upcomingMatchRecyclerView.setAdapter(scheduledMatchesAdapter);
//        DividerItemDecoration itemDecor = new DividerItemDecoration(this, VERTICAL);
//        upcomingMatchRecyclerView.addItemDecoration(itemDecor);
//    }

    public void getScoreFixture(String seasonName) {
//        progressBar.setVisibility(View.VISIBLE);
        //TODO: remove hardcoded value
        restApi.getScoresAndFixtures(seasonName, "Premier League", "England")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<ScoreFixtureModel>>>() {
                    @Override
                    public void onCompleted() {
//                        progressBar.setVisibility(View.INVISIBLE);
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error: " + e);
                    }

                    @Override
                    public void onNext(Response<List<ScoreFixtureModel>> response) {
//                        progressBar.setVisibility(View.INVISIBLE);

                        switch (response.code()) {

                            case HttpURLConnection.HTTP_OK:
                                if (response.body() != null) {
                                    matchDayMap = footballFixtureClassifierService.getMatchDayMap(response.body());
                                    classifiedMatchesMap = footballFixtureClassifierService.GetClassifiedMatchesMap(response.body());
                                    pastMatchAdapter.update(classifiedMatchesMap);
                                    liveMatchesAdapter.update(classifiedMatchesMap);
//                                    for (int i = 0; i < classifiedMatchesMap.size(); i++) {
//                                        pastMatchDayTextView.setText(classifiedMatchesMap.get(FootballFixtureClassifierService.FixtureClassification.PAST_MATCHES).get(i).getMatchDay());
//                                    }

                                } else {
                                    Toast.makeText(FixtureAndResultActivity.this, R.string.matches_not_found, Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(FixtureAndResultActivity.this, R.string.matches_not_found, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(FixtureAndResultActivity.this, R.string.matches_not_found, Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                });
    }
}
