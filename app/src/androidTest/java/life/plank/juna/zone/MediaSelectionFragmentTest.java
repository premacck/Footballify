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
    public FragmentTestRule<MediaSelectionFragment> mFragmentTestRule = new FragmentTestRule<>(MediaSelectionFragment.class);
    private Resources resource;

    @Before
    public void setUp() {
        mFragmentTestRule.launchActivity(null);
        resource = mFragmentTestRule.getActivity().getResources();
    }

    @Test
    public void photosVideosTextViewShouldBePresent() {
        onView(withId(R.id.photos_text_view)).check(matches(isDisplayed()));
    }

    @Test
    public void photosVideosTextViewShouldContainText() {
        onView(withId(R.id.photos_text_view)).check(matches(withText(resource.getString(R.string.photos_videos))));
    }

    @Test
    public void stickersTextViewShouldBePresent() {
        onView(withId(R.id.stickers_text_view)).check(matches(isDisplayed()));
    }

    @Test
    public void stickersTextViewShouldContainText() {
        onView(withId(R.id.stickers_text_view)).check(matches(withText(resource.getString(R.string.stickers))));
    }

    @Test
    public void gifsTextViewShouldBePresent() {
        onView(withId(R.id.gifs_text_view)).check(matches(isDisplayed()));
    }

    @Test
    public void gifTextViewShouldContainText() {
        onView(withId(R.id.gifs_text_view)).check(matches(withText(resource.getString(R.string.gifs))));
    }

    @Test
    public void memesMakerTextViewShouldBePresent() {
        onView(withId(R.id.meme_maker_text_view)).check(matches(isDisplayed()));
    }

    @Test
    public void memeMakerTextViewShouldContainText() {
        onView(withId(R.id.meme_maker_text_view)).check(matches(withText(resource.getString(R.string.meme_maker))));
    }

    @Test
    public void closeImageViewTextViewShouldBePresent() {
        onView(withId(R.id.meme_maker_text_view)).check(matches(isDisplayed()));
    }
}
