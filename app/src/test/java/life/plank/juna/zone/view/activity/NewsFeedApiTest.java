package life.plank.juna.zone.view.activity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

/**
 * Created by plank-sharath on 2/23/2018.
 */

public class NewsFeedApiTest {

    @InjectMocks
    SwipePageActivity swipePageActivity;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void CheckNewsFeedAPi() {
        String check = swipePageActivity.updateToken("{\"token\":\"+RID:S6gVAN+HdgBLAAAAAAAAAA==#RT:1#TRC:20#RTD:vjAyMDE4LTAyLTE4VDE0OjMwOjAwWg==\",\"range\":{\"min\":\"\",\"max\":\"FF\"}}"
                , "RT:(\\d*)", "TRC:(\\\\d*)", "RT:2", "TRC:40");
        assertEquals(check.isEmpty(), (false));
    }
}
