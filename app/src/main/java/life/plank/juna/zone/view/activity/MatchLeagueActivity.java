package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.SpacesItemDecoration;
import life.plank.juna.zone.view.adapter.MatchLeagueAdapter;

public class MatchLeagueActivity extends AppCompatActivity {

    @BindView(R.id.league_recycler_view)
    RecyclerView leagueRecyclerView;
    @BindView(R.id.info)
    TextView textInfo;
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
    @OnClick({R.id.info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.info:
                Intent intent = new Intent( this,MatchResultActivity.class );
                startActivity( intent );
                break;
        }
    }
}
