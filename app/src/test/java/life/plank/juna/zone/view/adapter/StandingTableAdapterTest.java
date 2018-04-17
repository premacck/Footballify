package life.plank.juna.zone.view.adapter;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.data.network.model.StandingModel;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.spy;

/**
 * Created by plank-prachi on 2/23/2018.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({StandingTableAdapter.class})
public class StandingTableAdapterTest {
    List<StandingModel> standingModelList;
    private StandingModel standingModel;

    @InjectMocks
    private StandingTableAdapter standingTableAdapter;
    private Context context;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        standingModel = new StandingModel();
        standingTableAdapter = mock(StandingTableAdapter.class);
        standingModelList = new ArrayList<>();
    }

    @Test
    public void itemCountOnStandingView() {
        standingTableAdapter = spy(new StandingTableAdapter(context, standingModelList));
        assertThat(standingTableAdapter.getItemCount(), is(0));
    }

    //checking if list Item count having some data
    @Test
    public void isStandingDataNotEmpty() {
        addStandingData();
        assertThat(standingModelList.isEmpty(), is(false));
    }

    //checking if list Item count is empty
    @Test
    public void itemsCountToStandingViewListIsNull() {
        when(standingTableAdapter.getItemCount() < 0).thenThrow(NullPointerException.class);
    }

    public void addStandingData() {
        standingModel.setFootballTeam("Chelsea");
        standingModel.setGamesWon(23);
        standingModel.setGamesDrawn(3);
        standingModel.setGamesLost(0);
        standingModel.setGamesPlayed(26);
        standingModel.setPoints(46);
        standingModel.setGoalsAgainst(45);
        standingModelList.add(standingModel);
    }
}