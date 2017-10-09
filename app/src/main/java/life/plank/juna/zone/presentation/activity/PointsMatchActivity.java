package life.plank.juna.zone.presentation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.model.Arena;
import life.plank.juna.zone.data.network.model.FootballMatch;
import life.plank.juna.zone.presentation.adapter.PointsMatchAdapter;

public class PointsMatchActivity extends AppCompatActivity {

    @BindView(R.id.match_points_recycler_view)
    RecyclerView recyclerView;

    private PointsMatchAdapter pointsMatchAdapter = new PointsMatchAdapter();
    private List<FootballMatch> footballMatchList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_match);
        ButterKnife.bind(this);
        initRecyclerView();
        footballMatchList = Arena.getInstance()
                .getRounds()
                .get(ZoneApplication.roundNumber)
                .getFootballMatches();
        pointsMatchAdapter.setFootballMatchList(footballMatchList);
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pointsMatchAdapter);
    }
}
