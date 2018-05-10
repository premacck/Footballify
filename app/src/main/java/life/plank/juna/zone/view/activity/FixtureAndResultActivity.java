package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.net.HttpURLConnection;
import java.util.ArrayList;
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
import life.plank.juna.zone.view.adapter.LiveMatchesAdapter;
import life.plank.juna.zone.view.adapter.ScheduledMatchesAdapter;
import life.plank.juna.zone.view.adapter.TomorrowsMatchesAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.widget.GridLayout.VERTICAL;

public class FixtureAndResultActivity extends AppCompatActivity {
    @Inject
    @Named("default")
    Retrofit retrofit;
    @BindView(R.id.current_match_recycler_view)
    RecyclerView currentMatchRecyclerView;
    @BindView(R.id.tommorow_match_recycler_view)
    RecyclerView tommorowMatchRecyclerView;
    @BindView(R.id.weekend_match_recycler_view)
    RecyclerView weekendMatchRecyclerView;
    List<ScoreFixtureModel> scoreFixtureModelList;
    private LiveMatchesAdapter liveMatchesAdapter;
    private TomorrowsMatchesAdapter tomorrowsMatchesAdapter;
    private ScheduledMatchesAdapter scheduledMatchesAdapter;
    private RestApi restApi;
    private String TAG = FixtureAndResultActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_fixture_and_result );
        ButterKnife.bind( this );
        populateTommorowMatchFixtureRecyclerView();
        populateCurrentMatchScoreRecyclerView();
        ((ZoneApplication) getApplication()).getScoreFixtureNetworkComponent().inject( this );
        restApi = retrofit.create( RestApi.class );
        getScoreFixture( AppConstants.SEASON_NAME );
        populateSheduledScoreFixtureRecyclerView();

    }

    public void populateCurrentMatchScoreRecyclerView() {
        liveMatchesAdapter = new LiveMatchesAdapter( this );
        currentMatchRecyclerView.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false ) );
        currentMatchRecyclerView.setAdapter( liveMatchesAdapter );
        DividerItemDecoration itemDecor = new DividerItemDecoration( this, VERTICAL );
        currentMatchRecyclerView.addItemDecoration( itemDecor );

    }

    public void populateTommorowMatchFixtureRecyclerView() {
        tomorrowsMatchesAdapter = new TomorrowsMatchesAdapter( this );
        tommorowMatchRecyclerView.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false ) );
        tommorowMatchRecyclerView.setAdapter( tomorrowsMatchesAdapter );
        DividerItemDecoration itemDecor = new DividerItemDecoration( this, VERTICAL );
        tommorowMatchRecyclerView.addItemDecoration( itemDecor );

    }

    public void populateSheduledScoreFixtureRecyclerView() {
        scoreFixtureModelList = new ArrayList<>();
        scheduledMatchesAdapter = new ScheduledMatchesAdapter( this, scoreFixtureModelList );
        weekendMatchRecyclerView.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false ) );
        weekendMatchRecyclerView.setAdapter( scheduledMatchesAdapter );
        DividerItemDecoration itemDecor = new DividerItemDecoration( this, VERTICAL );
        weekendMatchRecyclerView.addItemDecoration( itemDecor );
    }

    public void populateScoreFixtureRecyclerView(List<ScoreFixtureModel> scoreFixtureModelResponse) {
        scoreFixtureModelList.addAll( scoreFixtureModelResponse );
        scheduledMatchesAdapter.notifyDataSetChanged();
    }

    public void getScoreFixture(String seasonName) {
        restApi.getScoresAndFixtures( seasonName )
                .subscribeOn( Schedulers.io() )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe( new Observer<Response<List<ScoreFixtureModel>>>() {
                    @Override
                    public void onCompleted() {
                        Log.e( TAG, "response: " );
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d( TAG, "In onCompleted()" + e );
                    }

                    @Override
                    public void onNext(Response<List<ScoreFixtureModel>> response) {
                        Log.e( TAG, "response: " + ", list data " + response.toString() );
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            populateScoreFixtureRecyclerView( response.body() );
                        }
                    }
                } );
    }
}
