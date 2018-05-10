package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import life.plank.juna.zone.data.network.model.LineupsModel;
import life.plank.juna.zone.view.adapter.MatchStatsAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LineupActivity extends AppCompatActivity {
    @Inject
    @Named("default")
    Retrofit retrofit;
    @BindView(R.id.visiting_team_linear_layout)
    LinearLayout visitingTeamLinearLayout;
    @BindView(R.id.match_stats_recyclerview)
    RecyclerView matchStatsRecyclerView;
    @BindView(R.id.home_team_linear_layout)
    LinearLayout homeTeamLinearLayout;
    List<List<LineupsModel.Formation>> homeTeamLineups;
    List<List<LineupsModel.Formation>> awayTeamLineups;
    private RestApi restApi;
    private ArrayList<Integer> visitingTeamFormation;
    private ArrayList<Integer> homeTeamFormation;
    private MatchStatsAdapter matchStatsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_line_up );
        ((ZoneApplication) getApplication()).getLineupNetworkComponent().inject( this );
        restApi = retrofit.create( RestApi.class );
        ButterKnife.bind( this );
        getLineUpData();
        populateMatchStatsRecyclerView();
    }

    public void getLineUpData() {
        restApi.getLineUpsData()
                .subscribeOn( Schedulers.io() )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe( new Subscriber<Response<LineupsModel>>() {
                    @Override
                    public void onCompleted() {
                        Log.e( "", "onCompleted: " );
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e( "", "onError: " + e );

                    }

                    @Override
                    public void onNext(Response<LineupsModel> listResponse) {
                        Log.e( "", "response: " + ", list data " + listResponse.toString() );
                        if (listResponse.code() == HttpURLConnection.HTTP_OK) {
                            if (listResponse.body() != null) {
                                homeTeamLineups = listResponse.body().getHomeTeamFormation();
                                awayTeamLineups = listResponse.body().getAwayTeamFormation();
                                setUpFormations();
                                setWeightSumToVisitingTeamLinearLayout();
                                setWeightSumToHomeTeamLinearLayout();
                                setUpVisitingTeamGrid();
                                setUpHomeTeamGrid();
                            }
                        } else {
                            Toast.makeText( LineupActivity.this, "Response Not Found", Toast.LENGTH_SHORT ).show();
                        }
                    }
                } );
    }

    private void setUpFormations() {
        visitingTeamFormation = new ArrayList<>();
        homeTeamFormation = new ArrayList<>();
        for (int i = 0; i < homeTeamLineups.size(); i++) {
            homeTeamFormation.add( homeTeamLineups.get( i ).size() );
        }
        for (int i = 0; i < awayTeamLineups.size(); i++) {
            visitingTeamFormation.add( awayTeamLineups.get( i ).size() );
        }
    }

    private void setWeightSumToVisitingTeamLinearLayout() {
        visitingTeamLinearLayout.setWeightSum( visitingTeamFormation.size() );
    }

    private void setWeightSumToHomeTeamLinearLayout() {
        homeTeamLinearLayout.setWeightSum( homeTeamFormation.size() );
    }

    private void setUpVisitingTeamGrid() {
        for (Integer formationSegment : visitingTeamFormation) {
            LinearLayout visitingTeamLineUpLinearLayout = new LinearLayout( this );
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, 0, 1 );
            visitingTeamLineUpLinearLayout.setLayoutParams( layoutParams );
            visitingTeamLineUpLinearLayout.setOrientation( LinearLayout.HORIZONTAL );
            visitingTeamLinearLayout.addView( visitingTeamLineUpLinearLayout );
            for (int j = 1; j <= formationSegment; j++) {
                visitingTeamLineUpLinearLayout.setWeightSum( formationSegment );
                View child = getLayoutInflater().inflate( R.layout.layout_line_up_text_view_visiting_team, null );
                TextView visitingPlayerName = (TextView) child.findViewById( R.id.visiting_team_name );
                TextView visitingPlayerNumber = (TextView) child.findViewById( R.id.visiting_team_number );
                child.setLayoutParams( new TableLayout.LayoutParams( 0, TableLayout.LayoutParams.MATCH_PARENT, 1f ) );
                visitingTeamLineUpLinearLayout.addView( child );
                for (int i = 0; i < awayTeamLineups.size(); i++) {
                    for (int k = 0; k < awayTeamLineups.get( i ).size(); k++) {
                        visitingPlayerName.setText( awayTeamLineups.get( i ).get( k ).getFullName() );
                        visitingPlayerNumber.setText( awayTeamLineups.get( i ).get( k ).getNumber().toString() );
                    }
                }
            }
        }
    }

    private void setUpHomeTeamGrid() {
        for (Integer formationSegment : homeTeamFormation) {
            LinearLayout homeTeamLineUpLinearLayout = new LinearLayout( this );
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, 0, 1 );
            homeTeamLineUpLinearLayout.setLayoutParams( layoutParams );
            homeTeamLineUpLinearLayout.setOrientation( LinearLayout.HORIZONTAL );
            homeTeamLinearLayout.addView( homeTeamLineUpLinearLayout );
            for (int j = 1; j <= formationSegment; j++) {
                homeTeamLineUpLinearLayout.setWeightSum( formationSegment );
                View playerView = getLayoutInflater().inflate( R.layout.layout_line_up_text_view_home_team, null );
                TextView playerName = (TextView) playerView.findViewById( R.id.player_name );
                TextView playerNumber = (TextView) playerView.findViewById( R.id.player_number );
                playerView.setLayoutParams( new TableLayout.LayoutParams( 0, TableLayout.LayoutParams.MATCH_PARENT, 1f ) );
                homeTeamLineUpLinearLayout.addView( playerView );
                for (int i = 0; i < homeTeamLineups.size(); i++) {
                    for (int k = 0; k < homeTeamLineups.get( i ).size(); k++) {
                        playerName.setText( homeTeamLineups.get( i ).get( k ).getFullName() );
                        playerNumber.setText( homeTeamLineups.get( i ).get( k ).getNumber().toString() );
                    }
                }
            }
        }
    }

    public void populateMatchStatsRecyclerView() {
        matchStatsAdapter = new MatchStatsAdapter( this );
        matchStatsRecyclerView.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false ) );
        matchStatsRecyclerView.setAdapter( matchStatsAdapter );
    }
}
