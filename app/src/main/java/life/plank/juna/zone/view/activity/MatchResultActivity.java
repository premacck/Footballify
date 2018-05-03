package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.view.adapter.PlayerStatsAdapter;
import life.plank.juna.zone.view.adapter.StandingTableAdapter;
import life.plank.juna.zone.view.adapter.TeamStatsAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MatchResultActivity extends AppCompatActivity {
    @Inject
    @Named("default")
    Retrofit retrofit;
    @BindView(R.id.standing_recycler_view)
    RecyclerView standingRecyclerView;
    @BindView(R.id.team_stats_recycler_view)
    RecyclerView teamStatsRecyclerView;
    @BindView(R.id.player_stats_recycler_view)
    RecyclerView playerStatsRecyclerView;
    @BindView(R.id.tap_for_score_and_fixtures)
    TextView tapForScoreAndFixture;
    List<StandingModel> standingModel;
    List<PlayerStatsModel> playerStatsModelList;
    List<TeamStatsModel> teamStatsModelList;
    private StandingTableAdapter standingTableAdapter;
    private RestApi restApi;
    private PlayerStatsAdapter playerStatsAdapter;
    private TeamStatsAdapter teamStatsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_match_result );
        ((ZoneApplication) getApplication()).getStandingNetworkComponent().inject( this );
        restApi = retrofit.create( RestApi.class );
        ButterKnife.bind( this );
        getStandings( AppConstants.LEAGUE_NAME );
        getPlayerStats( AppConstants.SEASON_NAME );
        populateStandingRecyclerView();
        populatePlayerStatsRecyclerView();
        populateTeamStatsRecyclerView();
        getTeamStats( AppConstants.SEASON_NAME );
    }

    public void populateStandingRecyclerView() {
        standingModel = new ArrayList<>();
        standingTableAdapter = new StandingTableAdapter( this, standingModel );
        standingRecyclerView.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false ) );
        standingRecyclerView.setAdapter( standingTableAdapter );
    }

    public void populatePlayerStatsRecyclerView() {
        playerStatsModelList = new ArrayList<>();
        playerStatsAdapter = new PlayerStatsAdapter( this, playerStatsModelList );
        playerStatsRecyclerView.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false ) );
        playerStatsRecyclerView.setAdapter( playerStatsAdapter );
    }

    public void populateStandingRecyclerView(List<StandingModel> standingModels) {
        standingModel.addAll( standingModels );
        standingTableAdapter.notifyDataSetChanged();
    }

    public void populatePlayerStatsRecyclerView(List<PlayerStatsModel> playerStatsModels) {
        playerStatsModelList.addAll( playerStatsModels );
        playerStatsAdapter.notifyDataSetChanged();
    }

    public void populateTeamStatsRecyclerView(List<TeamStatsModel> teamStatsModels) {
        teamStatsModelList.addAll( teamStatsModels );
        teamStatsAdapter.notifyDataSetChanged();
    }

    public void populateTeamStatsRecyclerView() {
        teamStatsModelList = new ArrayList<>();
        teamStatsAdapter = new TeamStatsAdapter( this, teamStatsModelList );
        teamStatsRecyclerView.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false ) );
        teamStatsRecyclerView.setAdapter( teamStatsAdapter );
    }


    public void getStandings(String leagueName) {
        restApi.getStandings( leagueName )
                .subscribeOn( Schedulers.io() )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe( new Observer<Response<List<StandingModel>>>() {
                    @Override
                    public void onCompleted() {
                        Log.e( "", "response: " );
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d( "", "In onCompleted()" );
                    }

                    @Override
                    public void onNext(Response<List<StandingModel>> response) {
                        Log.e( "", "response: " + ", list data " + response.toString() );
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            populateStandingRecyclerView( response.body() );
                        } else {
                            Toast.makeText( MatchResultActivity.this, "Responce Not Found", Toast.LENGTH_SHORT ).show();
                        }
                    }
                } );
    }

    public void getPlayerStats(String seasonName) {
        restApi.getPlayerStats( seasonName )
                .subscribeOn( Schedulers.io() )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe( new Observer<Response<List<PlayerStatsModel>>>() {
                    @Override
                    public void onCompleted() {
                        Log.e( "", "response: " );
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d( "", "In onCompleted()" );
                    }

                    @Override
                    public void onNext(Response<List<PlayerStatsModel>> response) {
                        Log.e( "", "response: " + ", list data " + response.toString() );
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            populatePlayerStatsRecyclerView( response.body() );
                        }

                    }
                } );
    }

    public void getTeamStats(String seasonName) {
        restApi.getTeamStats( seasonName )
                .subscribeOn( Schedulers.io() )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe( new Observer<Response<List<TeamStatsModel>>>() {
                    @Override
                    public void onCompleted() {
                        Log.e( "", "response: " );
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d( "", "In onCompleted()" );
                    }

                    @Override
                    public void onNext(Response<List<TeamStatsModel>> response) {
                        Log.e( "", "response: " + ", list data " + response.toString() );
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            populateTeamStatsRecyclerView( response.body() );
                        }

                    }
                } );
    }
    @OnClick({R.id.tap_for_score_and_fixtures})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tap_for_score_and_fixtures:
                Intent intent = new Intent( this,FixtureAndResultActivity.class );
                startActivity( intent );
                break;
        }
    }
}

