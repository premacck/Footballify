package life.plank.juna.zone.injection.component;

import dagger.Subcomponent;
import life.plank.juna.zone.injection.module.AdaptersModule;
import life.plank.juna.zone.injection.module.UiModule;
import life.plank.juna.zone.injection.scope.UiScope;
import life.plank.juna.zone.view.SplashScreenActivity;
import life.plank.juna.zone.view.auth.SignInActivity;
import life.plank.juna.zone.view.auth.SignUpActivity;
import life.plank.juna.zone.view.auth.TokenActivity;
import life.plank.juna.zone.view.board.CreateBoardActivity;
import life.plank.juna.zone.view.board.fragment.fixture.BoardTilesFragment;
import life.plank.juna.zone.view.board.fragment.fixture.CommentaryPopup;
import life.plank.juna.zone.view.board.fragment.fixture.LineupFragment;
import life.plank.juna.zone.view.board.fragment.fixture.MatchBoardFragment;
import life.plank.juna.zone.view.board.fragment.fixture.MatchMediaFragment;
import life.plank.juna.zone.view.board.fragment.fixture.MatchStatsFragment;
import life.plank.juna.zone.view.board.fragment.fixture.TimelinePopup;
import life.plank.juna.zone.view.board.fragment.fixture.extra.DartBoardPopup;
import life.plank.juna.zone.view.board.fragment.fixture.extra.KeyBoardPopup;
import life.plank.juna.zone.view.board.fragment.user.BoardPreviewPopup;
import life.plank.juna.zone.view.board.fragment.user.JoinBoardPopup;
import life.plank.juna.zone.view.board.fragment.user.PrivateBoardFragment;
import life.plank.juna.zone.view.board.fragment.user.PrivateBoardInfoFragment;
import life.plank.juna.zone.view.camera.UploadActivity;
import life.plank.juna.zone.view.feed.FeedItemPeekPopup;
import life.plank.juna.zone.view.feed.PostCommentActivity;
import life.plank.juna.zone.view.feed.PostDetailFragment;
import life.plank.juna.zone.view.football.fragment.FixtureFragment;
import life.plank.juna.zone.view.football.fragment.LeagueInfoDetailPopup;
import life.plank.juna.zone.view.football.fragment.LeagueStatsFragment;
import life.plank.juna.zone.view.football.fragment.StandingsFragment;
import life.plank.juna.zone.view.forum.ForumFragment;
import life.plank.juna.zone.view.home.HomeActivity;
import life.plank.juna.zone.view.home.HomeFragment;
import life.plank.juna.zone.view.notification.UserNotificationActivity;
import life.plank.juna.zone.view.onboarding.SearchUserPopup;
import life.plank.juna.zone.view.onboarding.TeamSelectionFragment;
import life.plank.juna.zone.view.user.card.CardWalletActivity;
import life.plank.juna.zone.view.user.card.CreateCardActivity;
import life.plank.juna.zone.view.user.profile.EditProfilePopup;
import life.plank.juna.zone.view.user.profile.ProfileCardFragment;
import life.plank.juna.zone.view.user.profile.UserProfileActivity;
import life.plank.juna.zone.view.user.share.ShareLinkPopup;
import life.plank.juna.zone.view.zone.SelectZoneActivity;
import life.plank.juna.zone.view.zone.ZoneActivity;
import life.plank.juna.zone.view.zone.ZoneFragment;

/**
 * This Component is the sub-component of AppComponent.
 * Use this component to get the dependencies pertaining to views/UI.
 */
@UiScope
@Subcomponent(modules = {UiModule.class, AdaptersModule.class})
public interface UiComponent {

    void inject(TokenActivity tokenActivity);

    void inject(CreateBoardActivity createBoardActivity);

    void inject(BoardPreviewPopup boardPreviewPopup);

    void inject(UploadActivity uploadActivity);

    void inject(PostCommentActivity postCommentActivity);

    void inject(SignInActivity signInActivity);

    void inject(SignUpActivity signUpActivity);

    void inject(UserProfileActivity userProfileActivity);

    void inject(LeagueInfoDetailPopup leagueInfoDetailPopup);

    void inject(BoardTilesFragment boardTilesFragment);

    void inject(TimelinePopup timelinePopup);

    void inject(CommentaryPopup commentaryPopup);

    void inject(PrivateBoardInfoFragment privateBoardInfoFragment);

    void inject(SplashScreenActivity splashScreenActivity);

    void inject(EditProfilePopup editProfilePopup);

    void inject(JoinBoardPopup joinBoardPopup);

    void inject(SelectZoneActivity selectZoneActivity);

    void inject(PostDetailFragment postDetailFragment);

    void inject(UserNotificationActivity userNotificationActivity);

    void inject(HomeFragment homeFragment);

    void inject(MatchBoardFragment matchBoardFragment);

    void inject(PrivateBoardFragment privateBoardFragment);

    void inject(ZoneFragment zoneFragment);

    void inject(ForumFragment forumFragment);

    void inject(FeedItemPeekPopup feedItemPeekPopup);

    void inject(FixtureFragment fixtureFragment);

    void inject(LineupFragment lineupFragment);

    void inject(MatchStatsFragment matchStatsFragment);

    void inject(HomeActivity homeActivity);

    void inject(ZoneActivity zoneActivity);

    void inject(LeagueStatsFragment leagueStatsFragment);

    void inject(StandingsFragment standingsFragment);

    void inject(MatchMediaFragment matchMediaFragment);

    void inject(KeyBoardPopup keyBoardPopup);

    void inject(DartBoardPopup dartBoardPopup);

    void inject(TeamSelectionFragment teamSelectionFragment);

    void inject(SearchUserPopup searchUserPopup);

    void inject(ShareLinkPopup shareLinkPopup);

    void inject(CreateCardActivity createCardActivity);

    void inject(CardWalletActivity cardWalletActivity);

    void inject(ProfileCardFragment profileCardFragment);

    @Subcomponent.Builder
    interface Builder {
        UiComponent build();
    }
}