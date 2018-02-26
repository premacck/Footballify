package life.plank.juna.zone.view.activity;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.Tile;
import life.plank.juna.zone.view.adapter.LiveZoneGridAdapter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by plank-hasan on 2/22/2018.
 */

public class LiveZoneGridAdapterTest {
    @InjectMocks
    private LiveZoneGridAdapter liveZoneGridAdapter;
    @InjectMocks
    private LiveZoneActivity liveZoneActivity;
    @Mock
    Context context;
    private List<Tile> tileList = new ArrayList<>();


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        liveZoneGridAdapter = mock(LiveZoneGridAdapter.class);
        liveZoneActivity = mock(LiveZoneActivity.class);
    }

    @Test
    public void isTagNull() {
        //check if tag of any tile is empty
        addDataToTileList();
        for (int i = 0; i <= tileList.size() - 1; i++) {
            assertThat(tileList.get(i).getTag().isEmpty(), is(false));
        }
    }

    @Test
    public void addGridItemsToViewNotifiesParentAndAddsItemToTileList() {
        //add data to adater and check if data gets added to adapter
        //TODO: to be done later unable to call  notifyItemInserted(position) of the adapter
       /* liveZoneGridAdapter = new LiveZoneGridAdapter(context);
        liveZoneGridAdapter.addGridItemsToView(0,new Tile("",0,0,""));
        assertThat(liveZoneGridAdapter.getItemCount() == 1, is(true));*/

    }

    @Test
    public void checkIfNewTilesAreGettingAddedAndNotifyItemInsertedCalled() {
        // add events from LiveZoneActivity and check if data is getting added into the adapter
        //TODO: to be done later,
       /* ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile("",0,0,""));
        liveZoneActivity.onNewEvent(new ScrubberViewData("Monreal upendes Hazard on the half.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tiles), false));
        verify(liveZoneGridAdapter,atLeastOnce()).addGridItemsToView(0,tiles.get(0));*/
    }

    private void addDataToTileList() {
        tileList.add(new Tile("image", R.drawable.image0, R.drawable.ic_sticker_four, ""));
        tileList.add(new Tile("text", 0, 0, "Why would Mourinho do that? Isn't he done with"));
        tileList.add(new Tile("sticker", R.drawable.image1, R.drawable.ic_sticker_four, "Why would Mourinho do that? Isn't he done with"));
        tileList.add(new Tile("image", R.drawable.image3, 0, ""));
        tileList.add(new Tile("image", R.drawable.ic_grid_one, 0, ""));
    }
}
