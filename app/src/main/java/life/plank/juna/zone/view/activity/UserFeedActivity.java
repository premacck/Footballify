package life.plank.juna.zone.view.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bvapp.arcmenulibrary.ArcMenu;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import net.openid.appauth.AuthorizationService;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.Board;
import life.plank.juna.zone.data.network.model.FeedEntry;
import life.plank.juna.zone.data.network.model.User;
import life.plank.juna.zone.data.network.model.UserPreference;
import life.plank.juna.zone.interfaces.ZoneToolbarListener;
import life.plank.juna.zone.util.AuthUtil;
import life.plank.juna.zone.util.BoomMenuUtil;
import life.plank.juna.zone.util.customview.ZoneToolBar;
import life.plank.juna.zone.view.activity.base.BaseBoardActivity;
import life.plank.juna.zone.view.adapter.BoardFeedDetailAdapter;
import life.plank.juna.zone.view.adapter.OnboardingAdapter;
import life.plank.juna.zone.view.adapter.UserBoardsAdapter;
import life.plank.juna.zone.view.adapter.UserFeedAdapter;
import life.plank.juna.zone.view.adapter.UserZoneAdapter;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.AppConstants.BoomMenuPage.BOOM_MENU_FULL;
import static life.plank.juna.zone.util.DataUtil.getStaticLeagues;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.PreferenceManager.getToken;
import static life.plank.juna.zone.util.UIDisplayUtil.loadBitmap;

public class UserFeedActivity extends BaseBoardActivity implements ZoneToolbarListener {
    private static final String TAG = UserFeedActivity.class.getSimpleName();

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout rootLayout;
    @BindView(R.id.user_feed_recycler_view)
    RecyclerView userFeedRecyclerView;
    @BindView(R.id.user_zone_recycler_view)
    RecyclerView userZoneRecyclerView;
    @BindView(R.id.feed_header)
    ZoneToolBar toolbar;
    @BindView(R.id.user_boards_recycler_view)
    RecyclerView userBoardsRecyclerView;
    @BindView(R.id.onboarding_recycler_view)
    RecyclerView onboardingRecyclerView;
    @BindView(R.id.onboarding_bottom_sheet)
    RelativeLayout onboardingBottomSheet;
    @BindView(R.id.arc_menu)
    ArcMenu arcMenu;
    @BindView(R.id.board_blur_background_image_view)
    ImageView boardBlurBackgroundImageView;
    @BindView(R.id.board_tiles_list_full)
    RecyclerView boardTilesFullRecyclerView;

    @Inject
    @Named("default")
    RestApi restApi;
    @Inject
    public
    Gson gson;
    @Inject
    Picasso picasso;
    @Inject
    PagerSnapHelper pagerSnapHelper;

    private Dialog signUpDialog;
    private AuthorizationService authService;
    private BottomSheetBehavior onboardingBottomSheetBehavior;
    private OnboardingAdapter onboardingAdapter;
    private UserFeedAdapter userFeedAdapter;
    private UserZoneAdapter userZoneAdapter;
    private UserBoardsAdapter userBoardsAdapter;

    private ArrayList<UserPreference> userPreferences = new ArrayList<>();

