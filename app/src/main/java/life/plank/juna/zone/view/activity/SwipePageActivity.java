package life.plank.juna.zone.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v8.renderscript.RenderScript;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import life.plank.juna.zone.interfaces.OnLongPressListener;
import life.plank.juna.zone.interfaces.PinFeedListener;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.GlobalVariable;
import life.plank.juna.zone.util.NetworkStateReceiver;
import life.plank.juna.zone.util.NetworkStatus;
import life.plank.juna.zone.util.PreferenceManager;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.view.adapter.FootballFeedAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by plank-hasan on 5/01/18.
 */

public class SwipePageActivity extends AppCompatActivity implements PinFeedListener, NetworkStateReceiver.NetworkStateReceiverListener, OnLongPressListener {
    private static final String TAG = SwipePageActivity.class.getSimpleName();
    @Inject
    @Named("azure")
    Retrofit retrofit;
    @BindView(R.id.football_feed_recycler_view)
    RecyclerView feedRecyclerView;
    FootballFeedAdapter footballFeedAdapter;
    int TRCNumber = 20;
    Context context;
    @BindView(R.id.parent_layout)
    LinearLayout parentLayout;
    private Subscription subscription;
    private RestApi restApi;
    private GridLayoutManager gridLayoutManager;
    private int PAGE_SIZE;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private String nextPageToken = "";
    private List<FootballFeed> footballFeeds;
    private int apiHitCount = 0;
    private NetworkStateReceiver networkStateReceiver;
    public static Bitmap parentViewBitmap = null;
    RenderScript renderScript;
    Bitmap bluredBitmap = null;
    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
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
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_page);
        ButterKnife.bind(this);
        startNetworkBroadcastReceiver(this);
        ((ZoneApplication) getApplication()).getFootballFeedNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        initRecyclerView();
        setUpData();
    }

    private void setUpData() {
        if (NetworkStatus.isNetworkAvailable(parentLayout, this)) {
            getFootballFeed();
        } else {
            retriveDataFromCacheMemory();
        }
    }

    private void initRecyclerView() {
        int numberOfRows = 3;
        gridLayoutManager = new GridLayoutManager(this, numberOfRows, GridLayoutManager.VERTICAL, false);
        feedRecyclerView.setLayoutManager(gridLayoutManager);
        footballFeedAdapter = new FootballFeedAdapter(this);
        feedRecyclerView.setAdapter(footballFeedAdapter);
        feedRecyclerView.setHasFixedSize(true);
        feedRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        footballFeedAdapter.setPinFeedListener(this);
        footballFeedAdapter.setOnLongPressListener(this);
        footballFeeds = new ArrayList<>();
        renderScript = RenderScript.create(this);
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
        subscription = restApi.getFootballFeed(updateToken(nextPageToken,
                getString(R.string.replace_rt) +
                        String.valueOf(apiHitCount), getString(R.string.replace_trc)
                        + String.valueOf(apiHitCount * TRCNumber)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<FootballFeed>>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "In onCompleted()");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "In onCompleted()");
                    }

                    public void onNext(Response<List<FootballFeed>> response) {
                        GlobalVariable.getInstance().setFootballFeeds(response.body());
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            if (apiHitCount == 0) {
                                nextPageToken = response.headers().get(AppConstants.FOOTBALL_FEEDS_HEADER_KEY);
                                saveDataToCacheMemory(new Gson().toJson(response.body()));
                            }
                            setUpAdapterWithNewData(response.body());
                            apiHitCount = apiHitCount + 1;
                        } else {
                            showToast(AppConstants.DEFAULT_ERROR_MESSAGE);
                        }
                    }
                });

    }

    private void setUpAdapterWithNewData(List<FootballFeed> footballFeedsList) {
        if (!footballFeedsList.isEmpty() && footballFeedsList.size() > 0) {
            if ("".contentEquals(nextPageToken) ? (isLastPage = true) : (isLoading = false)) ;
            footballFeedAdapter.setFootballFeedList(footballFeedsList);
            PAGE_SIZE = footballFeedsList.size();
            footballFeeds.addAll(footballFeedsList);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPinFeed(int positon) {
        savePinnedFeedsToPrefrence(positon);
    }

    private void savePinnedFeedsToPrefrence(int position) {
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

    private void retriveDataFromCacheMemory() {
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

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        NetworkStatus.isNetworkAvailable(parentLayout, SwipePageActivity.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterNetworkBroadcastReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
        currentContext.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
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

    private Bitmap loadBitmap(View backgroundView, View targetView) {
        Rect backgroundBounds = new Rect();
        backgroundView.getHitRect(backgroundBounds);
        if (!targetView.getLocalVisibleRect(backgroundBounds)) {
            return null;
        }
        Bitmap bluredBitmap = captureView(backgroundView);
        int[] location = new int[2];
        int[] backgroundViewLocation = new int[2];
        backgroundView.getLocationInWindow(backgroundViewLocation);
        targetView.getLocationInWindow(location);
        int height = targetView.getHeight();
        int y = location[1];
        if (backgroundViewLocation[1] >= location[1]) {
            height -= (backgroundViewLocation[1] - location[1]);
            if (y < 0)
                y = 0;
        }
        if (y + height > bluredBitmap.getHeight()) {
            height = bluredBitmap.getHeight() - y;
            if (height <= 0) {
                return null;
            }
        }
        Matrix matrix = new Matrix();
        matrix.setScale(0.5f, 0.5f);
        Bitmap bitmap = Bitmap.createBitmap(bluredBitmap,
                (int) targetView.getX(),
                y,
                targetView.getMeasuredWidth(),
                height,
                matrix,
                true);
        return bitmap;
    }

    public Bitmap captureView(View view) {
        if (bluredBitmap != null) {
            return bluredBitmap;
        }
        bluredBitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(),
                Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bluredBitmap);
        view.draw(canvas);
        UIDisplayUtil.blurBitmapWithRenderscript(renderScript, bluredBitmap);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //ColorFilter filter = new LightingColorFilter(0xFFFFFFFF, 0x00222222); // lighten
        ColorFilter filter = new LightingColorFilter(0xFF7F7F7F, 0x00000000);    // darken
        paint.setColorFilter(filter);
        canvas.drawBitmap(bluredBitmap, 0, 0, paint);
        return bluredBitmap;
    }

    @Override
    public void onItemLongPress(int position) {
        parentViewBitmap = loadBitmap(parentLayout, parentLayout);
        Intent intent = new Intent(this, FootballFeedDetailActivity.class);
        intent.putExtra(AppConstants.POSITION, String.valueOf(position));
        intent.putExtra(AppConstants.FEED_ITEMS, new Gson().toJson(footballFeeds));
        startActivity(intent);
    }
}