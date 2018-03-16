package life.plank.juna.zone.view.activity;


import android.content.res.Resources;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.DrawerActions;
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
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by plank-sharath on 3/14/2018.
 */
@RunWith(AndroidJUnit4.class)
public class LiveZoneActivityInstrumentalTest {
    //TODO: needs better solution for Idling the resources
    //TODO: try catch will be removed once a better solution is found
    @Rule
    public ActivityTestRule<LiveZoneActivity> activityTestRule = new ActivityTestRule<>(
            LiveZoneActivity.class);
    private Resources resource;
    private int waitingTime = 10;
    private IdlingResource idlingResource;

    @Before
    public void setUp() {
        resource = activityTestRule.getActivity().getResources();
        IdlingPolicies.setMasterPolicyTimeout(60, TimeUnit.SECONDS);
        IdlingPolicies.setIdlingResourceTimeout(26, TimeUnit.SECONDS);
        idlingResource = new ElapsedTimeIdlingResource(waitingTime);
    }

    @Test
    public void clickingOnMenuButtonShouldOpenNavigationDrawer() {
        /* click on the menu button
        * check if drawer is opened*/
        try {
            onView(withId(R.id.football_menu)).perform(click());
            Espresso.registerIdlingResources(idlingResource);
            onView(withId(R.id.drawer_layout))
                    .check(matches(isOpen(GravityCompat.END)));
            Espresso.unregisterIdlingResources(idlingResource);
        } catch (Exception e) {
            e.printStackTrace();
            Espresso.unregisterIdlingResources(idlingResource);
        }

    }

    @Test
    public void clickingOnMenuButtonShouldCloseNavigationDrawer() {
        /* click on the menu button
        * check if drawer is opened*/
        try {
            onView(withId(R.id.football_menu)).perform(click());
            Espresso.registerIdlingResources(idlingResource);
            onView(withId(R.id.drawer_layout))
                    .check(matches(isOpen(GravityCompat.END)));
            Espresso.registerIdlingResources(idlingResource);
            onView(withId(R.id.drawer_layout)).perform(DrawerActions.close(GravityCompat.END));
            onView(withId(R.id.drawer_layout))
                    .check(matches(isClosed(GravityCompat.END)));
            Espresso.unregisterIdlingResources(idlingResource);
        } catch (Exception e) {
            e.printStackTrace();
            Espresso.unregisterIdlingResources(idlingResource);
        }
    }
}
