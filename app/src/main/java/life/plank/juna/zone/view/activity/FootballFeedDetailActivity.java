package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.CustomLinearLayoutManager;
import life.plank.juna.zone.view.adapter.FootballFeedDetailAdapter;

public class FootballFeedDetailActivity extends AppCompatActivity {
    @BindView(R.id.football_feed_recyclerView)
    RecyclerView footballFeedRecyclerView;
    @BindView(R.id.zone_logo)
    ImageView zoneLogo;
    CustomLinearLayoutManager customLinearLayoutManager;
    private static final String TAG = FootballFeedDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football_feed_detail);
        ButterKnife.bind(this);
        populateRecyclerView();
    }

    public void populateRecyclerView() {
        FootballFeedDetailAdapter mAdapter = new FootballFeedDetailAdapter(FootballFeedDetailActivity.this);
        customLinearLayoutManager = new CustomLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL);
        footballFeedRecyclerView.setLayoutManager(customLinearLayoutManager);
        customLinearLayoutManager.setScrollEnabled(true);
        footballFeedRecyclerView.setAdapter(mAdapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(footballFeedRecyclerView);
    }

    public void setUpRecyclerViewScroll(boolean status) {
        customLinearLayoutManager.setScrollEnabled(status);
    }
}
