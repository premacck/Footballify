package life.plank.juna.zone.view.LatestMatch;

import android.app.Activity;

import com.ahamed.multiviewadapter.DataListManager;
import com.ahamed.multiviewadapter.RecyclerAdapter;

import java.util.List;

import life.plank.juna.zone.data.model.League;

public class MultiListAdapter extends RecyclerAdapter {

    private DataListManager<League> topMatchDataManager;
    private DataListManager<League> lowerMatchDataManager;
    private DataListManager<LeagueModel> leagueDataManager;

    public MultiListAdapter(Activity activity) {
        topMatchDataManager = new DataListManager<>(this);
        lowerMatchDataManager = new DataListManager<>(this);
        leagueDataManager = new DataListManager<>(this);

        addDataManager(topMatchDataManager);
        addDataManager(leagueDataManager);
        addDataManager(lowerMatchDataManager);

        registerBinder(new NextMatchBinder(activity));
        registerBinder(new LeagueBinder());
    }

    public void addTopMatch(List<League> dataList) {
        topMatchDataManager.set(dataList);
    }

    public void addLowerMatch(List<League> dataList) {
        lowerMatchDataManager.set(dataList);
    }

    public void addLeague(LeagueModel dataList) {
        leagueDataManager.add(dataList);
    }
}
