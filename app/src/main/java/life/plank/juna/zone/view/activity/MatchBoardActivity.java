package life.plank.juna.zone.view.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.RestApiAggregator;
import life.plank.juna.zone.data.model.Board;
import life.plank.juna.zone.data.model.FeedEntry;
import life.plank.juna.zone.data.model.FeedItem;
import life.plank.juna.zone.data.model.League;
import life.plank.juna.zone.data.model.LiveScoreData;
import life.plank.juna.zone.data.model.LiveTimeStatus;
import life.plank.juna.zone.data.model.MatchDetails;
import life.plank.juna.zone.data.model.MatchFixture;
import life.plank.juna.zone.data.model.Thumbnail;
import life.plank.juna.zone.data.model.ZoneLiveData;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.interfaces.PublicBoardHeaderListener;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.FixtureListUpdateTask;
import life.plank.juna.zone.util.customview.PublicBoardToolbar;
import life.plank.juna.zone.view.activity.base.BaseBoardActivity;
import life.plank.juna.zone.view.adapter.BoardFeedDetailAdapter;
import life.plank.juna.zone.view.fragment.board.fixture.BoardInfoFragment;
import life.plank.juna.zone.view.fragment.board.fixture.BoardTilesFragment;
import rx.Subscriber;

import static life.plank.juna.zone.util.AppConstants.BOARD_ACTIVATED;
import static life.plank.juna.zone.util.AppConstants.BOARD_DEACTIVATED;
import static life.plank.juna.zone.util.AppConstants.DASH;
import static life.plank.juna.zone.util.AppConstants.SCORE_DATA;
import static life.plank.juna.zone.util.AppConstants.TIME_STATUS_DATA;
import static life.plank.juna.zone.util.DataUtil.getZoneLiveData;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.DataUtil.updateScoreLocally;
import static life.plank.juna.zone.util.DataUtil.updateTimeStatusLocally;
import static life.plank.juna.zone.util.UIDisplayUtil.loadBitmap;
import static life.plank.juna.zone.util.UIDisplayUtil.setupSwipeGesture;
import static life.plank.juna.zone.util.UIDisplayUtil.showBoardExpirationDialog;

/**
 * Created by plank-hasan on 5/3/2018.
 */

public class MatchBoardActivity extends BaseBoardActivity implements PublicBoardHeaderListener {
    private static final String TAG = MatchBoardActivity.class.getSimpleName();

    @BindView(R.id.root_card)
    CardView rootCard;
    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;
    @BindView(R.id.drag_area)
    TextView dragArea;
    @BindView(R.id.board_parent_layout)
    CoordinatorLayout boardParentLayout;
    @BindView(R.id.board_toolbar)
    PublicBoardToolbar publicBoardToolbar;
    @BindView(R.id.board_view_pager)
    ViewPager viewPager;
    @BindView(R.id.board_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.board_blur_background_image_view)
    ImageView boardBlurBackgroundImageView;
    @BindView(R.id.board_tiles_list_full)
    RecyclerView boardTilesFullRecyclerView;

    @Inject
    @Named("default")
    RestApi restApi;
    @Inject
    @Named("footballData")
    RestApi footballRestApi;
    @Inject
    Picasso picasso;
    @Inject
    Gson gson;
    @Inject
    PagerSnapHelper pagerSnapHelper;
    private long currentMatchId;
    private boolean isBoardActive;
    private String boardId;
    private League league;
    private MatchFixture fixture;
    private MatchDetails matchDetails;

