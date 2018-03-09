package life.plank.juna.zone;

import android.content.res.Resources;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.junit.runner.RunWith;

import life.plank.juna.zone.view.fragment.MediaSelectionFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static life.plank.juna.zone.Util.RecyclerViewTestHelper.withIndex;
import static org.hamcrest.Matchers.not;

/**
 * Created by plank-niraj on 07-03-2018.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MediaSelectionFragmentTest {

    @Rule
    public FragmentTestRule<MediaSelectionFragment> fragmentTestRule = new FragmentTestRule<>(MediaSelectionFragment.class);
    private Resources resource;

    @Before
    public void setUp() {
        fragmentTestRule.launchActivity(null);
        resource = fragmentTestRule.getActivity().getResources();
    }

    @Test
    public void photosVideosTextViewShouldBePresent() {
        // Checking media selection fragment has a photos and videos text view.
        onView(withId(R.id.photos_text_view)).check(matches(isDisplayed()));
    }

    @Test
    public void photosVideosTextViewShouldContainText() {
        // Checking photos and videos text view for a proper text.
        onView(withId(R.id.photos_text_view)).check(matches(withText(resource.getString(R.string.photos_videos))));
    }

    @Test
    public void stickersTextViewShouldBePresent() {
        // Checking media selection fragment has a stickers text view.
        onView(withId(R.id.stickers_text_view)).check(matches(isDisplayed()));
    }

    @Test
    public void stickersTextViewShouldContainText() {
        // Checking stickers text view for a proper text.
        onView(withId(R.id.stickers_text_view)).check(matches(withText(resource.getString(R.string.stickers))));
    }

    @Test
    public void gifsTextViewShouldBePresent() {
        // Checking media selection fragment has a gifs text view.
        onView(withId(R.id.gifs_text_view)).check(matches(isDisplayed()));
    }

    @Test
    public void gifTextViewShouldContainText() {
        // Checking gifs text view for a proper text.
        onView(withId(R.id.gifs_text_view)).check(matches(withText(resource.getString(R.string.gifs))));
    }

    @Test
    public void memesMakerTextViewShouldBePresent() {
        // Checking media selection fragment has a meme maker text view.
        onView(withId(R.id.meme_maker_text_view)).check(matches(isDisplayed()));
    }

    @Test
    public void memeMakerTextViewShouldContainText() {
        // Checking meme Maker text view for a proper text.
        onView(withId(R.id.meme_maker_text_view)).check(matches(withText(resource.getString(R.string.meme_maker))));
    }

    @Test
    public void launchingMediaSelectionFragmentShouldDisplayCloseImageView() {
        // Checking media selection fragment has a close image view.
        onView(withId(R.id.meme_maker_text_view)).check(matches(isDisplayed()));
    }

    @Test
    public void mediaSelectionItemShouldHaveImageView() {
        // Checking recycler view item has image view
        mediaSelectionRecyclerViewShouldScroll();
        onView(withIndex(withId(R.id.photos_image_view), 1)).check(matches(isDisplayed()));
    }

    @Test
    public void mediaSelectionItemShouldNotHaveSelectionImageView() {
        //Checking that item selection image is not displaying on launch of media selection fragment.
        mediaSelectionRecyclerViewShouldScroll();
        onView(withIndex(withId(R.id.select_item), 1)).check(matches(not(isDisplayed())));
    }

    @Test
    public void mediaSelectionRecyclerViewShouldScroll() {
        //Checking for recycler view scroll.
        onView(withId(R.id.media_container_recycler_view)).perform(RecyclerViewActions.scrollToPosition(1));
    }
}