    public static void launch(Context packageContext, boolean isClearTop) {
        Intent intent = new Intent(packageContext, UserFeedActivity.class);
        if (isClearTop) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        packageContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        ButterKnife.bind(this);

        ((ZoneApplication) getApplicationContext()).getUiComponent().inject(this);
        signUpDialog = new Dialog(this);
        SharedPreferences editor = getApplicationContext().getSharedPreferences(getString(R.string.pref_user_details), MODE_PRIVATE);
        String userObjectId = editor.getString(getApplicationContext().getString(R.string.pref_object_id), getString(R.string.na));

        String topic = getString(R.string.juna_user_topic) + userObjectId;
        FirebaseMessaging.getInstance().subscribeToTopic(topic);

        setupBottomSheet();
        initBottomSheetRecyclerView();
        prepareFullScreenRecyclerView();

        //TODO: Retrieve leagues from backend
        getLeagues();
        getUserZones();

        setUpToolbarAndBoomMenu();
        initRecyclerView();
        initZoneRecyclerView();
        initBoardsRecyclerView();
        getUserFeed();
        getUserBoards();
        toolbar.initListeners(this);
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_user_details), MODE_PRIVATE);
        if (!sharedPref.getString(getString(R.string.pref_profile_pic_url), getString(R.string.na)).equals(getString(R.string.na))) {
            toolbar.setProfilePic(sharedPref.getString(getString(R.string.pref_profile_pic_url), getString(R.string.na)));
        }
    }

    private void initBottomSheetRecyclerView() {
        onboardingAdapter = new OnboardingAdapter(this);
        onboardingRecyclerView.setAdapter(onboardingAdapter);
    }

    public void getLeagues() {
        onboardingAdapter.setLeagueList(getStaticLeagues());
    }

    private void setupBottomSheet() {
        onboardingBottomSheetBehavior = BottomSheetBehavior.from(onboardingBottomSheet);
        onboardingBottomSheetBehavior.setPeekHeight(0);
    }

    private void setUpToolbarAndBoomMenu() {
        if (isNullOrEmpty(getToken())) {
            toolbar.setProfilePic(R.drawable.ic_default_profile);
            toolbar.setCoinCount(getString(R.string.hello_stranger));
            arcMenu.setVisibility(View.GONE);
        } else {
            //TODO: set user profile picture and name in coin count
            toolbar.setCoinCount(null);
            BoomMenuUtil.setupBoomMenu(BOOM_MENU_FULL, this, null, arcMenu);
        }
    }

    private void initRecyclerView() {
        userFeedAdapter = new UserFeedAdapter(this, picasso);
        userFeedRecyclerView.setAdapter(userFeedAdapter);
    }

    private void initZoneRecyclerView() {
        userZoneAdapter = new UserZoneAdapter(this, userPreferences);
        userZoneRecyclerView.setAdapter(userZoneAdapter);
    }

    private void initBoardsRecyclerView() {
        userBoardsAdapter = new UserBoardsAdapter(this, gson, restApi, picasso);
        userBoardsRecyclerView.setAdapter(userBoardsAdapter);
    }

    private void setUpUserZoneAdapter(List<UserPreference> userPreferenceList) {
        userPreferences.clear();
        userPreferences.addAll(userPreferenceList);
        userZoneAdapter.notifyDataSetChanged();
    }

    private void getUserZones() {
        if (isNullOrEmpty(getToken())) {
            userZoneRecyclerView.setVisibility(View.GONE);
            return;
        }
        restApi.getUser(getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<User>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                        Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<User> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                User user = response.body();
                                if (user != null) {
                                    setUpUserZoneAdapter(user.userPreferences);
                                }
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(getApplicationContext(), R.string.failed_to_retrieve_zones, Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Log.e(TAG, response.message());
                                break;
                        }
                    }
                });
    }

    public void getUserFeed() {
        Observable<Response<List<FeedEntry>>> userFeedApiCall = isNullOrEmpty(getToken()) ? restApi.getUserFeed() : restApi.getUserFeed(getToken());
        userFeedApiCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<List<FeedEntry>>>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "In onError()" + e);
                        Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<List<FeedEntry>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                List<FeedEntry> feedEntries = response.body();
                                if (feedEntries != null) {
                                    userFeedAdapter.setUserFeed(feedEntries);
                                    updateFullScreenAdapter(feedEntries);
                                } else
                                    Toast.makeText(UserFeedActivity.this, R.string.failed_to_retrieve_feed, Toast.LENGTH_SHORT).show();
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(UserFeedActivity.this, R.string.failed_to_retrieve_feed, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(UserFeedActivity.this, R.string.failed_to_retrieve_feed, Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                });
    }

    public void getUserBoards() {
        if (isNullOrEmpty(getToken())) {
            return;
        }
        restApi.getFollowingBoards(getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<List<Board>>>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "In onError()" + e);
                        Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<List<Board>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                if (response.body() != null) {
                                    userBoardsAdapter.setUserBoards(response.body());
                                } else
                                    userBoardsRecyclerView.setVisibility(View.GONE);
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                userBoardsRecyclerView.setVisibility(View.GONE);
                                break;
                            default:
                                Toast.makeText(UserFeedActivity.this, R.string.failed_to_retrieve_board, Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                });
    }

    @Override
    public void profilePictureClicked(ImageView profilePicture) {
        if (isNullOrEmpty(getToken())) {
            showPopup();
        } else {
            UserProfileActivity.launch(this);
        }
    }

    public void showPopup() {
        authService = new AuthorizationService(this);
        signUpDialog.setContentView(R.layout.signup_dialogue);

        signUpDialog.findViewById(R.id.drag_handle).setOnClickListener(v -> signUpDialog.dismiss());

        signUpDialog.findViewById(R.id.signup_button).setOnClickListener(view ->
                AuthUtil.loginOrRefreshToken(UserFeedActivity.this, authService, null, false));
        Window window = signUpDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        signUpDialog.show();
    }

    @Override
    public void prepareFullScreenRecyclerView() {
        pagerSnapHelper.attachToRecyclerView(boardTilesFullRecyclerView);
        boardFeedDetailAdapter = new BoardFeedDetailAdapter(this, null, true);
        boardTilesFullRecyclerView.setAdapter(boardFeedDetailAdapter);
    }

    @Override
    public void updateFullScreenAdapter(List<FeedEntry> feedEntryList) {
        boardFeedDetailAdapter.update(feedEntryList);
    }

    @Override
    public void moveItem(int position, int previousPosition) {
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

    @OnClick(R.id.board_blur_background_image_view)
    public void dismissFullScreenRecyclerView() {
        setBlurBackgroundAndShowFullScreenTiles(false, 0);
    }

    @Override
    protected void onDestroy() {
        if (authService != null) {
            authService.dispose();
        }
        userBoardsAdapter = null;
        userFeedAdapter = null;
        userZoneAdapter = null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (isTileFullScreenActive) {
            setBlurBackgroundAndShowFullScreenTiles(false, 0);
        } else {
            boardFeedDetailAdapter = null;
            onboardingAdapter = null;
            userBoardsAdapter = null;
            userFeedAdapter = null;
            userZoneAdapter = null;
            super.onBackPressed();
        }
    }
}
