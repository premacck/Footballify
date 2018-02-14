package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.TypedValue;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.GlobalVariable;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.util.helper.StartSnapHelper;
import life.plank.juna.zone.view.adapter.PinBoardFootballFeedAdapter;


/**
 * Created by plank-hasan on 19/01/18.
 */

public class PinboardActivity extends AppCompatActivity {


    @BindView(R.id.football_feed_recycler_view)
    RecyclerView feedRecyclerView;
    private static final String TAG = PinboardActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_board);
        ButterKnife.bind(this);
        initRecyclerView();
    }

    private void initRecyclerView() {
        // Calculate ActionBar height
        int actionBarHeight = 0;
        TypedValue typedValue = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
        }
        int banterSize = (int) getResources().getDimension(R.dimen.swipe_page_banter_zone_height);
        // TODO: 29-01-2018 Change based on performance

        //Football Feed recycler view
        int numberOfRows = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfRows, GridLayoutManager.HORIZONTAL, false);
        feedRecyclerView.setLayoutManager(gridLayoutManager);
        PinBoardFootballFeedAdapter pinBoardFootballFeedAdapter = new PinBoardFootballFeedAdapter(this,
                UIDisplayUtil.getDisplayMetricsData(this, GlobalVariable.getInstance().getDisplayHeight()),
                UIDisplayUtil.getDisplayMetricsData(this, GlobalVariable.getInstance().getDisplayWidth()),
                actionBarHeight + banterSize);
        feedRecyclerView.setAdapter(pinBoardFootballFeedAdapter);
        feedRecyclerView.setHasFixedSize(true);
        SnapHelper snapHelperFeedRecycler = new StartSnapHelper();
        snapHelperFeedRecycler.attachToRecyclerView(feedRecyclerView);
        pinBoardFootballFeedAdapter.setPinnedFootballFeedList();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}



