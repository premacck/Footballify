package life.plank.juna.zone.viewmodel;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.data.network.builder.JunaUserBuilder;
import life.plank.juna.zone.data.network.builder.UserChoiceBuilder;
import life.plank.juna.zone.data.network.model.Arena;
import life.plank.juna.zone.data.network.model.Player;
import life.plank.juna.zone.data.network.model.UserChoice;
import life.plank.juna.zone.view.adapter.SuddenDeathGameAdapter;
import life.plank.juna.zone.view.adapter.SuddenDeathResultAdapter;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by plank-sobia on 12/5/2017.
 */

public class SuddenDeathResultViewModel {
    private RecyclerView recyclerView;
    private List<Player> playerList = new ArrayList<>();
    private SuddenDeathResultAdapter suddenDeathGameAdapter = new SuddenDeathResultAdapter();
    private List<UserChoice> userChoicesList = new ArrayList<>();
    private String selectedTeamName;

    public SuddenDeathResultViewModel(RecyclerView recyclerView, String selectedTeamName) {
        this.recyclerView = recyclerView;
        this.selectedTeamName = selectedTeamName;
    }

    public void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(suddenDeathGameAdapter);
        playerList.addAll(Arena.getInstance().getPlayers());
        for (Player player : playerList) {
            userChoicesList.add(UserChoiceBuilder.getInstance().withJunaUser(JunaUserBuilder.getInstance()
                    .withUserName(player.getUsername()).build())
                    .build());
        }
        suddenDeathGameAdapter.setUserChoiceList(userChoicesList, selectedTeamName);
    }
}
