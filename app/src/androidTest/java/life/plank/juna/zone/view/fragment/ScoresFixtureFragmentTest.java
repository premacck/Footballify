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
import life.plank.juna.zone.view.adapter.ScoreFixtureAdapter;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static life.plank.juna.zone.Util.RecyclerViewTestHelper.withIndex;
import static org.hamcrest.CoreMatchers.not;

/**
 * Created by plank-sharath on 3/8/2018.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ScoresFixtureFragmentTest {
    @Rule
    public FragmentTestRule<ScoreFixtureFragment> fragmentTestRule = new FragmentTestRule<>(ScoreFixtureFragment.class);
    ScoreFixtureAdapter scoreFixtureAdapter;
    private Resources resource;

    @Before
    public void setUp() {
        fragmentTestRule.launchActivity(null);
        resource = fragmentTestRule.getActivity().getResources();
    }

    @Test
    public void launchOfScoresAndFixtureFragmentShouldDisplayCloseImageView() {
        /*launch ScoresAndFixtures fragmnet
        * check if close image view is displayed*/
        onView(withId(R.id.cancel_image_view)).check(matches(isDisplayed()));
    }

    @Test
    public void scoreFixtureRecyclerViewShouldScroll() {
        /*check if scoreFixtureRecyclerView scrolls*/
        onView(withId(R.id.score_recycler_view)).perform(RecyclerViewActions.scrollToPosition(1));
    }

    @Test
    public void scoreFixtureShouldHaveTextView() {
        /*check if scoreFixtureRecyclerView item displays score_fixture_time textView*/
        scoreFixtureRecyclerViewShouldScroll();
        onView(withIndex(withId(R.id.score_fixture_time_text_view), 0)).check(matches(isDisplayed()));
    }

    @Test
    public void scoreFixtureRecyclerViewItemShouldDisplayTimeTextView() {
        /*check if scoreFixtureRecyclerView 0th item displays 0th time for score_fixture_time textView*/
        scoreFixtureRecyclerViewShouldScroll();
        onView(withIndex(withId(R.id.score_fixture_time_text_view), 0)).check(matches(withText(resource.getString(R.string.time_fa_cup))));
    }

    @Test
    public void scoresFixtureShouldNotHaveSelectionTextView() {
        /*check if scoresFixtureRecyclerview item does not display a score_fixture_rounds textView*/
        scoreFixtureRecyclerViewShouldScroll();
        onView(withIndex(withId(R.id.score_fixture_rounds_textview), 1)).check(matches(not(isDisplayed())));
    }

    @Test
    public void scoresFixtureRecyclerviewCount() {
        /*check if recyclerView is displayed
        * check the recyclerView item count*/
        onView(ViewMatchers.withId(R.id.score_recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.score_recycler_view)).check(new RecyclerViewItemCountAssertion(8));
    }
}
