package life.plank.juna.zone.presentation.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.builder.JunaUserBuilder;
import life.plank.juna.zone.data.network.builder.UserChoiceBuilder;
import life.plank.juna.zone.data.network.model.Arena;
import life.plank.juna.zone.data.network.model.Player;
import life.plank.juna.zone.data.network.model.UserChoice;
import life.plank.juna.zone.presentation.adapter.PointsGameResultAdapter;
import life.plank.juna.zone.util.CustomizeStatusBar;

public class PointsGameResultActivity extends AppCompatActivity implements Serializable {

    @BindView(R.id.results_round_number)
    TextView roundNumberText;
    @BindView(R.id.round_label)
    TextView roundTextLabel;
    @BindView(R.id.points_game_result_recycler_view)
    RecyclerView recyclerView;

    private PointsGameResultAdapter pointsGameResultAdapter = new PointsGameResultAdapter();
    private List<Player> playerList = new ArrayList<>();
    private List<UserChoice> userChoicesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_game_result);
        CustomizeStatusBar.removeStatusBar(getWindow());
        ButterKnife.bind(this);

        Typeface aileronBold = Typeface.createFromAsset(getAssets(), getString(R.string.aileron_bold));
        roundNumberText.setTypeface(aileronBold);
        Typeface aileronSemiBold = Typeface.createFromAsset(getAssets(), getString(R.string.aileron_semibold));
        roundTextLabel.setTypeface(aileronSemiBold);

        initRecyclerView();
        roundNumberText.setText(ZoneApplication.roundNumber.toString());
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pointsGameResultAdapter);
        playerList.addAll(Arena.getInstance().getPlayers());
        for (Player player : playerList) {
            userChoicesList.add(UserChoiceBuilder.getInstance().withJunaUser(JunaUserBuilder.getInstance()
                    .withUserName(player.getUsername()).build())
                    .build());
        }
        pointsGameResultAdapter.setUserChoiceList(userChoicesList);
    }

    @OnClick(R.id.results_home_icon)
    public void exitPointsGameResultActivity() {
        startActivity(new Intent(this, GameLaunchActivity.class));
    }

    @OnClick(R.id.advance_image)
    public void startNextRound() {
        startActivity(new Intent(this, PointsGameActivity.class));
    }

    @Override
    public void onBackPressed() {

    }
}
