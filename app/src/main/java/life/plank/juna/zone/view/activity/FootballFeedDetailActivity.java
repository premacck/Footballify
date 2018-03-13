package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.CustomLinearLayoutManager;
import life.plank.juna.zone.view.adapter.FootballFeedDetailAdapter;

public class FootballFeedDetailActivity extends AppCompatActivity {
    private static final String TAG = FootballFeedDetailActivity.class.getSimpleName();
    @BindView(R.id.football_feed_details_recycler_view)
    RecyclerView footballFeedDetailsRecyclerView;
    @BindView(R.id.zone_logo)
    ImageView zoneLogo;
    CustomLinearLayoutManager customLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football_feed_detail);
        ButterKnife.bind(this);
        populateRecyclerView();
    }

    public void populateRecyclerView() {
        FootballFeedDetailAdapter mAdapter = new FootballFeedDetailAdapter(FootballFeedDetailActivity.this,
                new Gson().fromJson(getIntent().getStringExtra(AppConstants.FEED_ITEMS), new TypeToken<List<FootballFeed>>() {
                }.getType())
        );
        customLinearLayoutManager = new CustomLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL);
        footballFeedDetailsRecyclerView.setLayoutManager(customLinearLayoutManager);
        try {
            footballFeedDetailsRecyclerView.getLayoutManager().scrollToPosition(Integer.parseInt((getIntent().getStringExtra(AppConstants.POSITION))));
        } catch (NumberFormatException e) {
            footballFeedDetailsRecyclerView.getLayoutManager().scrollToPosition(0);
        }
        customLinearLayoutManager.setScrollEnabled(true);
        footballFeedDetailsRecyclerView.setAdapter(mAdapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(footballFeedDetailsRecyclerView);
    }

    public void setUpRecyclerViewScroll(boolean status) {
        customLinearLayoutManager.setScrollEnabled(status);
    }
}