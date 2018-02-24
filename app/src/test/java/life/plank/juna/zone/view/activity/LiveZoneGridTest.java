package life.plank.juna.zone.view.activity;

import android.content.Context;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.Tile;
import life.plank.juna.zone.view.adapter.LiveZoneGridAdapter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by plank-hasan on 2/22/2018.
 */

public class LiveZoneGridTest {
    @Mock
    private LiveZoneGridAdapter liveZoneGridAdapter;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        liveZoneGridAdapter = mock(LiveZoneGridAdapter.class);
    }

 /*   @Test
    public void isTagNull() {
      //check if tag of any tile is empty
        for (int i = 0; i <= tileList.size() - 1; i++) {
            assertThat(tileList.get(i).getTag().isEmpty(), is(false));
        }
    }*/

    @Test
    public void checkAdpaterHasItems() {
        //check if adapter has childrens
        when(liveZoneGridAdapter.getItemCount() < 0)
                .thenThrow(NullPointerException.class);
    }

    @Mock
    Context context;

    @Test
    public void checkIfNewTilesAreGettingAddedAndNotifyItemInsertedCalled(){
        liveZoneGridAdapter = new LiveZoneGridAdapter(context);
        liveZoneGridAdapter.addGridItemsToView(0,new Tile("",0,0,""));
        //verify(liveZoneGridAdapter, times(1)).notifyItemInserted(0);
        assertThat(liveZoneGridAdapter.getItemCount(), is(1));
    }
}
