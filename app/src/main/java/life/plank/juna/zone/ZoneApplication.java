package life.plank.juna.zone;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import life.plank.juna.zone.data.network.dagger.CreateArenaNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerCreateArenaNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerFixtureAndResultNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerFootballFeedNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerFootballMatchNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerJoinGameNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerLikeFeedNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerLineupNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerLoginUserNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerMultipleUserJoinGameNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerNewsFeedsNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerOnBoardSocialLoginNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerPlayerStatsNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerPointsGameComponent;
import life.plank.juna.zone.data.network.dagger.DaggerPointsGameResultComponent;
import life.plank.juna.zone.data.network.dagger.DaggerRegisterUserNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerSigninUserNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerSignupUserNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerSocialLoginNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerStandingsNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerSuddenDeathGameComponent;
import life.plank.juna.zone.data.network.dagger.DaggerSuddenDeathResultComponent;
import life.plank.juna.zone.data.network.dagger.DaggerTeamStatsNetworkComponent;
import life.plank.juna.zone.data.network.dagger.FixtureAndResultNetworkComponent;
import life.plank.juna.zone.data.network.dagger.FootballFeedNetworkComponent;
import life.plank.juna.zone.data.network.dagger.FootballMatchNetworkComponent;
import life.plank.juna.zone.data.network.dagger.JoinGameNetworkComponent;
import life.plank.juna.zone.data.network.dagger.LikeFeedNetworkComponent;
import life.plank.juna.zone.data.network.dagger.LineupNetworkComponent;
import life.plank.juna.zone.data.network.dagger.LoginUserNetworkComponent;
import life.plank.juna.zone.data.network.dagger.MultipleUserJoinGameNetworkComponent;
import life.plank.juna.zone.data.network.dagger.NewsFeedsNetworkComponent;
import life.plank.juna.zone.data.network.dagger.OnBoardSocialLoginNetworkComponent;
import life.plank.juna.zone.data.network.dagger.PlayerStatsNetworkComponent;
import life.plank.juna.zone.data.network.dagger.PointsGameComponent;
import life.plank.juna.zone.data.network.dagger.PointsGameResultComponent;
import life.plank.juna.zone.data.network.dagger.RegisterUserNetworkComponent;
import life.plank.juna.zone.data.network.dagger.SigninUserNetworkComponent;
import life.plank.juna.zone.data.network.dagger.SignupUserNetworkComponent;
import life.plank.juna.zone.data.network.dagger.SocialLoginNetworkComponent;
import life.plank.juna.zone.data.network.dagger.StandingsNetworkComponent;
import life.plank.juna.zone.data.network.dagger.SuddenDeathGameComponent;
import life.plank.juna.zone.data.network.dagger.SuddenDeathResultComponent;
import life.plank.juna.zone.data.network.dagger.TeamStatsNetworkComponent;
import life.plank.juna.zone.data.network.model.JunaUser;
import life.plank.juna.zone.data.network.module.RestServiceModule;
import life.plank.juna.zone.domain.module.GameServiceModule;

/**
 * Created by plank-sobia on 9/19/2017.
 */

public class ZoneApplication extends Application {

    public static Integer roundNumber = 0;
    public static Map<JunaUser, Boolean> pointsGameResultMap = new HashMap<>();
    public static Map<JunaUser, Boolean> suddenDeathGameResultMap = new HashMap<>();
    public static Integer suddenDeathLivesRemaining = 5;
    public static List<String> selectedTeamsList = new ArrayList<>();
    private static ZoneApplication zoneApplication;
    private NewsFeedsNetworkComponent newsFeedsNetworkComponent;
    private LoginUserNetworkComponent loginUserNetworkComponent;
    private RegisterUserNetworkComponent registerUserNetworkComponent;
    private CreateArenaNetworkComponent createArenaNetworkComponent;
    private PointsGameComponent pointsGameComponent;
    private MultipleUserJoinGameNetworkComponent multipleUserJoinGameNetworkComponent;
    private SocialLoginNetworkComponent socialLoginNetworkComponent;
    private SuddenDeathGameComponent suddenDeathGameComponent;
    private FootballMatchNetworkComponent footballMatchNetworkComponent;
    private PointsGameResultComponent pointsGameResultComponent;
    private JoinGameNetworkComponent joinGameNetworkComponent;
    private SuddenDeathResultComponent suddenDeathResultComponent;
    private FootballFeedNetworkComponent footballFeedNetworkComponent;
    private OnBoardSocialLoginNetworkComponent onBoardSocialLoginNetworkComponent;
    private StandingsNetworkComponent standingsNetworkComponent;
    private PlayerStatsNetworkComponent playerStatsNetworkComponent;
    private TeamStatsNetworkComponent teamStatsNetworkComponent;
    private LineupNetworkComponent lineupNetworkComponent;
    private SignupUserNetworkComponent signupUserNetworkComponent;
    private FixtureAndResultNetworkComponent fixtureAndResultNetworkComponent;
    private SigninUserNetworkComponent signinUserNetworkComponent;
    private LikeFeedNetworkComponent likeFeedNetworkComponent;

