package life.plank.juna.zone.data.network.dagger.component;

import dagger.Subcomponent;
import life.plank.juna.zone.data.network.dagger.module.AdaptersModule;
import life.plank.juna.zone.data.network.dagger.module.UiModule;
import life.plank.juna.zone.data.network.dagger.scope.UiScope;
import life.plank.juna.zone.view.activity.InviteToBoardActivity;
import life.plank.juna.zone.view.activity.JoinBoardActivity;
import life.plank.juna.zone.view.activity.PostCommentActivity;
import life.plank.juna.zone.view.activity.PrivateBoardActivity;
import life.plank.juna.zone.view.activity.SelectZoneActivity;
import life.plank.juna.zone.view.activity.SignInActivity;
import life.plank.juna.zone.view.activity.SignUpActivity;
import life.plank.juna.zone.view.activity.SplashScreenActivity;
import life.plank.juna.zone.view.activity.TokenActivity;
import life.plank.juna.zone.view.activity.UserNotificationActivity;
import life.plank.juna.zone.view.activity.board.CreateBoardActivity;
import life.plank.juna.zone.view.activity.camera.UploadActivity;
import life.plank.juna.zone.view.activity.home.HomeActivity;
import life.plank.juna.zone.view.activity.profile.UserProfileActivity;
import life.plank.juna.zone.view.adapter.BoardFeedDetailAdapter;
import life.plank.juna.zone.view.adapter.EmojiAdapter;
import life.plank.juna.zone.view.fragment.board.fixture.BoardInfoFragment;
import life.plank.juna.zone.view.fragment.board.fixture.BoardTilesFragment;
import life.plank.juna.zone.view.fragment.board.fixture.CommentaryPopup;
import life.plank.juna.zone.view.fragment.board.fixture.MatchBoardFragment;
import life.plank.juna.zone.view.fragment.board.fixture.MatchInfoFragment;
import life.plank.juna.zone.view.fragment.board.fixture.TimelinePopup;
import life.plank.juna.zone.view.fragment.board.user.BoardPreviewPopup;
import life.plank.juna.zone.view.fragment.board.user.PrivateBoardFragment;
import life.plank.juna.zone.view.fragment.board.user.PrivateBoardInfoFragment;
import life.plank.juna.zone.view.fragment.clickthrough.FeedItemPeekPopup;
import life.plank.juna.zone.view.fragment.football.FixtureFragment;
import life.plank.juna.zone.view.fragment.football.LeagueInfoDetailPopup;
import life.plank.juna.zone.view.fragment.football.LeagueInfoFragment;
import life.plank.juna.zone.view.fragment.forum.ForumFragment;
import life.plank.juna.zone.view.fragment.home.HomeFragment;
import life.plank.juna.zone.view.fragment.post.PostDetailFragment;
import life.plank.juna.zone.view.fragment.profile.EditProfilePopup;
import life.plank.juna.zone.view.fragment.zone.ZoneFragment;

/**
 * This Component is the sub-component of AppComponent.
 * Use this component to get the dependencies pertaining to views/UI.
 */
@UiScope
@Subcomponent(modules = {UiModule.class, AdaptersModule.class})
public interface UiComponent {

    void inject(TokenActivity tokenActivity);

    void inject(BoardFeedDetailAdapter boardFeedDetailAdapter);

    void inject(CreateBoardActivity createBoardActivity);

    void inject(BoardPreviewPopup boardPreviewPopup);

    void inject(UploadActivity uploadActivity);

    void inject(PostCommentActivity postCommentActivity);

    void inject(SignInActivity signInActivity);

    void inject(SignUpActivity signUpActivity);

    void inject(UserProfileActivity userProfileActivity);

    void inject(LeagueInfoDetailPopup leagueInfoDetailPopup);

    void inject(BoardTilesFragment boardTilesFragment);

    void inject(BoardInfoFragment boardInfoFragment);

    void inject(TimelinePopup timelinePopup);

    void inject(PrivateBoardActivity privateBoardActivity);

    void inject(CommentaryPopup commentaryPopup);

    void inject(InviteToBoardActivity inviteToBoardActivity);

    void inject(PrivateBoardInfoFragment privateBoardInfoFragment);

    void inject(SplashScreenActivity splashScreenActivity);

    void inject(EditProfilePopup editProfilePopup);

    void inject(JoinBoardActivity joinBoardActivity);

    void inject(SelectZoneActivity selectZoneActivity);

    void inject(PostDetailFragment postDetailFragment);

    void inject(UserNotificationActivity userNotificationActivity);

    void inject(EmojiAdapter emojiAdapter);

    void inject(HomeFragment homeFragment);

    void inject(MatchBoardFragment matchBoardFragment);

    void inject(PrivateBoardFragment privateBoardFragment);

    void inject(ZoneFragment zoneFragment);

    void inject(LeagueInfoFragment leagueInfoFragment);

    void inject(ForumFragment forumFragment);

    void inject(FeedItemPeekPopup feedItemPeekPopup);

    void inject(FixtureFragment fixtureFragment);

    void inject(MatchInfoFragment matchInfoFragment);

    void inject(HomeActivity homeActivity);

    @Subcomponent.Builder
    interface Builder {
        UiComponent build();
    }
}