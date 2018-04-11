package life.plank.juna.zone.view.fragment;

import android.content.res.Resources;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import life.plank.juna.zone.FragmentTestRule;
import life.plank.juna.zone.R;
import life.plank.juna.zone.view.adapter.ShowScoreAdapter;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static life.plank.juna.zone.Util.RecyclerViewTestHelper.withIndex;

/**
 * Created by plank-sharath on 4/11/2018.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ShowScoreFragmentTest {
    @Rule
    public FragmentTestRule<ShowScoresFragment> fragmentTestRule = new FragmentTestRule<>(ShowScoresFragment.class);
    ShowScoreAdapter showScoreAdapter;
    private Resources resource;

    @Before
    public void setUp() {
        fragmentTestRule.launchActivity(null);
        resource = fragmentTestRule.getActivity().getResources();
    }

    @Test
    public void launchOfShowScoreFragmentShouldDisplayCloseImageView() {
        /*launch Showscores fragmnet
        * check if close image view is displayed*/
        onView(withId(R.id.cancel_image_view)).check(matches(isDisplayed()));
    }
    @Test
    public void showScoresRecyclerViewShouldScroll() {
        /*check if ShowScoresRecyclerview scrolls*/
        onView(withId(R.id.show_score_recycler_view)).perform(RecyclerViewActions.scrollToPosition(1));
    }

    @Test
    public void showScoresShouldHaveRoundsTextView() {
        /*check if ShowScoresRecyclerview item displays  Rounds textView*/
        showScoreRecyclerviewShouldScroll();
        onView(withIndex(withId(R.id.show_scores_rounds), 0)).check(matches(isDisplayed()));
    }

    @Test
    public void showScoresRecyclerViewItemShouldDisplayRoundsTextView() {
        /*check if showScoresRecyclerView 0th item displays 0th time for show_scores_rounds textView*/
        showScoreRecyclerviewShouldScroll();
        onView(withIndex(withId(R.id.show_scores_rounds), 0)).check(matches(withText(resource.getString(R.string.round_fa_cup))));
    }

    @Test
    public void showScoreRecyclerviewShouldScroll() {
        /*check if showScoreRecyclerview scrolls*/
        onView(withId(R.id.show_score_recycler_view)).perform(RecyclerViewActions.scrollToPosition(1));
    }
}
