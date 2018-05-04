package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.view.adapter.CurrentMatchScoreAdapter;
import life.plank.juna.zone.view.adapter.TommorowMatchSheduleAdapter;
import life.plank.juna.zone.view.adapter.UpcomingMatchSheduleAdapter;

import static android.widget.GridLayout.HORIZONTAL;
import static android.widget.GridLayout.VERTICAL;

public class FixtureAndResultActivity extends AppCompatActivity {

    @BindView(R.id.current_match_recycler_view)
    RecyclerView currentMatchRecyclerView;
    @BindView(R.id.tommorow_match_recycler_view)
    RecyclerView tommorowMatchRecyclerView;
    @BindView(R.id.weekend_match_recycler_view)
    RecyclerView weekendMatchRecyclerView;
    private CurrentMatchScoreAdapter currentMatchScoreAdapter;
    private TommorowMatchSheduleAdapter tommorowMatchSheduleAdapter;
    private UpcomingMatchSheduleAdapter upcomingMatchSheduleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_fixture_and_result );
        ButterKnife.bind( this );
        populateScoreFixtureRecyclerView();
        populateTommorowMatchFixtureRecyclerView();
        populateCurrentMatchScoreRecyclerView();
    }

    public void populateCurrentMatchScoreRecyclerView() {
        currentMatchScoreAdapter = new CurrentMatchScoreAdapter( this );
        currentMatchRecyclerView.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false ) );
        currentMatchRecyclerView.setAdapter( currentMatchScoreAdapter );
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, VERTICAL);
        currentMatchRecyclerView.addItemDecoration(itemDecor);

    }

    public void populateTommorowMatchFixtureRecyclerView() {
        tommorowMatchSheduleAdapter = new TommorowMatchSheduleAdapter( this );
        tommorowMatchRecyclerView.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false ) );
        tommorowMatchRecyclerView.setAdapter( tommorowMatchSheduleAdapter );
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, VERTICAL);
        tommorowMatchRecyclerView.addItemDecoration(itemDecor);

    }

    public void populateScoreFixtureRecyclerView() {
        upcomingMatchSheduleAdapter = new UpcomingMatchSheduleAdapter( this );
        weekendMatchRecyclerView.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false ) );
        weekendMatchRecyclerView.setAdapter( upcomingMatchSheduleAdapter );
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, VERTICAL);
        weekendMatchRecyclerView.addItemDecoration(itemDecor);
    }
}
