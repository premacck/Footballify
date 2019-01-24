package life.plank.juna.zone.data.network.interfaces;

import com.google.gson.JsonObject;

import java.util.Date;
import java.util.List;
import java.util.Set;

import life.plank.juna.zone.data.model.Board;
import life.plank.juna.zone.data.model.Emoji;
import life.plank.juna.zone.data.model.FeedEntry;
import life.plank.juna.zone.data.model.FeedItem;
import life.plank.juna.zone.data.model.FeedItemComment;
import life.plank.juna.zone.data.model.FootballTeam;
import life.plank.juna.zone.data.model.Lineups;
import life.plank.juna.zone.data.model.MatchDetails;
import life.plank.juna.zone.data.model.MatchFixture;
import life.plank.juna.zone.data.model.MatchStats;
import life.plank.juna.zone.data.model.NextMatch;
import life.plank.juna.zone.data.model.PlayerStats;
import life.plank.juna.zone.data.model.ScrubberData;
import life.plank.juna.zone.data.model.SignUpModel;
import life.plank.juna.zone.data.model.Standings;
import life.plank.juna.zone.data.model.TeamStats;
import life.plank.juna.zone.data.model.User;
import life.plank.juna.zone.data.model.Zones;
import life.plank.juna.zone.data.model.card.JunaCard;
import life.plank.juna.zone.data.model.notification.CardNotification;
import life.plank.juna.zone.data.model.notification.SocialNotification;
import life.plank.juna.zone.data.model.poll.Poll;
import life.plank.juna.zone.data.model.poll.PollAnswerRequest;
import life.plank.juna.zone.data.model.poll.PollAnswerResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by plank-dhamini on 6/8/2017.
 * This class consists of all the rest api calls for zone
 */

public interface RestApi {

    String FOOTBALL_SUFFIX = "football";
    String ZONE_BACKEND_SUFFIX = "backend";
    String CARD_SUFFIX = "card";
    String FEED_FLOWS_SUFFIX = "feedflows";

    //working
    @GET(FOOTBALL_SUFFIX + "/seasons/standings")
    Observable<Response<List<Standings>>> getStandings(@Query("leagueName") String leagueName,
                                                       @Query("seasonName") String seasonName,
                                                       @Query("countryName") String countryName);

    //working
    @GET(FOOTBALL_SUFFIX + "/seasons/playerstats")
    Observable<Response<List<PlayerStats>>> getPlayerStats(@Query("leagueName") String leagueName,
                                                           @Query("seasonName") String seasonName,
                                                           @Query("countryName") String countryName);

    //working
    @GET(FOOTBALL_SUFFIX + "/teams/stats")
    Observable<Response<List<TeamStats>>> getTeamStats(@Query("leagueName") String leagueName,
                                                       @Query("seasonName") String seasonName,
                                                       @Query("countryName") String countryName);

    //working
    @GET(ZONE_BACKEND_SUFFIX + "/boards")
    Observable<Response<Board>> getBoard(@Query("foreignId") Long matchId, @Query("boardType") String boardType, @Header("Authorization") String authHeader);

    //working
    @GET(ZONE_BACKEND_SUFFIX + "/boards/createdBy")
    Observable<Response<List<Board>>> getUserBoards(@Header("Authorization") String authHeader);

    //working
    @POST(ZONE_BACKEND_SUFFIX + "/boards/{id}/invite")
    Observable<Response<JsonObject>> inviteUserToJoinBoard(@Body Set<User> user, @Path("id") String boardId, @Header("Authorization") String authHeader);

    //working
    @GET(ZONE_BACKEND_SUFFIX + "/boards/feedItems")
    Observable<Response<List<FeedEntry>>> getBoardFeedItems(@Query("id") String boardId, @Header("Authorization") String authHeader);

    //working
    @GET(ZONE_BACKEND_SUFFIX + "/zones")
    Observable<Response<List<Zones>>> retrieveZones();

    //working
    @DELETE(ZONE_BACKEND_SUFFIX + "/boards/{id}/activities/removeUser")
    Observable<Response<JsonObject>> deleteUserFromPrivateBoard(@Path("id") String boardId,
                                                                @Query("boardUserId") String userId,
                                                                @Header("Authorization") String authHeader);

    //working
    @GET(ZONE_BACKEND_SUFFIX + "/users")
    Observable<Response<User>> getUser(@Header("Authorization") String authHeader);

