package life.plank.juna.zone.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bvapp.arcmenulibrary.ArcMenu;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.Board;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.data.network.model.LiveScoreData;
import life.plank.juna.zone.data.network.model.MatchEvent;
import life.plank.juna.zone.data.network.model.Thumbnail;
import life.plank.juna.zone.data.network.model.ZoneLiveData;
import life.plank.juna.zone.interfaces.PublicBoardHeaderListener;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.util.customview.PublicBoardToolbar;
import life.plank.juna.zone.view.fragment.board.BoardInfoFragment;
import life.plank.juna.zone.view.fragment.board.BoardTilesFragment;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.AppConstants.MATCH_EVENTS;
import static life.plank.juna.zone.util.AppConstants.SCORE_DATA;
import static life.plank.juna.zone.util.DataUtil.getZoneLiveData;
import static life.plank.juna.zone.util.PreferenceManager.getToken;
import static life.plank.juna.zone.util.UIDisplayUtil.loadBitmap;

/**
 * Created by plank-hasan on 5/3/2018.
 */

public class BoardActivity extends AppCompatActivity implements PublicBoardHeaderListener {
    private static final String TAG = BoardActivity.class.getSimpleName();
    public static Bitmap boardParentViewBitmap = null;

    @BindView(R.id.board_arc_menu)
    ArcMenu arcMenu;
    @BindView(R.id.board_toolbar)
    PublicBoardToolbar publicBoardToolbar;
    @BindView(R.id.board_view_pager)
    ViewPager viewPager;

    @Inject
    @Named("default")
    RestApi restApi;
    @Inject
    Picasso picasso;
    @Inject
    Gson gson;
    private long currentMatchId;
    private String boardId;
    private String homeTeamLogo, visitingTeamLogo;
    private int homeGoals, awayGoals, matchDay;

    private LiveScoreData liveScoreData;
    private List<MatchEvent> matchEventList;
    private BoardPagerAdapter boardPagerAdapter;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(getString(R.string.intent_content_type))) {
                setDataReceivedFromPushNotification(intent);
            } else if (intent.hasExtra(getString(R.string.zone_live_data))) {
                setZoneLiveData(intent);
            }
        }
    };

    public static void launch(Context packageContext, int homeGoals, int visitingGoals, long matchId,
                              String homeTeamLogo, String visitingTeamLogo, Integer matchDay) {
        Intent intent = new Intent(packageContext, BoardActivity.class);
        intent.putExtra(packageContext.getString(R.string.intent_home_team_score), homeGoals)
                .putExtra(packageContext.getString(R.string.intent_visiting_team_score), visitingGoals)
                .putExtra(packageContext.getString(R.string.match_id_string), matchId)
                .putExtra(packageContext.getString(R.string.pref_home_team_logo), homeTeamLogo)
                .putExtra(packageContext.getString(R.string.pref_visiting_team_logo), visitingTeamLogo)
                .putExtra(packageContext.getString(R.string.matchday_), matchDay);
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
        Log.e(TAG, "content_type: " + contentType);
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
            e.printStackTrace();
        }
    }

    private void setZoneLiveData(Intent intent) {
        ZoneLiveData zoneLiveData = getZoneLiveData(intent, getString(R.string.zone_live_data), gson);
        switch (zoneLiveData.getLiveDataType()) {
            case SCORE_DATA:
                liveScoreData = zoneLiveData.getLiveScoreDataObject(gson);
//                TODO: update live data here
                break;
            case MATCH_EVENTS:
                matchEventList = zoneLiveData.getMatchEventObject(gson);
//                TODO: update lineup substitutions from here
                break;
            default:
                break;
        }
        try {
            if (boardPagerAdapter.getCurrentFragment() instanceof BoardInfoFragment) {
                ((BoardInfoFragment) boardPagerAdapter.getCurrentFragment()).updateZoneLiveData(zoneLiveData, gson);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Device ID", FirebaseInstanceId.getInstance().getToken());
        setContentView(R.layout.activity_board);
        ButterKnife.bind(this);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);

        Intent intent = getIntent();
        currentMatchId = intent.getLongExtra(getString(R.string.match_id_string), 0);
        homeTeamLogo = intent.getStringExtra(getString(R.string.pref_home_team_logo));
        visitingTeamLogo = intent.getStringExtra(getString(R.string.pref_visiting_team_logo));
        homeGoals = intent.getIntExtra(getString(R.string.intent_home_team_score), 0);
        awayGoals = intent.getIntExtra(getString(R.string.intent_visiting_team_score), 0);
        matchDay = intent.getIntExtra(getString(R.string.matchday_), 1);

        retrieveBoardId(currentMatchId, AppConstants.BOARD_TYPE);
        setUpToolbar();
    }

    private void setUpToolbar() {
        publicBoardToolbar.setHomeTeamLogo(picasso, homeTeamLogo);
        publicBoardToolbar.setVisitingTeamLogo(picasso, visitingTeamLogo);
        publicBoardToolbar.setScore(true, homeGoals + " - " + awayGoals);
        publicBoardToolbar.setBoardTitle(getString(R.string.matchday_) + matchDay);
    }

    private void setupViewPagerWithFragments() {
        boardPagerAdapter = new BoardPagerAdapter(getSupportFragmentManager(), boardId, currentMatchId);
        viewPager.setAdapter(boardPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(publicBoardToolbar.getInfoTilesTabLayout()));
        publicBoardToolbar.getInfoTilesTabLayout().addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        Objects.requireNonNull(publicBoardToolbar.getInfoTilesTabLayout().getTabAt(1)).select();
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

    public void setUpBoomMenu() {
        UIDisplayUtil.setupBoomMenu(this, arcMenu, boardId);
    }

    @OnClick(R.id.line_chart)
    public void openTimeLine(View view) {
        if (boardParentViewBitmap == null) {
            boardParentViewBitmap = loadBitmap(getWindow().getDecorView(), getWindow().getDecorView(), this);
        }
        TimelineActivity.launch(this, view, currentMatchId);
    }

    public void retrieveBoardId(Long currentMatchId, String boardType) {
        restApi.retrieveBoard(currentMatchId, boardType, getToken(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
                            saveBoardId();

                            setUpBoomMenu();
                            setupViewPagerWithFragments();

                            String topic = getString(R.string.board_id_prefix) + boardId;
                            FirebaseMessaging.getInstance().subscribeToTopic(topic);
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
        private String boardId;
        private final long matchId;

        BoardPagerAdapter(FragmentManager fragmentManager, String boardId, long matchId) {
            super(fragmentManager);
            this.boardId = boardId;
            this.matchId = matchId;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return BoardInfoFragment.newInstance(matchId);
                case 1:
                    return BoardTilesFragment.newInstance(boardId);
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