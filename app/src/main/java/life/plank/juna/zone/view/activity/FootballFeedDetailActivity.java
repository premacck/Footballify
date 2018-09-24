package life.plank.juna.zone.view.activity;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FeedItem;
import life.plank.juna.zone.util.CustomLinearLayoutManager;
import life.plank.juna.zone.util.NetworkStateReceiver;
import life.plank.juna.zone.view.adapter.FootballFeedDetailAdapter;

public class FootballFeedDetailActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {

    private static final String TAG = FootballFeedDetailActivity.class.getSimpleName();

    @BindView(R.id.football_feed_details_recycler_view)
    RecyclerView footballFeedDetailsRecyclerView;
    @BindView(R.id.parent_layout)
    RelativeLayout parentLayout;
    @BindView(R.id.blur_background_image_view)
    ImageView blurBackgroundImageView;
    CustomLinearLayoutManager customLinearLayoutManager;
    private NetworkStateReceiver networkStateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football_feed_detail);
        ButterKnife.bind(this);
        startNetworkBroadcastReceiver(this);
        blurBackgroundImageView.setBackground(new BitmapDrawable(getResources(), SwipePageActivity.parentViewBitmap));
        populateRecyclerView();
    }

    public void populateRecyclerView() {
        FootballFeedDetailAdapter mAdapter = new FootballFeedDetailAdapter(FootballFeedDetailActivity.this,
                new Gson().fromJson(getIntent().getStringExtra(getString(R.string.intent_feed_items)), new TypeToken<List<FeedItem>>() {
                }.getType())
        );
        customLinearLayoutManager = new CustomLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL);
        footballFeedDetailsRecyclerView.setLayoutManager(customLinearLayoutManager);
        try {
            footballFeedDetailsRecyclerView.getLayoutManager().scrollToPosition(Integer.parseInt((getIntent().getStringExtra(getString(R.string.intent_position)))));
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

    @Override
    public void networkAvailable() {
    }

    @Override
    public void networkUnavailable() {
        Snackbar.make(parentLayout, getString(R.string.cannot_connect_to_the_internet), Snackbar.LENGTH_SHORT).show();
    }

    public void startNetworkBroadcastReceiver(Context currentContext) {
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener((NetworkStateReceiver.NetworkStateReceiverListener) currentContext);
        registerNetworkBroadcastReceiver(currentContext);
    }

    /**
     * Register the NetworkStateReceiver
     *
     * @param currentContext
     */
    public void registerNetworkBroadcastReceiver(Context currentContext) {
        currentContext.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    /**
     * Unregister the NetworkStateReceiver with your activity
     *
     * @param
     */
    public void unregisterNetworkBroadcastReceiver() {
        try {
            this.unregisterReceiver(networkStateReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterNetworkBroadcastReceiver();
    }
}