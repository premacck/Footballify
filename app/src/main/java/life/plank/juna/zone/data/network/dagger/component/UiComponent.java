package life.plank.juna.zone.data.network.dagger.component;

import dagger.Subcomponent;
import life.plank.juna.zone.data.network.dagger.module.AdaptersModule;
import life.plank.juna.zone.data.network.dagger.module.UiModule;
import life.plank.juna.zone.data.network.dagger.scope.UiScope;
import life.plank.juna.zone.view.activity.PostCommentActivity;
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
import life.plank.juna.zone.view.activity.zone.ZoneActivity;
import life.plank.juna.zone.view.adapter.common.BoardFeedDetailAdapter;
import life.plank.juna.zone.view.fragment.board.fixture.BoardTilesFragment;
import life.plank.juna.zone.view.fragment.board.fixture.CommentaryPopup;
import life.plank.juna.zone.view.fragment.board.fixture.LineupFragment;
import life.plank.juna.zone.view.fragment.board.fixture.MatchBoardFragment;
import life.plank.juna.zone.view.fragment.board.fixture.MatchMediaFragment;
import life.plank.juna.zone.view.fragment.board.fixture.MatchStatsFragment;
import life.plank.juna.zone.view.fragment.board.fixture.TimelinePopup;
import life.plank.juna.zone.view.fragment.board.fixture.extra.DartBoardPopup;
import life.plank.juna.zone.view.fragment.board.fixture.extra.KeyBoardPopup;
import life.plank.juna.zone.view.fragment.board.user.BoardPreviewPopup;
import life.plank.juna.zone.view.fragment.board.user.JoinBoardPopup;
import life.plank.juna.zone.view.fragment.board.user.PrivateBoardFragment;
import life.plank.juna.zone.view.fragment.board.user.PrivateBoardInfoFragment;
import life.plank.juna.zone.view.fragment.clickthrough.FeedItemPeekPopup;
import life.plank.juna.zone.view.fragment.football.FixtureFragment;
import life.plank.juna.zone.view.fragment.football.LeagueInfoDetailPopup;
import life.plank.juna.zone.view.fragment.football.LeagueStatsFragment;
import life.plank.juna.zone.view.fragment.football.StandingsFragment;
import life.plank.juna.zone.view.fragment.forum.ForumFragment;
import life.plank.juna.zone.view.fragment.home.HomeFragment;
import life.plank.juna.zone.view.fragment.onboarding.SearchUserPopup;
import life.plank.juna.zone.view.fragment.onboarding.TeamSelectionFragment;
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

    @Subcomponent.Builder
    interface Builder {
        UiComponent build();
    }
}