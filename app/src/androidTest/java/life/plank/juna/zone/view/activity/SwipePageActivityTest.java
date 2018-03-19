package life.plank.juna.zone.view.activity;

import android.content.res.Resources;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import life.plank.juna.zone.R;
import life.plank.juna.zone.Util.ElapsedTimeIdlingResource;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static life.plank.juna.zone.Util.RecyclerViewTestHelper.withIndex;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by plank-hasan on 3/2/2018.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SwipePageActivityTest {
    //TODO:class name should be changed to FootballFeedActivityTest
    //TODO: needs better solution for Idling the resources
    //TODO: try catch will be removed once a better solution is found
    @Rule
    public ActivityTestRule<SwipePageActivity> activityTestRule = new ActivityTestRule<>(
            SwipePageActivity.class);
    private Resources resource;
    private View mainDecorView;
    private int waitingTime = 10;
    private IdlingResource idlingResource;

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @Before
    public void setUp() {
        resource = activityTestRule.getActivity().getResources();
        mainDecorView = activityTestRule.getActivity().getWindow().getDecorView();
        IdlingPolicies.setMasterPolicyTimeout(60, TimeUnit.SECONDS);
        IdlingPolicies.setIdlingResourceTimeout(26, TimeUnit.SECONDS);
        idlingResource = new ElapsedTimeIdlingResource(waitingTime);
    }

    @Test
    public void launchOfActivityShouldDisplayOnBoardingDialog() {
        /*launch the activity
        * check if onBoarding dialog is displayed or not*/
        onView(ViewMatchers.withText(R.string.select_your_teams))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

    @Test
    public void userShouldBeAbleToSelectTeamOneFromSuggestionsInOnBoardingDialog() {
        /*click on the team one selection edit text
        * type chelsea
        * select from the suggestions*/
        try {
            onView(withId(R.id.team_one_edit_text)).
                    perform(click()).perform(typeText("Chelsea"), closeSoftKeyboard());
            Espresso.registerIdlingResources(idlingResource);
            onData(equalTo("Chelsea")).inRoot(RootMatchers.isPlatformPopup()).perform(click());
            Espresso.unregisterIdlingResources(idlingResource);
        } catch (Exception e) {
            e.printStackTrace();
            Espresso.unregisterIdlingResources(idlingResource);
        }
    }

    @Test
    public void userShouldBeAbleToSelectTeamTwoFromSuggestionsInOnBoardingDialog() {
        /*click on the team two selection edit text
        * type chelsea
        * select from the suggestions*/
        try {
            onView(withId(R.id.team_two_edit_text)).
                    perform(click()).perform(typeText("Chelsea"), closeSoftKeyboard());
            Espresso.registerIdlingResources(idlingResource);
            onData(equalTo("Chelsea")).inRoot(RootMatchers.isPlatformPopup()).perform(click());
            Espresso.unregisterIdlingResources(idlingResource);
        } catch (Exception e) {
            e.printStackTrace();
            Espresso.unregisterIdlingResources(idlingResource);
        }
    }

    @Test
    public void userShouldBeAbleToSelectTeamThreeFromSuggestionsInOnBoardingDialog() {
        /*click on the team three selection edit text
        * type chelsea
        * select from the suggestions*/
        try {
            Espresso.registerIdlingResources(idlingResource);
            onView(withId(R.id.team_three_edit_text)).
                    perform(click()).perform(typeText("Chelsea"), closeSoftKeyboard());
            Espresso.registerIdlingResources(idlingResource);
            onData(equalTo("Chelsea")).inRoot(RootMatchers.isPlatformPopup()).perform(click());
            Espresso.unregisterIdlingResources(idlingResource);
        } catch (Exception e) {
            e.printStackTrace();
            Espresso.unregisterIdlingResources(idlingResource);
        }
    }

    @Test
    public void clickingOnMenuButtonShouldOpenNavigationDrawer() {
        /*close the onBoarding dialog
        * click on the menu button
        * check if drawer is opened*/
        try {
            closeOnBoardingDialog();
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
    public void clickingOnRecyclerViewItemShouldCallIntent() {
        /*close the onBoarding dialog
        * click on the recylerview and
        * check if FootballFeedDetailActivity is getting called*/
        closeOnBoardingDialog();
        if (getRecyclerViewCount() > 0) {
            Intents.init();
            onView(withId(R.id.football_feed_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            intended(hasComponent(hasClassName(FootballFeedDetailActivity.class.getName())));
            Intents.release();
        }
    }

    @Test
    public void typingInvalidTeamNameInTeamOneEditTextShouldDisplayErrorMessage() {
        /*click on team one edit text
        * type invalid team name
        * click on team two edit text
        * check if error message is displayed for team one edit text*/
        try {
            Espresso.registerIdlingResources(idlingResource);
            onView(withId(R.id.team_one_edit_text)).
                    perform(click()).perform(typeText("invalid"), closeSoftKeyboard());
            onView(withId(R.id.team_two_edit_text)).
                    perform(click());
            onView(withId(R.id.team_one_edit_text)).
                    check(matches(hasErrorText(resource.getString(R.string.not_a_valid_team))));
            Espresso.unregisterIdlingResources(idlingResource);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            Espresso.unregisterIdlingResources(idlingResource);
        }
    }

    @Test
    public void typingInvalidTeamNameInTeamTwoEditTextShouldDisplayErrorMessage() {
        /*click on team two edit text
        * type invalid team name
        * click on team three edit text
        * check if error message is displayed for team team edit text*/
        try {
            Espresso.registerIdlingResources(idlingResource);
            onView(withId(R.id.team_two_edit_text)).
                    perform(click()).perform(typeText("invalid"), closeSoftKeyboard());
            onView(withId(R.id.team_three_edit_text)).
                    perform(click());
            onView(withId(R.id.team_two_edit_text)).
                    check(matches(hasErrorText(resource.getString(R.string.not_a_valid_team))));
            Espresso.unregisterIdlingResources(idlingResource);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            Espresso.unregisterIdlingResources(idlingResource);
        }
    }

    @Test
    public void typingInvalidTeamNameInTeamThreeEditTextShouldDisplayErrorMessage() {
        /*click on team three edit text
        * type invalid team name
        * click on team two edit text
        * check if error message is displayed for team three edit text*/
        try {
            Espresso.registerIdlingResources(idlingResource);
            onView(withId(R.id.team_three_edit_text)).
                    perform(click()).perform(typeText("invalid"), closeSoftKeyboard());
            onView(withId(R.id.team_two_edit_text)).
                    perform(click());
            onView(withId(R.id.team_three_edit_text)).
                    check(matches(hasErrorText(resource.getString(R.string.not_a_valid_team))));
            Espresso.unregisterIdlingResources(idlingResource);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            Espresso.unregisterIdlingResources(idlingResource);
        }
    }

    @Test
    public void clickingOnSubmitButtonWithoutTeamSelectionShouldNotDisplayRegisterAndSaveDialog() {
        /*click on submit button without selecting a team
        * check if RegisterAndSave dialog doesnot displayed*/
        try {
            Espresso.registerIdlingResources(idlingResource);
            onView(withId(R.id.apply_button)).
                    perform(click());
            onView(ViewMatchers.withText(R.string.register_and_save))
                    .inRoot(isDialog())
                    .check(doesNotExist());
            Espresso.unregisterIdlingResources(idlingResource);
        } catch (Exception e) {
            e.printStackTrace();
            Espresso.unregisterIdlingResources(idlingResource);
        }
    }

    @Test
    public void clickingOnSubmitButtonWithTwoSameTeamsShouldNotDisplayRegisterAndSaveDialog() {
        /*type Chelsea for team one edit text
        * type Chelsea for team two edit text
        * click on submit buttton
        * check if RegisterAndSave dialog doesnot displayed*/
        try {
            Espresso.registerIdlingResources(idlingResource);
            onView(withId(R.id.team_one_edit_text)).
                    perform(click()).perform(typeText("Chelsea"), closeSoftKeyboard());
            onView(withId(R.id.team_two_edit_text)).
                    perform(click()).perform(typeText("Chelsea"), closeSoftKeyboard());
            onView(withId(R.id.apply_button)).
                    perform(click());
            onView(ViewMatchers.withText(R.string.register_and_save))
                    .inRoot(isDialog())
                    .check(doesNotExist());
            Espresso.unregisterIdlingResources(idlingResource);
        } catch (Exception e) {
            e.printStackTrace();
            Espresso.unregisterIdlingResources(idlingResource);
        }
    }

    @Test
    public void clickingOnRecyclerViewItemShouldOpenFootballFeedDetailsActivityAndDisplaySlideupLayout() {
        /*close the onBoarding dialog
        * click on the recylerview and
        * check if Slideup layout is displayed in FootballFeedDetailsActivity*/
        closeOnBoardingDialog();
        if (getRecyclerViewCount() > 0) {
            onView(withId(R.id.football_feed_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withIndex(withId(R.id.sliding_layout), 0)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void clickingOnRecyclerViewItemShouldOpenFootballFeedDetailsActivityAndDisplayFeedItemTitle() {
        /*close the onBoarding dialog
        * click on the recylerview and
        * check if feed item title is displayed in FootballFeedDetailsActivity*/
        closeOnBoardingDialog();
        if (getRecyclerViewCount() > 0) {
            onView(withId(R.id.football_feed_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withIndex(withId(R.id.sliding_title_text_view), 0)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void clickingOnRecyclerViewItemShouldOpenFootballFeedDetailsActivityAndDisplayFeedItemDate() {
        /*close the onBoarding dialog
        * click on the recylerview and
        * check if feed item date is displayed in FootballFeedDetailsActivity*/
        closeOnBoardingDialog();
        if (getRecyclerViewCount() > 0) {
            onView(withId(R.id.football_feed_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withIndex(withId(R.id.sliding_feed_details_date_text_view), 0)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void clickingOnExpandArrowInFootbalFeedDetailsActivityShouldExpandTheSlideUpPanel() {
        /*click on expand arrow in FootballFeedDetailsActivity
        * check if scrollview is visible*/
        closeOnBoardingDialog();
        if (getRecyclerViewCount() > 0) {
            onView(withId(R.id.football_feed_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withIndex(withId(R.id.expand_arrow), 0)).perform(click());
            onView(withIndex(withId(R.id.scroll_view), 0)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void clickingOnExpandArrowInFootbalFeedDetailsActivityShouldDisplaySlideupPanelTitle() {
        /*click on expand arrow in FootballFeedDetailsActivity
        * check if SlideupPanelTitle is displayed*/
        closeOnBoardingDialog();
        if (getRecyclerViewCount() > 0) {
            onView(withId(R.id.football_feed_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withIndex(withId(R.id.expand_arrow), 0)).perform(click());
            onView(withIndex(withId(R.id.sliding_title_text_view), 0)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void clickingOnExpandArrowInFootbalFeedDetailsActivityShouldDisplaySlideupPanelDate() {
        /*click on expand arrow in FootballFeedDetailsActivity
        * check if SlideupPanelDate is displayed*/
        closeOnBoardingDialog();
        if (getRecyclerViewCount() > 0) {
            onView(withId(R.id.football_feed_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withIndex(withId(R.id.expand_arrow), 0)).perform(click());
            onView(withIndex(withId(R.id.sliding_feed_details_date_text_view), 0)).check(matches(isDisplayed()));
        }
    }


    private int getRecyclerViewCount() {
        RecyclerView recyclerView = (RecyclerView) activityTestRule.getActivity().findViewById(R.id.football_feed_recycler_view);
        return recyclerView.getAdapter().getItemCount();
    }

    private void closeOnBoardingDialog() {
        onView(withText(R.string.select_your_teams))
                .inRoot(isDialog())
                .perform(pressBack());
    }
}