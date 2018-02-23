package life.plank.juna.zone.view.activity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.Tile;
import life.plank.juna.zone.view.adapter.LiveZoneGridAdapter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by plank-hasan on 2/22/2018.
 */

public class LiveZoneGridTest {
    @InjectMocks
    private LiveZoneGridAdapter liveZoneGridAdapter;
    @Mock
    private List<Tile> tileList;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void isTileListEmpty() {
        tileList.add(new Tile("image", R.drawable.image0, R.drawable.ic_sticker_four, ""));
        tileList.add(new Tile("text", 0, 0, "Why would Mourinho do that? Isn't he done with"));
        assertThat(tileList.isEmpty(), is(false));
    }

    @Test
    public void isTagNull() {
        tileList.add(new Tile("image", R.drawable.image0, R.drawable.ic_sticker_four, ""));
        tileList.add(new Tile("text", 0, 0, "Why would Mourinho do that? Isn't he done with"));
        for (int i = 0; i <= tileList.size() - 1; i++) {
            assertThat(tileList.get(i).getTag().isEmpty(), is(false));
        }
    }
}
