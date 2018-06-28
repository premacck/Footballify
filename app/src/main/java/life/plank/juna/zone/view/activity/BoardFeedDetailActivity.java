package life.plank.juna.zone.view.activity;

import android.graphics.drawable.BitmapDrawable;
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
import life.plank.juna.zone.view.adapter.BoardFeedDetailAdapter;

public class BoardFeedDetailActivity extends AppCompatActivity {
    private static final String TAG = BoardFeedDetailActivity.class.getSimpleName();
    @BindView(R.id.board_feed_details_recycler_view)
    RecyclerView boardFeedDetailsRecyclerView;
    @BindView(R.id.board_blur_background_image_view)
    ImageView boardBlurBackgroundImageView;
    CustomLinearLayoutManager customLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_feed_detail);
        ButterKnife.bind(this);
        boardBlurBackgroundImageView.setBackground(new BitmapDrawable(getResources(), BoardActivity.boardParentViewBitmap));
        populateRecyclerView();
    }

    public void populateRecyclerView() {
        BoardFeedDetailAdapter mAdapter = new BoardFeedDetailAdapter(BoardFeedDetailActivity.this,
                new Gson().fromJson(getIntent().getStringExtra(AppConstants.FEED_ITEMS), new TypeToken<List<FootballFeed>>() {
                }.getType())
        );
        customLinearLayoutManager = new CustomLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL);
        boardFeedDetailsRecyclerView.setLayoutManager(customLinearLayoutManager);
        try {
            boardFeedDetailsRecyclerView.getLayoutManager().scrollToPosition(Integer.parseInt((getIntent().getStringExtra(AppConstants.POSITION))));
        } catch (NumberFormatException e) {
            boardFeedDetailsRecyclerView.getLayoutManager().scrollToPosition(0);
        }
        customLinearLayoutManager.setScrollEnabled(true);
        boardFeedDetailsRecyclerView.setAdapter(mAdapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(boardFeedDetailsRecyclerView);
    }
}