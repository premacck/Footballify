package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.SpacesItemDecoration;
import life.plank.juna.zone.view.adapter.MatchLeagueAdapter;

public class MatchLeagueActivity extends AppCompatActivity {

    @BindView(R.id.league_recycler_view)
    RecyclerView leagueRecyclerView;
    MatchLeagueAdapter matchLeagueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_match_league );
        ButterKnife.bind( this );
        populateRecyclerView();
    }

    public void populateRecyclerView() {
        int numberOfColumns = 4;
        leagueRecyclerView.setLayoutManager( new GridLayoutManager( this, numberOfColumns ) );
        leagueRecyclerView.addItemDecoration( new SpacesItemDecoration( 10 ) );
        matchLeagueAdapter = new MatchLeagueAdapter( this );
        leagueRecyclerView.setAdapter( matchLeagueAdapter );

    }

}