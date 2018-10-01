package life.plank.juna.zone.data.network.dagger.component;

import dagger.Subcomponent;
import life.plank.juna.zone.data.network.dagger.module.AdaptersModule;
import life.plank.juna.zone.data.network.dagger.module.UiModule;
import life.plank.juna.zone.data.network.dagger.scope.UiScope;
import life.plank.juna.zone.view.activity.BoardPreviewActivity;
import life.plank.juna.zone.view.activity.CameraActivity;
import life.plank.juna.zone.view.activity.CommentaryActivity;
import life.plank.juna.zone.view.activity.CreateBoardActivity;
import life.plank.juna.zone.view.activity.EditProfileActivity;
import life.plank.juna.zone.view.activity.FixtureActivity;
import life.plank.juna.zone.view.activity.InviteToBoardActivity;
import life.plank.juna.zone.view.activity.JoinBoardActivity;
import life.plank.juna.zone.view.activity.LeagueInfoActivity;
import life.plank.juna.zone.view.activity.LeagueInfoDetailActivity;
import life.plank.juna.zone.view.activity.MatchBoardActivity;
import life.plank.juna.zone.view.activity.PostCommentActivity;
import life.plank.juna.zone.view.activity.PrivateBoardActivity;
import life.plank.juna.zone.view.activity.SignInActivity;
import life.plank.juna.zone.view.activity.SignUpActivity;
import life.plank.juna.zone.view.activity.SplashScreenActivity;
import life.plank.juna.zone.view.activity.SwipePageActivity;
import life.plank.juna.zone.view.activity.TimelineActivity;
import life.plank.juna.zone.view.activity.TokenActivity;
import life.plank.juna.zone.view.activity.UserFeedActivity;
import life.plank.juna.zone.view.activity.UserProfileActivity;
import life.plank.juna.zone.view.activity.ZoneActivity;
import life.plank.juna.zone.view.adapter.BoardFeedDetailAdapter;
import life.plank.juna.zone.view.adapter.UserBoardsAdapter;
import life.plank.juna.zone.view.fragment.board.fixture.BoardInfoFragment;
import life.plank.juna.zone.view.fragment.board.fixture.BoardTilesFragment;
import life.plank.juna.zone.view.fragment.board.user.PrivateBoardInfoFragment;

/**
 * This Component is the sub-component of AppComponent.
 * Use this component to get the dependencies pertaining to views/UI.
 */
@UiScope
@Subcomponent(modules = {UiModule.class, AdaptersModule.class})
public interface UiComponent {

    void inject(TokenActivity tokenActivity);

    void inject(MatchBoardActivity matchBoardActivity);

    void inject(BoardFeedDetailAdapter boardFeedDetailAdapter);

    void inject(CreateBoardActivity createBoardActivity);

    void inject(FixtureActivity fixtureActivity);

    void inject(BoardPreviewActivity boardPreviewActivity);

    void inject(SwipePageActivity swipePageActivity);

    void inject(CameraActivity cameraActivity);

    void inject(LeagueInfoActivity leagueInfoActivity);

    void inject(PostCommentActivity postCommentActivity);

    void inject(SignInActivity signInActivity);

    void inject(SignUpActivity signUpActivity);

    void inject(UserProfileActivity userProfileActivity);

    void inject(LeagueInfoDetailActivity leagueInfoDetailActivity);

    void inject(UserFeedActivity userFeedActivity);

    void inject(BoardTilesFragment boardTilesFragment);

    void inject(BoardInfoFragment boardInfoFragment);

    void inject(TimelineActivity timelineActivity);

    void inject(PrivateBoardActivity privateBoardActivity);

    void inject(CommentaryActivity commentaryActivity);

    void inject(InviteToBoardActivity inviteToBoardActivity);

    void inject(PrivateBoardInfoFragment privateBoardInfoFragment);

    void inject(SplashScreenActivity splashScreenActivity);

    void inject(UserBoardsAdapter userBoardsAdapter);

    void inject(EditProfileActivity editProfileActivity);

    void inject(JoinBoardActivity joinBoardActivity);

    void inject(ZoneActivity zoneActivity);

    @Subcomponent.Builder
    interface Builder {
        UiComponent build();
    }
}