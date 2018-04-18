package life.plank.juna.zone.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

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
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.dagger.RegisterUserNetworkComponent;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.interfaces.PinFeedListener;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.GlobalVariable;
import life.plank.juna.zone.util.NetworkStateReceiver;
import life.plank.juna.zone.util.NetworkStatus;
import life.plank.juna.zone.util.PreferenceManager;
import life.plank.juna.zone.util.ScrubberConstants;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.util.helper.StartSnapHelper;
import life.plank.juna.zone.view.adapter.FootballFeedAdapter;
import life.plank.juna.zone.view.adapter.HorizontalFootballFeedAdapter;
import life.plank.juna.zone.view.fragment.LiveZoneListFragment;
import life.plank.juna.zone.view.fragment.MatchListFragment;
import life.plank.juna.zone.view.fragment.ScoreFixtureFragment;
import life.plank.juna.zone.view.fragment.StandingFragment;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by plank-arfaa on 19/01/18.
 */

public class SwipePageActivity extends OnBoardDialogActivity implements HorizontalFootballFeedAdapter.AddMoreClickListeners, PinFeedListener, NetworkStateReceiver.NetworkStateReceiverListener {
    private static final String TAG = SwipePageActivity.class.getSimpleName();
    public boolean isStandingFragmentDisplayed = false;
    public boolean isScoreFixtureFragmentDisplayed = false;
    public boolean isMatchListFragmentDisplayed = false;
    ListPopupWindow listPopupWindow;
    @Inject
    @Named("azure")
    Retrofit retrofit;
    @BindView(R.id.football_feed_horizontal_view)
    RecyclerView horizontalRecyclerView;
    @BindView(R.id.football_feed_recycler_view)
    RecyclerView feedRecyclerView;
    @BindView(R.id.zone_logo)
    ImageView zoneLogo;
    @BindView(R.id.container_relative_layout)
    FrameLayout containerRelativeLayout;
    @BindView(R.id.live_zone_text_view)
    TextView liveZoneTextView;
    @BindView(R.id.fragment_container_frame_layout)
    FrameLayout fragmentContainerFrameLayout;
    @BindView(R.id.football_filter_spinner_textView)
    TextView footballFilterSpinnerTextView;
    @BindView(R.id.calendar_spinner_textView)
    TextView calendarSpinnerTextView;
    @BindView(R.id.spinners_relative_layout)
    RelativeLayout spinnersRelativeLayout;
    ArrayList<String> horizontalData;
    @BindView(R.id.football_menu)
    AppCompatImageButton footballMenu;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    HorizontalFootballFeedAdapter horizontalfootballFeedAdapter;
    FootballFeedAdapter footballFeedAdapter;
    int TRCNumber = 20;
    Context context;
    @BindView(R.id.football_menu_linear_layout)
    LinearLayout footballMenuLinearLayout;
    @BindView(R.id.football_toolbar)
    Toolbar footballToolbar;
    @BindView(R.id.parent__layout)
    RelativeLayout parentLayout;
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
    public static SwipePageActivity swipePageActivity;
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
                            if (NetworkStatus.isNetworkAvailable(parentLayout,SwipePageActivity.this)) {
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
        swipePageActivity = this;
        horizontalData = new ArrayList<>();
        horizontalData.add("6S: MARK F");
        horizontalData.add("23S: SUE M");
        horizontalData.add("27S: GRAHAM");
        horizontalData.add("6S: MARK F");
        horizontalData.add("addMore");
        startNetworkBroadcastReceiver(this);
        ((ZoneApplication) getApplication()).getFootballFeedNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);

