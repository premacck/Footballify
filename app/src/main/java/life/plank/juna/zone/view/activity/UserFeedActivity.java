package life.plank.juna.zone.view.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.Board;
import life.plank.juna.zone.data.network.model.FeedEntry;
import life.plank.juna.zone.data.network.model.User;
import life.plank.juna.zone.data.network.model.UserPreference;
import life.plank.juna.zone.interfaces.ZoneToolbarListener;
import life.plank.juna.zone.util.AuthUtil;
import life.plank.juna.zone.util.customview.ZoneToolBar;
import life.plank.juna.zone.view.adapter.OnboardingAdapter;
import life.plank.juna.zone.view.adapter.UserBoardsAdapter;
import life.plank.juna.zone.view.adapter.UserFeedAdapter;
import life.plank.juna.zone.view.adapter.UserZoneAdapter;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.DataUtil.getStaticLeagues;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.PreferenceManager.getToken;

public class UserFeedActivity extends AppCompatActivity implements ZoneToolbarListener {
    private static final String TAG = UserFeedActivity.class.getSimpleName();

    @BindView(R.id.user_feed_recycler_view)
    RecyclerView userFeedRecyclerView;
    @BindView(R.id.user_zone_recycler_view)
    RecyclerView userZoneRecyclerView;
    @BindView(R.id.feed_header)
    ZoneToolBar toolbar;
    @BindView(R.id.onboarding_recycler_view)
    RecyclerView onboardingRecyclerView;
    @BindView(R.id.onboarding_bottom_sheet)
    RelativeLayout onboardingBottomSheet;

    @Inject
    @Named("default")
    RestApi restApi;
    @Inject
    Gson gson;
    @Inject
    Picasso picasso;

    private Dialog signUpDialog;
    private AuthorizationService authService;
    private BottomSheetBehavior onboardingBottomSheetBehavior;
    private OnboardingAdapter adapter;
    private UserFeedAdapter userFeedAdapter;
    private UserZoneAdapter userZoneAdapter;
    private UserBoardsAdapter userBoardsAdapter;

    private ArrayList<UserPreference> userPreferences = new ArrayList<>();

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
        //TODO: Retrieve leagues from backend
        getLeagues();
        getUserZones();

        setUpToolbar();
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
        adapter = new OnboardingAdapter(this);
        onboardingRecyclerView.setAdapter(adapter);
    }

    public void getLeagues() {
        adapter.setLeagueList(getStaticLeagues());
    }

    private void setupBottomSheet() {
        onboardingBottomSheetBehavior = BottomSheetBehavior.from(onboardingBottomSheet);
        onboardingBottomSheetBehavior.setPeekHeight(0);
    }

    private void setUpToolbar() {
        if (isNullOrEmpty(getToken())) {
            toolbar.setProfilePic(R.drawable.ic_default_profile);
            toolbar.setCoinCount(getString(R.string.hello_stranger));
        } else {
            //TODO: set user profile picture and coin count
            toolbar.setCoinCount(null);
        }
    }

    private void initRecyclerView() {
        userFeedAdapter = new UserFeedAdapter(picasso);
        userFeedRecyclerView.setAdapter(userFeedAdapter);
    }

    private void initZoneRecyclerView() {
        userZoneAdapter = new UserZoneAdapter(this, userPreferences);
        userZoneRecyclerView.setAdapter(userZoneAdapter);
    }

    private void initBoardsRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.user_boards_recycler_view);
        userBoardsAdapter = new UserBoardsAdapter(this, gson, restApi, picasso);
        recyclerView.setAdapter(userBoardsAdapter);
    }

    private void setUpUserZoneAdapter(List<UserPreference> userPreferenceList) {
        userPreferences.clear();
        userPreferences.addAll(userPreferenceList);
        userZoneAdapter.notifyDataSetChanged();
    }

    private void getUserZones() {
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
                                if (response.body() != null)
                                    userFeedAdapter.setUserFeed(response.body());
                                else
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
                                    Toast.makeText(UserFeedActivity.this, R.string.failed_to_retrieve_board, Toast.LENGTH_SHORT).show();
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(UserFeedActivity.this, R.string.failed_to_retrieve_board, Toast.LENGTH_SHORT).show();
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
    protected void onDestroy() {
        if (authService != null) {
            authService.dispose();
        }
        userBoardsAdapter = null;
        userFeedAdapter = null;
        userZoneAdapter = null;
        super.onDestroy();
    }
}
