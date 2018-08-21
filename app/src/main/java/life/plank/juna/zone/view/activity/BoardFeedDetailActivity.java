package life.plank.juna.zone.view.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.util.CustomLinearLayoutManager;
import life.plank.juna.zone.view.adapter.BoardFeedDetailAdapter;

import static life.plank.juna.zone.util.UIDisplayUtil.setSharedElementTransitionDuration;

public class BoardFeedDetailActivity extends AppCompatActivity {
    private static final String TAG = BoardFeedDetailActivity.class.getSimpleName();
    @BindView(R.id.board_feed_details_recycler_view)
    RecyclerView boardFeedDetailsRecyclerView;
    @BindView(R.id.board_blur_background_image_view)
    ImageView boardBlurBackgroundImageView;
    CustomLinearLayoutManager customLinearLayoutManager;
    String boardId;

    public static void launch(Activity packageContext, int position, List<FootballFeed> boardFeed, String boardId, View fromView) {
        Intent intent = new Intent(packageContext, BoardFeedDetailActivity.class);
        intent.putExtra(packageContext.getString(R.string.intent_position), String.valueOf(position));
        intent.putExtra(packageContext.getString(R.string.intent_feed_items), new Gson().toJson(boardFeed));
        intent.putExtra(packageContext.getString(R.string.intent_board_id), boardId);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(packageContext, Pair.create(fromView, "board_tile_transition"));
        packageContext.startActivity(intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_feed_detail);
        ButterKnife.bind(this);
        setSharedElementTransitionDuration(this, 200);
        boardId = getIntent().getStringExtra(getString(R.string.intent_board_id));
        boardBlurBackgroundImageView.setBackground(new BitmapDrawable(getResources(), BoardActivity.boardParentViewBitmap));
        populateRecyclerView();
    }

    @OnClick({R.id.board_blur_background_image_view})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.board_blur_background_image_view:
                this.onBackPressed();
        }
    }

    public void populateRecyclerView() {
        BoardFeedDetailAdapter mAdapter = new BoardFeedDetailAdapter(BoardFeedDetailActivity.this,
                new Gson().fromJson(getIntent().getStringExtra(getString(R.string.intent_feed_items)), new TypeToken<List<FootballFeed>>() {
                }.getType()), boardId);
        customLinearLayoutManager = new CustomLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL);
        boardFeedDetailsRecyclerView.setLayoutManager(customLinearLayoutManager);
        try {
            boardFeedDetailsRecyclerView.getLayoutManager().scrollToPosition(Integer.parseInt((getIntent().getStringExtra(getString(R.string.intent_position)))));
        } catch (NumberFormatException e) {
            boardFeedDetailsRecyclerView.getLayoutManager().scrollToPosition(0);
        }
        customLinearLayoutManager.setScrollEnabled(true);
        boardFeedDetailsRecyclerView.setAdapter(mAdapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(boardFeedDetailsRecyclerView);
    }
}