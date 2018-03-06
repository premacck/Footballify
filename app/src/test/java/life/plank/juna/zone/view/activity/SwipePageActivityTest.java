package life.plank.juna.zone.view.activity;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import life.plank.juna.zone.BaseUnitTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by plank-hasan on 3/1/2018.
 */

public class SwipePageActivityTest extends BaseUnitTest {
    @Mock
    Context context;
    @InjectMocks
    private SwipePageActivity swipePageActivity;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        swipePageActivity = mock(SwipePageActivity.class);
    }


    @Test
    public void IsStandingScoreVisibleFlagSet() {
        //call retainHomeLayout() and check if standing fragment  is set
        swipePageActivity.retainHomeLayout();
        assertThat(swipePageActivity.isStandingFragmentVisible, is(false));
    }
    //TODO: standing fragment need to be test with Instrumental Testing
    //TODO: instrumental test cases needs to be added for spiners,liveZone button,News feeds grid and drawer
}