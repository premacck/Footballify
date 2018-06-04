package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.io.Serializable;
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
import life.plank.juna.zone.view.adapter.PointsGameResultAdapter;
import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PointsGameResultActivity extends AppCompatActivity implements Serializable {

    @Inject
    @Named("feed")
    Retrofit retrofit;

    @BindView(R.id.results_round_number)
    TextView roundNumberText;
    @BindView(R.id.round_label)
    TextView roundTextLabel;
    @BindView(R.id.points_game_result_recycler_view)
    RecyclerView recyclerView;
    private RestApi restApi;

    private PointsGameResultAdapter pointsGameResultAdapter = new PointsGameResultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_game_result);
        CustomizeStatusBar.removeStatusBar(getWindow());
        ButterKnife.bind(this);

        ((ZoneApplication) getApplication()).getPointsGameResultComponent().inject(this);
        restApi = retrofit.create(RestApi.class);

        Typeface aileronBold = Typeface.createFromAsset(getAssets(), getString(R.string.aileron_bold));
        roundNumberText.setTypeface(aileronBold);
        Typeface aileronSemiBold = Typeface.createFromAsset(getAssets(), getString(R.string.aileron_semibold));
        roundTextLabel.setTypeface(aileronSemiBold);

        getUserChoice();

        initRecyclerView();
        roundNumberText.setText(String.valueOf(ZoneApplication.roundNumber));

    }

    public void getUserChoice() {

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
                            pointsGameResultAdapter.setUserChoiceList(GlobalVariable.getInstance().getUserChoice());
                            getUserChoice();
                        } else {
                            GlobalVariable.getInstance().setUserChoice(response);
                            pointsGameResultAdapter.setUserChoiceList(GlobalVariable.getInstance().getUserChoice());
                        }
                        pointsGameResultAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pointsGameResultAdapter);
        pointsGameResultAdapter.setUserChoiceList(GlobalVariable.getInstance().getUserChoice());
    }

    @OnClick(R.id.results_home_icon)
    public void exitPointsGameResultActivity() {
        startActivity(new Intent(this, GameLaunchActivity.class));
        ZoneApplication.roundNumber = 0;
        ZoneApplication.selectedTeamsList.clear();
    }

    @OnClick(R.id.advance_image)
    public void startNextRound() {

        GlobalVariable.getInstance().getUserChoice().clear();

        if (ZoneApplication.roundNumber < (Arena.getInstance().getRounds().size())) {
            startActivity(new Intent(this, PointsGameActivity.class));
        } else {
            ZoneApplication.roundNumber = 0;
            startActivity(new Intent(this, GameLaunchActivity.class));
        }


    }

    @Override
    public void onBackPressed() {

    }
}