        initRecyclerView();
        setUpData();
        showOnboardingDialog();
    }

    private void setUpData(){
        if (NetworkStatus.isNetworkAvailable(parentLayout,this)) {
            getFootballFeed();
        }else {
            retriveDataFromCacheMemory();
        }
    }
    
    private void initRecyclerView() {
        NetworkStatus.isNetworkAvailable(parentLayout, SwipePageActivity.this);
        // Calculate ActionBar height
        int actionBarHeight = 0;
        TypedValue typedValue = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
        }
        int spinnerSize = (int) getResources().getDimension(R.dimen.swipe_page_spinner_height);
        int banterSize = (int) getResources().getDimension(R.dimen.swipe_page_banter_zone_height);
        int banterRecyclerSize = (int) getResources().getDimension(R.dimen.football_feed_height);
        // TODO: 29-01-2018 Change based on performance
        SnapHelper snapHelper = new StartSnapHelper();

        //Setup the horizontal recycler view
        horizontalRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        horizontalRecyclerView.setHasFixedSize(true);
        horizontalfootballFeedAdapter = new HorizontalFootballFeedAdapter(this, horizontalData, UIDisplayUtil.getDisplayMetricsData(this, GlobalVariable.getInstance().getDisplayWidth()));
        horizontalRecyclerView.setAdapter(horizontalfootballFeedAdapter);
        snapHelper.attachToRecyclerView(horizontalRecyclerView);

        //Football Feed recycler view
        int numberOfRows = 2;
        gridLayoutManager = new GridLayoutManager(this, numberOfRows, GridLayoutManager.HORIZONTAL, false);
        feedRecyclerView.setLayoutManager(gridLayoutManager);
        footballFeedAdapter = new FootballFeedAdapter(this, UIDisplayUtil.getDisplayMetricsData(this, GlobalVariable.getInstance().getDisplayHeight()), UIDisplayUtil.getDisplayMetricsData(this, GlobalVariable.getInstance().getDisplayWidth()), actionBarHeight + spinnerSize + banterSize + banterRecyclerSize);
        feedRecyclerView.setAdapter(footballFeedAdapter);
        feedRecyclerView.setHasFixedSize(true);
        feedRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        footballFeedAdapter.setPinFeedListener(this);
        SnapHelper snapHelperFeedRecycler = new StartSnapHelper();
        snapHelperFeedRecycler.attachToRecyclerView(feedRecyclerView);
        footballFeeds = new ArrayList<>();

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
        showProgress();
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
                        hideProgress();
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

    private void showSpinner(TextView activeTextView, TextView inActiveTextView, String[] arrayData) {
        activeTextView.setBackground(getResources().getDrawable(R.drawable.square_red_bg));
        inActiveTextView.setBackground(getResources().getDrawable(R.drawable.square_white_bg));
        listPopupWindow = new ListPopupWindow(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.calendar_spinner_dropdown_item,
                R.id.spinnerDropdownTextView, arrayData);
        listPopupWindow.setAdapter(adapter);
        listPopupWindow.setAnchorView(activeTextView);

        listPopupWindow.setHeight(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setWidth(UIDisplayUtil.dpToPx(AppConstants.SPINNER_DIALOG_WIDTH, this));

        listPopupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, android.R.color.transparent)));

        listPopupWindow.setVerticalOffset(0);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activeTextView.setText(arrayData[position]);
                activeTextView.setBackground(getResources().getDrawable(R.drawable.square_white_bg));
                listPopupWindow.dismiss();
                spinnerView();
            }
        });
        listPopupWindow.show();
        listPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                activeTextView.setBackground(getResources().getDrawable(R.drawable.square_white_bg));
                inActiveTextView.setBackground(getResources().getDrawable(R.drawable.square_white_bg));
            }
        });
    }

    @OnClick({R.id.football_filter_spinner_textView, R.id.calendar_spinner_textView
            , R.id.live_zone_text_view, R.id.football_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.football_filter_spinner_textView:
                popUpListDisplay(calendarSpinnerTextView, view);
                break;
            case R.id.calendar_spinner_textView:
                popUpListDisplay(footballFilterSpinnerTextView, view);
                break;
            case R.id.live_zone_text_view:
                UIDisplayUtil.getInstance().dismissPopupListWindow(listPopupWindow);
                retainLayout();
                liveZoneListFragment();
                footballFeedAdapter.dismissPopupDialog();
                break;
            case R.id.football_menu:
                UIDisplayUtil.getInstance().dismissPopupListWindow(listPopupWindow);
                footballFeedAdapter.dismissPopupDialog();
                drawerLayout.openDrawer(GravityCompat.END);
                break;
        }
    }

    public void popUpListDisplay(TextView spinnerTextview, View view) {
        if (view.isSelected()) {
            view.setSelected(false);
            listPopupWindow.dismiss();
        } else {
            view.setSelected(true);
            UIDisplayUtil.getInstance().dismissPopupListWindow(listPopupWindow);
            if (spinnerTextview == calendarSpinnerTextView) {
                showSpinner((TextView) view, calendarSpinnerTextView,
                        getResources().getStringArray(R.array.football_filter_array));
            } else {
                showSpinner((TextView) view, footballFilterSpinnerTextView,
                        getResources().getStringArray(R.array.calendar_array));
            }
        }
    }

    public void liveZoneListFragment() {
        fragmentContainerFrameLayout.removeAllViews();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                .replace(R.id.fragment_container_frame_layout, new LiveZoneListFragment())
                .commit();
    }

    public void scoreFixtureFragment() {
        //TODO: replace fragment will be uncommented once fragment class is added
        isScoreFixtureFragmentDisplayed = true;
        fragmentContainerFrameLayout.setVisibility(View.VISIBLE);
        fragmentContainerFrameLayout.removeAllViews();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                .replace(R.id.fragment_container_frame_layout, new ScoreFixtureFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (liveZoneTextView.isSelected()) {
            retainLayout();
        } else if (isStandingFragmentDisplayed) {
            retainHomeLayout();
        } else if (isScoreFixtureFragmentDisplayed) {
            retainFeedContainer();
        } else if (isMatchListFragmentDisplayed) {
            retainMatchListContainer();
        } else {
            super.onBackPressed();
        }
    }

    public void retainLayout() {
        if (liveZoneTextView.isSelected()) {
            liveZoneTextView.setSelected(false);
            containerRelativeLayout.setVisibility(View.VISIBLE);
            fragmentContainerFrameLayout.setVisibility(View.GONE);
        } else {
            liveZoneTextView.setSelected(true);
            containerRelativeLayout.setVisibility(View.GONE);
            fragmentContainerFrameLayout.setVisibility(View.VISIBLE);
        }
    }

    public void scoreTableFragment() {
        isStandingFragmentDisplayed = true;
        feedRecyclerView.setVisibility(View.GONE);
        fragmentContainerFrameLayout.setVisibility(View.VISIBLE);
        fragmentContainerFrameLayout.removeAllViews();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_frame_layout, new StandingFragment())
                .commit();
    }

    public void matchListFragment() {
        isMatchListFragmentDisplayed = true;
        feedRecyclerView.setVisibility(View.GONE);
        fragmentContainerFrameLayout.setVisibility(View.VISIBLE);
        fragmentContainerFrameLayout.removeAllViews();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_frame_layout, new MatchListFragment())
                .commit();
    }

    public void retainHomeLayout() {
        isStandingFragmentDisplayed = false;
        feedRecyclerView.setVisibility(View.VISIBLE);
        footballFilterSpinnerTextView.setText(R.string.all);
        containerRelativeLayout.setVisibility(View.VISIBLE);
        fragmentContainerFrameLayout.setVisibility(View.GONE);
    }

    public void spinnerView() {
        if (footballFilterSpinnerTextView.getText().toString().equalsIgnoreCase(getString(R.string.fixture_result))) {
            containerRelativeLayout.setVisibility(View.GONE);
            scoreFixtureFragment();
        }
        if (footballFilterSpinnerTextView.getText().toString().equalsIgnoreCase(getString(R.string.standing))) {
            scoreTableFragment();
        }
        if (footballFilterSpinnerTextView.getText().toString().equalsIgnoreCase(getString(R.string.matches))) {
            matchListFragment();
        }
    }

    @Override
    public void addMore() {
        // TODO: 30-01-2018 Get data from server
        horizontalData.remove(horizontalData.size() - 1);
        horizontalData.add("6S: MARK F");
        horizontalData.add("23S: SUE M");
        horizontalData.add("27S: GRAHAM");
        horizontalData.add("6S: MARK F");
        horizontalData.add("addMore");
        horizontalfootballFeedAdapter.notifyDataSetChanged();
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
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

    public void goToLiveMatch(int matchNumber) {
        //TODO match number is needed for future
        Intent liveZoneIntent = new Intent(this, LiveZoneActivity.class);
        liveZoneIntent.putExtra(ScrubberConstants.SCRUBBER_MATCH_NUMBER, matchNumber);
        startActivity(liveZoneIntent);
    }

    public void retainFeedContainer() {
        isScoreFixtureFragmentDisplayed = false;
        footballFilterSpinnerTextView.setText(R.string.all);
        containerRelativeLayout.setVisibility(View.VISIBLE);
        fragmentContainerFrameLayout.setVisibility(View.GONE);
    }

    public void retainMatchListContainer() {
        isMatchListFragmentDisplayed = false;
        footballFilterSpinnerTextView.setText(R.string.all);
        containerRelativeLayout.setVisibility(View.VISIBLE);
        fragmentContainerFrameLayout.setVisibility(View.GONE);
    }

    private void saveDataToCacheMemory(String feedItems){
        try {
            ObjectOutput objectOutput = new ObjectOutputStream(new FileOutputStream(new File(getCacheDir(),"") + AppConstants.CACHE_FILE_NAME));
            objectOutput.writeObject(feedItems);
            objectOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void retriveDataFromCacheMemory(){
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File(new File(getCacheDir(),"") + AppConstants.CACHE_FILE_NAME)));
            String jsonObject = (String) objectInputStream.readObject();
            objectInputStream.close();
            setUpAdapterWithNewData(new Gson().fromJson(jsonObject, new TypeToken<List<FootballFeed>>(){}.getType()));
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
        Snackbar.make(parentLayout,getString(R.string.cannot_connect_to_the_internet),Snackbar.LENGTH_SHORT).show();
    }
}