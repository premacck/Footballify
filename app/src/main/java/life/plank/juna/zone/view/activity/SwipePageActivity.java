package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.bvapp.arcmenulibrary.ArcMenu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.data.network.model.User;
import life.plank.juna.zone.interfaces.OnClickFeedItemListener;
import life.plank.juna.zone.interfaces.OnItemClickListener;
import life.plank.juna.zone.interfaces.PinFeedListener;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.NetworkStatus;
import life.plank.juna.zone.util.PreferenceManager;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.view.adapter.FootballFeedAdapter;
import life.plank.juna.zone.view.adapter.SearchViewAdapter;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.AppConstants.AUDIO;
import static life.plank.juna.zone.util.AppConstants.GALLERY;
import static life.plank.juna.zone.util.AppConstants.IMAGE;
import static life.plank.juna.zone.util.AppConstants.VIDEO;
import static life.plank.juna.zone.util.DataUtil.getStaticFeedItems;
import static life.plank.juna.zone.util.PreferenceManager.getToken;
import static life.plank.juna.zone.util.UIDisplayUtil.loadBitmap;
import static life.plank.juna.zone.util.customview.CustomPopup.showOptionPopup;


/**
 * Created by plank-hasan on 5/01/18.
 */

public class SwipePageActivity extends AppCompatActivity implements PinFeedListener, OnClickFeedItemListener, SearchView.OnQueryTextListener, OnItemClickListener {

    private static final String TAG = SwipePageActivity.class.getSimpleName();
    public static Bitmap parentViewBitmap = null;

