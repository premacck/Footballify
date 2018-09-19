package life.plank.juna.zone.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.RestApiAggregator;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.Board;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.data.network.model.MatchDetails;
import life.plank.juna.zone.data.network.model.MatchFixture;
import life.plank.juna.zone.data.network.model.Thumbnail;
import life.plank.juna.zone.data.network.model.ZoneLiveData;
import life.plank.juna.zone.interfaces.PublicBoardHeaderListener;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.DataUtil;
import life.plank.juna.zone.util.customview.PublicBoardToolbar;
import life.plank.juna.zone.view.fragment.board.fixture.BoardInfoFragment;
import life.plank.juna.zone.view.fragment.board.fixture.BoardTilesFragment;
import rx.Observer;

import static life.plank.juna.zone.util.AppConstants.DASH;
import static life.plank.juna.zone.util.AppConstants.SCORE_DATA;
import static life.plank.juna.zone.util.DataUtil.getZoneLiveData;

/**
 * Created by plank-hasan on 5/3/2018.
 */

public class BoardActivity extends AppCompatActivity implements PublicBoardHeaderListener {
    private static final String TAG = BoardActivity.class.getSimpleName();
    public static Bitmap boardParentViewBitmap = null;

    @BindView(R.id.board_parent_layout)
    CoordinatorLayout boardParentLayout;
    @BindView(R.id.board_toolbar)
    PublicBoardToolbar publicBoardToolbar;
    @BindView(R.id.board_view_pager)
    ViewPager viewPager;
    @BindView(R.id.board_progress_bar)
    ProgressBar progressBar;

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
    private long currentMatchId;
    private boolean isBoardActive;
    private String boardId;
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

    public static void launch(Context packageContext, String fixtureJson) {
        Intent intent = new Intent(packageContext, BoardActivity.class);
        intent.putExtra(packageContext.getString(R.string.intent_score_data), fixtureJson);
        packageContext.startActivity(intent);
    }

    public void setDataReceivedFromPushNotification(Intent intent) {
        String title = intent.getStringExtra(getString(R.string.intent_comment_title));
        String contentType = intent.getStringExtra(getString(R.string.intent_content_type));
        String thumbnailUrl = intent.getStringExtra(getString(R.string.intent_thumbnail_url));
        Integer thumbnailHeight = intent.getIntExtra(getString(R.string.intent_thumbnail_height), 0);
        Integer thumbnailWidth = intent.getIntExtra(getString(R.string.intent_thumbnail_width), 0);
        String imageUrl = intent.getStringExtra(getString(R.string.intent_image_url));
        FootballFeed footballFeed = new FootballFeed();
        Log.d(TAG, "content_type: " + contentType);
        footballFeed.setContentType(contentType);
        if (contentType.equals(AppConstants.ROOT_COMMENT)) {
            footballFeed.setTitle(title);
        } else {
            Thumbnail thumbnail = new Thumbnail();
            thumbnail.setImageWidth(thumbnailWidth);
            thumbnail.setImageHeight(thumbnailHeight);
            thumbnail.setImageUrl(thumbnailUrl);
            footballFeed.setThumbnail(thumbnail);
            footballFeed.setUrl(imageUrl);
        }
        try {
            if (boardPagerAdapter.getCurrentFragment() instanceof BoardTilesFragment) {
                ((BoardTilesFragment) boardPagerAdapter.getCurrentFragment()).updateNewPost(footballFeed);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void setZoneLiveData(Intent intent) {
        ZoneLiveData zoneLiveData = getZoneLiveData(intent, getString(R.string.intent_zone_live_data), gson);
        switch (zoneLiveData.getLiveDataType()) {
            case SCORE_DATA:
                publicBoardToolbar.setScore(zoneLiveData.getScoreData().getHomeGoals() + DASH + zoneLiveData.getScoreData().getAwayGoals());
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
        setContentView(R.layout.activity_board);
        ButterKnife.bind(this);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);

        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.intent_score_data))) {
            fixture = gson.fromJson(intent.getStringExtra(getString(R.string.intent_score_data)), MatchFixture.class);
            currentMatchId = fixture.getMatchId();
            if (fixture != null) {
                publicBoardToolbar.prepare(picasso, fixture);
            }
        } else {
            currentMatchId = intent.getLongExtra(getString(R.string.match_id_string), 0);
        }

        publicBoardToolbar.setUpPopUp(this, currentMatchId);
        if (isBoardActive) {
            FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.pref_football_match_sub) + currentMatchId);
        }

        getBoardIdAndMatchDetails(currentMatchId);
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
    }

    public void getBoardIdAndMatchDetails(Long currentMatchId) {
        RestApiAggregator.getBoardAndMatchDetails(restApi, footballRestApi, currentMatchId)
                .doOnSubscribe(() -> progressBar.setVisibility(View.VISIBLE))
                .doOnTerminate(() -> progressBar.setVisibility(View.GONE))
                .subscribe(new Observer<Pair<Board, MatchDetails>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: getBoardIdAndMatchDetails");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "In onError() : getBoardIdAndMatchDetails" + e);
                        Toast.makeText(BoardActivity.this, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Pair<Board, MatchDetails> boardMatchDetailsPair) {
                        if (boardMatchDetailsPair != null) {
                            matchDetails = boardMatchDetailsPair.second;
                            if (matchDetails != null) {
                                publicBoardToolbar.prepare(picasso, MatchFixture.from(matchDetails));
                            }
                            boardId = boardMatchDetailsPair.first.getId();
                            isBoardActive = DataUtil.isBoardActive(matchDetails.getMatchStartTime());
                            toggleBoardActivation();
                            saveBoardId();
                            setupViewPagerWithFragments();

                            String topic = getString(R.string.board_id_prefix) + boardId;
                            if (isBoardActive) {
                                FirebaseMessaging.getInstance().subscribeToTopic(topic);
                            }
                        } else {
                            Toast.makeText(BoardActivity.this, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void toggleBoardActivation() {
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
            Toast.makeText(BoardActivity.this, R.string.board_not_active, Toast.LENGTH_LONG).show();
        }
    }

    static class BoardPagerAdapter extends FragmentPagerAdapter {

        private Fragment currentFragment;
        private WeakReference<BoardActivity> ref;

        BoardPagerAdapter(FragmentManager supportFragmentManager, BoardActivity boardActivity) {
            super(supportFragmentManager);
            ref = new WeakReference<>(boardActivity);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return BoardInfoFragment.newInstance(ref.get().gson.toJson(ref.get().matchDetails));
                case 1:
                    return BoardTilesFragment.newInstance(ref.get().boardId, ref.get().isBoardActive);
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
}