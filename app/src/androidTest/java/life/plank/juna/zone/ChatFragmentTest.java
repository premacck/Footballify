package life.plank.juna.zone;

import android.content.res.Resources;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import life.plank.juna.zone.view.fragment.ChatFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by plank-prachi on 3/8/2018.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ChatFragmentTest {
    @Rule
    public FragmentTestRule<ChatFragment> fragmentTestRule = new FragmentTestRule<>(ChatFragment.class);
    private Resources fragmentResource;

    @Before
    public void setUp() {
        fragmentTestRule.launchActivity(null);
        fragmentResource = fragmentTestRule.getActivity().getResources();
    }

    @Test
    public void chatPlusScreenBePresent() {
        onView(withId(R.id.add_image)).check(matches(isDisplayed()));
    }

    @Test
    public void cameraButtonBePresent() {
        onView(withId(R.id.camera_image)).check(matches(isDisplayed()));
    }

    @Test
    public void checkMediaSelectionFragmentBeDisplay() {
        onView(withId(R.id.add_image)).perform(click());
        onView(withId(R.id.media_container_frame_layout)).check(matches(isDisplayed()));
    }
    //TODO : check if permission is allow or deny state
}
