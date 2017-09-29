package life.plank.juna.zone.presentation.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.support.design.widget.RxBottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.presentation.fragment.EngageFragment;
import life.plank.juna.zone.presentation.fragment.NewsFeedsFragment;
import life.plank.juna.zone.presentation.fragment.NotificationFragment;
import life.plank.juna.zone.presentation.fragment.TrendingFragment;
import life.plank.juna.zone.presentation.fragment.ZoneFragment;
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

        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.windowBackground, typedValue, true);
        if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            int color = typedValue.data;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(color);
            }
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setUpBottomNavigationView();
        setUpFragment();
    }

    private void setUpBottomNavigationView() {
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setItemIconTintList(null);
        //todo: make this code reactive
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }
        Menu menu = bottomNavigationView.getMenu();

        RxBottomNavigationView.itemSelections(bottomNavigationView)
                .subscribe(menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.feeds_icon:
                            menu.findItem(R.id.feeds_icon).setIcon(R.drawable.nav_bar_feeds_icon_selector);
                            fragment = new NewsFeedsFragment();
                            setUpFragment();
                            break;

                        case R.id.trending_icon:
                            menu.findItem(R.id.trending_icon).setIcon(R.drawable.nav_bar_trending_icon_selector);
                            fragment = new TrendingFragment();
                            setUpFragment();
                            break;

                        case R.id.zone_icon:
                            menu.findItem(R.id.zone_icon).setIcon(R.drawable.nav_bar_zone_icon_selector);
                            fragment = new ZoneFragment();
                            setUpFragment();
                            break;

                        case R.id.engage_icon:
                            menu.findItem(R.id.engage_icon).setIcon(R.drawable.nav_bar_engage_icon_selector);
                            fragment = new EngageFragment();
                            setUpFragment();
                            break;
                        case R.id.notification_icon:
                            menu.findItem(R.id.notification_icon).setIcon(R.drawable.nav_bar_notification_icon_selector);
                            fragment = new NotificationFragment();
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
