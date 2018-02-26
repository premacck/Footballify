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

    //checking if list size is zero
    @Test
    public void isFeedDataEmpty() {
        assertThat(footballFeedsList.size(), is(0));
    }

    // checking for all field data is empty
    @Test
    public void isFeedSummaryNotEmpty() {
        addFeedData();
        assertThat(footballFeedsList.isEmpty(), is(false));
    }

    //checking if feed adapter item count is empty
    @Test
    public void addItemsToHorizontalViewListIsNull() {
        when(footballFeedDetailAdapter.getItemCount() < 0).thenThrow(NullPointerException.class);
    }

    //created method  and adding data for all field in a Football Feed
    public void addFeedData() {
        footballFeed.setSummary("Man United");
        footballFeed.setTitle("Southampton");
        footballFeed.setId("1");
        footballFeed.setUrl("web_url");
        footballFeed.setContentType("Southampton");
        footballFeed.setReactionType(1);
        footballFeed.setSource("Chelsea");
        //TODO will be check later
        // footballFeed.setTags(new Object());
        footballFeedsList.add(footballFeed);
    }
}
