package life.plank.juna.zone.view.activity;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
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
import com.squareup.picasso.Picasso;

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
import life.plank.juna.zone.data.model.User;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.interfaces.OnItemClickListener;
import life.plank.juna.zone.util.BoomMenuUtil;
import life.plank.juna.zone.util.NetworkStatus;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.view.activity.base.StackableCardActivity;
import life.plank.juna.zone.view.adapter.FootballLeagueAdapter;
import life.plank.juna.zone.view.adapter.SearchViewAdapter;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.AppConstants.BoomMenuPage.BOOM_MENU_SETTINGS_AND_HOME;
import static life.plank.juna.zone.util.BoomMenuUtil.setupWith;
import static life.plank.juna.zone.util.DataUtil.getStaticLeagues;
import static life.plank.juna.zone.util.PreferenceManager.getToken;
import static life.plank.juna.zone.util.customview.CustomPopup.showOptionPopup;


/**
 * Created by plank-hasan on 5/01/18.
 */

public class SwipePageActivity extends StackableCardActivity implements SearchView.OnQueryTextListener, OnItemClickListener {

    private static final String TAG = SwipePageActivity.class.getSimpleName();
    @Inject
    public Gson gson;
    @BindView(R.id.root_card)
    CardView rootCard;
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
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @Inject
    Picasso picasso;
    FootballLeagueAdapter adapter;
    SearchViewAdapter searchViewAdapter;
    ArrayList<User> userList = new ArrayList<>();
    Point point;
    @Inject
    @Named("default")
    RestApi restApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_page);
        ButterKnife.bind(this);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);

        initRecyclerView();
        setUpData();
        BoomMenuUtil.setupBoomMenu(BOOM_MENU_SETTINGS_AND_HOME, this, null, arcMenu, null);
        initBottomSheetRecyclerView();
        search.setQueryHint(getString(R.string.search_query_hint));

        View bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(0);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    UIDisplayUtil.hideSoftKeyboard(findViewById(android.R.id.content));
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });
        setupWith(arcMenu, feedRecyclerView);
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
        adapter = new FootballLeagueAdapter(this);
        feedRecyclerView.setAdapter(adapter);
        feedRecyclerView.setHasFixedSize(true);
    }

    private void initBottomSheetRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        searchViewAdapter = new SearchViewAdapter(userList, this, this, picasso);
        recyclerView.setAdapter(searchViewAdapter);
        search.setOnQueryTextListener(this);

    }

    public void getFootballFeed() {
        progressBar.setVisibility(View.GONE);
        adapter.setLeagueList(getStaticLeagues());
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

    @Override
    public View getScreenshotLayout() {
        return rootCard;
    }

    private void getSearchedUsers(String displayName) {

        restApi.getSearchedUsers(getToken(), displayName)
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

    @Override
    public int getFragmentContainer() {
        return -1;
    }
}