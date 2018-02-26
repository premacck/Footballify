package life.plank.juna.zone.domain.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
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

    List<FootballFeed> footballFeedsList;
    private FootballFeed footballFeed;

    @InjectMocks
    private FootballFeedDetailAdapter footballFeedDetailAdapter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        footballFeed = new FootballFeed();
        footballFeedDetailAdapter = mock(FootballFeedDetailAdapter.class);
        footballFeedsList = new ArrayList<>();
    }

    @Test
    public void isFeedSummaryNotEmpty() {
        addSummary();
        assertThat(footballFeedsList.get(0).getSummary(), is("Man United"));
    }

    @Test
    public void isFeedTitleNotEmpty() {
        addTitle();
        assertThat(footballFeedsList.get(0).getTitle(), is("Southampton"));
    }

    @Test
    public void footballFeedDataIsEmpty() {
        assertThat(footballFeedsList.isEmpty(), is(false));
    }

    @Test
    public void feedItemCountEmpty() {
        for (int i = 0; i < footballFeedsList.size(); i++)
            assertThat(footballFeedsList.isEmpty(), is(false));
    }

    @Test
    public void addItemsToHorizontalViewListIsNull() {
        when(footballFeedDetailAdapter.getItemCount() < 0).thenThrow(NullPointerException.class);
    }

    @Test
    public void isFeedDataEmpty() {
        assertThat(footballFeedsList.size(), is(0));
    }


    public void addSummary() {
        footballFeed.setSummary("Man United");
        footballFeedsList.add(footballFeed);
    }

    private void addTitle() {
        footballFeed.setTitle("Southampton");
        footballFeedsList.add(footballFeed);
    }

}