    @POST(ZONE_BACKEND_SUFFIX + "/users/editUserDetails")
    Observable<Response<JsonObject>> updateUserDetails(@Header("Authorization") String authHeader,
                                                       @Body User user);

    //working
    @Multipart
    @POST(ZONE_BACKEND_SUFFIX + "/activities/upload")
    Observable<Response<JsonObject>> postMediaContentToServer(@Part MultipartBody.Part file,
                                                              @Query("targetId") String targetId,
                                                              @Query("contentType") String contentType,
                                                              @Query("userId") String userId,
                                                              @Query("dateCreated") String dateCreated,
                                                              @Query("feedType") String feedType,
                                                              @Query("title") String title,
                                                              @Header("Authorization") String authHeader);

    //working
    @POST(ZONE_BACKEND_SUFFIX + "/activities/feeditems")
    Observable<Response<JsonObject>> postFeedItemOnBoard(@Body FeedItem feedItem,
                                                         @Query("boardId") String boardId,
                                                         @Query("contentType") String contentType,
                                                         @Header("Authorization") String authHeader);

    //working
    @Multipart
    @POST(ZONE_BACKEND_SUFFIX + "/boards")
    Observable<Response<String>> createPrivateBoard(@Query("boardType") String boardType,
                                                    @Part("displayName") RequestBody displayName,
                                                    @Part("zone") RequestBody zone,
                                                    @Part("description") RequestBody description,
                                                    @Part("color") RequestBody color,
                                                    @Part MultipartBody.Part file,
                                                    @Header("Authorization") String authHeader);

    //working
    @Multipart
    @POST(ZONE_BACKEND_SUFFIX + "/users/profilePicture")
    Observable<Response<String>> uploadProfilePicture(@Part MultipartBody.Part file,
                                                      @Header("Authorization") String authHeader);

    //working
    @DELETE(ZONE_BACKEND_SUFFIX + "/boards/{id}")
    Observable<Response<JsonObject>> deleteBoard(@Path("id") String boardId,
                                                 @Header("Authorization") String authHeader);

    //working
    @GET(FOOTBALL_SUFFIX + "/seasons/matches")
    Observable<Response<List<MatchFixture>>> getFixtures(@Query("seasonName") String seasonName,
                                                         @Query("leagueName") String leagueName,
                                                         @Query("countryName") String countryName);

    //working
    @GET(ZONE_BACKEND_SUFFIX + "/feedEntries")
    Observable<Response<List<FeedEntry>>> getUserFeed(@Header("Authorization") String... authHeader);

    //working
    @GET(ZONE_BACKEND_SUFFIX + "/boards/{id}")
    Observable<Response<Board>> getBoardById(@Path("id") String boardId, @Header("Authorization") String authHeader);

    //working
    @GET(ZONE_BACKEND_SUFFIX + "/activities/{feedItemId}/emojiCounts")
    Observable<Response<List<Emoji>>> getTopFeedItemEmoji(@Path("feedItemId") String feedItemId, @Header("Authorization") String authHeader);

    //working
    @POST(ZONE_BACKEND_SUFFIX + "/activities/{feedItemId}/emojis")
    Observable<Response<JsonObject>> postEmojiOnFeedItem(@Path("feedItemId") String feedItemId,
                                                         @Query("boardId") String boardId,
                                                         @Query("reaction") Integer emojiUnicode,
                                                         @Query("time") String dateCreated,
                                                         @Header("Authorization") String authHeader);

    //working
    @GET(ZONE_BACKEND_SUFFIX + "/boards/{boardId}/emojiCounts")
    Observable<Response<List<Emoji>>> getTopBoardEmoji(@Path("boardId") String boardId, @Header("Authorization") String authHeader);

    //working
    @POST(ZONE_BACKEND_SUFFIX + "/boards/{boardId}/emojis")
    Observable<Response<JsonObject>> postEmojiOnBoard(@Path("boardId") String boardId,
                                                      @Query("reaction") Integer emojiUnicode,
                                                      @Query("time") String dateCreated,
                                                      @Header("Authorization") String authHeader);

    //working
    @GET(ZONE_BACKEND_SUFFIX + "/users/search")
    Observable<Response<List<User>>> getSearchedUsers(@Header("Authorization") String authHeader, @Query("q") String displayName);

    //working
    @GET(ZONE_BACKEND_SUFFIX + "/boards/{boardId}/members")
    Observable<Response<List<User>>> getBoardMembers(@Path("boardId") String boardId, @Header("Authorization") String authHeader);

