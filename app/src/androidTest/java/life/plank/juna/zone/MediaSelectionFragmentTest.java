package life.plank.juna.zone;

import android.content.res.Resources;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import life.plank.juna.zone.view.fragment.MediaSelectionFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by plank-niraj on 07-03-2018.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MediaSelectionFragmentTest {
    @Rule
    public FragmentTestRule<MediaSelectionFragment> mFragmentTestRule = new FragmentTestRule<>(MediaSelectionFragment.class, R.id.media_container_frame_layout);
/*    @Rule
    public ActivityTestRule<MediaSelectionFragment> activityTestRule = new ActivityTestRule<>(
            SwipePageActivity.class);
    private Resources resource;
    private View mainDecorView;


    @Before
    public void setUp() {
        resource = activityTestRule.getActivity().getResources();
        mainDecorView = activityTestRule.getActivity().getWindow().getDecorView();
    }*/

    @Before
    public void setUp() {
        mFragmentTestRule.launchActivity(null);
        resource = mFragmentTestRule.getActivity().getResources();
    }

    private Resources resource;
    public void getMediaSelectionFragment() {
        // Launch the activity to make the fragment visible
        mFragmentTestRule.launchActivity(null);
        resource = mFragmentTestRule.getActivity().getResources();
    }

    @Test
    public void photosVideosTextViewShouldBePresent() {
        //getMediaSelectionFragment();
        onView(withId(R.id.photos_text_view)).check(matches(isDisplayed()));
    }


    @Test
    public void photosVideosTextViewShouldContainText() {
        //getMediaSelectionFragment();
        onView(withId(R.id.photos_text_view)).check(matches(withText(resource.getString(R.string.photos_videos))));
    }

    @Test
    public void stickersTextViewShouldBePresent() {
        //getMediaSelectionFragment();
        onView(withId(R.id.stickers_text_view)).check(matches(isDisplayed()));
    }

    @Test
    public void stickersTextViewShouldText() {
        //getMediaSelectionFragment();
        onView(withId(R.id.stickers_text_view)).check(matches(withText(resource.getString(R.string.stickers))));
    }
    @Test
    public void gifsTextViewShouldBePresent() {
        //getMediaSelectionFragment();
        onView(withId(R.id.gifs_text_view)).check(matches(isDisplayed()));
    }

    @Test
    public void gifsTextViewShouldText() {
        //getMediaSelectionFragment();
        onView(withId(R.id.gifs_text_view)).check(matches(withText(resource.getString(R.string.gifs))));
    }

}
