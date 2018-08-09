package life.plank.juna.zone;

import android.app.Application;
import android.content.Context;

import life.plank.juna.zone.data.network.dagger.AzureNetworkComponent;
import life.plank.juna.zone.data.network.dagger.BoardFeedNetworkComponent;
import life.plank.juna.zone.data.network.dagger.BoardItemLikeNetworkComponent;
import life.plank.juna.zone.data.network.dagger.CreatePrivateBoardNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerAzureNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerBoardFeedNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerBoardItemLikeNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerCreatePrivateBoardNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerFixtureAndResultNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerFootballFeedNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerImageUploaderNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerLikeFeedNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerLineupNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerPostCommentFeedNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerSignInUserNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerSignUpUserNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerStandingsNetworkComponent;
import life.plank.juna.zone.data.network.dagger.DaggerViewComponent;
import life.plank.juna.zone.data.network.dagger.FixtureAndResultNetworkComponent;
import life.plank.juna.zone.data.network.dagger.FootballFeedNetworkComponent;
import life.plank.juna.zone.data.network.dagger.ImageUploaderNetworkComponent;
import life.plank.juna.zone.data.network.dagger.LikeFeedNetworkComponent;
import life.plank.juna.zone.data.network.dagger.LineupNetworkComponent;
import life.plank.juna.zone.data.network.dagger.PostCommentFeedNetworkComponent;
import life.plank.juna.zone.data.network.dagger.SignInUserNetworkComponent;
import life.plank.juna.zone.data.network.dagger.SignUpUserNetworkComponent;
import life.plank.juna.zone.data.network.dagger.StandingsNetworkComponent;
import life.plank.juna.zone.data.network.dagger.ViewComponent;
import life.plank.juna.zone.data.network.module.ContextModule;

/**
 * Created by plank-sobia on 9/19/2017.
 */

public class ZoneApplication extends Application {

    private static ZoneApplication zoneApplication;
    private FootballFeedNetworkComponent footballFeedNetworkComponent;
    private StandingsNetworkComponent standingsNetworkComponent;
    private LineupNetworkComponent lineupNetworkComponent;
    private SignUpUserNetworkComponent signUpUserNetworkComponent;
    private FixtureAndResultNetworkComponent fixtureAndResultNetworkComponent;
    private SignInUserNetworkComponent signInUserNetworkComponent;
    private LikeFeedNetworkComponent likeFeedNetworkComponent;
    private ImageUploaderNetworkComponent imageUploaderNetworkComponent;
    private BoardFeedNetworkComponent boardFeedNetworkComponent;
    private BoardItemLikeNetworkComponent boardItemLikeNetworkComponent;
    private PostCommentFeedNetworkComponent postCommentFeedNetworkComponent;
    private AzureNetworkComponent azureNetworkComponent;
    private CreatePrivateBoardNetworkComponent createPrivateBoardNetworkComponent;
    private ViewComponent viewComponent;

    public static ZoneApplication getApplication() {
        return zoneApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override public void onCreate() {
        super.onCreate();
        zoneApplication = this;

        footballFeedNetworkComponent = DaggerFootballFeedNetworkComponent.builder().build();

        standingsNetworkComponent = DaggerStandingsNetworkComponent.builder().build();

        lineupNetworkComponent = DaggerLineupNetworkComponent.builder().build();

        signUpUserNetworkComponent = DaggerSignUpUserNetworkComponent.builder().build();

        fixtureAndResultNetworkComponent = DaggerFixtureAndResultNetworkComponent.builder().build();

        signInUserNetworkComponent = DaggerSignInUserNetworkComponent.builder().build();

        signInUserNetworkComponent = DaggerSignInUserNetworkComponent.builder().build();

        azureNetworkComponent = DaggerAzureNetworkComponent.builder().build();

        likeFeedNetworkComponent = DaggerLikeFeedNetworkComponent.builder().build();

        imageUploaderNetworkComponent = DaggerImageUploaderNetworkComponent.builder().build();

        boardFeedNetworkComponent = DaggerBoardFeedNetworkComponent.builder().build();

        boardItemLikeNetworkComponent = DaggerBoardItemLikeNetworkComponent.builder().build();

        postCommentFeedNetworkComponent = DaggerPostCommentFeedNetworkComponent.builder().build();

        createPrivateBoardNetworkComponent = DaggerCreatePrivateBoardNetworkComponent.builder().build();

        viewComponent = DaggerViewComponent.builder().contextModule(new ContextModule(getApplicationContext())).build();
    }

    public FootballFeedNetworkComponent getFootballFeedNetworkComponent() {
        return footballFeedNetworkComponent;
    }

    public StandingsNetworkComponent getStandingNetworkComponent() {
        return standingsNetworkComponent;

    }

    public LineupNetworkComponent getLineupNetworkComponent() {
        return lineupNetworkComponent;
    }

    public SignUpUserNetworkComponent getSignUpUserNetworkComponent() {
        return signUpUserNetworkComponent;
    }

    public FixtureAndResultNetworkComponent getFixtureAndResultNetworkComponent() {
        return fixtureAndResultNetworkComponent;
    }

    public SignInUserNetworkComponent getSignInUserNetworkComponent() {
        return signInUserNetworkComponent;
    }

    public AzureNetworkComponent getAzureNetworkComponent() {
        return azureNetworkComponent;
    }

    public LikeFeedNetworkComponent getLikeFeedNetworkComponent() {
        return likeFeedNetworkComponent;
    }

    public ImageUploaderNetworkComponent getImageUploaderNetworkComponent() {
        return imageUploaderNetworkComponent;
    }

    public BoardFeedNetworkComponent getBoardFeedNetworkComponent() {
        return boardFeedNetworkComponent;
    }

    public BoardItemLikeNetworkComponent getBoardItemLikeNetworkComponent() {
        return boardItemLikeNetworkComponent;
    }

    public PostCommentFeedNetworkComponent getPostCommentFeedNetworkComponent() {
        return postCommentFeedNetworkComponent;
    }

    public CreatePrivateBoardNetworkComponent getCreatePrivateBoardNetworkComponent() {
        return createPrivateBoardNetworkComponent;
    }

    public ViewComponent getViewComponent() {
        return viewComponent;
    }
}