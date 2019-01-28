package life.plank.juna.zone.data.api

import com.google.gson.JsonObject
import life.plank.juna.zone.data.model.board.*
import life.plank.juna.zone.data.model.board.poll.*
import life.plank.juna.zone.data.model.card.*
import life.plank.juna.zone.data.model.feed.*
import life.plank.juna.zone.data.model.football.*
import life.plank.juna.zone.data.model.notification.*
import life.plank.juna.zone.data.model.user.*
import life.plank.juna.zone.sharedpreference.IdToken
import okhttp3.*
import retrofit2.Response
import retrofit2.http.*
import rx.Observable
import java.util.*

const val FOOTBALL_SUFFIX = "football"
const val ZONE_BACKEND_SUFFIX = "backend"
const val CARD_SUFFIX = "card"
const val FEED_FLOWS_SUFFIX = "feedflows"

/**
 * This class consists of all the rest api calls for zone
 */
interface RestApi {

    //working
    @GET("$FOOTBALL_SUFFIX/seasons/standings")
    fun getStandings(@Query("leagueName") leagueName: String,
                     @Query("seasonName") seasonName: String,
                     @Query("countryName") countryName: String): Observable<Response<MutableList<Standings>>>

    //working
    @GET("$FOOTBALL_SUFFIX/seasons/playerstats")
    fun getPlayerStats(@Query("leagueName") leagueName: String,
                       @Query("seasonName") seasonName: String,
                       @Query("countryName") countryName: String): Observable<Response<MutableList<PlayerStats>>>

    //working
    @GET("$FOOTBALL_SUFFIX/teams/stats")
    fun getTeamStats(@Query("leagueName") leagueName: String,
                     @Query("seasonName") seasonName: String,
                     @Query("countryName") countryName: String): Observable<Response<MutableList<TeamStats>>>

    //working
    @GET("$ZONE_BACKEND_SUFFIX/boards")
    fun getBoard(@Query("foreignId") matchId: Long?, @Query("boardType") boardType: String, @Header("Authorization") authHeader: String? = IdToken): Observable<Response<Board>>

    //working
    @GET("$ZONE_BACKEND_SUFFIX/boards/createdBy")
    fun getUserBoards(@Header("Authorization") authHeader: String? = IdToken): Observable<Response<MutableList<Board>>>

    //working
    @POST("$ZONE_BACKEND_SUFFIX/boards/{id}/invite")
    fun inviteUserToJoinBoard(@Body user: Set<User>, @Path("id") boardId: String, @Header("Authorization") authHeader: String? = IdToken): Observable<Response<JsonObject>>

    //working
    @GET("$ZONE_BACKEND_SUFFIX/boards/feedItems")
    fun getBoardFeedItems(@Query("id") boardId: String, @Header("Authorization") authHeader: String? = IdToken): Observable<Response<MutableList<FeedEntry>>>

    //working
    @GET("$ZONE_BACKEND_SUFFIX/zones")
    fun retrieveZones(): Observable<Response<MutableList<Zones>>>

    //working
    @DELETE("$ZONE_BACKEND_SUFFIX/boards/{id}/activities/removeUser")
    fun deleteUserFromPrivateBoard(@Path("id") boardId: String,
                                   @Query("boardUserId") userId: String,
                                   @Header("Authorization") authHeader: String? = IdToken): Observable<Response<JsonObject>>

    //working
    @GET("$ZONE_BACKEND_SUFFIX/users")
    fun getUser(@Header("Authorization") authHeader: String? = IdToken): Observable<Response<User>>

    @POST("$ZONE_BACKEND_SUFFIX/users/editUserDetails")
    fun updateUserDetails(@Body user: User, @Header("Authorization") authHeader: String? = IdToken): Observable<Response<JsonObject>>

    //working
    @Multipart
    @POST("$ZONE_BACKEND_SUFFIX/activities/upload")
    fun postMediaContentToServer(@Part file: MultipartBody.Part,
                                 @Query("targetId") targetId: String?,
                                 @Query("contentType") contentType: String,
                                 @Query("userId") userId: String,
                                 @Query("dateCreated") dateCreated: String,
                                 @Query("feedType") feedType: String,
                                 @Query("title") title: String,
                                 @Header("Authorization") authHeader: String? = IdToken): Observable<Response<JsonObject>>

    //working
    @POST("$ZONE_BACKEND_SUFFIX/activities/feeditems")
    fun postFeedItemOnBoard(@Body feedItem: FeedItem,
                            @Query("boardId") boardId: String?,
                            @Query("contentType") contentType: String,
                            @Header("Authorization") authHeader: String? = IdToken): Observable<Response<JsonObject>>

