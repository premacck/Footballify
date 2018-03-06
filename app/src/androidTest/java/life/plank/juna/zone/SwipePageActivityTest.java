package life.plank.juna.zone;

import android.content.res.Resources;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.RootMatchers;
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
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import life.plank.juna.zone.view.activity.FootballFeedDetailActivity;
import life.plank.juna.zone.view.activity.SwipePageActivity;

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
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;

/**
 * Created by plank-hasan on 3/2/2018.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SwipePageActivityTest {
    @Rule
    public ActivityTestRule<SwipePageActivity> activityTestRule = new ActivityTestRule<>(
            SwipePageActivity.class);
   /* @Rule
    public IntentsTestRule<SwipePageActivity> intentsTestRule =
            new IntentsTestRule<>(SwipePageActivity.class);*/
    private Resources resource;
    private View mainDecorView;

    @Before
    public void setUp() throws Exception {
        resource = activityTestRule.getActivity().getResources();
        mainDecorView = activityTestRule.getActivity().getWindow().getDecorView();
    }

    @Test
    public void checkIfDialogIsDisplayedWhenActivityIsLaunched() {
        onView(withText(R.string.select_your_teams))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

    @Test
    public void checkIfUserIsAbleToSelectTeamsInOnBoardingDialog(){
        onView(withId(R.id.team_one_edit_text)).
        perform(click()).perform(typeText("Chelsea"));
        onData(equalTo("Chelsea")).inRoot(RootMatchers.isPlatformPopup()).perform(click());
    }

    @Test
    public void checkIfLiveZoneTextViewIsDisplayedProperly() {
        closeDialog();
        onView(withId(R.id.live_zone_text_view))
                .check(matches(withText(R.string.livezone)));
    }

    @Test
    public void clickOnMenuButtonAndCheckIfDrawerIsOpened(){
        //click on the menu button and check if drawer is opened
        closeDialog();
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
        /*close the on boarding dialog
        * click on the recylerview and
        * check if FootballFeedDetailActivity is getting called*/
        closeDialog();
        if (getRecyclerViewCount() > 0) {
            onView(withId(R.id.football_feed_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            Intents.init();
            intended(hasComponent(hasClassName(FootballFeedDetailActivity.class.getName())));
            Intents.release();
        }
    }

    @Test
    public void checkIfFootballFilterSpinnerIsDisplayingAllTheFilters() {
        //close on boarding dialog
        closeDialog();
        //click on football filter spinner is displayed
        onView(withId(R.id.football_filter_spinner_textView)).perform(click());
        onView(withClassName(Matchers.is("android.widget.PopupWindow$PopupBackgroundView")))
                .inRoot(isPlatformPopup()).check(matches(isDisplayed()));
        //check if football filter spinner matches the text with the string array stored in the resources.
        onView(withText(resource.getStringArray(R.array.football_filter_array)[0]))
                .inRoot(withDecorView(not(is(mainDecorView))))
                .check(matches(isDisplayed()));
        onView(withText(resource.getStringArray(R.array.football_filter_array)[1]))
                .inRoot(withDecorView(not(is(mainDecorView))))
                .check(matches(isDisplayed()));
        onView(withText(resource.getStringArray(R.array.football_filter_array)[2]))
                .inRoot(withDecorView(not(is(mainDecorView))))
                .check(matches(isDisplayed()));
        onView(withText(resource.getStringArray(R.array.football_filter_array)[3]))
                .inRoot(withDecorView(not(is(mainDecorView))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void checkIfCalendarSpinnerIsDisplayingAllTheFilters() {
        //close on boarding dialog
        closeDialog();
        //click on football filter spinner is displayed
        onView(withId(R.id.calendar_spinner_textView)).perform(click());
        onView(withClassName(Matchers.is("android.widget.PopupWindow$PopupBackgroundView")))
                .inRoot(isPlatformPopup()).check(matches(isDisplayed()));
        //check if football filter spinner matches the text with the string array stored in the resources.
        onView(withText(resource.getStringArray(R.array.calendar_array)[0]))
                .inRoot(withDecorView(not(is(mainDecorView))))
                .check(matches(isDisplayed()));
        onView(withText(resource.getStringArray(R.array.calendar_array)[1]))
                .inRoot(withDecorView(not(is(mainDecorView))))
                .check(matches(isDisplayed()));
        onView(withText(resource.getStringArray(R.array.calendar_array)[2]))
                .inRoot(withDecorView(not(is(mainDecorView))))
                .check(matches(isDisplayed()));
        onView(withText(resource.getStringArray(R.array.calendar_array)[3]))
                .inRoot(withDecorView(not(is(mainDecorView))))
                .check(matches(isDisplayed()));
        onView(withText(resource.getStringArray(R.array.calendar_array)[4]))
                .inRoot(withDecorView(not(is(mainDecorView))))
                .check(matches(isDisplayed()));
        onView(withText(resource.getStringArray(R.array.calendar_array)[5]))
                .inRoot(withDecorView(not(is(mainDecorView))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void clickOnSpinnerAndCheckIfSelectedTextAdded(){
        closeDialog();
        onView(withId(R.id.calendar_spinner_textView)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("text")))
                .inAdapterView(withId(R.id.spinnerDropdownTextView))
                .perform(click());
    }



    private int getRecyclerViewCount() {
        RecyclerView recyclerView = (RecyclerView) activityTestRule.getActivity().findViewById(R.id.football_feed_recycler_view);
        return recyclerView.getAdapter().getItemCount();
    }

    private void closeDialog() {
        onView(withText(R.string.select_your_teams))
                .inRoot(isDialog())
                .perform(pressBack());
    }

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

}