    //working
    @POST(ZONE_BACKEND_SUFFIX + "/boards/{boardId}/activities/follow")
    Observable<Response<JsonObject>> followBoard(@Header("Authorization") String authHeader, @Path("boardId") String boardId);

    //working
    @POST(ZONE_BACKEND_SUFFIX + "/zones/follows")
    Observable<Response<JsonObject>> followZones(@Header("Authorization") String authHeader, @Body Zones zones);

    @GET(ZONE_BACKEND_SUFFIX + "/boards/following")
    Observable<Response<List<Board>>> getFollowingBoards(@Query("zone") String zone, @Header("Authorization") String authHeader);

    @GET(FOOTBALL_SUFFIX + "/matches/{matchId}/lineups")
    Observable<Response<Lineups>> getLineUpsData(@Path("matchId") long matchId);

    @POST(ZONE_BACKEND_SUFFIX + "/ausers")
    Observable<Response<SignUpModel>> createUser(@Body SignUpModel signUpModel);

    @GET(FOOTBALL_SUFFIX + "/matches/{matchId}/stats")
    Observable<Response<MatchStats>> getMatchStatsForMatch(@Path("matchId") long matchId);

    @GET(FOOTBALL_SUFFIX + "/matches/{matchId}")
    Observable<Response<MatchDetails>> getMatchDetails(@Path("matchId") long matchId);

    @GET(FOOTBALL_SUFFIX + "/matches/{matchId}/standings")
    Observable<Response<List<Standings>>> getMatchStandingsForMatch(@Path("matchId") long matchId);

    @GET(FOOTBALL_SUFFIX + "/matches/{matchId}/teamStats")
    Observable<Response<List<TeamStats>>> getTeamStatsForMatch(@Path("matchId") long matchId);

    @GET(FOOTBALL_SUFFIX + "/matches/{matchId}/scrubber/{hour}")
    Observable<Response<List<ScrubberData>>> getScrubberDetails(@Path("matchId") long matchId, @Path("hour") Date currentMatchTime);

    @POST(ZONE_BACKEND_SUFFIX + "/activities/{id}/pins")
    Observable<Response<String>> pinFeedItem(@Path("id") String feedItemId,
                                             @Query("target") String target,
                                             @Query("targetId") String boardId,
                                             @Query("time") String dateCreated,
                                             @Header("Authorization") String authHeader);

    @DELETE(ZONE_BACKEND_SUFFIX + "/activities/{id}/pins/{pinId}")
    Observable<Response<JsonObject>> unpinFeedItem(@Path("id") String boardId, @Path("pinId") String pinId, @Header("Authorization") String authHeader);

    @GET(ZONE_BACKEND_SUFFIX + "/activities/{feedItemId}/comments")
    Observable<Response<List<FeedItemComment>>> getCommentsForFeed(@Path("feedItemId") String feedId,
                                                                   @Header("Authorization") String authHeader);

    @GET(ZONE_BACKEND_SUFFIX + "/boards/{boardId}/comments")
    Observable<Response<List<FeedItemComment>>> getCommentsForBoard(@Path("boardId") String boardId,
                                                                    @Header("Authorization") String authHeader);

    @POST(ZONE_BACKEND_SUFFIX + "/boards/{boardId}/comments")
    Observable<Response<FeedItemComment>> postCommentOnBoard(@Body String comment,
                                                             @Path("boardId") String boardId,
                                                             @Query("time") String time,
                                                             @Header("Authorization") String authHeader);

    @POST(ZONE_BACKEND_SUFFIX + "/boards/{boardId}/comments/{commentId}")
    Observable<Response<FeedItemComment>> postReplyOnBoardComment(@Body String reply,
                                                                  @Path("commentId") String commentId,
                                                                  @Path("boardId") String boardId,
                                                                  @Query("time") String time,
                                                                  @Header("Authorization") String authHeader);

    @POST(ZONE_BACKEND_SUFFIX + "/activities/{feedItemId}/comments")
    Observable<Response<FeedItemComment>> postCommentOnFeedItem(@Body String comment,
                                                                @Path("feedItemId") String feedItemId,
                                                                @Query("boardId") String boardId,
                                                                @Query("time") String time,
                                                                @Header("Authorization") String authHeader);

    @POST(ZONE_BACKEND_SUFFIX + "/activities/{feedItemId}/comments/{commentId}")
    Observable<Response<FeedItemComment>> postReplyOnComment(@Body String reply,
                                                             @Path("feedItemId") String feedItemId,
                                                             @Path("commentId") String commentId,
                                                             @Query("boardId") String boardId,
                                                             @Query("time") String time,
                                                             @Header("Authorization") String authHeader);

