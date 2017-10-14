package life.plank.juna.zone.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.builder.FootballTeamBuilder;
import life.plank.juna.zone.data.network.builder.JunaUserBuilder;
import life.plank.juna.zone.data.network.builder.UserChoiceBuilder;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.Arena;
import life.plank.juna.zone.data.network.model.FootballMatch;
import life.plank.juna.zone.domain.service.GameService;
import life.plank.juna.zone.presentation.adapter.PointsMatchAdapter;
import life.plank.juna.zone.util.CustomizeStatusBar;
import life.plank.juna.zone.util.TeamNameMap;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PointsGameActivity extends AppCompatActivity {

    @Inject
    Retrofit retrofit;
    @Inject
    GameService gameService;

    @BindView(R.id.match_points_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.year_text)
    TextView yearText;
    @BindView(R.id.round_number)
    TextView roundNumberText;

    private ImageView homeTeamImage;
    private TextView homeTeamName;
    private EditText homeTeamScore;
    private ImageView visitingTeamImage;
    private TextView visitingTeamName;
    private EditText visitingTeamScore;
    private ImageView submitScoreButton;

    private static final String TAG = PointsGameActivity.class.getSimpleName();
    private PointsMatchAdapter pointsMatchAdapter = new PointsMatchAdapter();
    private List<FootballMatch> footballMatchList = new ArrayList<>();
    private FootballTeamBuilder footballTeamBuilder = new FootballTeamBuilder();
    private Subscription subscription;
    private RestApi restApi;
    private UserChoiceBuilder userChoiceBuilder = new UserChoiceBuilder();
    private PopupWindow popupWindow;

    private Integer roundId;
    private Integer homeTeamGuessScore, visitingTeamGuessScore, selectedTeamId;
    private String selectedTeamName;
    private Integer playerScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_game);
        CustomizeStatusBar.removeStatusBar(getWindow());
        ButterKnife.bind(this);
        ((ZoneApplication) getApplication()).getPointsGameComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        initializePointsMatchGamesView();
        initRecyclerView();
    }

    private void initializePointsMatchGamesView() {
        footballMatchList = Arena.getInstance()
                .getRounds()
                .get(ZoneApplication.roundNumber)
                .getFootballMatches();
        pointsMatchAdapter.setFootballMatchList(footballMatchList);

        Integer leagueStartYear = Arena.getInstance().getLeagueYearStart();
        Integer previousYear = leagueStartYear - 1;
        yearText.setText(previousYear.toString() + "-" + leagueStartYear.toString());
        roundNumberText.setText((++ZoneApplication.roundNumber).toString());
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.horizontal_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(pointsMatchAdapter);

        pointsMatchAdapter.getViewClickedObservable()
                .subscribe(footballMatch -> initiatePopUp(footballMatch));
    }

    private void initiatePopUp(FootballMatch footballMatch) {
        LayoutInflater inflater = (LayoutInflater) PointsGameActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.user_choice_layout,
                (ViewGroup) findViewById(R.id.relative_layout_user_choice));
        popupWindow = new PopupWindow(layout,
                getResources().getInteger(R.integer.popup_window_dimension),
                getResources().getInteger(R.integer.popup_window_dimension));
        popupWindow.showAtLocation(layout, Gravity.CENTER,
                getResources().getInteger(R.integer.popup_location_offset),
                getResources().getInteger(R.integer.popup_location_offset));
        dimBehind(popupWindow);

        homeTeamImage = (ImageView) layout.findViewById(R.id.user_choice_home_team_image);
        homeTeamName = (TextView) layout.findViewById(R.id.user_choice_home_team_name);
        homeTeamScore = (EditText) layout.findViewById(R.id.user_choice_home_team_score);

        homeTeamImage.setImageDrawable(TeamNameMap.getTeamNameMap()
                .get(footballMatch.getHomeTeam().getName()));
        homeTeamName.setText(footballMatch.getHomeTeam().getName());

        visitingTeamImage = (ImageView) layout.findViewById(R.id.user_choice_visiting_team_image);
        visitingTeamName = (TextView) layout.findViewById(R.id.user_choice_visiting_team_name);
        visitingTeamScore = (EditText) layout.findViewById(R.id.user_choice_visiting_team_score);

        visitingTeamImage.setImageDrawable(TeamNameMap.getTeamNameMap()
                .get(footballMatch.getVisitingTeam().getName()));
        visitingTeamName.setText(footballMatch.getVisitingTeam().getName());

        submitScoreButton = (ImageView) layout.findViewById(R.id.user_choice_submit_button);
        RxView.clicks(submitScoreButton)
                .subscribe(v -> computeGamePoints(footballMatch));
    }

    private void computeGamePoints(FootballMatch footballMatch) {
        homeTeamGuessScore = Integer.parseInt(homeTeamScore.getText().toString());
        visitingTeamGuessScore = Integer.parseInt(visitingTeamScore.getText().toString());

        if (homeTeamGuessScore > visitingTeamGuessScore) {
            selectedTeamName = homeTeamName.getText().toString();
            selectedTeamId = footballMatch.getHomeTeam().getId();
        } else {
            selectedTeamName = visitingTeamName.getText().toString();
            selectedTeamId = footballMatch.getVisitingTeam().getId();
        }

        playerScore = gameService.computeScore(footballMatch, selectedTeamName, homeTeamGuessScore, visitingTeamGuessScore, playerScore);
        //Todo: Remove toast once the result page is created
        Toast.makeText(this, playerScore.toString(), Toast.LENGTH_SHORT).show();

        roundId = Arena.getInstance().getRounds()
                .get(ZoneApplication.roundNumber - 1).getId();

        subscription = restApi.postUserChoice(roundId, userChoiceBuilder.withId(roundId)
                .withHomeTeamScore(homeTeamGuessScore)
                .withVisitingTeamScore(visitingTeamGuessScore)
                .withPoints(playerScore)
                .withFootballMatch(footballMatch)
                .withFootballTeam(footballTeamBuilder.withId(selectedTeamId)
                        .withName(selectedTeamName)
                        .build())
                .withJunaUser(JunaUserBuilder.getInstance().build())
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "In onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(Response<Void> response) {
                        if (response.code() == HttpURLConnection.HTTP_CREATED) {
                            Log.d(TAG, "User choice posted");
                        } else
                            Log.d(TAG, "Error occured onPostUser choice with response code: " + response.code());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    @OnClick(R.id.home_icon)
    public void exitPointsMatchGame() {
        startActivity(new Intent(this, GameLaunchActivity.class));
    }

    public static void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) container.getLayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.9f;
        windowManager.updateViewLayout(container, layoutParams);
    }
}
