package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.Arena;
import life.plank.juna.zone.data.network.model.UserChoice;
import life.plank.juna.zone.util.CustomizeStatusBar;
import life.plank.juna.zone.util.GlobalVariable;
import life.plank.juna.zone.view.adapter.SuddenDeathResultAdapter;
import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SuddenDeathResultActivity extends AppCompatActivity {
    @Inject
    @Named("default")
    Retrofit retrofit;

    @BindView(R.id.sudden_death_result_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.round_number_label)
    TextView roundNumberLabel;

    private String selectedTeamName;
    private SuddenDeathResultAdapter suddenDeathResultAdapter = new SuddenDeathResultAdapter();
    private RestApi restApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudden_death_result);
        ButterKnife.bind(this);
        CustomizeStatusBar.removeStatusBar(getWindow());
        selectedTeamName = getIntent().getStringExtra(getString(R.string.selected_team));
        ((ZoneApplication) getApplication()).getSuddenDeathResultComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        getUserChoice();
        roundNumberLabel.setText(String.valueOf(ZoneApplication.roundNumber));
        initRecyclerView();
    }

    public void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(suddenDeathResultAdapter);
        suddenDeathResultAdapter.setUserChoiceList(GlobalVariable.getInstance().getUserChoice());
    }

    private void getUserChoice() {

        restApi.getUserChoice(getIntent().getIntExtra("roundId", 0))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<UserChoice>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<UserChoice> response) {

                        if (response.size() <= Arena.getInstance().getPlayers().size()) {
                            GlobalVariable.getInstance().setUserChoice(response);
                            suddenDeathResultAdapter.setUserChoiceList(GlobalVariable.getInstance().getUserChoice());
                            getUserChoice();
                        } else {
                            GlobalVariable.getInstance().setUserChoice(response);
                            suddenDeathResultAdapter.setUserChoiceList(GlobalVariable.getInstance().getUserChoice());
                        }
                        suddenDeathResultAdapter.notifyDataSetChanged();
                    }
                });

    }

    @OnClick(R.id.advance_image)
    public void startNextRound() {
        GlobalVariable.getInstance().getUserChoice().clear();
        if (ZoneApplication.suddenDeathLivesRemaining == 0) {
            ZoneApplication.roundNumber = 0;
            ZoneApplication.suddenDeathLivesRemaining = 5;
            ZoneApplication.selectedTeamsList.clear();
            startActivity(new Intent(this, WarriorGameActivity.class));
        } else {
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