    @BindView(R.id.football_feed_recycler_view)
    RecyclerView feedRecyclerView;
    @BindView(R.id.parent_layout)
    RelativeLayout parentLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.arc_menu)
    ArcMenu arcMenu;
    @BindView(R.id.search_view)
    SearchView search;
    BottomSheetBehavior bottomSheetBehavior;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.options_image)
    ImageView optionsImage;

    CoordinatorLayout coordinatorLayout;
    FootballFeedAdapter adapter;
    SearchViewAdapter searchViewAdapter;
    ArrayList<User> userList = new ArrayList<>();
    Point point;

    @Inject
    @Named("default")
    RestApi restApi;
    @Inject
    public Gson gson;

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE)
                arcMenu.show();
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_page);
        ButterKnife.bind(this);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);

        initRecyclerView();
        setUpData();
        setUpBoomMenu();
        initBottomSheetRecyclerView();
        search.setQueryHint(getString(R.string.search_query_hint));

        coordinatorLayout = findViewById(R.id.coordinator_layout);

        View bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(0);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    UIDisplayUtil.hideSoftKeyboard(findViewById(android.R.id.content), getApplicationContext());
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });


    }

    @OnClick(R.id.options_image)
    public void onOptionClick(View view) {
        int[] location = new int[2];

        view.getLocationOnScreen(location);

        //Initialize the Point with x, and y positions
        point = new Point();
        point.x = location[0];
        point.y = location[1];
        showOptionPopup(this, point, getString(R.string.home_pop_up), null, -440, 100);

    }

    private void setUpData() {
        if (NetworkStatus.isNetworkAvailable(parentLayout, this)) {
            getFootballFeed();
        } else {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    private void initRecyclerView() {
        adapter = new FootballFeedAdapter(this);
        feedRecyclerView.setAdapter(adapter);
        feedRecyclerView.setHasFixedSize(true);
        feedRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);

    }

    private void initBottomSheetRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        searchViewAdapter = new SearchViewAdapter(userList, this, this);
        recyclerView.setAdapter(searchViewAdapter);
        search.setOnQueryTextListener(this);

    }

    public void getFootballFeed() {
        progressBar.setVisibility(View.GONE);
        adapter.setFootballFeedList(getStaticFeedItems());
    }

    @Override
    public void onPinFeed(int position) {
        savePinnedFeedsToPreference(position);
    }

    @Override
    protected void onResume() {
        super.onResume();
        arcMenu.menuIn();
    }

    @Override
    protected void onPause() {
        super.onPause();
        arcMenu.menuOut();
    }

    private void savePinnedFeedsToPreference(int position) {
        PreferenceManager preferenceManager = new PreferenceManager(this);
        Gson gson = new Gson();
        String pinnedList = preferenceManager.getPinnedFeeds(AppConstants.PINNED_FEEDS);
        List<FootballFeed> pinnedFeedsList;
        if ("".contentEquals(pinnedList)) {
            pinnedFeedsList = new ArrayList<>();
        } else {
            pinnedFeedsList = gson.fromJson(pinnedList,
                    new TypeToken<List<FootballFeed>>() {
                    }.getType());
        }
        pinnedFeedsList.add(adapter.getFootballFeedList().get(position));
        preferenceManager.savePinnedFeeds(gson.toJson(pinnedFeedsList));
    }

    @Override
    public void onItemClick(int position, View fromView) {
        parentViewBitmap = loadBitmap(parentLayout, parentLayout, this);
        Intent intent = new Intent(this, FootballFeedDetailActivity.class);
        intent.putExtra(getString(R.string.intent_position), String.valueOf(position));
        intent.putExtra(getString(R.string.intent_feed_items), new Gson().toJson(adapter.getFootballFeedList()));
        startActivity(intent);
    }

    private void getSearchedUsers(String displayName) {

        restApi.getSearchedUsers(getToken(this), displayName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<List<User>>>() {
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
                    public void onNext(Response<List<User>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                searchViewAdapter.update(response.body());
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                userList.clear();
                                searchViewAdapter.notifyDataSetChanged();
                                break;
                            default:
                                Log.e(TAG, response.message());
                                break;
                        }
                    }
                });
    }

    public void setUpBoomMenu() {
        //todo: will be add in Utils so we can Reuse the Code
        arcMenu.setIcon(R.drawable.ic_un, R.drawable.ic_close_white);
        int[] fabImages = {R.drawable.ic_settings_white,
                R.drawable.ic_person, R.drawable.ic_home_purple, R.drawable.ic_gallery,
                R.drawable.ic_camera_white, R.drawable.ic_mic, R.drawable.ic_link, R.drawable.ic_video};
        int[] backgroundColors = {R.drawable.fab_circle_background_grey,
                R.drawable.fab_circle_background_grey, R.drawable.fab_circle_background_white, R.drawable.fab_circle_background_pink,
                R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink};
        String[] titles = {"Settings", "Profile", "Home", "Gallery", "Camera", "Audio", "Attachment", "Video"};
        for (int i = 0; i < fabImages.length; i++) {
            View child = getLayoutInflater().inflate(R.layout.layout_floating_action_button, null);
            //child.setId(i);
            RelativeLayout fabRelativeLayout = child.findViewById(R.id.fab_relative_layout);
            ImageView fabImageVIew = child.findViewById(R.id.fab_image_view);
            fabRelativeLayout.setBackground(ContextCompat.getDrawable(this, backgroundColors[i]));
            fabImageVIew.setImageResource(fabImages[i]);
            final int position = i;
            arcMenu.addItem(child, titles[i], new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0: {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            search.setQueryHint(getString(R.string.search_query_hint));

                            break;
                        }
                        case 1: {
                            Intent intent = new Intent(SwipePageActivity.this, UserProfileActivity.class);
                            startActivity(intent);
                            break;
                        }
                        case 2: {

                            break;
                        }
                        case 3: {
                            Intent intent = new Intent(SwipePageActivity.this, CameraActivity.class);
                            intent.putExtra(getString(R.string.intent_open_from), GALLERY);
                            intent.putExtra(getString(R.string.intent_api), getString(R.string.intent_swipe_page_activity));
                            startActivity(intent);
                            break;
                        }
                        case 4: {
                            Intent intent = new Intent(SwipePageActivity.this, CameraActivity.class);
                            intent.putExtra(getString(R.string.intent_open_from), IMAGE);
                            intent.putExtra(getString(R.string.intent_api), getString(R.string.intent_swipe_page_activity));
                            startActivity(intent);
                            break;
                        }
                        case 5: {
                            Intent intent = new Intent(SwipePageActivity.this, CameraActivity.class);
                            intent.putExtra(getString(R.string.intent_open_from), AUDIO);
                            intent.putExtra(getString(R.string.intent_api), getString(R.string.intent_swipe_page_activity));
                            startActivity(intent);
                            break;
                        }
                        case 6: {

                        }
                        case 7: {
                            Intent intent = new Intent(SwipePageActivity.this, CameraActivity.class);
                            intent.putExtra(getString(R.string.intent_open_from), VIDEO);
                            intent.putExtra(getString(R.string.intent_api), getString(R.string.intent_swipe_page_activity));
                            startActivity(intent);
                            break;
                        }
                    }
                }
            });
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (!s.isEmpty()) {
            getSearchedUsers(s);
        } else {
            userList.clear();
            searchViewAdapter.notifyDataSetChanged();
        }
        return true;
    }

    @Override
    public void onItemClicked(String objectId, Boolean isSelected) {
        //TODO: handle on item click
    }
}