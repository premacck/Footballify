package life.plank.juna.zone.view.fragment;

import android.content.res.Resources;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import life.plank.juna.zone.FragmentTestRule;
import life.plank.juna.zone.R;
import life.plank.juna.zone.Util.RecyclerViewItemCountAssertion;
import life.plank.juna.zone.view.adapter.StandingTableAdapter;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static life.plank.juna.zone.Util.RecyclerViewTestHelper.withIndex;

/**
 * Created by plank-prachi on 3/12/2018.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class StandingFragmentTest {
    private static final String MESSAGE = "Leicester";
    @Rule
    public FragmentTestRule<StandingFragment> fragmentTestRule = new FragmentTestRule<>(StandingFragment.class);
    StandingTableAdapter standingTableAdapter;
    private Resources resource;

    @Before
    public void setUp() {
        fragmentTestRule.launchActivity(null);
        resource = fragmentTestRule.getActivity().getResources();
    }

    @Test
    public void launchOfStandingFragmentShouldDisplayCloseImageView() {
        /*launch StandingFragment
        * check if cancel image view is displayed*/
        onView(withId(R.id.cancel_image_view)).check(matches(isDisplayed()));
    }

    @Test
    public void launchOfStandingFragmentShouldDisplaySearchBar() {
        /*launch StandingFragment check if searchBar  view is displayed*/
        onView(withId(R.id.search_bar)).check(matches(isDisplayed()));
    }

    @Test
    public void standingRecyclerViewShouldScroll() {
        /*check if standingRecyclerView scrolls*/
        onView(withId(R.id.table_recycler_view)).perform(RecyclerViewActions.scrollToPosition(1));
    }

    @Test
    public void standingRecyclerViewItemShouldDisplaySerialNumberTextView() {
        /*check if standingFragment RecyclerView  item displays */
        standingRecyclerViewShouldScroll();
        onView(withIndex(withId(R.id.serial_number_text_view), 1)).check(matches(withText(resource.getString(R.string.serial_number))));
    }

    @Test
    public void launchOfStandingFragmentShouldDisplayTeamName() {
        /*launch StandingFragment fragmnet
        * check if TeamName is displayed*/
        standingRecyclerViewShouldScroll();
        onView(withIndex(withId(R.id.team_name_text_view), 1)).check(matches((isDisplayed())));
    }

    @Test
    public void launchOfStandingFragmentShouldDisplayTotalPlayedMatch() {
        /*launch Standing fragment
        * check if TotalPlayedMatch  is displayed*/
        standingRecyclerViewShouldScroll();
        onView(withIndex(withId(R.id.played_text_view), 1)).check(matches((isDisplayed())));
    }

    @Test
    public void launchOfStandingFragmentShouldDisplayTotalWinMatch() {
        /*launch Standing fragment
        * check if TotalWin In a match is displayed*/
        standingRecyclerViewShouldScroll();
        onView(withIndex(withId(R.id.win_text_view), 1)).check(matches((isDisplayed())));
    }

    @Test
    public void launchOfStandingFragmentShouldDisplayTotalDrawMatch() {
        /*launch Standing fragment
        * check if draw textView  is displayed*/
        standingRecyclerViewShouldScroll();
        onView(withIndex(withId(R.id.draw_text_view), 1)).check(matches((isDisplayed())));
    }

    @Test
    public void launchOfStandingFragmentShouldDisplayTotalLossInMatch() {
        /*launch Standing fragment
        * check if Loss textView  is displayed*/
        standingRecyclerViewShouldScroll();
        onView(withIndex(withId(R.id.loss_text_view), 1)).check(matches((isDisplayed())));
    }

    @Test
    public void launchOfStandingFragmentShouldDisplaGoalDifference() {
        /*launch Standing fragment
        * check if GoalDifference textView  is displayed*/
        standingRecyclerViewShouldScroll();
        onView(withIndex(withId(R.id.goal_difference_text_view), 1)).check(matches((isDisplayed())));
    }

    @Test
    public void launchOfStandingFragmentShouldDisplaPointTable() {
        /*launch Standing fragment
        * check if PointTable textView  is displayed*/
        standingRecyclerViewShouldScroll();
        onView(withIndex(withId(R.id.point_table_text_view), 1)).check(matches((isDisplayed())));
    }

    @Test
    public void standingFragmentRecyclerViewCount() {
        /*check if recyclerView is displayed
        * check the recyclerView item count*/
        onView(ViewMatchers.withId(R.id.table_recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.table_recycler_view)).check(new RecyclerViewItemCountAssertion(10));
    }

    // Types a message into a EditText element.
    @Test
    public void verifySearchTextInSearchBar() {
        onView(withId(R.id.search_bar))
                .perform(typeText(MESSAGE), closeSoftKeyboard());

    }
}
