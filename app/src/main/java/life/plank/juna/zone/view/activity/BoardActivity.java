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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.Board;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.data.network.model.MatchFixture;
import life.plank.juna.zone.data.network.model.Thumbnail;
import life.plank.juna.zone.data.network.model.ZoneLiveData;
import life.plank.juna.zone.interfaces.PublicBoardHeaderListener;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.customview.PublicBoardToolbar;
import life.plank.juna.zone.view.fragment.board.fixture.BoardInfoFragment;
import life.plank.juna.zone.view.fragment.board.fixture.BoardTilesFragment;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.AppConstants.DASH;
import static life.plank.juna.zone.util.AppConstants.SCORE_DATA;
import static life.plank.juna.zone.util.DataUtil.getZoneLiveData;
import static life.plank.juna.zone.util.PreferenceManager.getToken;

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

    public static void launch(Context packageContext, MatchFixture fixture) {
        Intent intent = new Intent(packageContext, BoardActivity.class);
        intent.putExtra(packageContext.getString(R.string.intent_score_data), new Gson().toJson(fixture));
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
                publicBoardToolbar.setScore(true, zoneLiveData.getScoreData().getHomeGoals() + DASH + zoneLiveData.getScoreData().getAwayGoals());
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
            currentMatchId = fixture.getForeignId();
            setUpToolbar(
                    fixture.getMatchDay(),
                    fixture.getHomeTeam().getLogoLink(),
                    fixture.getAwayTeam().getLogoLink(),
                    fixture.getHomeGoals(),
                    fixture.getAwayGoals()
            );
        } else {
            currentMatchId = intent.getLongExtra(getString(R.string.match_id_string), 0);
            getMatchDetails();
        }

        publicBoardToolbar.setUpPopUp(this, currentMatchId);
        FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.pref_football_match_sub) + currentMatchId);
        retrieveBoardId(currentMatchId, AppConstants.BOARD_TYPE);
    }

    private void setUpToolbar(int matchDay, String homeTeamLogo, String visitingTeamLogo, int homeGoals, int visitingGoals) {
        publicBoardToolbar.setHomeTeamLogo(picasso, homeTeamLogo);
        publicBoardToolbar.setVisitingTeamLogo(picasso, visitingTeamLogo);
        publicBoardToolbar.setScore(true, homeGoals + DASH + visitingGoals);
        publicBoardToolbar.setBoardTitle(getString(R.string.matchday_) + matchDay);
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

    public void retrieveBoardId(Long currentMatchId, String boardType) {
        restApi.retrieveBoard(currentMatchId, boardType, getToken(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> progressBar.setVisibility(View.VISIBLE))
                .doOnCompleted(() -> progressBar.setVisibility(View.GONE))
                .subscribe(new Observer<Response<Board>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "In onError()" + e);
                        Toast.makeText(BoardActivity.this, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<Board> response) {
                        Board board = response.body();
                        if (response.code() == HttpURLConnection.HTTP_OK && board != null) {
                            boardId = board.getId();
                            isBoardActive = board.getIsActive();
                            toggleBoardActivation();
                            saveBoardId();
                            setupViewPagerWithFragments();

                            String topic = getString(R.string.board_id_prefix) + boardId;
                            FirebaseMessaging.getInstance().subscribeToTopic(topic);
                        }
                    }
                });
    }

    private void toggleBoardActivation() {
        boardParentLayout.getBackground().setColorFilter(getColor(R.color.grey_0_7), PorterDuff.Mode.SRC_OVER);
    }

    public void getMatchDetails() {
        footballRestApi.getMatchDetails(currentMatchId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> progressBar.setVisibility(View.VISIBLE))
                .doOnCompleted(() -> progressBar.setVisibility(View.GONE))
                .subscribe(new Observer<Response<MatchFixture>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "getMatchDetails() : onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(BoardActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Response<MatchFixture> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                MatchFixture matchSummary = response.body();
                                if (matchSummary != null) {
                                    setUpToolbar(
                                            matchSummary.getMatchDay(),
                                            matchSummary.getHomeTeam().getLogoLink(),
                                            matchSummary.getAwayTeam().getLogoLink(),
                                            matchSummary.getHomeGoals(),
                                            matchSummary.getAwayGoals()
                                    );
                                }
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(BoardActivity.this, R.string.match_summary_not_found, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(BoardActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    public void saveBoardId() {
        SharedPreferences.Editor boardIdEditor;
        boardIdEditor = getSharedPreferences(getString(R.string.pref_enter_board_id), Context.MODE_PRIVATE).edit();
        boardIdEditor.putString(getString(R.string.pref_enter_board_id), boardId).apply();
    }

    @Override
    public void followClicked(TextView followBtn) {
        String id = getString(R.string.board_id_prefix) + boardId;
        if (followBtn.getText().toString().equalsIgnoreCase(getString(R.string.follow))) {
            followBtn.setText(R.string.unfollow);
            FirebaseMessaging.getInstance().subscribeToTopic(id);
        } else {
            followBtn.setText(R.string.follow);
            FirebaseMessaging.getInstance().unsubscribeFromTopic(id);
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
                    return BoardInfoFragment.newInstance(ref.get().fixture, ref.get().gson);
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