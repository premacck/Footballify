package life.plank.juna.zone.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.ScoreFixtureModel;
import life.plank.juna.zone.view.adapter.FixtureAndResultAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.getClassifiedMatchesMap;

public class FixtureAndResultActivity extends AppCompatActivity {

    private static final String TAG = FixtureAndResultActivity.class.getSimpleName();

    @BindView(R.id.fixtures_section_list) RecyclerView fixtureRecyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.no_data) TextView noMatchesView;

    @Inject Picasso picasso;
    @Inject @Named("footballData") Retrofit retrofit;

    private FixtureAndResultAdapter fixtureAndResultAdapter;
    private RestApi restApi;
    private String seasonName;
    private String leagueName;
    private String countryName;

    public static void launch(Context packageContext, String seasonName, String leagueName, String countryName) {
        Intent intent = new Intent(packageContext, FixtureAndResultActivity.class);
        intent.putExtra(packageContext.getString(R.string.season_name), seasonName);
        intent.putExtra(packageContext.getString(R.string.league_name), leagueName);
        intent.putExtra(packageContext.getString(R.string.country_name), countryName);
        packageContext.startActivity(intent);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixture_and_result);
        ButterKnife.bind(this);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        restApi = retrofit.create(RestApi.class);

        Intent intent = getIntent();
        seasonName = intent.getStringExtra(getString(R.string.season_name));
        leagueName = intent.getStringExtra(getString(R.string.league_name));
        countryName = intent.getStringExtra(getString(R.string.country_name));

        populatePastMatchFixtureRecyclerView();
        getScoreFixture();
    }

    @OnClick(R.id.header) public void goBack() {
        onBackPressed();
    }

    public void populatePastMatchFixtureRecyclerView() {
        fixtureAndResultAdapter = new FixtureAndResultAdapter(this, picasso);
        fixtureRecyclerView.setAdapter(fixtureAndResultAdapter);
    }

    public void getScoreFixture() {
        progressBar.setVisibility(View.VISIBLE);
        restApi.getScoresAndFixtures(seasonName, leagueName, countryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<ScoreFixtureModel>>>() {
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
                    public void onNext(Response<List<ScoreFixtureModel>> response) {
                        progressBar.setVisibility(View.GONE);
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                List<ScoreFixtureModel> scoreFixtureModelList = response.body();
                                if (scoreFixtureModelList != null) {
                                    fixtureAndResultAdapter.update(getClassifiedMatchesMap(scoreFixtureModelList));
                                }
                                else onNoMatchesFound();
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

    @Override protected void onDestroy() {
        fixtureAndResultAdapter = null;
        super.onDestroy();
    }
}