    //working
    @Multipart
    @POST("$ZONE_BACKEND_SUFFIX/boards")
    fun createPrivateBoard(@Query("boardType") boardType: String,
                           @Part("displayName") displayName: RequestBody,
                           @Part("zone") zone: RequestBody,
                           @Part("description") description: RequestBody,
                           @Part("color") color: RequestBody,
                           @Part file: MultipartBody.Part,
                           @Header("Authorization") authHeader: String? = IdToken): Observable<Response<String>>

    //working
    @Multipart
    @POST("$ZONE_BACKEND_SUFFIX/users/profilePicture")
    fun uploadProfilePicture(@Part file: MultipartBody.Part, @Header("Authorization") authHeader: String? = IdToken): Observable<Response<String>>

    //working
    @DELETE("$ZONE_BACKEND_SUFFIX/boards/{id}")
    fun deleteBoard(@Path("id") boardId: String, @Header("Authorization") authHeader: String? = IdToken): Observable<Response<JsonObject>>

    //working
    @GET("$FOOTBALL_SUFFIX/seasons/matches")
    fun getFixtures(@Query("seasonName") seasonName: String,
                    @Query("leagueName") leagueName: String,
                    @Query("countryName") countryName: String): Observable<Response<MutableList<MatchFixture>>>

    //working
    @GET("$ZONE_BACKEND_SUFFIX/feedEntries")
    fun getUserFeed(@Header("Authorization") authHeader: String? = IdToken): Observable<Response<MutableList<FeedEntry>>>

    @GET("$ZONE_BACKEND_SUFFIX/feedEntries")
    fun getAnonymousFeed(): Observable<Response<MutableList<FeedEntry>>>

    //working
    @GET("$ZONE_BACKEND_SUFFIX/boards/{id}")
    fun getBoardById(@Path("id") boardId: String, @Header("Authorization") authHeader: String? = IdToken): Observable<Response<Board>>

    //working
    @GET("$ZONE_BACKEND_SUFFIX/activities/{feedItemId}/emojiCounts")
    fun getTopFeedItemEmoji(@Path("feedItemId") feedItemId: String, @Header("Authorization") authHeader: String? = IdToken): Observable<Response<MutableList<Emoji>>>

    //working
    @POST("$ZONE_BACKEND_SUFFIX/activities/{feedItemId}/emojis")
    fun postEmojiOnFeedItem(@Path("feedItemId") feedItemId: String,
                            @Query("boardId") boardId: String,
                            @Query("reaction") emojiUnicode: Int?,
                            @Query("time") dateCreated: String,
                            @Header("Authorization") authHeader: String? = IdToken): Observable<Response<JsonObject>>

    //working
    @GET("$ZONE_BACKEND_SUFFIX/boards/{boardId}/emojiCounts")
    fun getTopBoardEmoji(@Path("boardId") boardId: String, @Header("Authorization") authHeader: String? = IdToken): Observable<Response<MutableList<Emoji>>>

    //working
    @POST("$ZONE_BACKEND_SUFFIX/boards/{boardId}/emojis")
    fun postEmojiOnBoard(@Path("boardId") boardId: String,
                         @Query("reaction") emojiUnicode: Int?,
                         @Query("time") dateCreated: String,
                         @Header("Authorization") authHeader: String? = IdToken): Observable<Response<JsonObject>>

    //working
    @GET("$ZONE_BACKEND_SUFFIX/users/search")
    fun getSearchedUsers(@Query("q") displayName: String, @Header("Authorization") authHeader: String? = IdToken): Observable<Response<MutableList<User>>>

    //working
    @GET("$ZONE_BACKEND_SUFFIX/boards/{boardId}/members")
    fun getBoardMembers(@Path("boardId") boardId: String, @Header("Authorization") authHeader: String? = IdToken): Observable<Response<MutableList<User>>>

    //working
    @POST("$ZONE_BACKEND_SUFFIX/boards/{boardId}/activities/follow")
    fun followBoard(@Path("boardId") boardId: String, @Header("Authorization") authHeader: String? = IdToken): Observable<Response<JsonObject>>

    //working
    @POST("$ZONE_BACKEND_SUFFIX/zones/follows")
    fun followZones(@Body zones: Zones, @Header("Authorization") authHeader: String? = IdToken): Observable<Response<JsonObject>>

    @GET("$ZONE_BACKEND_SUFFIX/boards/following")
    fun getFollowingBoards(@Query("zone") zone: String, @Header("Authorization") authHeader: String? = IdToken): Observable<Response<MutableList<Board>>>

