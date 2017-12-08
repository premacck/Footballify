package life.plank.juna.zone.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.builder.FootballTeamBuilder;
import life.plank.juna.zone.data.network.builder.JunaUserBuilder;
import life.plank.juna.zone.data.network.model.Arena;
import life.plank.juna.zone.data.network.model.FootballMatch;
import life.plank.juna.zone.data.network.model.FootballTeam;
import life.plank.juna.zone.domain.service.GameService;
import life.plank.juna.zone.util.CustomizeStatusBar;
import life.plank.juna.zone.util.TeamNameMap;
import life.plank.juna.zone.viewmodel.SuddenDeathGameViewModel;
import retrofit2.Retrofit;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.view.activity.PointsGameActivity.dimBehind;

public class SuddenDeathGameActivity extends AppCompatActivity {

    @Inject
    @Named("default")
    Retrofit retrofit;
    @Inject
    GameService gameService;

    @BindView(R.id.sudden_death_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.round_number)
    TextView roundNumberText;
    @BindView(R.id.year_text)
    TextView yearText;
    @BindView(R.id.lives_remaining_count)
    TextView livesRemainingLabel;

    private ImageView selectedTeamImage;
    private TextView selectedTeamName;
    private ImageView confirmYesButton;
    private ImageView confirmNoButton;

    private final String TAG = SuddenDeathGameActivity.class.getSimpleName();
    private StringBuilder seasonLabel = new StringBuilder();
    private PopupWindow popupWindow;
    private Integer roundId;
    private SuddenDeathGameViewModel suddenDeathGameViewModel;
    private FootballTeamBuilder footballTeamBuilder = new FootballTeamBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudden_death_game);
        ButterKnife.bind(this);
        CustomizeStatusBar.removeStatusBar(getWindow());

        ((ZoneApplication) getApplication()).getSuddenDeathGameComponent().inject(this);
        suddenDeathGameViewModel = new SuddenDeathGameViewModel(recyclerView, retrofit);
        seasonLabel = suddenDeathGameViewModel.initializeSuddenDeathMatchView();
        suddenDeathGameViewModel.initializeRecyclerView();
        initializeView();

        suddenDeathGameViewModel.getHomeTeamObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(footballMatch -> initiateHomeTeamPopUp(footballMatch,
                        footballTeamBuilder.withId(footballMatch.getHomeTeam().getId())
                                .withName(footballMatch.getHomeTeam().getName())
                                .build()));

        suddenDeathGameViewModel.getVisitingTeamObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(footballMatch -> initiateVisitingTeamPopUp(footballMatch,
                        footballTeamBuilder.withId(footballMatch.getVisitingTeam().getId())
                                .withName(footballMatch.getVisitingTeam().getName())
                                .build()));
    }

    private void initializeView() {
        yearText.setText(seasonLabel);
        roundNumberText.setText(String.valueOf(++ZoneApplication.roundNumber));
        livesRemainingLabel.setText(String.valueOf(ZoneApplication.suddenDeathLivesRemaining));
    }


    private void initiateHomeTeamPopUp(FootballMatch footballMatch, FootballTeam footballTeam) {
        initiatePopUp();

        selectedTeamName.setText(footballMatch.getHomeTeam().getName());
        selectedTeamImage.setImageDrawable(TeamNameMap.getTeamNameMap().get(footballMatch.getHomeTeam().getName()));

        confirmYesOrNo(footballMatch, footballTeam);
    }

    private void initiateVisitingTeamPopUp(FootballMatch footballMatch, FootballTeam footballTeam) {
        initiatePopUp();

        selectedTeamName.setText(footballMatch.getVisitingTeam().getName());
        selectedTeamImage.setImageDrawable(TeamNameMap.getTeamNameMap().get(footballMatch.getVisitingTeam().getName()));

        confirmYesOrNo(footballMatch, footballTeam);
    }

    private void initiatePopUp() {
        LayoutInflater inflater = (LayoutInflater) SuddenDeathGameActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.sudden_death_confirm_team_popup,
                (ViewGroup) findViewById(R.id.confirm_popup_relative_layout));
        popupWindow = new PopupWindow(layout,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(layout, Gravity.CENTER,
                getResources().getInteger(R.integer.popup_location_offset),
                getResources().getInteger(R.integer.popup_location_offset));
        dimBehind(popupWindow);

        selectedTeamName = (TextView) layout.findViewById(R.id.selected_team_name);
        selectedTeamImage = (ImageView) layout.findViewById(R.id.selected_team_image);
        confirmYesButton = layout.findViewById(R.id.confirm_yes_button);
        confirmNoButton = layout.findViewById(R.id.confirm_no_button);
    }

    private void confirmYesOrNo(FootballMatch footballMatch, FootballTeam footballTeam) {
        roundId = Arena.getInstance().getRounds()
                .get(ZoneApplication.roundNumber - 1).getId();
        RxView.clicks(confirmYesButton)
                .subscribe(confirmYes -> {
                    if (gameService.isWinner(footballMatch, footballTeam.getName())) {
                        suddenDeathGameViewModel.saveResultInHashMap(JunaUserBuilder.getInstance().build(), true);

                        Intent intent = new Intent(this, SuddenDeathWinnerOrLoserActivity.class);
                        intent.putExtra(getString(R.string.result_string), getString(R.string.right_label));
                        intent.putExtra(getString(R.string.selected_team), footballTeam.getName());

                        postUserChoice(footballMatch, footballTeam, true, intent);
                    } else {
                        suddenDeathGameViewModel.saveResultInHashMap(JunaUserBuilder.getInstance().build(), false);
                        gameService.livesRemaining();

                        Intent intent = new Intent(this, SuddenDeathWinnerOrLoserActivity.class);
                        intent.putExtra(getString(R.string.result_string), getString(R.string.wrong_label));
                        intent.putExtra(getString(R.string.selected_team), footballTeam.getName());

                        postUserChoice(footballMatch, footballTeam, false, intent);
                    }
                });

        ZoneApplication.selectedTeamsList.add(footballTeam.getName());

        RxView.clicks(confirmNoButton)
                .subscribe(v -> popupWindow.dismiss());
    }

    private void postUserChoice(FootballMatch footballMatch, FootballTeam footballTeam, boolean isWinner, Intent intent) {
        suddenDeathGameViewModel.postUserChoice(roundId, footballMatch, footballTeam, isWinner, intent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.code() == HttpURLConnection.HTTP_CREATED) {
                        startActivity(intent);
                        Log.d(TAG, "User choice posted for round number:" + ZoneApplication.roundNumber);
                    } else
                        Log.d(TAG, "Error occured onPostUser choice with response code: " + response.code());
                });
    }

    @OnClick(R.id.home_icon)
    public void exitSuddenDeathGame() {
        startActivity(new Intent(this, GameLaunchActivity.class));
        ZoneApplication.roundNumber = 0;
        suddenDeathGameViewModel.clearFootballMatchList();
    }

    @Override
    public void onBackPressed() {

    }
}
