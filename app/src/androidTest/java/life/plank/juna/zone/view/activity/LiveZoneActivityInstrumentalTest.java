package life.plank.juna.zone.view.activity;


import android.content.res.Resources;
import android.support.test.espresso.AppNotIdleException;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.GravityCompat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import life.plank.juna.zone.R;
import life.plank.juna.zone.Util.ElapsedTimeIdlingResource;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by plank-sharath on 3/14/2018.
 */
@RunWith(AndroidJUnit4.class)
public class LiveZoneActivityInstrumentalTest {
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
        try {
        /* click on the menu button
        * check if drawer is opened*/
            onView(withId(R.id.football_menu)).perform(click());

             /*Make sure Espresso does not time out*/
            IdlingPolicies.setMasterPolicyTimeout(300 * 2, TimeUnit.MILLISECONDS);
            IdlingPolicies.setIdlingResourceTimeout(300 * 2, TimeUnit.MILLISECONDS);

           /* Now we wait*/
            IdlingResource idlingResource = new ElapsedTimeIdlingResource(500);
            Espresso.registerIdlingResources(idlingResource);

           /*  Stop and verify*/
            onView(withId(R.id.drawer_layout))
                    .check(matches(isOpen(GravityCompat.END)));

             /*Clean up*/
            Espresso.unregisterIdlingResources(idlingResource);
        } catch (AppNotIdleException e) {
            e.printStackTrace();
        }
    }
}
