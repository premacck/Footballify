package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.GlobalVariable;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.util.helper.StartSnapHelper;
import life.plank.juna.zone.view.adapter.FootballFeedAdapter;
import life.plank.juna.zone.view.adapter.HorizontalFootballFeedAdapter;
import life.plank.juna.zone.view.fragment.LiveZoneFragment;


/**
 * Created by plank-arfaa on 19/01/18.
 */

public class SwipePageActivity extends AppCompatActivity implements HorizontalFootballFeedAdapter.AddMoreClickListeners {

    @BindView(R.id.football_feed_horizontal_view)
    RecyclerView horizontalRecyclerView;
    @BindView(R.id.football_feed_recycler_view)
    RecyclerView feedRecyclerView;
    @BindView(R.id.zone_logo)
    ImageView zoneLogo;
    HorizontalFootballFeedAdapter horizontalfootballFeedAdapter;
    FootballFeedAdapter footballFeedAdapter;
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
    @BindView(R.id.footbalFilterListView)
    ListView footbalFilterListView;
    @BindView(R.id.calenderListView)
    ListView calenderListView;

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
        //Feed recycler view
        String[] data = {"Reece Burke celebrates after breaking the deadlock in extra time at London Stadium",
                "Utd to subsidised fans in Sevilla row. $89 to visit Old Trafford for spanish supporters",
                "Charlie Nicholas : FA Cup third round replay predictions",
                "Brighton vs Chelsea",
                "Reece Burke celebrates after breaking the deadlock in extra time at London Stadium",
                "Utd to subsidised fans in Sevilla row. $89 to visit Old Trafford for spanish supporters",
                "Charlie Nicholas : FA Cup third round replay predictions",
                "Brighton vs Chelsea"};
        int numberOfRows = 2;
        feedRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfRows, GridLayoutManager.HORIZONTAL, false));
        footballFeedAdapter = new FootballFeedAdapter(this, data, UIDisplayUtil.getDisplayMetricsData(this, GlobalVariable.getInstance().getDisplayHeight()), UIDisplayUtil.getDisplayMetricsData(this, GlobalVariable.getInstance().getDisplayWidth()), actionBarHeight + spinnerSize + banterSize + banterRecyclerSize);
        feedRecyclerView.setAdapter(footballFeedAdapter);
        feedRecyclerView.setHasFixedSize(true);
        SnapHelper snapHelperFeedRecycler = new StartSnapHelper();
        snapHelperFeedRecycler.attachToRecyclerView(feedRecyclerView);
    }

    private void showSpinner(TextView activeTextView, ListView activeListView, TextView inActiveTextView, ListView inActiveListView,
                             String[] arrayData) {
        resetListView(inActiveListView, inActiveTextView);
        setListViewWidth(activeTextView);
        if (activeTextView.isSelected()) {
            activeListView.setVisibility(View.GONE);
            activeTextView.setSelected(false);
            activeTextView.setBackground(getResources().getDrawable(R.drawable.square_white_bg));
        } else {
            activeTextView.setSelected(true);
            activeListView.setVisibility(View.VISIBLE);
            activeTextView.setBackground(getResources().getDrawable(R.drawable.square_red_bg));

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.calendar_spinner_dropdown_item,
                    arrayData);
            activeListView.setAdapter(adapter);
            activeListView.bringToFront();
            activeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    activeTextView.setText(arrayData[i]);
                }
            });
        }
    }

    private void resetListView(ListView inActiveListView, TextView inActiveTextView) {
        inActiveListView.setVisibility(View.GONE);
        inActiveTextView.setBackground(getResources().getDrawable(R.drawable.square_white_bg));
        inActiveTextView.setSelected(false);
    }

    private void setListViewWidth(View view) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.addRule(RelativeLayout.BELOW, spinnersRelativeLayout.getId());
        int coords[] = {0, 0};
        view.getLocationOnScreen(coords);
        int absoluteLeft = coords[0];
        params.setMargins(absoluteLeft, -10, 0, 0);
        if (view.getId() == R.id.footbalFilterSpinnerTextView) {
            footbalFilterListView.setLayoutParams(params);
        } else {
            calenderListView.setLayoutParams(params);
        }
    }


    @OnClick({R.id.footbalFilterSpinnerTextView, R.id.calenderSpinnerTextView
            , R.id.liveZoneTextView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.footbalFilterSpinnerTextView:
                showSpinner((TextView) view, footbalFilterListView, calenderSpinnerTextView,
                        calenderListView, getResources().getStringArray(R.array.football_filter_array));
                break;
            case R.id.calenderSpinnerTextView:
                showSpinner((TextView) view, calenderListView, footbalFilterSpinnerTextView,
                        footbalFilterListView, getResources().getStringArray(R.array.calendar_array));
                break;
            case R.id.liveZoneTextView:
                retainLayout();
                footballFeedFragment();
                break;
        }
    }


    public void footballFeedFragment() {
        fragmentContainerFrameLayout.removeAllViews();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out)
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


}



