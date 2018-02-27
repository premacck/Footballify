package life.plank.juna.zone.view.activity;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.BaseUnitTest;
import life.plank.juna.zone.data.network.model.Tile;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by plank-hasan on 2/22/2018.
 */

public class LiveZoneActivityTest extends BaseUnitTest {
    @Mock
    Context context;
    @InjectMocks
    private LiveZoneActivity liveZoneActivity;
    private List<Tile> tileList = new ArrayList<>();


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        liveZoneActivity = mock(LiveZoneActivity.class);
    }

    //add new event in LiveZoneActivity and check if data gets added to adapter
    //TODO: to be done later unable to call  notifyItemInserted(position) of the adapter
    //TODO: test case onNewEventWillUpdateTheGridWithTheChangedTiles

    @Test
    public void checkIfIsChatScreenVisibleFlagIsSet() {
        //call retainLayout() and check if isChatScreenVisible is set
        liveZoneActivity.retainLayout();
        assertThat(liveZoneActivity.isChatScreenVisible, is(false));
    }
}
