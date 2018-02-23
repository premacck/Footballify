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

/**
 * Created by plank-prachi on 2/23/2018.
 */

public class FootballFeedDetailsTest {
    private FootballFeed footballFeed;

    @InjectMocks
    private FootballFeedDetailAdapter footballFeedDetailAdapter;
    @Mock
    List<FootballFeed> footballFeedsList;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        footballFeed = new FootballFeed();
    }

    @Test
    public void isFeedDataNotEmpty() {
        footballFeed.setSummary("Man United");
        footballFeed.setTitle("Southampton");
        assertThat(footballFeedsList.isEmpty(), is(false));
    }

    @Test
    public void isFeedDataEmpty() {
        footballFeed.setSummary("");
        footballFeed.setTitle("Southampton");
        for (int i = 0; i <= footballFeedsList.size() - 1; i++)
            assertThat(footballFeedsList.get(i).getSummary().isEmpty(), is(true));
    }
}
