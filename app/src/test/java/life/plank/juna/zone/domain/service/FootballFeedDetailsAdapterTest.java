package life.plank.juna.zone.domain.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.data.network.model.Thumbnail;
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

    // checking for all field data is empty or fed
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
    public void isFeedIdNotEmpty() {
        addId();
        assertThat(footballFeedsList.get(0).getId(), is("1"));
    }

    @Test
    public void isFeedUrlNotEmpty() {
        addUrl();
        assertThat(footballFeedsList.get(0).getUrl(), is("web_url"));
    }

    @Test
    public void isFeedContaintTypeNotEmpty() {
        addContaintType();
        assertThat(footballFeedsList.get(0).getContentType(), is("Southampton"));
    }

    @Test
    public void isFeedReactionNotEmpty() {
        addReactionType();
        assertThat(footballFeedsList.get(0).getReactionType(), is(1));
    }

    @Test
    public void isFeedSourceNotEmpty() {
        addSource();
        assertThat(footballFeedsList.get(0).getSource(), is("Chelsea"));
    }
    
    //checking if feed adapter item count is empty
    @Test
    public void addItemsToHorizontalViewListIsNull() {
        when(footballFeedDetailAdapter.getItemCount() < 0).thenThrow(NullPointerException.class);
    }

    //checking if list size is zero
    @Test
    public void isFeedDataEmpty() {
        assertThat(footballFeedsList.size(), is(0));
    }

    //created method  and adding data for all field in a Football Feed
    public void addSummary() {
        footballFeed.setSummary("Man United");
        footballFeedsList.add(footballFeed);
    }

    private void addTitle() {
        footballFeed.setTitle("Southampton");
        footballFeedsList.add(footballFeed);
    }

    public void addId() {
        footballFeed.setId("1");
        footballFeedsList.add(footballFeed);
    }

    private void addUrl() {
        footballFeed.setUrl("web_url");
        footballFeedsList.add(footballFeed);
    }

    //TODO will be check later
    public void addThumbnail() {
        footballFeed.setThumbnail(new Thumbnail());
        footballFeedsList.add(footballFeed);
    }

    private void addContaintType() {
        footballFeed.setContentType("Southampton");
        footballFeedsList.add(footballFeed);
    }

    public void addReactionType() {
        footballFeed.setReactionType(1);
        footballFeedsList.add(footballFeed);
    }

    //TODO will be check later
    private void addTags() {
        footballFeed.setTags(new Object());
        footballFeedsList.add(footballFeed);
    }

    private void addSource() {
        footballFeed.setSource("Chelsea");
        footballFeedsList.add(footballFeed);

    }
}