    @GET("$FOOTBALL_SUFFIX/matches/{matchId}/lineups")
    fun getLineUpsData(@Path("matchId") matchId: Long): Observable<Response<Lineups>>

    @POST("$ZONE_BACKEND_SUFFIX/ausers")
    fun createUser(@Body signUpModel: SignUpModel): Observable<Response<SignUpModel>>

    @GET("$FOOTBALL_SUFFIX/matches/{matchId}/stats")
    fun getMatchStatsForMatch(@Path("matchId") matchId: Long): Observable<Response<MatchStats>>

    @GET("$FOOTBALL_SUFFIX/matches/{matchId}")
    fun getMatchDetails(@Path("matchId") matchId: Long): Observable<Response<MatchDetails>>

    @GET("$FOOTBALL_SUFFIX/matches/{matchId}/standings")
    fun getMatchStandingsForMatch(@Path("matchId") matchId: Long): Observable<Response<MutableList<Standings>>>

    @GET("$FOOTBALL_SUFFIX/matches/{matchId}/teamStats")
    fun getTeamStatsForMatch(@Path("matchId") matchId: Long): Observable<Response<MutableList<TeamStats>>>

    @GET("$FOOTBALL_SUFFIX/matches/{matchId}/scrubber/{hour}")
    fun getScrubberDetails(@Path("matchId") matchId: Long, @Path("hour") currentMatchTime: Date): Observable<Response<MutableList<ScrubberData>>>

    @POST("$ZONE_BACKEND_SUFFIX/activities/{id}/pins")
    fun pinFeedItem(@Path("id") feedItemId: String,
                    @Query("target") target: String,
                    @Query("targetId") boardId: String,
                    @Query("time") dateCreated: String,
                    @Header("Authorization") authHeader: String? = IdToken): Observable<Response<String>>

    @DELETE("$ZONE_BACKEND_SUFFIX/activities/{id}/pins/{pinId}")
    fun unpinFeedItem(@Path("id") boardId: String, @Path("pinId") pinId: String, @Header("Authorization") authHeader: String? = IdToken): Observable<Response<JsonObject>>

    @GET("$ZONE_BACKEND_SUFFIX/activities/{feedItemId}/comments")
    fun getCommentsForFeed(@Path("feedItemId") feedId: String,
                           @Header("Authorization") authHeader: String? = IdToken): Observable<Response<MutableList<FeedItemComment>>>

    @GET("$ZONE_BACKEND_SUFFIX/boards/{boardId}/comments")
    fun getCommentsForBoard(@Path("boardId") boardId: String,
                            @Header("Authorization") authHeader: String? = IdToken): Observable<Response<MutableList<FeedItemComment>>>

    @POST("$ZONE_BACKEND_SUFFIX/boards/{boardId}/comments")
    fun postCommentOnBoard(@Body comment: String,
                           @Path("boardId") boardId: String,
                           @Query("time") time: String,
                           @Header("Authorization") authHeader: String? = IdToken): Observable<Response<FeedItemComment>>

    @POST("$ZONE_BACKEND_SUFFIX/boards/{boardId}/comments/{commentId}")
    fun postReplyOnBoardComment(@Body reply: String,
                                @Path("commentId") commentId: String,
                                @Path("boardId") boardId: String,
                                @Query("time") time: String,
                                @Header("Authorization") authHeader: String? = IdToken): Observable<Response<FeedItemComment>>

    @POST("$ZONE_BACKEND_SUFFIX/activities/{feedItemId}/comments")
    fun postCommentOnFeedItem(@Body comment: String,
                              @Path("feedItemId") feedItemId: String,
                              @Query("boardId") boardId: String,
                              @Query("time") time: String,
                              @Header("Authorization") authHeader: String? = IdToken): Observable<Response<FeedItemComment>>

    @POST("$ZONE_BACKEND_SUFFIX/activities/{feedItemId}/comments/{commentId}")
    fun postReplyOnComment(@Body reply: String,
                           @Path("feedItemId") feedItemId: String,
                           @Path("commentId") commentId: String,
                           @Query("boardId") boardId: String,
                           @Query("time") time: String,
                           @Header("Authorization") authHeader: String? = IdToken): Observable<Response<FeedItemComment>>

    @GET("$FEED_FLOWS_SUFFIX/polls/getPoll")
    fun getBoardPoll(@Query("boardId") boardId: String, @Header("Authorization") authHeader: String? = IdToken): Observable<Response<Poll>>

