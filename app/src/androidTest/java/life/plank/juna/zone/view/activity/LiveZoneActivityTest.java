package life.plank.juna.zone.view.activity;

import android.content.res.Resources;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.view.GravityCompat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import life.plank.juna.zone.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by plank-sharath on 3/14/2018.
 */
public class LiveZoneActivityTest {
    @Rule
    public ActivityTestRule<LiveZoneActivity> activityTestRule = new ActivityTestRule<>(
            LiveZoneActivity.class);
    private Resources resource;

    @Before
    public void setUp() {
        resource = activityTestRule.getActivity().getResources();
    }

    @Test
    public void clickingOnMenuButtonShouldOpenNavigationDrawer() {
        /* click on the menu button
        * check if drawer is opened*/
        onView(withId(R.id.football_menu)).perform(click());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.drawer_layout))
                .check(matches(isOpen(GravityCompat.END)));
    }
}
