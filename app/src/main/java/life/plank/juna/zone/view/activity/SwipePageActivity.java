package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import life.plank.juna.zone.interfaces.OnLongClickListener;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.GlobalVariable;
import life.plank.juna.zone.util.PreferenceManager;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.util.helper.StartSnapHelper;
import life.plank.juna.zone.view.adapter.FootballFeedAdapter;
import life.plank.juna.zone.view.adapter.HorizontalFootballFeedAdapter;
import life.plank.juna.zone.view.fragment.LiveZoneFragment;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by plank-arfaa on 19/01/18.
 */

public class SwipePageActivity extends OnBoardDialogActivity implements HorizontalFootballFeedAdapter.AddMoreClickListeners, OnLongClickListener {
    @Inject
    @Named("azure")
    Retrofit retrofit;
    @BindView(R.id.football_feed_horizontal_view)
    RecyclerView horizontalRecyclerView;
    @BindView(R.id.football_feed_recycler_view)
    RecyclerView feedRecyclerView;
    @BindView(R.id.zone_logo)
    ImageView zoneLogo;
    @BindView(R.id.containerRelativeLayout)
    FrameLayout containerRelativeLayout;
    @BindView(R.id.liveZoneTextView)
    TextView liveZoneTextView;
    @BindView(R.id.fragmentContainerFrameLayout)
    FrameLayout fragmentContainerFrameLayout;
    @BindView(R.id.footbalFilterSpinnerTextView)
    TextView footbalFilterSpinnerTextView;
    @BindView(R.id.calenderSpinnerTextView)
    TextView calenderSpinnerTextView;
    @BindView(R.id.spinnersRelativeLayout)
    RelativeLayout spinnersRelativeLayout;
    ArrayList<String> horizontalData;
    @BindView(R.id.football_menu)
    AppCompatImageButton footballMenu;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    HorizontalFootballFeedAdapter horizontalfootballFeedAdapter;
    FootballFeedAdapter footballFeedAdapter;
    private Subscription subscription;
    private RestApi restApi;
    private GridLayoutManager gridLayoutManager;
    private int PAGE_SIZE;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private String nextPageToken = "";
    private static final String TAG = SwipePageActivity.class.getSimpleName();
    private List<FootballFeed> footballFeeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_page);
        ButterKnife.bind(this);
        horizontalData = new ArrayList<>();
        horizontalData.add("6S: MARK F");
        horizontalData.add("23S: SUE M");
        horizontalData.add("27S: GRAHAM");
        horizontalData.add("6S: MARK F");
        horizontalData.add("addMore");
        ((ZoneApplication) getApplication()).getFootballFeedNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        getFootballFeed();
        initRecyclerView();
        showOnboardingDialog();
    }

    private void initRecyclerView() {
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
        footballFeedAdapter.setOnLongClickListener(this);
        SnapHelper snapHelperFeedRecycler = new StartSnapHelper();
        snapHelperFeedRecycler.attachToRecyclerView(feedRecyclerView);
        footballFeeds = new ArrayList<>();

    }

    public void getFootballFeed() {
        subscription = restApi.getFootballFeed(nextPageToken)
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
                        hideProgress();
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            nextPageToken = response.headers().get(AppConstants.footballFeedsHeaderKey);
                            setUpAdapterWithNewData(response.body());

                        } else {
                            showToast(AppConstants.defaultErrorMessage);
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

        final ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.calendar_spinner_dropdown_item,
                R.id.spinnerDropdownTextView, arrayData);
        listPopupWindow.setAdapter(adapter);
        listPopupWindow.setAnchorView(activeTextView);

        listPopupWindow.setHeight(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setWidth(UIDisplayUtil.dpToPx(150, this));

        listPopupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, android.R.color.transparent)));

        listPopupWindow.setVerticalOffset(0);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activeTextView.setText(arrayData[position]);
                activeTextView.setBackground(getResources().getDrawable(R.drawable.square_white_bg));
                listPopupWindow.dismiss();

            }
        });
        listPopupWindow.show();
    }

    @OnClick({R.id.footbalFilterSpinnerTextView, R.id.calenderSpinnerTextView
            , R.id.liveZoneTextView, R.id.football_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.footbalFilterSpinnerTextView:
                showSpinner((TextView) view,calenderSpinnerTextView,
                       getResources().getStringArray(R.array.football_filter_array));
                break;
            case R.id.calenderSpinnerTextView:
                showSpinner((TextView) view, footbalFilterSpinnerTextView,
                        getResources().getStringArray(R.array.calendar_array));
                break;
            case R.id.liveZoneTextView:
                retainLayout();
                footballFeedFragment();
                //TODO will be moved to navigation drawer once the drawer part is done
              /*  if (!"".contentEquals(new PreferenceManager(this).getPinnedFeeds(AppConstants.pinnedFeeds))) {
                    startActivity(new Intent(this, PinboardActivity.class));
                }*/
              startActivity(new Intent(this,FootballFeedDetailActivity.class));
                break;

            case R.id.football_menu:
                mDrawer.openDrawer(GravityCompat.END);
                break;
        }
    }

    public void footballFeedFragment() {
        fragmentContainerFrameLayout.removeAllViews();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                .replace(R.id.fragmentContainerFrameLayout, new LiveZoneFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {

        if (liveZoneTextView.isSelected()) {
            retainLayout();
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
                            showProgress();
                            getFootballFeed();
                        }
                    }, 1000);
                }
            }
        }
    };

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
    public void onLongClick(int positon) {
        savePinnedFeedsToPrefrence(positon);
    }

    private void savePinnedFeedsToPrefrence(int position) {
        PreferenceManager preferenceManager = new PreferenceManager(this);
        Gson gson = new Gson();
        String pinnedList = preferenceManager.getPinnedFeeds(AppConstants.pinnedFeeds);
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

}