    @GET("$FEED_FLOWS_SUFFIX/polls/userSelection")
    fun getBoardPollAnswer(@Query("pollId") pollId: Long, @Header("Authorization") authHeader: String? = IdToken): Observable<Response<PollAnswerResponse>>

    @POST("$FEED_FLOWS_SUFFIX/polls/userSelection")
    fun postBoardPollAnswer(@Body pollAnswer: PollAnswerRequest, @Header("Authorization") authHeader: String? = IdToken): Observable<Response<PollAnswerResponse>>

    @GET("$FOOTBALL_SUFFIX/teams")
    fun getSearchedFootballTeams(@Query("teamNamePart") teamName: String, @Header("Authorization") authHeader: String? = IdToken): Observable<Response<MutableList<FootballTeam>>>

    @POST("$ZONE_BACKEND_SUFFIX/boards/{boardId}/clap")
    fun postClap(@Path("boardId") boardId: String, @Body player: User): Observable<Response<JsonObject>>

    @POST("$ZONE_BACKEND_SUFFIX/boards/{boardId}/dart")
    fun postDart(@Path("boardId") boardId: String, @Body player: User): Observable<Response<JsonObject>>

    @POST("$ZONE_BACKEND_SUFFIX/users/zonePreferences")
    fun postTeamPreferences(@Query("zone") zone: String,
                            @Body teamList: Set<String>,
                            @Header("Authorization") authHeader: String? = IdToken): Observable<Response<String>>

    @GET("$FOOTBALL_SUFFIX/teams/popular")
    fun getPopularTeams(@Header("Authorization") authHeader: String? = IdToken): Observable<Response<MutableList<FootballTeam>>>

    @GET("$ZONE_BACKEND_SUFFIX/feedEntries/{id}")
    fun getFeedEntry(@Path("id") feedItemId: String, @Header("Authorization") authHeader: String? = IdToken): Observable<Response<FeedEntry>>

    @GET("$ZONE_BACKEND_SUFFIX/notifications/unread")
    fun getNotifications(@Header("Authorization") authHeader: String? = IdToken): Observable<Response<MutableList<SocialNotification>>>

    @POST("$ZONE_BACKEND_SUFFIX/notifications/{id}/setIsRead")
    fun setNotificationAsRead(@Path("id") notificationId: String, @Header("Authorization") authHeader: String? = IdToken): Observable<Response<MutableList<SocialNotification>>>

    @POST("$ZONE_BACKEND_SUFFIX/notifications/{id}/setIsReadAfter")
    fun setAllNotificationsAsRead(@Path("id") firstNotificationId: String, @Header("Authorization") authHeader: String? = IdToken): Observable<Response<MutableList<SocialNotification>>>

    @GET("$FOOTBALL_SUFFIX/matches/nextMatches")
    fun getNextMatches(@Query("leagues") userSelectedLeagues: List<String>, @Header("Authorization") authHeader: String? = IdToken): Observable<Response<MutableList<NextMatch>>>

    @GET("$CARD_SUFFIX/cards/wallet")
    fun getCardWallet(@Header("Authorization") authHeader: String? = IdToken): Observable<Response<MutableList<JunaCard>>>

    @GET("$CARD_SUFFIX/cards/{id}")
    fun getCardDetail(@Path("id") cardId: String, @Header("Authorization") authHeader: String? = IdToken): Observable<Response<JunaCard>>

    @Multipart
    @POST("$CARD_SUFFIX/cards")
    fun createCard(@Query("cardTemplate") cardTemplate: String,
                   @Part image: MultipartBody.Part,
                   @Header("Authorization") authHeader: String? = IdToken): Observable<Response<JunaCardTemplate>>

    @Multipart
    @PATCH("$CARD_SUFFIX/cards")
    fun updateCard(@Part("cardTemplate") cardTemplate: String,
                   @Part image: MultipartBody.Part,
                   @Header("Authorization") authHeader: String? = IdToken): Observable<Response<JunaCardTemplate>>

    @PUT("$CARD_SUFFIX/cards/{id}/publish")
    fun publishCard(@Path("id") cardId: String, @Header("Authorization") authHeader: String? = IdToken): Observable<Response<Void>>

    @GET("$CARD_SUFFIX/cardNotifications")
    fun getCardNotifications(@Header("Authorization") authHeader: String? = IdToken): Observable<Response<MutableList<CardNotification>>>

    @PATCH("$CARD_SUFFIX/cardNotifications")
    fun markCardNotificationRead(@Header("Authorization") authHeader: String? = IdToken): Observable<Response<Void>>
}