    private BoardPagerAdapter boardPagerAdapter;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(getString(R.string.intent_content_type))) {
                setDataReceivedFromPushNotification(intent);
            } else if (intent.hasExtra(getString(R.string.intent_zone_live_data))) {
                setZoneLiveData(intent);
            }
        }
    };

    public static void launch(Activity from, MatchFixture fixture, League league) {
        Intent intent = new Intent(from, MatchBoardActivity.class);
        intent.putExtra(from.getString(R.string.intent_fixture_data), fixture);
        intent.putExtra(from.getString(R.string.intent_league), league);
        from.startActivity(intent);
        from.overridePendingTransition(R.anim.float_up, R.anim.sink_up);
    }

    public void setDataReceivedFromPushNotification(Intent intent) {
        String title = intent.getStringExtra(getString(R.string.intent_comment_title));
        String contentType = intent.getStringExtra(getString(R.string.intent_content_type));
        String thumbnailUrl = intent.getStringExtra(getString(R.string.intent_thumbnail_url));
        Integer thumbnailHeight = intent.getIntExtra(getString(R.string.intent_thumbnail_height), 0);
        Integer thumbnailWidth = intent.getIntExtra(getString(R.string.intent_thumbnail_width), 0);
        String imageUrl = intent.getStringExtra(getString(R.string.intent_image_url));
        FeedEntry feed = new FeedEntry();
        Log.d(TAG, "content_type: " + contentType);
        if (feed.getFeedItem() == null) {
            feed.setFeedItem(new FeedItem());
        }
        feed.getFeedItem().setContentType(contentType);
        if (contentType.equals(AppConstants.ROOT_COMMENT)) {
            feed.getFeedItem().setTitle(title);
        } else {
            Thumbnail thumbnail = new Thumbnail();
            thumbnail.setImageWidth(thumbnailWidth);
            thumbnail.setImageHeight(thumbnailHeight);
            thumbnail.setImageUrl(thumbnailUrl);
            feed.getFeedItem().setThumbnail(thumbnail);
            feed.getFeedItem().setUrl(imageUrl);
        }
        try {
            if (boardPagerAdapter.getCurrentFragment() instanceof BoardTilesFragment) {
                ((BoardTilesFragment) boardPagerAdapter.getCurrentFragment()).updateNewPost(feed);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void setZoneLiveData(Intent intent) {
        ZoneLiveData zoneLiveData = getZoneLiveData(intent, getString(R.string.intent_zone_live_data), gson);
        switch (zoneLiveData.getLiveDataType()) {
            case SCORE_DATA:
                LiveScoreData scoreData = zoneLiveData.getScoreData(gson);
                updateScoreLocally(fixture, scoreData);
                updateScoreLocally(matchDetails, scoreData);
                publicBoardToolbar.setScore(scoreData.getHomeGoals() + DASH + scoreData.getAwayGoals());
                FixtureListUpdateTask.update(fixture, scoreData, null, true);
                break;
            case TIME_STATUS_DATA:
                LiveTimeStatus timeStatus = zoneLiveData.getLiveTimeStatus(gson);
                updateTimeStatusLocally(fixture, timeStatus);
                updateTimeStatusLocally(matchDetails, timeStatus);
                FixtureListUpdateTask.update(fixture, null, timeStatus, false);
                Date matchStartTime = fixture != null ?
                        fixture.getMatchStartTime() :
                        matchDetails != null ?
                                matchDetails.getMatchStartTime() :
                                null;
                if (matchStartTime != null) {
                    publicBoardToolbar.setLiveTimeStatus(matchStartTime, timeStatus.getTimeStatus());
                }
                break;
            case BOARD_ACTIVATED:
                isBoardActive = true;
                clearColorFilter();
                setupViewPagerWithFragments();
                break;
            case BOARD_DEACTIVATED:
                isBoardActive = false;
                applyInactiveBoardColorFilter();
                showBoardExpirationDialog(this, ((dialog, which) -> {
                    setupViewPagerWithFragments();
                    dialog.cancel();
                }));
                break;
            default:
                break;
        }
        try {
            if (boardPagerAdapter.getCurrentFragment() instanceof BoardInfoFragment) {
                ((BoardInfoFragment) boardPagerAdapter.getCurrentFragment()).updateZoneLiveData(zoneLiveData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_board);
        ButterKnife.bind(this);
        setupSwipeGesture(this, dragArea, rootCard);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);

        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.intent_fixture_data))) {
            fixture = intent.getParcelableExtra(getString(R.string.intent_fixture_data));
            league = intent.getParcelableExtra(getString(R.string.intent_league));
            currentMatchId = fixture.getMatchId();
            publicBoardToolbar.prepare(picasso, fixture, league.getThumbUrl());
        } else {
            currentMatchId = intent.getLongExtra(getString(R.string.match_id_string), 0);
        }

        publicBoardToolbar.setUpPopUp(this, currentMatchId);

        getBoardIdAndMatchDetails(currentMatchId);
    }

    @Override
    public void prepareFullScreenRecyclerView() {
        pagerSnapHelper.attachToRecyclerView(boardTilesFullRecyclerView);
        boardFeedDetailAdapter = new BoardFeedDetailAdapter(this, boardId, isBoardActive);
        boardTilesFullRecyclerView.setAdapter(boardFeedDetailAdapter);
    }

    private void setupViewPagerWithFragments() {
        boardPagerAdapter = new BoardPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(boardPagerAdapter);
        publicBoardToolbar.setupWithViewPager(viewPager);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mMessageReceiver, new IntentFilter(getString(R.string.intent_board)));
        publicBoardToolbar.initListeners(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mMessageReceiver);
        publicBoardToolbar.dispose();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseMessaging.getInstance().unsubscribeFromTopic(getString(R.string.pref_football_match_sub) + currentMatchId);
        if (!isNullOrEmpty(boardId) && !isBoardActive) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(getString(R.string.board_id_prefix) + boardId);
        }
    }

    @OnClick(R.id.board_blur_background_image_view)
    public void dismissFullScreenRecyclerView() {
        setBlurBackgroundAndShowFullScreenTiles(false, 0);
    }

    public void getBoardIdAndMatchDetails(Long currentMatchId) {
        RestApiAggregator.getBoardAndMatchDetails(restApi, footballRestApi, currentMatchId)
                .doOnSubscribe(() -> progressBar.setVisibility(View.VISIBLE))
                .doOnTerminate(() -> progressBar.setVisibility(View.GONE))
                .subscribe(new Subscriber<Pair<Board, MatchDetails>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: getBoardIdAndMatchDetails");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "In onError() : getBoardIdAndMatchDetails" + e);
                        Toast.makeText(MatchBoardActivity.this, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Pair<Board, MatchDetails> boardMatchDetailsPair) {
                        if (boardMatchDetailsPair != null) {
                            matchDetails = boardMatchDetailsPair.second;
                            Board board = boardMatchDetailsPair.first;
                            if (matchDetails != null) {
                                publicBoardToolbar.prepare(picasso, MatchFixture.Companion.from(matchDetails), league.getThumbUrl());
                            }
                            if (board != null) {
                                boardId = board.getId();
                                isBoardActive = board.isActive();
                                saveBoardId();
                                prepareFullScreenRecyclerView();

                                if (isBoardActive) {
                                    FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.board_id_prefix) + boardId);
                                    FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.pref_football_match_sub) + currentMatchId);
                                } else applyInactiveBoardColorFilter();
                            } else applyInactiveBoardColorFilter();
                            
                            setupViewPagerWithFragments();
                        } else {
                            Toast.makeText(MatchBoardActivity.this, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void clearColorFilter() {
        boardParentLayout.getBackground().clearColorFilter();
    }

    private void applyInactiveBoardColorFilter() {
        boardParentLayout.getBackground().setColorFilter(getColor(R.color.grey_0_7), PorterDuff.Mode.SRC_OVER);
    }

    public void saveBoardId() {
        SharedPreferences.Editor boardIdEditor;
        boardIdEditor = getSharedPreferences(getString(R.string.pref_enter_board_id), Context.MODE_PRIVATE).edit();
        boardIdEditor.putString(getString(R.string.pref_enter_board_id), boardId).apply();
    }

    @Override
    public void followClicked(TextView followBtn) {
        if (isBoardActive) {
            String id = getString(R.string.board_id_prefix) + boardId;
            if (followBtn.getText().toString().equalsIgnoreCase(getString(R.string.follow))) {
                followBtn.setText(R.string.unfollow);
                FirebaseMessaging.getInstance().subscribeToTopic(id);
            } else {
                followBtn.setText(R.string.follow);
                FirebaseMessaging.getInstance().unsubscribeFromTopic(id);
            }
        } else {
            Toast.makeText(MatchBoardActivity.this, R.string.board_not_active, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMatchTimeStateChange() {
        getBoardIdAndMatchDetails(currentMatchId);
    }

    @Override
    public void updateFullScreenAdapter(List<FeedEntry> feedEntryList) {
        boardFeedDetailAdapter.update(feedEntryList);
    }

    @Override
    public void setBlurBackgroundAndShowFullScreenTiles(boolean setFlag, int position) {
        isTileFullScreenActive = setFlag;
        boardParentViewBitmap = setFlag ? loadBitmap(rootLayout, rootLayout, this) : null;
        boardBlurBackgroundImageView.setImageBitmap(boardParentViewBitmap);

        Animation.AnimationListener listener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                boardTilesFullRecyclerView.setVisibility(View.INVISIBLE);
                boardBlurBackgroundImageView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        };
        Animation recyclerViewAnimation = AnimationUtils.loadAnimation(this, setFlag ? R.anim.zoom_in : R.anim.zoom_out);
        Animation blurBackgroundAnimation = AnimationUtils.loadAnimation(this, setFlag ? android.R.anim.fade_in : android.R.anim.fade_out);
        if (!setFlag) {
            recyclerViewAnimation.setAnimationListener(listener);
            blurBackgroundAnimation.setAnimationListener(listener);
        }
        boardTilesFullRecyclerView.startAnimation(recyclerViewAnimation);
        boardBlurBackgroundImageView.startAnimation(blurBackgroundAnimation);

        if (setFlag) {
            boardTilesFullRecyclerView.scrollToPosition(position);
            boardTilesFullRecyclerView.setVisibility(View.VISIBLE);
            boardBlurBackgroundImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void moveItem(int position, int previousPosition) {
        if (boardPagerAdapter.getCurrentFragment() instanceof BoardTilesFragment) {
            ((BoardTilesFragment) boardPagerAdapter.getCurrentFragment()).moveItem(position, previousPosition);
        }
    }

    static class BoardPagerAdapter extends FragmentPagerAdapter {

        private Fragment currentFragment;
        private WeakReference<MatchBoardActivity> ref;

        BoardPagerAdapter(FragmentManager supportFragmentManager, MatchBoardActivity matchBoardActivity) {
            super(supportFragmentManager);
            ref = new WeakReference<>(matchBoardActivity);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return BoardInfoFragment.newInstance(ref.get().gson.toJson(ref.get().matchDetails));
                case 1:
                    return BoardTilesFragment.newInstance(ref.get().boardId, true);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (getCurrentFragment() != object) {
                currentFragment = (Fragment) object;
            }
            super.setPrimaryItem(container, position, object);
        }

        Fragment getCurrentFragment() {
            return currentFragment;
        }
    }

    @Override
    public void onBackPressed() {
        if (isTileFullScreenActive) {
            setBlurBackgroundAndShowFullScreenTiles(false, 0);
        } else {
            boardFeedDetailAdapter = null;
            boardPagerAdapter = null;
            super.onBackPressed();
            overridePendingTransition(R.anim.float_down, R.anim.sink_down);
        }
    }
}