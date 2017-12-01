package life.plank.juna.zone.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.model.FootballMatch;
import life.plank.juna.zone.util.CustomizeStatusBar;
import life.plank.juna.zone.util.TeamNameMap;
import life.plank.juna.zone.viewmodel.SuddenDeathGameViewModel;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.view.activity.PointsGameActivity.dimBehind;

public class SuddenDeathGameActivity extends AppCompatActivity {

    @BindView(R.id.sudden_death_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.round_number)
    TextView roundNumberText;
    @BindView(R.id.year_text)
    TextView yearText;

    private ImageView selectedTeamImage;
    private TextView selectedTeamName;
    private ImageView confirmYesButton;
    private ImageView confirmNoButton;

    private StringBuilder seasonLabel = new StringBuilder();
    private PopupWindow popupWindow;
    private SuddenDeathGameViewModel suddenDeathGameViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudden_death_game);
        ButterKnife.bind(this);
        CustomizeStatusBar.removeStatusBar(getWindow());
        suddenDeathGameViewModel = new SuddenDeathGameViewModel(recyclerView);
        seasonLabel = suddenDeathGameViewModel.initializeSuddenDeathMatchView();
        suddenDeathGameViewModel.initializeRecyclerView();
        initializeView();

        suddenDeathGameViewModel.getHomeTeamObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::initiateHomeTeamPopUp);

        suddenDeathGameViewModel.getVisitingTeamObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::initiateVisitingTeamPopUp);
    }

    private void initializeView() {
        yearText.setText(seasonLabel);
        roundNumberText.setText(String.valueOf(++ZoneApplication.roundNumber));
    }


    private void initiateHomeTeamPopUp(FootballMatch footballMatch) {
        initiatePopUp();

        selectedTeamName.setText(footballMatch.getHomeTeam().getName());
        selectedTeamImage.setImageDrawable(TeamNameMap.getTeamNameMap().get(footballMatch.getHomeTeam().getName()));

        confirmYesOrNo();
    }

    private void initiateVisitingTeamPopUp(FootballMatch footballMatch) {
        initiatePopUp();

        selectedTeamName.setText(footballMatch.getVisitingTeam().getName());
        selectedTeamImage.setImageDrawable(TeamNameMap.getTeamNameMap().get(footballMatch.getVisitingTeam().getName()));

        confirmYesOrNo();
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


    private void confirmYesOrNo() {
        //Todo: compute winner and navigate to result page here
        RxView.clicks(confirmYesButton)
                .subscribe();

        RxView.clicks(confirmNoButton)
                .subscribe(v -> popupWindow.dismiss());
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
