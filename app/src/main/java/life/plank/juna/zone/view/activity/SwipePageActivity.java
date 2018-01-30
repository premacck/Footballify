package life.plank.juna.zone.view.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.helper.StartSnapHelper;
import life.plank.juna.zone.view.adapter.FootballFeedAdapter;
import life.plank.juna.zone.view.adapter.HorizontalFootballFeedAdapter;
import life.plank.juna.zone.view.fragment.LiveZoneFragment;


/**
 * Created by plank-arfaa on 19/01/18.
 */

public class SwipePageActivity extends AppCompatActivity {

    @BindView(R.id.calendar_spinner)
    Spinner calendarSpinner;
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
    @BindView(R.id.allSpinnerTextView)
    TextView allSpinnerTextView;
    @BindView(R.id.todaySpinnerTextView)
    TextView todaySpinnerTextView;
    @BindView(R.id.spinnersRelativeLayout)
    RelativeLayout spinnersRelativeLayout;
    @BindView(R.id.relate)
    RelativeLayout relate;
    @BindView(R.id.allListView)
    ListView allListView;
    @BindView(R.id.todayListView)
    ListView todayListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_page);
        ButterKnife.bind(this);
        ArrayList<String> horizontalData = new ArrayList<>();
        horizontalData.add("6S: MARK F");
        horizontalData.add("23S: SUE M");
        horizontalData.add("27S: GRAHAM");
        horizontalData.add("6S: MARK F");

        // Calculate ActionBar height
        int actionBarHeight = 0;
        TypedValue typedValue = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
        }
        //Setup the horizontal recycler view
        horizontalRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        horizontalfootballFeedAdapter = new HorizontalFootballFeedAdapter(this, horizontalData);
        horizontalRecyclerView.setAdapter(horizontalfootballFeedAdapter);

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
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        footballFeedAdapter = new FootballFeedAdapter(this, data, height, width);
        feedRecyclerView.setAdapter(footballFeedAdapter);
        // TODO: 29-01-2018 Change based on performance
        SnapHelper snapHelper = new StartSnapHelper();
        snapHelper.attachToRecyclerView(feedRecyclerView);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.calendar_array, R.layout.custom_calendar_spinner);
        adapter.setDropDownViewResource(R.layout.calendar_spinner_dropdown_item);
        calendarSpinner.setAdapter(adapter);
    }


    private void showAllListView() {
        todayListView.setVisibility(View.GONE);
        todaySpinnerTextView.setBackground(getResources().getDrawable(R.drawable.square_white_bg));
        todaySpinnerTextView.setSelected(false);

        if (allSpinnerTextView.isSelected()) {
            allListView.setVisibility(View.GONE);
            allSpinnerTextView.setSelected(false);
            allSpinnerTextView.setBackground(getResources().getDrawable(R.drawable.square_white_bg));
        } else {
            allSpinnerTextView.setSelected(true);
            allListView.setVisibility(View.VISIBLE);
            allSpinnerTextView.setBackground(getResources().getDrawable(R.drawable.square_red_bg));
            String names[] = {
                    "All", "My Teams", "My Leagues/Cups", "My Pundits",
                    "Fixtures/Results", "Standings"};

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.calendar_spinner_dropdown_item, names);
            allListView.setAdapter(adapter);
            allListView.bringToFront();
        }

    }

    private void showTodayListView() {
        allListView.setVisibility(View.GONE);
        allSpinnerTextView.setBackground(getResources().getDrawable(R.drawable.square_white_bg));
        allSpinnerTextView.setSelected(false);
        if (todaySpinnerTextView.isSelected()) {
            todayListView.setVisibility(View.GONE);
            todaySpinnerTextView.setSelected(false);
            todaySpinnerTextView.setBackground(getResources().getDrawable(R.drawable.square_white_bg));
        } else {
            todaySpinnerTextView.setSelected(true);
            todayListView.setVisibility(View.VISIBLE);
            todaySpinnerTextView.setBackground(getResources().getDrawable(R.drawable.square_red_bg));
            String names[] = {
                    "All", "My Teams", "My Leagues/Cups", "My Pundits",
                    "Fixtures/Results", "Standings"};

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.calendar_spinner_dropdown_item, names);
            todayListView.setAdapter(adapter);
            todayListView.bringToFront();
        }

    }

    @OnClick({R.id.allSpinnerTextView, R.id.todaySpinnerTextView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.allSpinnerTextView:
                showAllListView();
                break;
            case R.id.todaySpinnerTextView:
                showTodayListView();
                break;
        }
    }


    @OnClick(R.id.liveZoneTextView)
    public void onLiveZoneTextViewClicked() {
        retainLayout();
        footballFeedFragment();
    }

    public void footballFeedFragment() {
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


}



