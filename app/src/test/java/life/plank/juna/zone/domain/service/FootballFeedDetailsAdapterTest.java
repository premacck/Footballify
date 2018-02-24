package life.plank.juna.zone.domain.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.view.adapter.FootballFeedDetailAdapter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by plank-prachi on 2/23/2018.
 */

public class FootballFeedDetailsAdapterTest {
    @Mock
    List<FootballFeed> footballFeedsList;
    private FootballFeed footballFeed;
    @InjectMocks
    private FootballFeedDetailAdapter footballFeedDetailAdapter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        footballFeed = new FootballFeed();
        footballFeedDetailAdapter = mock(FootballFeedDetailAdapter.class);

    }

    @Test
    public void isFeedDataNotEmpty() {
        isDataNotEmpty();
        assertThat(footballFeedsList.isEmpty(), is(false));
    }

    @Test
    public void isFeedDataEmpty() {
        isEmptyData();
        for (int i = 0; i <= footballFeedsList.size() - 1; i++)
            assertThat(footballFeedsList.get(i).getSummary().isEmpty(), is(true));
    }

    /* if list is null */
    @Test
    public void addItemsToHorizontalViewListIsNull() {
        when(footballFeedDetailAdapter.getItemCount() < 0).thenThrow(NullPointerException.class);
    }

    //model class with empty data
    public void isEmptyData() {
        footballFeed.setSummary("");
        footballFeed.setTitle("Southampton");
    }

    //added data in model class
    public void isDataNotEmpty() {
        footballFeed.setSummary("Man United");
        footballFeed.setTitle("Southampton");
    }

}
