package life.plank.juna.zone.domain.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import life.plank.juna.zone.view.adapter.FootballFeedDetailAdapter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by plank-prachi on 2/23/2018.
 */

public class FootballFeedDetailsAdapterTest {
    @InjectMocks
    private FootballFeedDetailAdapter footballFeedDetailAdapter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        footballFeedDetailAdapter = mock(FootballFeedDetailAdapter.class);
    }

    //if data is coming with null value
    @Test
    public void addItemsToHorizontalViewListIsNull() {
        when(footballFeedDetailAdapter.getItemCount() == 0).thenThrow(NullPointerException.class);

    }

    //if data is coming with  value
    @Test
    public void addItemsToHorizontalViewListIs() {
        int check = footballFeedDetailAdapter.getItemCount();
        Assert.assertEquals(check > 0, true);
    }
}
