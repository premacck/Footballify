package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnPageChange;
import life.plank.juna.zone.R;
import life.plank.juna.zone.view.adapter.HorizontalFootballFeedAdapter;
import life.plank.juna.zone.view.adapter.SwipePageAdapter;


/**
 * Created by plank-arfaa on 19/01/18.
 */

public class SwipePageActivity extends FragmentActivity {


    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.calendar_spinner)
    Spinner calendarSpinner;
    @BindView(R.id.football_feed_horizontal_view)
    RecyclerView horizontalRecyclerView;


    private PagerAdapter pagerAdapter;
    HorizontalFootballFeedAdapter horizontalfootballFeedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_page);
        ButterKnife.bind(this);
        pagerAdapter = new SwipePageAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        String[] horizontalData = {"6S: MARK F",
                "14S: ROBERT",
                "23S: SUE M",
                "27S: GRAHAM",
                "6S: MARK F",
                "14S: ROBERT",
                "23S: SUE M",
                "27S: GRAHAM"};
        //Setup the horizontal recycler view
        horizontalRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        horizontalfootballFeedAdapter = new HorizontalFootballFeedAdapter(this, horizontalData);
        horizontalRecyclerView.setAdapter(horizontalfootballFeedAdapter);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.calendar_array, R.layout.custom_calendar_spinner);
        adapter.setDropDownViewResource(R.layout.calendar_spinner_dropdown_item);
        calendarSpinner.setAdapter(adapter);
    }

    @OnPageChange(R.id.pager)
    public void onPageChanged() {
        invalidateOptionsMenu();
    }

}



