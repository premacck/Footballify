package life.plank.juna.zone.view.fragment;

import android.content.res.Resources;
import android.os.Build;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import life.plank.juna.zone.FragmentTestRule;
import life.plank.juna.zone.R;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getInstrumentation().getUiAutomation().executeShellCommand(
                    grant + getTargetContext().getPackageName()
                            + R.string.external_storage_permission);
            getInstrumentation().getUiAutomation().executeShellCommand(
                    grant + getTargetContext().getPackageName()
                            + R.string.camera_permission);
        }
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
        onView(withId(R.id.media_container_frame_layout)).check(matches(isDisplayed()));
    }
    //TODO : write unit test for verifying runtime permission dialog
        /*click on camera image
        * check if permission dialog is displayed*/

}
