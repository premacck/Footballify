package life.plank.juna.zone.presentation.activity;

import android.os.Bundle;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.support.design.widget.RxBottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.presentation.fragment.EngageFragment;
import life.plank.juna.zone.presentation.fragment.GamesFragment;
import life.plank.juna.zone.presentation.fragment.NewsFeedsFragment;
import life.plank.juna.zone.presentation.fragment.SearchFragment;
import life.plank.juna.zone.util.helper.BottomNavigationViewHelper;

public class ZoneHomeActivity extends AppCompatActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    private Fragment fragment = new NewsFeedsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setUpBottomNavigationView();
        setUpFragment();
    }

    private void setUpBottomNavigationView() {
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        //todo: make this code reactive
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }

        RxBottomNavigationView.itemSelections(bottomNavigationView)
                .subscribe(menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.zone_icon:
                            fragment = new NewsFeedsFragment();
                            setUpFragment();
                            break;

                        case R.id.football_icon:
                            fragment = new GamesFragment();
                            setUpFragment();
                            break;

                        case R.id.search_icon:
                            fragment = new SearchFragment();
                            setUpFragment();
                            break;

                        case R.id.engage_icon:
                            fragment = new EngageFragment();
                            setUpFragment();
                            break;
                    }
                });

    }

    public void setUpFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fragment)
                .commit();
    }
}
