package life.plank.juna.zone.data.network.dagger.component;

import dagger.Subcomponent;
import life.plank.juna.zone.data.network.dagger.module.AdaptersModule;
import life.plank.juna.zone.data.network.dagger.module.UiModule;
import life.plank.juna.zone.data.network.dagger.scope.UiScope;
import life.plank.juna.zone.view.activity.BoardActivity;
import life.plank.juna.zone.view.activity.CameraActivity;
import life.plank.juna.zone.view.activity.CreateBoardActivity;
import life.plank.juna.zone.view.activity.FixtureAndResultActivity;
import life.plank.juna.zone.view.activity.LineupActivity;
import life.plank.juna.zone.view.activity.MatchResultActivity;
import life.plank.juna.zone.view.activity.MatchResultDetailActivity;
import life.plank.juna.zone.view.activity.PostCommentActivity;
import life.plank.juna.zone.view.activity.SignInActivity;
import life.plank.juna.zone.view.activity.SignUpActivity;
import life.plank.juna.zone.view.activity.SwipePageActivity;
import life.plank.juna.zone.view.activity.TokenActivity;
import life.plank.juna.zone.view.activity.UserProfileActivity;
import life.plank.juna.zone.view.adapter.BoardFeedDetailAdapter;
import life.plank.juna.zone.view.adapter.FootballFeedDetailAdapter;

/**
 * This Component is the sub-component of AppComponent.
 * Use this component to get the dependencies pertaining to views/UI.
 */
@UiScope
@Subcomponent(modules = {UiModule.class, AdaptersModule.class})
public interface UiComponent {

    void inject(TokenActivity tokenActivity);

    void inject(BoardActivity boardActivity);

    void inject(BoardFeedDetailAdapter boardFeedDetailAdapter);

    void inject(CreateBoardActivity createBoardActivity);

    void inject(FixtureAndResultActivity fixtureAndResultActivity);

    void inject(SwipePageActivity swipePageActivity);

    void inject(CameraActivity cameraActivity);

    void inject(FootballFeedDetailAdapter footballFeedDetailAdapter);

    void inject(LineupActivity lineupActivity);

    void inject(MatchResultActivity matchResultActivity);

    void inject(PostCommentActivity postCommentActivity);

    void inject(SignInActivity signInActivity);

    void inject(SignUpActivity signUpActivity);

    void inject(UserProfileActivity userProfileActivity);

    void inject(MatchResultDetailActivity matchResultDetailActivity);

    @Subcomponent.Builder
    interface Builder {
        UiComponent build();
    }
}