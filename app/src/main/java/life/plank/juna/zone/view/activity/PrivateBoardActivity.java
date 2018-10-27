package life.plank.juna.zone.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.model.Board;
import life.plank.juna.zone.data.model.FeedEntry;
import life.plank.juna.zone.data.model.FeedItem;
import life.plank.juna.zone.data.model.Thumbnail;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.customview.GenericToolbar;
import life.plank.juna.zone.view.activity.base.BaseBoardActivity;
import life.plank.juna.zone.view.adapter.BoardFeedDetailAdapter;
import life.plank.juna.zone.view.adapter.EmojiAdapter;
import life.plank.juna.zone.view.fragment.board.fixture.BoardTilesFragment;
import life.plank.juna.zone.view.fragment.board.user.PrivateBoardInfoFragment;
import life.plank.juna.zone.view.fragment.forum.ForumFragment;
import retrofit2.Response;
import rx.Subscriber;

import static life.plank.juna.zone.util.AppConstants.BOARD;
import static life.plank.juna.zone.util.PreferenceManager.getToken;
import static life.plank.juna.zone.util.UIDisplayUtil.loadBitmap;
import static life.plank.juna.zone.util.UIDisplayUtil.setupSwipeGesture;

/**
 * Created by plank-dhamini on 25/7/2018.
 */

