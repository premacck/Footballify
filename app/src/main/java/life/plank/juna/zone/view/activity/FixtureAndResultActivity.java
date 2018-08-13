package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.ScoreFixtureModel;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.view.adapter.FixtureAndResultAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.R.string.matches_not_found;
import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.getClassifiedMatchesMap;
import static life.plank.juna.zone.util.UIDisplayUtil.makeToast;

public class FixtureAndResultActivity extends AppCompatActivity {

    @BindView(R.id.fixtures_section_list) RecyclerView pastMatchRecyclerView;

    @Inject Picasso picasso;
    @Inject @Named("footballData") Retrofit retrofit;
    private FixtureAndResultAdapter fixtureAndResultAdapter;
    private RestApi restApi;
    private String TAG = FixtureAndResultActivity.class.getSimpleName();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixture_and_result);
        ButterKnife.bind(this);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        populatePastMatchFixtureRecyclerView();
        getScoreFixture(AppConstants.SEASON_NAME);
    }

    public void populatePastMatchFixtureRecyclerView() {
        fixtureAndResultAdapter = new FixtureAndResultAdapter(this, picasso);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        pastMatchRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        pastMatchRecyclerView.setAdapter(fixtureAndResultAdapter);
    }

    public void getScoreFixture(String seasonName) {
        //TODO: remove hardcoded value
        restApi.getScoresAndFixtures(seasonName, "Premier League", "England")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<ScoreFixtureModel>>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error: " + e);
                    }

                    @Override
                    public void onNext(Response<List<ScoreFixtureModel>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                List<ScoreFixtureModel> scoreFixtureModelList = response.body();
                                if (scoreFixtureModelList != null) {
                                    fixtureAndResultAdapter.update(getClassifiedMatchesMap(scoreFixtureModelList));
                                } else {
                                    makeToast(FixtureAndResultActivity.this, matches_not_found);
                                }
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                makeToast(FixtureAndResultActivity.this, matches_not_found);
                                break;
                            default:
                                makeToast(FixtureAndResultActivity.this, matches_not_found);
                                break;
                        }

                    }
                });
    }
}