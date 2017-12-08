package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.util.CustomizeStatusBar;
import life.plank.juna.zone.viewmodel.SuddenDeathResultViewModel;

public class SuddenDeathResultActivity extends AppCompatActivity {

    @BindView(R.id.sudden_death_result_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.round_number_label)
    TextView roundNumberLabel;

    private SuddenDeathResultViewModel suddenDeathResultViewModel;
    private String selectedTeamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudden_death_result);
        ButterKnife.bind(this);
        CustomizeStatusBar.removeStatusBar(getWindow());
        selectedTeamName = getIntent().getStringExtra(getString(R.string.selected_team));

        roundNumberLabel.setText(String.valueOf(ZoneApplication.roundNumber));
        suddenDeathResultViewModel = new SuddenDeathResultViewModel(recyclerView, selectedTeamName);
        suddenDeathResultViewModel.initRecyclerView();
    }

    @OnClick(R.id.advance_image)
    public void startNextRound() {
        if (ZoneApplication.suddenDeathLivesRemaining == 0)
            startActivity(new Intent(this, WarriorGameActivity.class));
        else {
            //Todo: change this to navigate to the leader board activity
            startActivity(new Intent(this, SuddenDeathGameActivity.class));
        }
    }

    @OnClick(R.id.results_home_icon)
    public void exitSuddenDeathResultActivity() {
        startActivity(new Intent(this, GameLaunchActivity.class));
    }

    @Override
    public void onBackPressed() {

    }
}
