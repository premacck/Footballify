package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.view.adapter.FootballFeedDetailAdapter;


public class FootballFeedDetailActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.football_feed_recyclerView)
    RecyclerView footballFeedRecyclerView;
    @BindView(R.id.zone_logo)
    ImageView zone_logo;

    private SlidingUpPanelLayout mLayout;

    FootballFeed footballFeed;
    private static final String TAG = FootballFeedDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football_feed_detail);
        ButterKnife.bind(this);
        Gson gson = new Gson();
        footballFeed = gson.fromJson(getIntent().getStringExtra("FOOTBALL_FEED"), FootballFeed.class);
        populateRecyclerView();
    }



    public void populateRecyclerView() {
        FootballFeedDetailAdapter mAdapter = new FootballFeedDetailAdapter(FootballFeedDetailActivity.this, footballFeed);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        footballFeedRecyclerView.setLayoutManager(layoutManager);
        footballFeedRecyclerView.setAdapter(mAdapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(footballFeedRecyclerView);
        footballFeedRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.zone_logo:{
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                break;
            }
        }
    }

    public void setUpRecyclerViewScroll(boolean status){
        footballFeedRecyclerView.setNestedScrollingEnabled(status);
    }
}
