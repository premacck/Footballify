package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
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

    HorizontalFootballFeedAdapter horizontalfootballFeedAdapter;
    FootballFeedAdapter footballFeedAdapter;
    @BindView(R.id.containerRl)
    FrameLayout containerRl;
    @BindView(R.id.livezoneTv)
    TextView livezoneTv;
    @BindView(R.id.fragmentContainer)
    FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_page);
        ButterKnife.bind(this);

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

        //Feed recycler view

        String[] data = {"Reece Burke celebrates after breaking the deadlock in extra time at London Stadium",
                "Utd to subsidised fans in Sevilla row. $89 to visit Old Trafford for spanish supporters",
                "Charlie Nicholas : FA Cup third round replay predictions",
                "Brighton vs Chelsea"};


        int numberOfRows = 2;
        feedRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfRows, GridLayoutManager.HORIZONTAL, false));

        footballFeedAdapter = new FootballFeedAdapter(this, data);
        feedRecyclerView.setAdapter(footballFeedAdapter);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.calendar_array, R.layout.custom_calendar_spinner);
        adapter.setDropDownViewResource(R.layout.calendar_spinner_dropdown_item);
        calendarSpinner.setAdapter(adapter);
    }

    @OnClick(R.id.livezoneTv)
    public void onViewClicked() {
        retainLayouts();
        replaceFragment();
    }

    public void replaceFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                .replace(R.id.fragmentContainer, new LiveZoneFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {

        if (livezoneTv.isSelected()){
            retainLayouts();
        }else {
            super.onBackPressed();
        }

    }

    public void retainLayouts(){
        if (livezoneTv.isSelected()){
            livezoneTv.setSelected(false);
            containerRl.setVisibility(View.VISIBLE);
            fragmentContainer.setVisibility(View.GONE);
        }else {
            livezoneTv.setSelected(true);
            containerRl.setVisibility(View.GONE);
            fragmentContainer.setVisibility(View.VISIBLE);
        }

    }

}



