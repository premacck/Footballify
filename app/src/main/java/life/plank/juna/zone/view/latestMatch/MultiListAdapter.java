package life.plank.juna.zone.view.latestMatch;

import android.app.Activity;

import com.ahamed.multiviewadapter.DataListManager;
import com.ahamed.multiviewadapter.RecyclerAdapter;

import java.util.List;

import life.plank.juna.zone.data.model.NextMatch;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.interfaces.OnItemClickListener;

public class MultiListAdapter extends RecyclerAdapter {

    private DataListManager<NextMatch> topMatchDataManager;
    private DataListManager<NextMatch> lowerMatchDataManager;
    private DataListManager<LeagueModel> leagueDataManager;

    public MultiListAdapter(Activity activity, RestApi restApi, OnItemClickListener onItemClickListener) {
        topMatchDataManager = new DataListManager<>(this);
        lowerMatchDataManager = new DataListManager<>(this);
        leagueDataManager = new DataListManager<>(this);

        addDataManager(topMatchDataManager);
        addDataManager(leagueDataManager);
        addDataManager(lowerMatchDataManager);

        registerBinder(new NextMatchBinder(activity, restApi));
        registerBinder(new LeagueBinder(onItemClickListener, activity));
    }

    public void addTopMatch(List<NextMatch> dataList) {
        topMatchDataManager.set(dataList);
    }

    public void addLowerMatch(List<NextMatch> dataList) {
        lowerMatchDataManager.set(dataList);
    }

    public void addLeague(LeagueModel dataList) {
        leagueDataManager.add(dataList);
    }
}
