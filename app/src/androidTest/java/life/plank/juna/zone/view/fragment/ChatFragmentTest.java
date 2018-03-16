package life.plank.juna.zone.view.fragment;

import android.Manifest;
import android.content.res.Resources;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import life.plank.juna.zone.FragmentTestRule;
import life.plank.juna.zone.R;
import life.plank.juna.zone.Util.PermissionGranter;

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
    private Resources resources;
    private String grant = "pm grant ";

    @Before
    public void setUp() {
        fragmentTestRule.launchActivity(null);
        resources = fragmentTestRule.getActivity().getResources();
    }

    //checking add image is displayed on Fragment
    @Test
    public void launchOfFragmentShouldDisplayAddImage() {
        onView(ViewMatchers.withId(R.id.add_image)).check(matches(isDisplayed()));
    }

    //checking camera icon is displayed on Fragment
    @Test
    public void launchOfFragmentShouldDisplayCameraImage() {
        onView(withId(R.id.camera_image)).check(matches(isDisplayed()));
    }

    //checking onclick of add image, Media Selection fragment is displaying
    @Test
    public void clickingOnAddImageShouldDisplayMediaSelectionFragment() {
        onView(withId(R.id.add_image)).perform(click());
        PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.READ_EXTERNAL_STORAGE);
        onView(withId(R.id.media_container_frame_layout)).check(matches(isDisplayed()));
    }
}