public class PrivateBoardActivity extends BaseBoardActivity {
    private static final String TAG = PrivateBoardActivity.class.getSimpleName();
    static String boardId;
    private static RestApi staticRestApi;
    @Inject
    @Named("default")
    RestApi restApi;
    @Inject
    Picasso picasso;
    @Inject
    Gson gson;
    @Inject
    PagerSnapHelper pagerSnapHelper;
    @BindView(R.id.faded_card)
    CardView fadedCard;
    @BindView(R.id.blur_background_image_view)
    ImageView blurBackgroundImageView;
    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;
    @BindView(R.id.drag_area)
    TextView dragArea;
    @BindView(R.id.root_card)
    CardView rootCard;
    @BindView(R.id.private_board_toolbar)
    GenericToolbar toolbar;
    @BindView(R.id.private_board_view_pager)
    ViewPager viewPager;
    @BindView(R.id.board_blur_background_image_view)
    ImageView boardBlurBackgroundImageView;
    @BindView(R.id.recycler_view_drag_area)
    TextView recyclerViewDragArea;
    @BindView(R.id.board_tiles_full_recycler_view)
    RecyclerView boardTilesFullRecyclerView;
    @BindView(R.id.emoji_bottom_sheet)
    RelativeLayout emojiBottomSheet;
    @BindView(R.id.emoji_recycler_view)
    RecyclerView emojiRecyclerView;
    private Board board;
    private PrivateBoardPagerAdapter pagerAdapter;
    private BottomSheetBehavior emojiBottomSheetBehavior;
    private EmojiAdapter emojiAdapter;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setDataReceivedFromPushNotification(intent);
        }
    };

    public static void launch(Context packageContext, Board board) {
        Intent intent = new Intent(packageContext, PrivateBoardActivity.class);
        intent.putExtra(packageContext.getString(R.string.intent_board), board);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        packageContext.startActivity(intent);
    }

    public static void deletePrivateBoard() {
        staticRestApi.deleteBoard(boardId, getToken())
                .subscribeOn(rx.schedulers.Schedulers.io())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<JsonObject>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                        Toast.makeText(ZoneApplication.getContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<JsonObject> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_NO_CONTENT:
                                Toast.makeText(ZoneApplication.getContext(), R.string.board_deletion, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ZoneApplication.getContext(), UserProfileActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                ZoneApplication.getContext().startActivity(intent);
                                break;
                            default:
                                Toast.makeText(ZoneApplication.getContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_private_board);
        ButterKnife.bind(this);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        staticRestApi = restApi;
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.intent_board))) {
            board = intent.getParcelableExtra(getString(R.string.intent_board));
        }

        SharedPreferences editor = getApplicationContext().getSharedPreferences(getString(R.string.pref_user_details), MODE_PRIVATE);
        editor.getString(getString(R.string.pref_display_name), getString(R.string.na));

        boardId = board.getId();
        if (board.getOwner().getDisplayName().equals(editor.getString(getString(R.string.pref_display_name), getString(R.string.na)))) {
            toolbar.setUpPrivateBoardPopUp(this, getString(R.string.private_board_owner_popup));
        } else {
            toolbar.setUpPrivateBoardPopUp(this, getString(R.string.private_board_user_popup));
        }

        toolbar.setTitle(board.getName());
        toolbar.setBoardTitle(board.getBoardType().equals(getString(R.string.public_lowercase)) ? R.string.public_board : R.string.private_board);
        toolbar.setLeagueLogo(picasso, board.getBoardIconUrl());
        toolbar.setBackgroundColor(Color.parseColor(board.getColor()));
        rootCard.setCardBackgroundColor(Color.parseColor(board.getColor()));

        setupSwipeGesture(this, dragArea, rootCard, fadedCard);

        prepareFullScreenRecyclerView();
        setupViewPagerWithFragments();
        String topic = getString(R.string.board_id_prefix) + board.getId();
        FirebaseMessaging.getInstance().subscribeToTopic(topic);

    }

    private void initBottomSheetRecyclerView() {
        emojiAdapter = new EmojiAdapter(getApplicationContext(), boardId, emojiBottomSheetBehavior);
        emojiRecyclerView.setAdapter(emojiAdapter);
    }

    private void setupBottomSheet() {
        emojiBottomSheetBehavior = BottomSheetBehavior.from(emojiBottomSheet);
        emojiBottomSheetBehavior.setPeekHeight(0);
    }

    @Override
    public View getScreenshotLayout() {
        return rootCard;
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
        Integer thumbnailHeight = intent.getIntExtra(getString(R.string.intent_thumbnail_height), 0);
        Integer thumbnailWidth = intent.getIntExtra(getString(R.string.intent_thumbnail_width), 0);
        String imageUrl = intent.getStringExtra(getString(R.string.intent_image_url));
        FeedEntry feed = new FeedEntry();
        FeedItem feedItem = new FeedItem();

        feed.setFeedItem(feedItem);
        feed.getFeedItem().setContentType(contentType);
        if (contentType.equals(AppConstants.ROOT_COMMENT)) {
            feed.getFeedItem().setTitle(title);
        } else {
            Thumbnail thumbnail = new Thumbnail();
            thumbnail.setImageWidth(thumbnailWidth);
            thumbnail.setImageHeight(thumbnailHeight);
            thumbnail.setImageUrl(imageUrl);
            feed.getFeedItem().setThumbnail(thumbnail);
            feed.getFeedItem().setUrl(imageUrl);
        }
        try {
            if (pagerAdapter.getCurrentFragment() instanceof BoardTilesFragment) {
                ((BoardTilesFragment) pagerAdapter.getCurrentFragment()).updateNewPost(feed);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void prepareFullScreenRecyclerView() {
        setupBottomSheet();
        initBottomSheetRecyclerView();
        pagerSnapHelper.attachToRecyclerView(boardTilesFullRecyclerView);
        boardFeedDetailAdapter = new BoardFeedDetailAdapter(restApi, boardId, true, emojiBottomSheetBehavior, BOARD);
        boardTilesFullRecyclerView.setAdapter(boardFeedDetailAdapter);
    }

    @Override
    public void updateFullScreenAdapter(List<FeedEntry> feedEntryList) {
        boardFeedDetailAdapter.update(feedEntryList);
    }

    @Override
    public void moveItem(int position, int previousPosition) {
        if (pagerAdapter.getCurrentFragment() instanceof BoardTilesFragment) {
            ((BoardTilesFragment) pagerAdapter.getCurrentFragment()).moveItem(position, previousPosition);
        }
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
                recyclerViewDragArea.setVisibility(View.INVISIBLE);
                boardTilesFullRecyclerView.setVisibility(View.INVISIBLE);
                recyclerViewDragArea.setTranslationY(0);
                boardTilesFullRecyclerView.setTranslationY(0);
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
            recyclerViewDragArea.setVisibility(View.VISIBLE);
            boardTilesFullRecyclerView.setVisibility(View.VISIBLE);
            boardBlurBackgroundImageView.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.board_blur_background_image_view)
    public void dismissFullScreenRecyclerView() {
        emojiBottomSheetBehavior.setPeekHeight(0);
        emojiBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        setBlurBackgroundAndShowFullScreenTiles(false, 0);
    }

    @Override
    public void onBackPressed() {
        emojiBottomSheetBehavior.setPeekHeight(0);
        emojiBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        if (isTileFullScreenActive) {
            setBlurBackgroundAndShowFullScreenTiles(false, 0);
        } else {
            boardFeedDetailAdapter = null;
            pagerAdapter = null;
            Intent intent = new Intent(PrivateBoardActivity.this, UserProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            super.onBackPressed();
            overridePendingTransition(R.anim.float_down, R.anim.sink_down);
        }
    }

    @Override
    public void openFeedEntry(@NotNull List<FeedEntry> feedEntryList, @NotNull String boardId, int position, @NotNull String target) {
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
                    return ForumFragment.newInstance(boardId);
                case 1:
                    return PrivateBoardInfoFragment.newInstance(board.getDescription(), board.getId(), board.getOwner().getDisplayName(), board.getName());
                case 2:
                    return BoardTilesFragment.newInstance(board.getId(), true);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
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