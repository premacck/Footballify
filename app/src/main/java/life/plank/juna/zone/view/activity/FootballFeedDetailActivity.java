package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.view.adapter.FootballFeedDetailAdapter;


public class FootballFeedDetailActivity extends AppCompatActivity {
    @BindView(R.id.football_feed_recyclerView)
    RecyclerView footballFeedRecyclerView;
    @BindView(R.id.image_cancel)
    ImageView imageCancel;
    @BindView(R.id.post_comment)
    Button sendComment;
    @BindView(R.id.add_comment)
    EditText addComment;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    FootballFeed footballFeed;
    private FootballFeedDetailAdapter mAdapter;
    private static final String TAG = FootballFeedDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football_feed_detail);
        ButterKnife.bind(this);
        Gson gson = new Gson();
        footballFeed = gson.fromJson(getIntent().getStringExtra("FOOTBALL_FEED"),FootballFeed.class);
       // Log.e(TAG,"value"+gson.fromJson(getIntent().getStringExtra("FOOTBALL_FEED"),FootballFeed.class));
        populateRecyclerView();
    }
    @OnClick({R.id.image_cancel, R.id.post_comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_cancel:
                finish();
                break;
            case R.id.post_comment:
                Toast.makeText(FootballFeedDetailActivity.this, "commented", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void populateRecyclerView() {
        mAdapter = new FootballFeedDetailAdapter(FootballFeedDetailActivity.this,footballFeed);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        footballFeedRecyclerView.setLayoutManager(layoutManager);
        footballFeedRecyclerView.setAdapter(mAdapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(footballFeedRecyclerView);
        footballFeedRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

    }

}
