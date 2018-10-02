package life.plank.juna.zone.view.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
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
import life.plank.juna.zone.data.network.model.UserFeed;
import life.plank.juna.zone.interfaces.ZoneToolbarListener;
import life.plank.juna.zone.util.AuthUtil;
import life.plank.juna.zone.util.customview.ZoneToolBar;
import life.plank.juna.zone.view.adapter.OnboardingAdapter;
import life.plank.juna.zone.view.adapter.UserBoardsAdapter;
import life.plank.juna.zone.view.adapter.UserFeedAdapter;
import life.plank.juna.zone.view.adapter.UserZoneAdapter;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.DataUtil.getStaticLeagues;
import static life.plank.juna.zone.util.PreferenceManager.getToken;

public class UserFeedActivity extends AppCompatActivity implements ZoneToolbarListener {
    private static final String TAG = UserFeedActivity.class.getSimpleName();

    @BindView(R.id.user_feed_recycler_view)
    RecyclerView userFeedRecyclerView;
    @BindView(R.id.user_zone_recycler_view)
    RecyclerView userZoneRecyclerView;
    @BindView(R.id.feed_header)
    ZoneToolBar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Inject
    @Named("default")
    RestApi restApi;
    @Inject
    Picasso picasso;

    private ArrayList<UserFeed> userFeed = new ArrayList<>();
    private ArrayList<Board> userBoards = new ArrayList<>();

    private Dialog signUpDialog;
    private AuthorizationService authService;
    private BottomSheetBehavior onboardingBottomSheetBehavior;
    private OnboardingAdapter adapter;
    private UserFeedAdapter userFeedAdapter;
    private UserZoneAdapter userZoneAdapter;
    private UserBoardsAdapter userBoardsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        ButterKnife.bind(this);

        ((ZoneApplication) getApplicationContext()).getUiComponent().inject(this);
        signUpDialog = new Dialog(this);
        SharedPreferences editor = getApplicationContext().getSharedPreferences(getString(R.string.pref_user_details), MODE_PRIVATE);
        String userObjectId = editor.getString(getApplicationContext().getString(R.string.pref_object_id), "NA");

        String topic = getString(R.string.juna_user_topic) + userObjectId;
        FirebaseMessaging.getInstance().subscribeToTopic(topic);

        setupBottomSheet();
        initBottomSheetRecyclerView();
        //TODO: Retrieve leagues from backend
        getLeagues();

        setUpToolbar();
        initRecyclerView();
        initZoneRecyclerView();
        initBoardsRecyclerView();
        getUserFeed();
        getUserBoards();
        toolbar.initListeners(this);

    }

    private void initBottomSheetRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new OnboardingAdapter(this);
        recyclerView.setAdapter(adapter);

    }

    public void getLeagues() {
        adapter.setLeagueList(getStaticLeagues());
    }

    private void setupBottomSheet() {
        View onboardingBottomSheet = findViewById(R.id.onboarding_bottom_sheet);
        onboardingBottomSheetBehavior = BottomSheetBehavior.from(onboardingBottomSheet);
        onboardingBottomSheetBehavior.setPeekHeight(0);
    }

    private void setUpToolbar() {
        if (getToken().isEmpty()) {
            toolbar.setProfilePic(R.drawable.ic_default_profile);
            toolbar.setCoinCount(getString(R.string.hello_stranger), false);
        } else {
            //TODO: set user profile picture and coin count
            toolbar.setCoinCount(getString(R.string.dummy_32k), true);
        }
    }

    private void initRecyclerView() {
        userFeedAdapter = new UserFeedAdapter(this, userFeed);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        userFeedRecyclerView.setLayoutManager(gridLayoutManager);
        userFeedRecyclerView.setAdapter(userFeedAdapter);

    }

    private void initZoneRecyclerView() {
        userZoneAdapter = new UserZoneAdapter(this, onboardingBottomSheetBehavior);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        userZoneRecyclerView.setLayoutManager(gridLayoutManager);
        userZoneRecyclerView.setAdapter(userZoneAdapter);

    }

    private void initBoardsRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.user_boards_recycler_view);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(UserFeedActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        userBoardsAdapter = new UserBoardsAdapter(userBoards, this, picasso);
        recyclerView.setAdapter(userBoardsAdapter);

    }

    public void getUserFeed() {
        restApi.getUserFeed(getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<List<UserFeed>>>() {
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
                    public void onNext(Response<List<UserFeed>> response) {

                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                if (response.body() != null)
                                    setUpAdapterWithNewData(response.body());
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
                                    userBoards.clear();
                                    userBoards.addAll(response.body());
                                    userBoardsAdapter.notifyDataSetChanged();
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

    private void setUpAdapterWithNewData(List<UserFeed> userFeedList) {
        userFeed.clear();
        userFeed.addAll(userFeedList);
        userFeedAdapter.notifyDataSetChanged();
    }

    @Override
    public void profilePictureClicked(ImageView profilePicture) {
        if (getToken().isEmpty()) {
            ShowPopup();
        } else {
            //TODO: Navigate to user profile view
        }
    }

    public void ShowPopup() {
        authService = new AuthorizationService(this);
        signUpDialog.setContentView(R.layout.signup_dialogue);

        signUpDialog.findViewById(R.id.drag_handle).setOnClickListener(v -> signUpDialog.dismiss());

        signUpDialog.findViewById(R.id.signup_button).setOnClickListener(view ->
                AuthUtil.loginOrRefreshToken(UserFeedActivity.this, authService, null, false));
        signUpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        signUpDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (authService != null) {
            authService.dispose();
        }
    }
}