    public static ZoneApplication getApplication() {
        return zoneApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        zoneApplication = this;
        newsFeedsNetworkComponent = DaggerNewsFeedsNetworkComponent.builder()
                .restServiceModule( new RestServiceModule() )
                .build();

        loginUserNetworkComponent = DaggerLoginUserNetworkComponent.builder()
                .restServiceModule( new RestServiceModule() )
                .build();

        registerUserNetworkComponent = DaggerRegisterUserNetworkComponent.builder()
                .restServiceModule( new RestServiceModule() )
                .build();

        createArenaNetworkComponent = DaggerCreateArenaNetworkComponent.builder()
                .restServiceModule( new RestServiceModule() )
                .build();

        pointsGameComponent = DaggerPointsGameComponent.builder()
                .restServiceModule( new RestServiceModule() )
                .gameServiceModule( new GameServiceModule() )
                .build();

        multipleUserJoinGameNetworkComponent = DaggerMultipleUserJoinGameNetworkComponent.builder()
                .restServiceModule( new RestServiceModule() )
                .build();

        socialLoginNetworkComponent = DaggerSocialLoginNetworkComponent.builder()
                .restServiceModule( new RestServiceModule() )
                .build();

        suddenDeathGameComponent = DaggerSuddenDeathGameComponent.builder()
                .restServiceModule( new RestServiceModule() )
                .gameServiceModule( new GameServiceModule() )
                .build();

        footballMatchNetworkComponent = DaggerFootballMatchNetworkComponent.builder()
                .restServiceModule( new RestServiceModule() )
                .build();

        pointsGameResultComponent = DaggerPointsGameResultComponent.builder()
                .restServiceModule( new RestServiceModule() )
                .build();

        joinGameNetworkComponent = DaggerJoinGameNetworkComponent.builder()
                .restServiceModule( new RestServiceModule() )
                .build();

        suddenDeathResultComponent = DaggerSuddenDeathResultComponent.builder()
                .restServiceModule( new RestServiceModule() )
                .build();

        footballFeedNetworkComponent = DaggerFootballFeedNetworkComponent.builder()
                .restServiceModule( new RestServiceModule() )
                .build();

        onBoardSocialLoginNetworkComponent = DaggerOnBoardSocialLoginNetworkComponent.builder()
                .restServiceModule( new RestServiceModule() )
                .build();


        standingsNetworkComponent = DaggerStandingsNetworkComponent.builder()
                .restServiceModule( new RestServiceModule() ).build();

        playerStatsNetworkComponent = DaggerPlayerStatsNetworkComponent.builder()
                .restServiceModule( new RestServiceModule() ).build();

        teamStatsNetworkComponent = DaggerTeamStatsNetworkComponent.builder()
                .restServiceModule( new RestServiceModule() ).build();

        lineupNetworkComponent = DaggerLineupNetworkComponent.builder()
                .restServiceModule( new RestServiceModule() ).build();

        signupUserNetworkComponent = DaggerSignupUserNetworkComponent.builder()
                .restServiceModule( new RestServiceModule() ).build();

        fixtureAndResultNetworkComponent = DaggerFixtureAndResultNetworkComponent.builder()
                .restServiceModule( new RestServiceModule() ).build();

        signinUserNetworkComponent = DaggerSigninUserNetworkComponent.builder()
                .restServiceModule( new RestServiceModule() ).build();
        likeFeedNetworkComponent = DaggerLikeFeedNetworkComponent.builder()
                .restServiceModule( new RestServiceModule() ).build();
    }

    public NewsFeedsNetworkComponent getNewsFeedsNetworkComponent() {
        return newsFeedsNetworkComponent;
    }

    public LoginUserNetworkComponent getLoginNetworkComponent() {
        return loginUserNetworkComponent;
    }

    public RegisterUserNetworkComponent getRegisterNetworkComponent() {
        return registerUserNetworkComponent;
    }

    public CreateArenaNetworkComponent getCreateArenaNetworkComponent() {
        return createArenaNetworkComponent;
    }

    public FootballMatchNetworkComponent getfootballMatchNetworkComponent() {
        return footballMatchNetworkComponent;
    }

    public PointsGameComponent getPointsGameComponent() {
        return pointsGameComponent;
    }

    public MultipleUserJoinGameNetworkComponent getMultipleUserJoinGameNetworkComponent() {
        return multipleUserJoinGameNetworkComponent;
    }

    public SocialLoginNetworkComponent getSocialLoginNetworkComponent() {
        return socialLoginNetworkComponent;
    }

    public SuddenDeathGameComponent getSuddenDeathGameComponent() {
        return suddenDeathGameComponent;
    }

    public PointsGameResultComponent getPointsGameResultComponent() {
        return pointsGameResultComponent;
    }

    public JoinGameNetworkComponent getJoinGameNetworkComponent() {
        return joinGameNetworkComponent;
    }

    public SuddenDeathResultComponent getSuddenDeathResultComponent() {
        return suddenDeathResultComponent;
    }

    public FootballFeedNetworkComponent getFootballFeedNetworkComponent() {
        return footballFeedNetworkComponent;
    }

    public OnBoardSocialLoginNetworkComponent getOnBoardSocialLoginNetworkComponent() {
        return onBoardSocialLoginNetworkComponent;
    }

    public StandingsNetworkComponent getStandingNetworkComponent() {
        return standingsNetworkComponent;

    }

    public PlayerStatsNetworkComponent getPlayerStatsNetworkComponent() {
        return playerStatsNetworkComponent;
    }

    public TeamStatsNetworkComponent getTeamStatsNetworkComponent() {
        return teamStatsNetworkComponent;
    }

    public LineupNetworkComponent getLineupNetworkComponent() {
        return lineupNetworkComponent;
    }

    public SignupUserNetworkComponent getSignupUserNetworkComponent() {
        return signupUserNetworkComponent;
    }

    public FixtureAndResultNetworkComponent getFixtureAndResultNetworkComponent() {
        return fixtureAndResultNetworkComponent;
    }

    public SigninUserNetworkComponent getSigninUserNetworkComponent() {
        return signinUserNetworkComponent;
    }

    public LikeFeedNetworkComponent getLikeFeedNetworkComponent() {
        return likeFeedNetworkComponent;
    }
}
