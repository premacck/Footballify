package life.plank.juna.zone.view.adapter;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.spy;

/**
 * Created by plank-prachi on 2/23/2018.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({StandingTableAdapter.class})
public class StandingTableAdapterTest {
    private StandingTableAdapter standingTableAdapter;
    private Context context;

    //checking if count is added
    @Test
    public void itemCountOnStandingScoreView() {
        standingTableAdapter = spy(new StandingTableAdapter(context));
        assertThat(standingTableAdapter.getItemCount() == 10, is(true));
    }

    //TODO: test case OnBindViewHolderAddItem
}
