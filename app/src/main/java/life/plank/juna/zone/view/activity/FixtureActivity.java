package life.plank.juna.zone.view.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.ScoreFixture;
import life.plank.juna.zone.data.network.model.SectionedFixture;
import life.plank.juna.zone.view.adapter.FixtureMatchdayAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection.LIVE_MATCHES;
import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.classifyByMatchDay;
import static life.plank.juna.zone.util.UIDisplayUtil.setSharedElementTransitionDuration;
import static life.plank.juna.zone.util.UIDisplayUtil.setupSwipeGesture;
import static life.plank.juna.zone.view.activity.MatchResultActivity.matchStatsParentViewBitmap;

public class FixtureActivity extends AppCompatActivity {

    private static final String TAG = FixtureActivity.class.getSimpleName();

    @BindView(R.id.root_layout)
    FrameLayout rootLayout;
    @BindView(R.id.fixtures_section_list)
    RecyclerView fixtureRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.no_data)
    TextView noMatchesView;
    @BindView(R.id.header)
    TextView headerView;

    @Inject
    Picasso picasso;
    @Inject
    @Named("footballData")
    Retrofit retrofit;

    private FixtureMatchdayAdapter fixtureMatchdayAdapter;
    private RestApi restApi;
    private String seasonName;
    private String leagueName;
    private String countryName;

    public static void launch(Activity packageContext, String seasonName, String leagueName, String countryName, View fromView) {
        Intent intent = new Intent(packageContext, FixtureActivity.class);
        intent.putExtra(packageContext.getString(R.string.season_name), seasonName);
        intent.putExtra(packageContext.getString(R.string.league_name), leagueName);
        intent.putExtra(packageContext.getString(R.string.country_name), countryName);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(packageContext, Pair.create(fromView, "match_fixture_result"));
        packageContext.startActivity(intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixture_and_result);
        ButterKnife.bind(this);
        setSharedElementTransitionDuration(this, getResources().getInteger(R.integer.shared_element_animation_duration));
        setupSwipeGesture(this, headerView);

        rootLayout.setBackground(new BitmapDrawable(getResources(), matchStatsParentViewBitmap));

        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        restApi = retrofit.create(RestApi.class);

        Intent intent = getIntent();
        seasonName = intent.getStringExtra(getString(R.string.season_name));
        leagueName = intent.getStringExtra(getString(R.string.league_name));
        countryName = intent.getStringExtra(getString(R.string.country_name));

        populatePastMatchFixtureRecyclerView();
        getScoreFixture();
    }

    public void populatePastMatchFixtureRecyclerView() {
        fixtureMatchdayAdapter = new FixtureMatchdayAdapter(this, picasso);
        fixtureRecyclerView.setAdapter(fixtureMatchdayAdapter);
    }

    public void getScoreFixture() {
        progressBar.setVisibility(View.VISIBLE);
        restApi.getScoresAndFixtures(seasonName, leagueName, countryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<ScoreFixture>>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error: " + e);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(Response<List<ScoreFixture>> response) {
                        progressBar.setVisibility(View.GONE);
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                List<ScoreFixture> scoreFixtureList = response.body();
                                if (scoreFixtureList != null) {
                                    new UpdateAdapterTask(FixtureActivity.this, scoreFixtureList, fixtureMatchdayAdapter).execute();
                                } else onNoMatchesFound();
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                            default:
                                onNoMatchesFound();
                                break;
                        }
                    }
                });
    }

    private void onNoMatchesFound() {
        noMatchesView.setVisibility(View.VISIBLE);
        fixtureRecyclerView.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        fixtureMatchdayAdapter = null;
        super.onDestroy();
    }

    private static class UpdateAdapterTask extends AsyncTask<Void, Void, List<SectionedFixture>> {

        private WeakReference<FixtureActivity> ref;
        private List<ScoreFixture> scoreFixtureList;
        private FixtureMatchdayAdapter fixtureMatchdayAdapter;
        private int todayIndex;

        private UpdateAdapterTask(FixtureActivity activity, List<ScoreFixture> scoreFixtureList, FixtureMatchdayAdapter fixtureMatchdayAdapter) {
            this.ref = new WeakReference<>(activity);
            this.scoreFixtureList = scoreFixtureList;
            this.fixtureMatchdayAdapter = fixtureMatchdayAdapter;
        }

        @Override
        protected void onPreExecute() {
            ref.get().progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<SectionedFixture> doInBackground(Void... voids) {
            todayIndex = 0;
            List<SectionedFixture> sectionedFixtureList = classifyByMatchDay(scoreFixtureList);
            for (SectionedFixture fixture : sectionedFixtureList) {
                if (fixture.getSection() == LIVE_MATCHES) {
                    todayIndex = sectionedFixtureList.indexOf(fixture);
                    break;
                }
            }
            return sectionedFixtureList;
        }

        @Override
        protected void onPostExecute(List<SectionedFixture> sectionedFixtures) {
            if (fixtureMatchdayAdapter != null) {
                fixtureMatchdayAdapter.update(sectionedFixtures);
            }
            if (ref != null) {
                ref.get().progressBar.setVisibility(View.GONE);
                ref.get().fixtureRecyclerView.scrollToPosition(todayIndex);
            }
        }
    }
}