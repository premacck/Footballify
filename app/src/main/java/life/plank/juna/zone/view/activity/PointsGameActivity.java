package life.plank.juna.zone.view.activity;

import android.app.Service;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

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
import life.plank.juna.zone.util.CustomizeStatusBar;
import life.plank.juna.zone.util.TeamNameMap;
import life.plank.juna.zone.view.adapter.PointsGameAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PointsGameActivity extends AppCompatActivity {

    @Inject
    @Named("default")
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
    private PointsGameAdapter pointsGameAdapter = new PointsGameAdapter();
    private List<FootballMatch> footballMatchList = new ArrayList<>();
    private FootballTeamBuilder footballTeamBuilder = new FootballTeamBuilder();
    private Subscription subscription;
    private RestApi restApi;
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
        pointsGameAdapter.setFootballMatchList(footballMatchList);

        Integer leagueStartYear = Arena.getInstance().getLeagueYearStart();
        Integer previousYear = leagueStartYear - 1;
        String yearNumberText = previousYear + "-" + leagueStartYear;
        yearText.setText(yearNumberText);
        roundNumberText.setText(String.valueOf(++ZoneApplication.roundNumber));
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.horizontal_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(pointsGameAdapter);

        pointsGameAdapter.getViewClickedObservable()
                .subscribe(this::initiatePopUp);
    }

    private void initiatePopUp(FootballMatch footballMatch) {
        LayoutInflater inflater = (LayoutInflater) PointsGameActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.user_choice_layout,
                (ViewGroup) findViewById(R.id.relative_layout_user_choice));
        popupWindow = new PopupWindow(layout,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
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
        RxTextView.textChangeEvents(homeTeamScore)
                .subscribe(event -> shiftCursorFocus(homeTeamScore));

        visitingTeamImage = (ImageView) layout.findViewById(R.id.user_choice_visiting_team_image);
        visitingTeamName = (TextView) layout.findViewById(R.id.user_choice_visiting_team_name);
        visitingTeamScore = (EditText) layout.findViewById(R.id.user_choice_visiting_team_score);

        visitingTeamImage.setImageDrawable(TeamNameMap.getTeamNameMap()
                .get(footballMatch.getVisitingTeam().getName()));
        visitingTeamName.setText(footballMatch.getVisitingTeam().getName());
        RxTextView.textChangeEvents(visitingTeamScore)
                .subscribe(event -> shiftCursorFocus(visitingTeamScore));

        submitScoreButton = (ImageView) layout.findViewById(R.id.user_choice_submit_button);
        submitScoreButton.setEnabled(false);

        InitialValueObservable<CharSequence> homeTeamScoreObservable = RxTextView.textChanges(homeTeamScore);
        InitialValueObservable<CharSequence> visitingTeamScoreObservable = RxTextView.textChanges(visitingTeamScore);

        InitialValueObservable.combineLatest(homeTeamScoreObservable, visitingTeamScoreObservable, (homeScoreObservable, visitingScoreObservable) -> {
            boolean homeScoreCheck = homeScoreObservable.toString().length() == 1;
            boolean visitingScoreCheck = visitingScoreObservable.toString().length() == 1;
            boolean scoreEqualsCheck = !(homeScoreObservable.toString().equals(visitingScoreObservable.toString()));
            return homeScoreCheck && visitingScoreCheck && scoreEqualsCheck;
        }).subscribe(validationBoolean -> {
            submitScoreButton.setEnabled(validationBoolean);
            if (validationBoolean)
                submitScoreButton.setAlpha(1f);
            else
                submitScoreButton.setAlpha(0.7f);
        });

        RxView.clicks(submitScoreButton)
                .subscribe(v -> {
                    submitScoreButton.setEnabled(false);
                    computeGamePoints(footballMatch);
                });
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

        ZoneApplication.pointsGameResultMap.put(JunaUserBuilder.getInstance().build(),
                gameService.isWinner(footballMatch, selectedTeamName));

        ZoneApplication.selectedTeamsList.add(selectedTeamName);

        roundId = Arena.getInstance().getRounds()
                .get(ZoneApplication.roundNumber - 1).getId();

        subscription = restApi.postUserChoice(roundId, UserChoiceBuilder.getInstance()
                .withId(roundId)
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
                        submitScoreButton.setEnabled(true);
                        Log.d(TAG, "In onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(Response<Void> response) {
                        if (response.code() == HttpURLConnection.HTTP_CREATED) {
                            Log.d(TAG, "User choice posted" + ZoneApplication.roundNumber);
                            startActivity(new Intent(PointsGameActivity.this, PointsGameResultActivity.class));
                        } else
                            Log.d(TAG, "Error occured onPostUser choice with response code: " + response.code());
                        submitScoreButton.setEnabled(true);
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
        ZoneApplication.roundNumber = 0;
        ZoneApplication.selectedTeamsList.clear();
        pointsGameAdapter.setFootballMatchList(null);
    }

    private void shiftCursorFocus(EditText editText) {
        if (editText.length() == 1) {
            View next = editText.focusSearch(View.FOCUS_RIGHT);
            if (next == null) {
                InputMethodManager inputMethodManager = (InputMethodManager) ZoneApplication.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            } else next.requestFocus();
        }
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

    @Override
    public void onBackPressed() {

    }
}
