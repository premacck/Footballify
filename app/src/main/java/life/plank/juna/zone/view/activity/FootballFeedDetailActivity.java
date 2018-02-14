package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.CustomLinearLayoutManager;
import life.plank.juna.zone.view.adapter.FootballFeedDetailAdapter;


public class FootballFeedDetailActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.football_feed_recyclerView)
    RecyclerView footballFeedRecyclerView;
    @BindView(R.id.zone_logo)
    ImageView zone_logo;
    CustomLinearLayoutManager customLinearLayoutManager;
    LinearLayoutManager linearLayoutManager;
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
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        customLinearLayoutManager = new CustomLinearLayoutManager(this);
        linearLayoutManager.setAutoMeasureEnabled(true);
        footballFeedRecyclerView.setLayoutManager(linearLayoutManager);
        footballFeedRecyclerView.setAdapter(mAdapter);
        footballFeedRecyclerView.getLayoutManager().scrollToPosition(Integer.parseInt((getIntent().getStringExtra("position"))));
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(footballFeedRecyclerView);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zone_logo: {
                break;
            }
        }
    }
    public void setUpRecyclerViewScroll(boolean status) {
        if (status) {
            footballFeedRecyclerView.setLayoutManager(linearLayoutManager);
        } else {
            footballFeedRecyclerView.setLayoutManager(customLinearLayoutManager);
        }
    }
}
