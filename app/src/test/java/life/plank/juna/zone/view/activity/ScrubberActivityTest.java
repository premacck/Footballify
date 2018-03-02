package life.plank.juna.zone.view.activity;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;

import life.plank.juna.zone.data.network.model.ScrubberViewData;
import life.plank.juna.zone.interfaces.ScrubberPointerUpdate;
import life.plank.juna.zone.util.ScrubberConstants;
import life.plank.juna.zone.view.adapter.ScrubberViewAdapter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by plank-niraj on 23-02-2018.
 */

public class ScrubberActivityTest {

    @Mock
    HashMap<Integer, ScrubberViewData> scrubberViewDataHolder;
    @Mock
    Context context;
    LiveZoneActivity liveZoneActivity;
    @Mock
    ScrubberPointerUpdate scrubberPointerUpdate;
    ArrayList<Integer> data;
    ScrubberViewAdapter scrubberViewAdapter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        scrubberViewDataHolder = new HashMap<>();
        liveZoneActivity = mock(LiveZoneActivity.class);
        data = new ArrayList<>();
        scrubberViewAdapter = Mockito.spy(new ScrubberViewAdapter(context,
                data, scrubberViewDataHolder, scrubberPointerUpdate, 1));
        ScrubberConstants.getHighLightsMatchOne(scrubberViewDataHolder);
    }

    @Test
    public void updateTestAdapterData() {
        ArrayList<Integer> data = new ArrayList<>();
        ScrubberViewAdapter scrubberViewAdapter = new ScrubberViewAdapter(context,
                data, scrubberViewDataHolder, scrubberPointerUpdate, 1);
        data.add(1);
        assertThat(scrubberViewAdapter.getItemCount(), is(1));
    }

    @Test
    public void testAdapterDataForProgress() {
        data.clear();
        data.add(ScrubberConstants.SCRUBBER_VIEW_PROGRESS);
        assertThat(scrubberViewAdapter.getItemViewType(0), is(ScrubberConstants.SCRUBBER_VIEW_PROGRESS));
    }

    @Test
    public void testAdapterDataForGoal() {
        data.clear();
        data.add(ScrubberConstants.SCRUBBER_VIEW_GOAL);
        assertThat(scrubberViewAdapter.getItemViewType(0), is(ScrubberConstants.SCRUBBER_VIEW_GOAL));
    }

    @Test
    public void testAdapterDataForHalfTime() {
        data.clear();
        data.add(ScrubberConstants.SCRUBBER_VIEW_HALF_TIME);
        assertThat(scrubberViewAdapter.getItemViewType(0), is(ScrubberConstants.SCRUBBER_VIEW_HALF_TIME));
    }

    @Test
    public void testAdapterDataForSubstitute() {
        data.clear();
        data.add(ScrubberConstants.SCRUBBER_VIEW_SUBSTITUTE);
        assertThat(scrubberViewAdapter.getItemViewType(0), is(ScrubberConstants.SCRUBBER_VIEW_SUBSTITUTE));
    }

    @Test
    public void testAdapterDataForPointer() {
        data.clear();
        data.add(ScrubberConstants.SCRUBBER_VIEW_CURSOR);
        assertThat(scrubberViewAdapter.getItemViewType(0), is(ScrubberConstants.SCRUBBER_VIEW_CURSOR));
    }

    @Test
    public void testAdapterDataForCards() {
        data.clear();
        data.add(ScrubberConstants.SCRUBBER_VIEW_CARDS);
        assertThat(scrubberViewAdapter.getItemViewType(0), is(ScrubberConstants.SCRUBBER_VIEW_CARDS));
    }
}
