package life.plank.juna.zone.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.model.Arena;
import life.plank.juna.zone.data.network.model.FootballMatch;
import life.plank.juna.zone.presentation.adapter.PointsMatchAdapter;
import life.plank.juna.zone.util.CustomizeStatusBar;

public class PointsMatchActivity extends AppCompatActivity {

    @BindView(R.id.match_points_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.year_text)
    TextView yearText;
    @BindView(R.id.round_number)
    TextView roundNumberText;

    PointsMatchAdapter pointsMatchAdapter = new PointsMatchAdapter();
    List<FootballMatch> footballMatchList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_match);
        CustomizeStatusBar.removeStatusBar(getWindow());
        ButterKnife.bind(this);
        initRecyclerView();

        footballMatchList = Arena.arena.getRounds().get(ZoneApplication.roundNumber).getFootballMatches();
        pointsMatchAdapter.setNewsFeedList(footballMatchList);
        Integer leagueStartYear = Arena.arena.getLeagueYearStart();
        Integer previousYear = leagueStartYear - 1;
        yearText.setText(previousYear.toString() + "-" + leagueStartYear.toString());
        roundNumberText.setText((++ZoneApplication.roundNumber).toString());
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.horizontal_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(pointsMatchAdapter);

        //Todo: replace toast with dialog for on click of match
        pointsMatchAdapter.getViewClickedObservable()
                .subscribe(footballMatch -> Toast.makeText(this, footballMatch.getHomeTeam().getName(), Toast.LENGTH_SHORT).show());
    }

    @OnClick(R.id.home_icon)
    public void exitPointsMatchGame() {
        startActivity(new Intent(this, GameLaunchActivity.class));
    }
}
