package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnPageChange;
import life.plank.juna.zone.R;
import life.plank.juna.zone.view.adapter.SwipePageAdapter;


/**
 * Created by plank-arfaa on 19/01/18.
 */

public class SwipePageActivity extends FragmentActivity {


    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.calendar_spinner)
    Spinner calendarSpinner;

    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_page);
        ButterKnife.bind(this);
        pagerAdapter = new SwipePageAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

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



