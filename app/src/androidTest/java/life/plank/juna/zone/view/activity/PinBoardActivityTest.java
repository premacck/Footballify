package life.plank.juna.zone.view.activity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.LargeTest;

import com.google.gson.Gson;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.util.PreferenceManager;
import life.plank.juna.zone.view.adapter.PinBoardFootballFeedAdapter;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by plank-hasan on 3/13/2018.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class PinBoardActivityTest {
    @Rule
    public ActivityTestRule<PinboardActivity> activityTestRule = new ActivityTestRule<>(
            PinboardActivity.class);

    @Test
    @Ignore
    public void checkIfRecyclerViewIsVisible() {
        /*check if pinBoard recycler view is displayed*/
        if (getRecyclerViewCount() > 0) {
            onView(withId(R.id.football_feed_recycler_view)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void feedDummyAdapterToRecyclerViewAndCheckIfRecyclerViewFucntionsProperly() {
        /*Initialize dummy adapter
        * create a list of FootballFeeds
        * save this list to SharedPreference
        * check if recycler view populates the saved item*/
        try {
            activityTestRule.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RecyclerView recyclerView = activityTestRule.getActivity().findViewById(R.id.football_feed_recycler_view);
                    PinBoardFootballFeedAdapter footballFeedAdapter = new PinBoardFootballFeedAdapter(activityTestRule.getActivity(), 0, 0, 0);
                    List<FootballFeed> footballFeedList = new ArrayList<>();
                    footballFeedList.add(new FootballFeed());
                    recyclerView.setAdapter(footballFeedAdapter);
                    PreferenceManager preferenceManager = new PreferenceManager(activityTestRule.getActivity());
                    preferenceManager.savePinnedFeeds(new Gson().toJson(footballFeedList));
                    footballFeedAdapter.setPinnedFootballFeedList();
                    Assert.assertEquals(footballFeedAdapter.getItemCount(), 1);
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private int getRecyclerViewCount() {
        RecyclerView recyclerView = (RecyclerView) activityTestRule.getActivity().findViewById(R.id.football_feed_recycler_view);
        return recyclerView.getAdapter().getItemCount();
    }
}
