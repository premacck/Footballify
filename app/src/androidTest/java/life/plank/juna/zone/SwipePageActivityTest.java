package life.plank.juna.zone;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.view.activity.SwipePageActivity;
import life.plank.juna.zone.view.adapter.FootballFeedAdapter;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by plank-hasan on 3/2/2018.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SwipePageActivityTest {
    @Rule
    public ActivityTestRule<SwipePageActivity> mActivityRule = new ActivityTestRule<>(
            SwipePageActivity.class);

    @Test
    // be explicit in naming a test
    public void checkIfDialogIsDisplayedWhenActivityIsLaunched() {
        onView(withText(R.string.select_your_teams))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

     @Test
    public void checkIfLiveZoneTextViewIsDisplayedProperly() {
         closeDialog();
         onView(withId(R.id.live_zone_text_view))
                .check(matches(withText(R.string.livezone)));
    }

    @Test
    public void checkIfRecyclerViewItemClicked(){
        closeDialog();
        // inject item at position 0
        // send a click event
        // test whether onClick method has been called once and only once on mock item at position 0.
       // if (getRecyclerViewCount() > 0){
            RecyclerView recyclerView = (RecyclerView) mActivityRule.getActivity().findViewById(R.id.football_feed_recycler_view);
            FootballFeedAdapter footballFeedAdapter = new FootballFeedAdapter(mActivityRule.getActivity(),0,0,0);
            recyclerView.setAdapter(footballFeedAdapter);
            List<FootballFeed> list = new ArrayList();
            list.add(new FootballFeed());
            footballFeedAdapter.setFootballFeedList(list);
            //onView(withId(R.id.football_feed_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
       // }
    }

    private int getRecyclerViewCount(){
        RecyclerView recyclerView = (RecyclerView) mActivityRule.getActivity().findViewById(R.id.football_feed_recycler_view);
        return recyclerView.getAdapter().getItemCount();
    }

    private void closeDialog() {
        onView(withText(R.string.select_your_teams))
                .inRoot(isDialog())
                .perform(pressBack());
    }
}
