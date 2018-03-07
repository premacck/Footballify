package life.plank.juna.zone.view.activity;

import android.content.res.Resources;
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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import life.plank.juna.zone.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by plank-hasan on 3/2/2018.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SwipePageActivityTest {
    //TODO:class name should be changed to FootballFeedActivityTest
    @Rule
    public ActivityTestRule<SwipePageActivity> activityTestRule = new ActivityTestRule<>(
            SwipePageActivity.class);
    private Resources resource;
    private View mainDecorView;


    @Before
    public void setUp() {
        resource = activityTestRule.getActivity().getResources();
        mainDecorView = activityTestRule.getActivity().getWindow().getDecorView();
    }

    @Test
    public void checkIfOnBoardingDialogIsDisplayedWhenActivityIsLaunched() {
        /*launch the activity
        * check if onBoarding dialog is displayed or not*/
        onView(ViewMatchers.withText(R.string.select_your_teams))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

    @Test
    public void checkIfUserIsAbleToSelectTeamOneFromSuggestionsInOnBoardingDialog() {
        /*click on the team one selection edit text
        * type chelsea
        * select from the suggestions*/
        onView(withId(R.id.team_one_edit_text)).
                perform(click()).perform(typeText("Chelsea"));
        onData(equalTo("Chelsea")).inRoot(RootMatchers.isPlatformPopup()).perform(click());
    }

    @Test
    public void checkIfUserIsAbleToSelectTeamTwoFromSuggestionsInOnBoardingDialog() {
        /*click on the team two selection edit text
        * type chelsea
        * select from the suggestions*/
        onView(withId(R.id.team_two_edit_text)).
                perform(click()).perform(typeText("Chelsea"));
        onData(equalTo("Chelsea")).inRoot(RootMatchers.isPlatformPopup()).perform(click());
    }

    @Test
    public void checkIfUserIsAbleToSelectTeamThreeFromSuggestionsInOnBoardingDialog() {
        /*click on the team three selection edit text
        * type chelsea
        * select from the suggestions*/
        onView(withId(R.id.team_three_edit_text)).
                perform(click()).perform(typeText("Chelsea"));
        onData(equalTo("Chelsea")).inRoot(RootMatchers.isPlatformPopup()).perform(click());
    }

    @Test
    public void clickOnMenuButtonAndCheckIfDrawerIsOpened() {
        /*close the onBoarding dialog
        * click on the menu button
        * check if drawer is opened*/
        closeOnBoardingDialog();
        onView(withId(R.id.football_menu)).perform(click());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.drawer_layout))
                .check(matches(isOpen(GravityCompat.END)));
    }

    @Test
    public void checkIfRecyclerViewItemClicked() {
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
