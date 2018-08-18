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
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bvapp.arcmenulibrary.ArcMenu;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
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
import life.plank.juna.zone.data.network.model.Thumbnail;
import life.plank.juna.zone.interfaces.PublicBoardHeaderListener;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.customview.PublicBoardToolbar;
import life.plank.juna.zone.view.fragment.board.BoardInfoFragment;
import life.plank.juna.zone.view.fragment.board.BoardTilesFragment;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    private String boardId;
    private String homeTeamLogo, awayTeamLogo;
    private int homeGoals, awayGoals, matchDay;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setDataReceivedFromPushNotification(intent);
        }
    };

    public static void launch(Context packageContext, int homeGoals, int awayGoals, long matchId,
                              String homeTeamLogo, String awayTeamLogo, Integer matchDay) {
        Intent intent = new Intent(packageContext, BoardActivity.class);
        intent.putExtra(packageContext.getString(R.string.intent_home_team_score), homeGoals)
                .putExtra(packageContext.getString(R.string.intent_visiting_team_score), awayGoals)
                .putExtra(packageContext.getString(R.string.match_id_string), matchId)
                .putExtra(packageContext.getString(R.string.pref_home_team_logo), homeTeamLogo)
                .putExtra(packageContext.getString(R.string.pref_away_team_logo), awayTeamLogo)
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
        BoardTilesFragment boardTilesFragment = (BoardTilesFragment) getSupportFragmentManager().findFragmentByTag(BoardTilesFragment.class.getSimpleName());
        if (boardTilesFragment != null) {
            boardTilesFragment.updateNewPost(footballFeed);
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
        long currentMatchId = intent.getLongExtra(getString(R.string.match_id_string), 0);
        homeTeamLogo = intent.getStringExtra(getString(R.string.pref_home_team_logo));
        awayTeamLogo = intent.getStringExtra(getString(R.string.pref_away_team_logo));
        homeGoals = intent.getIntExtra(getString(R.string.intent_home_team_score), 0);
        awayGoals = intent.getIntExtra(getString(R.string.intent_visiting_team_score), 0);
        matchDay = intent.getIntExtra(getString(R.string.matchday_), 1);

        setUpBoomMenu();

        retrieveBoardId(currentMatchId, AppConstants.BOARD_TYPE);
        setUpToolbar();

    }

    private void setUpToolbar() {
        publicBoardToolbar.setHomeTeamLogo(picasso, homeTeamLogo);
        publicBoardToolbar.setAwayTeamLogo(picasso, awayTeamLogo);
        publicBoardToolbar.setScore(true, homeGoals + " - " + awayGoals);
        publicBoardToolbar.setBoardTitle(getString(R.string.matchday_) + matchDay);
    }

    private void setupViewPagerWithFragments() {
        viewPager.setAdapter(new BoardPagerAdapter(getSupportFragmentManager(), boardId));
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
        //todo: will be add in Utils so we can Reuse the Code
        arcMenu.setIcon(R.drawable.ic_un, R.drawable.ic_close_white);
        int[] fabImages = {R.drawable.ic_settings_white,
                R.drawable.ic_person, R.drawable.ic_home_purple, R.drawable.ic_gallery,
                R.drawable.ic_camera_white, R.drawable.ic_mic, R.drawable.text_icon, R.drawable.ic_link, R.drawable.ic_video};
        int[] backgroundColors = {R.drawable.fab_circle_background_grey,
                R.drawable.fab_circle_background_grey, R.drawable.fab_circle_background_white, R.drawable.fab_circle_background_pink,
                R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink};
        String[] titles = {"Settings", "Profile", "Home", "Gallery", "Camera", "Audio", "Comment", "Attachment", "Video"};
        for (int i = 0; i < fabImages.length; i++) {
            View child = getLayoutInflater().inflate(R.layout.layout_floating_action_button, null);
            RelativeLayout fabRelativeLayout = child.findViewById(R.id.fab_relative_layout);
            ImageView fabImageVIew = child.findViewById(R.id.fab_image_view);
            fabRelativeLayout.setBackground(ContextCompat.getDrawable(this, backgroundColors[i]));
            fabImageVIew.setImageResource(fabImages[i]);
            final int position = i;
            //TODO: move common code to separate method
            arcMenu.addItem(child, titles[i], new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0: {
                            break;
                        }
                        case 1: {
                            break;
                        }
                        case 2: {
                            break;
                        }
                        case 3: {
                            Intent intent = new Intent(BoardActivity.this, CameraActivity.class);
                            intent.putExtra(getString(R.string.intent_open_from), getString(R.string.gallery));
                            intent.putExtra(getString(R.string.intent_board_id), boardId);
                            intent.putExtra(getString(R.string.intent_api), getString(R.string.intent_board_activity));
                            startActivity(intent);
                            break;
                        }
                        case 4: {
                            Intent intent = new Intent(BoardActivity.this, CameraActivity.class);
                            intent.putExtra(getString(R.string.intent_open_from), getString(R.string.camera));
                            intent.putExtra(getString(R.string.intent_board_id), boardId);
                            intent.putExtra(getString(R.string.intent_api), getString(R.string.intent_board_activity));
                            startActivity(intent);
                            break;
                        }
                        case 5: {
                            Intent intent = new Intent(BoardActivity.this, CameraActivity.class);
                            intent.putExtra(getString(R.string.intent_open_from), getString(R.string.intent_audio));
                            intent.putExtra(getString(R.string.intent_board_id), boardId);
                            intent.putExtra(getString(R.string.intent_api), getString(R.string.intent_board_activity));
                            startActivity(intent);
                            break;
                        }
                        case 6: {
                            Intent intent = new Intent(BoardActivity.this, PostCommentActivity.class);
                            intent.putExtra(getString(R.string.intent_board_id), boardId);
                            startActivity(intent);
                            break;
                        }
                        case 7: {
                            break;
                        }
                        case 8: {
                            Intent intent = new Intent(BoardActivity.this, CameraActivity.class);
                            intent.putExtra(getString(R.string.intent_open_from), getString(R.string.video));
                            intent.putExtra(getString(R.string.intent_board_id), boardId);
                            intent.putExtra(getString(R.string.intent_api), getString(R.string.intent_board_activity));
                            startActivity(intent);
                            break;
                        }
                    }
                }
            });
        }
    }

    @OnClick(R.id.line_chart)
    public void openTimeLine(View view) {
        if (boardParentViewBitmap == null) {
            boardParentViewBitmap = loadBitmap(getWindow().getDecorView(), getWindow().getDecorView(), this);
        }
        TimelineActivity.launch(this, view);
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

        private String boardId;

        BoardPagerAdapter(FragmentManager fragmentManager, String boardId) {
            super(fragmentManager);
            this.boardId = boardId;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return BoardInfoFragment.newInstance();
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
    }
}