package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.view.adapter.FootballFeedDetailAdapter;

public class FootballFeedDetailActivity extends AppCompatActivity {


    @BindView(R.id.footballFeedRecyclerView)
    RecyclerView footballFeedRecyclerView;
    @BindView(R.id.image_cancel)
    ImageView imageCancel;
    @BindView(R.id.post_comment)
    Button sendComment;
    @BindView(R.id.add_comment)
    EditText addComment;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    private FootballFeedDetailAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football_feed_detail);
        ButterKnife.bind(this);
        populateRecyclerView();

    }

    public List<FootballFeed> getData() {
        List<FootballFeed> listData = new ArrayList<>();
        int[] images = {R.drawable.ic_third_dummy, R.drawable.ic_second_dummy, R.drawable.ic_fourth_dummy};
        String[] data = {"Reece Burke celebrates after breaking the deadlock in extra time at London Stadium",
                "Utd to subsidised fans in Sevilla row. $89 to visit Old Trafford for spanish supporters",
                "Charlie Nicholas : FA Cup third round replay predictions",
                "Brighton vs Chelsea",
                "Reece Burke celebrates after breaking the deadlock in extra time at London Stadium",
                "Utd to subsidised fans in Sevilla row. $89 to visit Old Trafford for spanish supporters",
                "Charlie Nicholas : FA Cup third round replay predictions",
                "Brighton vs Chelsea"};

        String[] feedContentDetails = {"Scrolling by some of your favourite Instagram feeds and noticed a trend of Instagram users putting a group of hashtags in the first comment," +
                " instead of in their image caption",
                "Reece Burke celebrates after breaking the deadlock in extra time at London Stadium",
                "Utd to subsidised fans in Sevilla row. $89 to visit Old Trafford for spanish supporters",
                "Charlie Nicholas : FA Cup third round replay predictions",
                "Scrolling by some of your favourite Football feeds and noticed a trend of Football users putting a group of hashtags in the first comment," +
                        " instead of in their image caption"};
        for (int x = 0; x < 3; x++) {
            FootballFeed footballFeedDetailsModel = new FootballFeed();
            footballFeedDetailsModel.setHeadline(data[x]);
            footballFeedDetailsModel.setSummary(feedContentDetails[x]);
            footballFeedDetailsModel.setUrl(String.valueOf(images[x]));
            listData.add(footballFeedDetailsModel);
        }
        return listData;
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
        mAdapter = new FootballFeedDetailAdapter(this, getData());
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
