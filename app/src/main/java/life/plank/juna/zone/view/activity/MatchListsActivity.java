package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.MatchListModel;
import life.plank.juna.zone.view.adapter.MatchListAdapter;

public class MatchListsActivity extends AppCompatActivity {

    @BindView(R.id.match_lists_recycler_view)
    RecyclerView matchListsRecyclerView;
    MatchListAdapter matchListAdapter;
    List<MatchListModel> matchListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_lists);
        ButterKnife.bind(this);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        matchListModel = new ArrayList<MatchListModel>();
        matchListAdapter = new MatchListAdapter(this, matchListModel);
        matchListsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        matchListsRecyclerView.setAdapter(matchListAdapter);
    }

}
