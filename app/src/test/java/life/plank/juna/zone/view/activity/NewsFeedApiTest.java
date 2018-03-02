package life.plank.juna.zone.view.activity;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by plank-sharath on 2/23/2018.
 */

public class NewsFeedApiTest {

    @InjectMocks
    SwipePageActivity swipePageActivity;
    private String token = "{\"token\":\"+RID:S6gVAN+HdgDtAQAAAAAAAA==#RT:1#TRC:20#RTD:vjAyMDE4LTAzLTAxVDE4OjI0OjAwWg==\",\"range\":{\"min\":\"\",\"max\":\"FF\"}";
    private String updateToken = "{\"token\":\"+RID:S6gVAN+HdgDtAQAAAAAAAA==#RT:2#TRC:40#RTD:vjAyMDE4LTAzLTAxVDE4OjI0OjAwWg==\",\"range\":{\"min\":\"\",\"max\":\"FF\"}";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void checkNewsFeedToken() {
        String check = swipePageActivity.updateToken(token,"RT:2", "TRC:40");
        assertThat(check.contentEquals(updateToken), is(true));
    }

    @Test
    public void checkNewsFeedTokenEmpty() {
        token = "";
        String check = swipePageActivity.updateToken(token,"RT:2", "TRC:40");
        assertEquals(check.isEmpty(), true);

    }
}