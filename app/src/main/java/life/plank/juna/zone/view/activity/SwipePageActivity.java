package life.plank.juna.zone.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bvapp.arcmenulibrary.ArcMenu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
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
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.data.network.model.Thumbnail;
import life.plank.juna.zone.interfaces.OnClickFeedItemListener;
import life.plank.juna.zone.interfaces.PinFeedListener;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.NetworkStateReceiver;
import life.plank.juna.zone.util.NetworkStatus;
import life.plank.juna.zone.util.PreferenceManager;
import life.plank.juna.zone.view.adapter.FootballFeedAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.UIDisplayUtil.loadBitmap;


/**
 * Created by plank-hasan on 5/01/18.
 */

public class SwipePageActivity extends AppCompatActivity implements PinFeedListener, NetworkStateReceiver.NetworkStateReceiverListener, OnClickFeedItemListener {
    private static final String TAG = SwipePageActivity.class.getSimpleName();
    public static SwipePageActivity swipePageActivity;
    public static Boolean isVisible = false;
    public static Bitmap parentViewBitmap = null;


    int TRCNumber = 20;
    @Inject
    @Named("azure")
    Retrofit retrofit;
    @BindView(R.id.football_feed_recycler_view)
    RecyclerView feedRecyclerView;
    @BindView(R.id.parent_layout)
    RelativeLayout parentLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.arc_menu)
    ArcMenu arcMenu;
    FootballFeedAdapter footballFeedAdapter;
    private RestApi restApi;
    private GridLayoutManager gridLayoutManager;
    private int PAGE_SIZE;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private String nextPageToken = "";
    private List<FootballFeed> footballFeeds;
    private int apiHitCount = 0;
    private NetworkStateReceiver networkStateReceiver;
    private Subscription subscription;
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
            int visibleItemCount = gridLayoutManager.getChildCount();
            int totalItemCount = gridLayoutManager.getItemCount();
            int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();
            if (!isLoading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE) {
                    isLoading = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (NetworkStatus.isNetworkAvailable(parentLayout, SwipePageActivity.this)) {
                                getFootballFeed();
                            }
                        }
                    }, AppConstants.PAGINATION_DELAY);
                }
            }
            if (dy > 0 || dy < 0 && arcMenu.isShown())
                arcMenu.hide();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_page);
        ButterKnife.bind(this);
        swipePageActivity = SwipePageActivity.this;
        startNetworkBroadcastReceiver(this);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        initRecyclerView();
        setUpData();
        setUpBoomMenu();
    }

    private void setUpData() {
        if (NetworkStatus.isNetworkAvailable(parentLayout, this)) {
            getFootballFeed();
        } else {
            retrieveDataFromCacheMemory();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        isVisible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isVisible = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
    }

    private void initRecyclerView() {
        int numberOfRows = 1;
        gridLayoutManager = new GridLayoutManager(this, numberOfRows, GridLayoutManager.VERTICAL, false);
        feedRecyclerView.setLayoutManager(gridLayoutManager);
        footballFeedAdapter = new FootballFeedAdapter(this);
        feedRecyclerView.setAdapter(footballFeedAdapter);
        feedRecyclerView.setHasFixedSize(true);
        feedRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        footballFeedAdapter.setPinFeedListener(this);
        footballFeedAdapter.setOnClickFeedItemListener(this);
    }

    public String updateToken(String nextPageToken, String replaceStringRT, String replaceStringTRC) {
        if (!nextPageToken.isEmpty()) {
            String updatedNextPageToken = nextPageToken.replaceFirst(AppConstants.REGULAR_EXPRESSION_RT, replaceStringRT);
            updatedNextPageToken = updatedNextPageToken.replaceFirst(AppConstants.REGULAR_EXPRESSION_TRC, replaceStringTRC);
            return updatedNextPageToken;
        }
        return "";
    }

    public void getFootballFeed() {
        progressBar.setVisibility(View.VISIBLE);
        subscription = restApi.getFootballFeed(updateToken(nextPageToken,
                getString(R.string.replace_rt) +
                        String.valueOf(apiHitCount), getString(R.string.replace_trc)
                        + String.valueOf(apiHitCount * TRCNumber)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<FootballFeed>>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, " onCompleted: ");
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError " + e);
                    }

                    public void onNext(Response<List<FootballFeed>> response) {
                        progressBar.setVisibility(View.INVISIBLE);


                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                if (apiHitCount == 0) {
                                    nextPageToken = response.headers().get(AppConstants.FOOTBALL_FEEDS_HEADER_KEY);
                                    saveDataToCacheMemory(new Gson().toJson(response.body()));
                                    setUpStaticItemsToFeeds();
                                }
                                setUpAdapterWithNewData(response.body());
                                apiHitCount = apiHitCount + 1;
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });

    }

    private void setUpAdapterWithNewData(List<FootballFeed> footballFeedsList) {
        footballFeeds = new ArrayList<>();
        if (!footballFeedsList.isEmpty() && footballFeedsList.size() > 0) {
            if ("".contentEquals(nextPageToken) ? (isLastPage = true) : (isLoading = false)) ;
            footballFeeds.addAll(footballFeedsList);
            footballFeedAdapter.setFootballFeedList(footballFeedsList);
            PAGE_SIZE = footballFeedsList.size();
        }
    }

    @Override
    public void onPinFeed(int position) {
        savePinnedFeedsToPreference(position);
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
        pinnedFeedsList.add(footballFeeds.get(position));
        preferenceManager.savePinnedFeeds(gson.toJson(pinnedFeedsList));
    }

    private void saveDataToCacheMemory(String feedItems) {
        try {
            ObjectOutput objectOutput = new ObjectOutputStream(new FileOutputStream(new File(getCacheDir(), "") + AppConstants.CACHE_FILE_NAME));
            objectOutput.writeObject(feedItems);
            objectOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void retrieveDataFromCacheMemory() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File(new File(getCacheDir(), "") + AppConstants.CACHE_FILE_NAME)));
            String jsonObject = (String) objectInputStream.readObject();
            objectInputStream.close();
            setUpAdapterWithNewData(new Gson().fromJson(jsonObject, new TypeToken<List<FootballFeed>>() {
            }.getType()));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void startNetworkBroadcastReceiver(Context currentContext) {
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener((NetworkStateReceiver.NetworkStateReceiverListener) currentContext);
        registerNetworkBroadcastReceiver(currentContext);
    }

    /**
     * Register the NetworkStateReceiver
     *
     * @param currentContext
     */
    public void registerNetworkBroadcastReceiver(Context currentContext) {
        currentContext.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    /**
     * Unregister the NetworkStateReceiver with your activity
     *
     * @param
     */
    public void unregisterNetworkBroadcastReceiver() {
        try {
            this.unregisterReceiver(networkStateReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void networkAvailable() {
    }

    @Override
    public void networkUnavailable() {
        Snackbar.make(parentLayout, getString(R.string.cannot_connect_to_the_internet), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int position) {
        parentViewBitmap = loadBitmap(parentLayout, parentLayout, this);
        Intent intent = new Intent(this, FootballFeedDetailActivity.class);
        intent.putExtra(getString(R.string.intent_position), String.valueOf(position));
        intent.putExtra(getString(R.string.intent_feed_items), new Gson().toJson(footballFeeds));
        startActivity(intent);
    }

    private void setUpStaticItemsToFeeds() {
        footballFeeds = new ArrayList<>();
        footballFeeds.add(new FootballFeed("", "Standings", "", "", "", "", new Thumbnail("http://images.fineartamerica.com/images-medium-large/illuminated-american-football-field-at-night-darrin-klimek.jpg", 0, 0), null, "", null));
        footballFeeds.add(new FootballFeed("", "Schedules", "", "", "", "", new Thumbnail("http://video.oneserviceplace.com/wp-content/uploads/2018/04/1523233581_maxresdefault.jpg", 0, 0), null, "", null));
        footballFeeds.add(new FootballFeed("", "Premier League", "", "", "", "", new Thumbnail("https://cdn.pulselive.com/test/client/pl/dev/i/elements/premier-league-logo-header.png", 0, 0), null, "", null));
        footballFeeds.add(new FootballFeed("", "World Cup", "", "", "", "", new Thumbnail("https://cdn.pulselive.com/test/client/pl/dev/i/elements/premier-league-logo-header.png", 0, 0), null, "", null));
        footballFeedAdapter.setFootballFeedList(footballFeeds);
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
                            intent.putExtra(getString(R.string.intent_open_from), getString(R.string.intent_gallery));
                            intent.putExtra(getString(R.string.intent_api), getString(R.string.intent_swipe_page_activity));
                            startActivity(intent);
                            break;
                        }
                        case 4: {
                            Intent intent = new Intent(SwipePageActivity.this, CameraActivity.class);
                            intent.putExtra(getString(R.string.intent_open_from), getString(R.string.intent_camera));
                            intent.putExtra(getString(R.string.intent_api), getString(R.string.intent_swipe_page_activity));
                            startActivity(intent);
                            break;
                        }
                        case 5: {
                            Intent intent = new Intent(SwipePageActivity.this, CameraActivity.class);
                            intent.putExtra(getString(R.string.intent_open_from), getString(R.string.intent_audio));
                            intent.putExtra(getString(R.string.intent_api), getString(R.string.intent_swipe_page_activity));
                            startActivity(intent);
                            break;
                        }
                        case 6: {

                        }
                        case 7: {
                            Intent intent = new Intent(SwipePageActivity.this, CameraActivity.class);
                            intent.putExtra(getString(R.string.intent_open_from), getString(R.string.intent_video));
                            intent.putExtra(getString(R.string.intent_api), getString(R.string.intent_swipe_page_activity));
                            startActivity(intent);
                            break;
                        }
                    }
                }
            });
        }
    }

}