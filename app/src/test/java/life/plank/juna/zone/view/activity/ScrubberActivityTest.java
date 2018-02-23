package life.plank.juna.zone.view.activity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

import life.plank.juna.zone.data.network.model.ScrubberViewData;
import life.plank.juna.zone.util.ScrubberConstants;
import life.plank.juna.zone.view.adapter.ScrubberViewAdapter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by plank-niraj on 23-02-2018.
 */

public class ScrubberActivityTest {

    // To check the event trigger.
    private static final int EVENT = 7;
    @Mock
    HashMap<Integer, ScrubberViewData> scrubberViewDataHolder;
    @Mock
    LiveZoneActivity liveZoneActivity;
    @Mock
    private ScrubberViewAdapter scrubberViewAdapter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        scrubberViewDataHolder = new HashMap<>();
        ScrubberConstants.getHighLightsMatchOne(scrubberViewDataHolder);
    }

    /**
     * Test to check dataSet update.
     */
    @Test
    public void testDataSet() {
        scrubberViewAdapter.addHardCodedData();
        assertThat(scrubberViewDataHolder.isEmpty(), is(false));
    }

    /**
     * Test to check the function call(onNewEvent).
     */
    @Test
    public void onNewEventTest() {
        if (scrubberViewDataHolder.containsKey(EVENT) && scrubberViewDataHolder.get(EVENT).isTriggerEvents()) {
            liveZoneActivity.onNewEvent(scrubberViewDataHolder.get(EVENT));
        }
        Mockito.verify(liveZoneActivity).onNewEvent(scrubberViewDataHolder.get(EVENT));
    }

    /**
     * Test to check updateRecentEvents function call
     */
    @Test
    public void eventUpdateTest() {
        if (scrubberViewDataHolder.containsKey(EVENT) && scrubberViewDataHolder.get(EVENT).isTriggerEvents()) {
            liveZoneActivity.updateRecentEvents(EVENT);
        }
        Mockito.verify(liveZoneActivity).updateRecentEvents(EVENT);
    }

    /**
     * Test to check displayTooltip function call
     */
    @Test
    public void toolTipDisplayTest() {
        if (scrubberViewDataHolder.containsKey(EVENT) &&
                scrubberViewDataHolder.get(EVENT).isTriggerEvents()) {
            scrubberViewAdapter.displayTooltip(null, scrubberViewDataHolder.get(EVENT).getMessage());
        }
        Mockito.verify(scrubberViewAdapter).displayTooltip(null, scrubberViewDataHolder.get(EVENT).getMessage());
    }
}
