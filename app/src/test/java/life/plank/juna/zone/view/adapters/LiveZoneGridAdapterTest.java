package life.plank.juna.zone.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import life.plank.juna.zone.data.network.model.Tile;
import life.plank.juna.zone.view.adapter.LiveZoneGridAdapter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.spy;

/**
 * Created by plank-hasan on 2/22/2018.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({LiveZoneGridAdapter.class})
public class LiveZoneGridAdapterTest {
    private LiveZoneGridAdapter liveZoneGridAdapter;
    private Context context;

    @Test
    public void addGridItemsToViewNotifiesParentAndAddsItemToTileList() {
        //Add data to adapter and check if adapter count has increased
        liveZoneGridAdapter = spy(new LiveZoneGridAdapter(context));
        doNothing().when((RecyclerView.Adapter) liveZoneGridAdapter).notifyItemInserted(0);
        liveZoneGridAdapter.addGridItemsToView(0, new Tile("", 0, 0, ""));
        liveZoneGridAdapter.addGridItemsToView(0, new Tile("", 0, 0, ""));
        assertThat(liveZoneGridAdapter.getItemCount() == 2, is(true));
    }

    //TODO: add events from LiveZoneActivity and check if data is getting added into the adapter
    //TODO: test case OnBindViewHolderSubscribesToOnItemClicked

}