    @GET(FEED_FLOWS_SUFFIX + "/polls/getPoll")
    Observable<Response<Poll>> getBoardPoll(@Query("boardId") String boardId, @Header("Authorization") String authHeader);

    @GET(FEED_FLOWS_SUFFIX + "/polls/userSelection")
    Observable<Response<PollAnswerResponse>> getBoardPollAnswer(@Query("pollId") long pollId, @Header("Authorization") String authHeader);

    @POST(FEED_FLOWS_SUFFIX + "/polls/userSelection")
    Observable<Response<PollAnswerResponse>> postBoardPollAnswer(@Body PollAnswerRequest pollAnswer, @Header("Authorization") String authHeader);

    @GET(FOOTBALL_SUFFIX + "/teams")
    Observable<Response<List<FootballTeam>>> getSearchedFootballTeams(@Query("teamNamePart") String teamName, @Header("Authorization") String authHeader);

    @POST(ZONE_BACKEND_SUFFIX + "/boards/{boardId}/clap")
    Observable<Response<JsonObject>> postClap(@Path("boardId") String boardId, @Body User player);

    @POST(ZONE_BACKEND_SUFFIX + "/boards/{boardId}/dart")
    Observable<Response<JsonObject>> postDart(@Path("boardId") String boardId, @Body User player);

    @POST(ZONE_BACKEND_SUFFIX + "/users/zonePreferences")
    Observable<Response<String>> postTeamPreferences(@Query("zone") String zone,
                                                     @Body Set<String> teamList,
                                                     @Header("Authorization") String authHeader);

    @GET(FOOTBALL_SUFFIX + "/teams/popular")
    Observable<Response<List<FootballTeam>>> getPopularTeams(@Header("Authorization") String authHeader);

    @GET(ZONE_BACKEND_SUFFIX + "/feedEntries/{id}")
    Observable<Response<FeedEntry>> getFeedEntry(@Path("id") String feedItemId, @Header("Authorization") String authHeader);

    @GET(ZONE_BACKEND_SUFFIX + "/notifications/unread")
    Observable<Response<List<SocialNotification>>> getNotifications(@Header("Authorization") String authHeader);

    @POST(ZONE_BACKEND_SUFFIX + "/notifications/{id}/setIsRead")
    Observable<Response<List<SocialNotification>>> setNotificationAsRead(@Path("id") String notificationId, @Header("Authorization") String authHeader);

    @POST(ZONE_BACKEND_SUFFIX + "/notifications/{id}/setIsReadAfter")
    Observable<Response<List<SocialNotification>>> setAllNotificationsAsRead(@Path("id") String firstNotificationId, @Header("Authorization") String authHeader);

    @GET(FOOTBALL_SUFFIX + "/matches/nextMatches")
    Observable<Response<List<NextMatch>>> getNextMatches(@Query("leagues") List<String> userSelectedLeagues, @Header("Authorization") String authHeader);

    @GET(CARD_SUFFIX + "/cards/wallet")
    Observable<Response<List<JunaCard>>> getCardWallet(@Header("Authorization") String authHeader);

    @GET(CARD_SUFFIX + "/cards/{id}")
    Observable<Response<JunaCard>> getCardDetail(@Path("id") String cardId, @Header("Authorization") String authHeader);

    @Multipart
    @POST(CARD_SUFFIX + "/cards")
    Observable<Response<JunaCard>> createCard(@Query("cardColor") String cardColor,
                                              @Part MultipartBody.Part image,
                                              @Header("Authorization") String authHeader);

    @Multipart
    @PATCH(CARD_SUFFIX + "/cards")
    Observable<Response<JunaCard>> updateCard(@Part("cardColor") String cardColor,
                                              @Part MultipartBody.Part image,
                                              @Header("Authorization") String authHeader);

    @PUT(CARD_SUFFIX + "/cards/{id}/publish")
    Observable<Response<Void>> publishCard(@Path("id") String cardId, @Header("Authorization") String authHeader);

    @GET(CARD_SUFFIX + "/cardNotifications")
    Observable<Response<List<CardNotification>>> getCardNotifications(@Header("Authorization") String authHeader);

    @PATCH(CARD_SUFFIX + "/cardNotifications")
    Observable<Response<Void>> markCardNotificationRead(@Header("Authorization") String authHeader);
}