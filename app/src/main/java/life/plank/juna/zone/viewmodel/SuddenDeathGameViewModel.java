package life.plank.juna.zone.viewmodel;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.model.Arena;
import life.plank.juna.zone.data.network.model.FootballMatch;
import life.plank.juna.zone.data.network.model.JunaUser;
import life.plank.juna.zone.view.adapter.SuddenDeathGameAdapter;
import rx.Observable;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by plank-sobia on 12/1/2017.
 */

public class SuddenDeathGameViewModel {
    private RecyclerView recyclerView;
    private SuddenDeathGameAdapter suddenDeathGameAdapter = new SuddenDeathGameAdapter();
    private List<FootballMatch> footballMatchList = new ArrayList<>();

    public SuddenDeathGameViewModel(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void initializeRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.horizontal_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(suddenDeathGameAdapter);
    }

    public StringBuilder initializeSuddenDeathMatchView() {
        footballMatchList = Arena.getInstance()
                .getRounds()
                .get(ZoneApplication.roundNumber)
                .getFootballMatches();
        suddenDeathGameAdapter.setFootballMatchList(footballMatchList);

        Integer leagueStartYear = Arena.getInstance().getLeagueYearStart();
        Integer previousYear = leagueStartYear - 1;
        StringBuilder seasonLabel = new StringBuilder();
        seasonLabel.append(previousYear)
                .append("-")
                .append(leagueStartYear);
        return seasonLabel;
    }

    public Observable<FootballMatch> getHomeTeamObservable() {
        return suddenDeathGameAdapter.getHomeTeamObservable();
    }

    public Observable<FootballMatch> getVisitingTeamObservable() {
        return suddenDeathGameAdapter.getVisitingTeamObservable();
    }

    public void clearFootballMatchList() {
        footballMatchList.clear();
        suddenDeathGameAdapter.setFootballMatchList(footballMatchList);
    }

    public void saveResultInHashMap(JunaUser junaUser, boolean isWinner) {
        ZoneApplication.suddenDeathGameResultMap.put(junaUser, isWinner);
    }
}
