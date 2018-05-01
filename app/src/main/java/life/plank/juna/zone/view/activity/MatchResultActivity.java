package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.view.adapter.StandingTableAdapter;
import life.plank.juna.zone.view.adapter.TeamStatsAdapter;

public class MatchResultActivity extends AppCompatActivity {
    @BindView(R.id.standing_recycler_view)
    RecyclerView standingRecyclerView;
    @BindView(R.id.team_stats_recycler_view)
    RecyclerView teamStatsRecyclerView;
    private StandingTableAdapter standingTableAdapter;
    private TeamStatsAdapter teamStatsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_match_result );
        ButterKnife.bind( this );
        populateStandingRecyclerView();
        teamStatsAdapter();
    }

    public void populateStandingRecyclerView() {
        standingTableAdapter = new StandingTableAdapter( this );
        standingRecyclerView.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false ) );
        standingRecyclerView.setAdapter( standingTableAdapter );
    }

    public void teamStatsAdapter() {
        teamStatsAdapter = new TeamStatsAdapter( this );
        teamStatsRecyclerView.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false ) );
        teamStatsRecyclerView.setAdapter( teamStatsAdapter );
    }
}

