package life.plank.juna.zone.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.ViewGroup;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.Board;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.data.network.model.Thumbnail;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.customview.GenericToolbar;
import life.plank.juna.zone.view.fragment.board.fixture.BoardTilesFragment;
import life.plank.juna.zone.view.fragment.board.user.PrivateBoardInfoFragment;

/**
 * Created by plank-dhamini on 25/7/2018.
 */

public class PrivateBoardActivity extends AppCompatActivity {
    private static final String TAG = PrivateBoardActivity.class.getSimpleName();

    @Inject
    @Named("default")
    RestApi restApi;
    @Inject
    Picasso picasso;
    @Inject
    Gson gson;

    @BindView(R.id.board_parent_layout)
    CardView boardCardView;
    @BindView(R.id.private_board_toolbar)
    GenericToolbar toolbar;
    @BindView(R.id.private_board_view_pager)
    ViewPager viewPager;

    private Board board;
    private PrivateBoardPagerAdapter pagerAdapter;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setDataReceivedFromPushNotification(intent);
        }
    };

    public static void launch(Context packageContext, String board) {
        Intent intent = new Intent(packageContext, PrivateBoardActivity.class);
        intent.putExtra(packageContext.getString(R.string.intent_board), board);
        packageContext.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_private_board);
        ButterKnife.bind(this);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.intent_board))) {
            board = gson.fromJson(intent.getStringExtra(getString(R.string.intent_board)), Board.class);
        }
        toolbar.setTitle(board.getDisplayname());
        toolbar.setBoardTitle(board.getBoardType().equals(getString(R.string.public_lowercase)) ? R.string.public_board : R.string.private_board);
        toolbar.setBackgroundColor(Color.parseColor(board.getColor()));
        boardCardView.setCardBackgroundColor(Color.parseColor(board.getColor()));

        setupViewPagerWithFragments();
        String topic = getString(R.string.board_id_prefix) + board.getId();
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
    }

    private void setupViewPagerWithFragments() {
        pagerAdapter = new PrivateBoardPagerAdapter(getSupportFragmentManager(), board);
        viewPager.setAdapter(pagerAdapter);
        toolbar.setupWithViewPager(viewPager);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mMessageReceiver, new IntentFilter(getString(R.string.intent_board)));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mMessageReceiver);
    }

    public void setDataReceivedFromPushNotification(Intent intent) {
        String title = intent.getStringExtra(getString(R.string.intent_comment_title));
        String contentType = intent.getStringExtra(getString(R.string.intent_content_type));
        String thumbnailUrl = intent.getStringExtra(getString(R.string.intent_thumbnail_url));
        Integer thumbnailHeight = intent.getIntExtra(getString(R.string.intent_thumbnail_height), 0);
        Integer thumbnailWidth = intent.getIntExtra(getString(R.string.intent_thumbnail_width), 0);
        String imageUrl = intent.getStringExtra(getString(R.string.intent_image_url));
        FootballFeed footballFeed = new FootballFeed();

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
            if (pagerAdapter.getCurrentFragment() instanceof BoardTilesFragment) {
                ((BoardTilesFragment) pagerAdapter.getCurrentFragment()).updateNewPost(footballFeed);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PrivateBoardActivity.this, SwipePageActivity.class));
        super.onBackPressed();
    }

    static class PrivateBoardPagerAdapter extends FragmentPagerAdapter {

        private Fragment currentFragment;
        private Board board;

        PrivateBoardPagerAdapter(FragmentManager fm, Board board) {
            super(fm);
            this.board = board;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PrivateBoardInfoFragment.newInstance(board.getDescription(), board.getId());
                case 1:
                    return BoardTilesFragment.newInstance(board.getId(), board.getIsActive());
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