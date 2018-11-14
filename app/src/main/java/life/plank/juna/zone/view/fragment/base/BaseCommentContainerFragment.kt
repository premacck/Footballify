package life.plank.juna.zone.view.fragment.base

import android.util.Log
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.CommentEvent
import life.plank.juna.zone.data.model.FeedItemComment
import life.plank.juna.zone.data.model.FeedItemCommentReply
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.DataUtil
import life.plank.juna.zone.util.DateUtil.getRequestDateStringOfNow
import life.plank.juna.zone.util.PreferenceManager
import life.plank.juna.zone.util.PreferenceManager.getToken
import life.plank.juna.zone.util.UIDisplayUtil.hideSoftKeyboard
import life.plank.juna.zone.util.errorToast
import life.plank.juna.zone.util.setObserverThreadsAndSmartSubscribe
import java.net.HttpURLConnection

abstract class BaseCommentContainerFragment : BaseFragment() {

    protected lateinit var commentEvent: CommentEvent

    override fun onStart() {
        super.onStart()
        specifyCommentEvent()
    }

    fun getCommentEventForBoardComment(boardId: String): CommentEvent {
        commentEvent = CommentEvent.getForBoardComment(boardId)
        return commentEvent
    }

    fun getCommentEventForFeedItemComment(boardId: String, feedItemId: String): CommentEvent {
        commentEvent = CommentEvent.getForFeedItemComment(boardId, feedItemId)
        return commentEvent
    }

    fun getCommentEventForReply(boardId: String, parentCommentId: String): CommentEvent {
        commentEvent = CommentEvent.getForReply(boardId, parentCommentId)
        return commentEvent
    }

    abstract fun specifyCommentEvent()

    abstract fun getTheRestApi(): RestApi

    abstract fun onPostReplyOnComment(reply: String, position: Int, comment: FeedItemComment)

    abstract fun onCommentSuccessful(feedItemComment: FeedItemComment)

    abstract fun onReplySuccessful(feedItemCommentReply: FeedItemCommentReply?, comment: FeedItemComment?, position: Int)

    protected fun postCommentOrReply(commentOrReply: String, commentEvent: CommentEvent, isCommentOrReplyOnBoard: Boolean = true, comment: FeedItemComment? = null, position: Int = 0) {
        hideSoftKeyboard(activity?.window?.decorView)
        if (commentEvent.isReply) {
//            Posting a reply
            if (isCommentOrReplyOnBoard) {
//                On the specified board's comment
                postReplyOnBoardComment(commentOrReply, commentEvent, comment, position)
            } else {
//                 On the specified Feed item's comment
                postReplyOnFeedItemComment(commentOrReply, commentEvent, comment, position)
            }
        } else {
//            Posting a comment
            if (isCommentOrReplyOnBoard) {
//                On the specified board
                postCommentOnBoard(commentOrReply, commentEvent)
            } else {
//                 On the specified Feed item
                postCommentOnFeedItem(commentOrReply, commentEvent)
            }
        }
    }

    private fun postCommentOnBoard(comment: String, commentEvent: CommentEvent) {
        getTheRestApi().postCommentOnBoard(comment, commentEvent.boardId, getRequestDateStringOfNow(), getToken())
                .setObserverThreadsAndSmartSubscribe({
                    Log.e("commentOnBoard()", "ERROR: ", it)
                }, {
                    when (it.code()) {
                        HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_CREATED -> handleCommentResponse(it.body())
                        else -> errorToast(R.string.failed_to_post_comment, it)
                    }
                })
    }

    private fun postCommentOnFeedItem(comment: String, commentEvent: CommentEvent) {
        getTheRestApi().postCommentOnFeedItem(comment, commentEvent.feedItemId, commentEvent.boardId, getRequestDateStringOfNow(), getToken())
                .setObserverThreadsAndSmartSubscribe({
                    Log.e("commentOnFeedItem()", "ERROR: ", it)
                }, {
                    when (it.code()) {
                        HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_CREATED -> handleCommentResponse(it.body())
                        else -> errorToast(R.string.failed_to_post_comment, it)
                    }
                })
    }

    private fun postReplyOnBoardComment(reply: String, commentEvent: CommentEvent, comment: FeedItemComment?, position: Int) {
        getTheRestApi().postReplyOnBoardComment(reply, commentEvent.parentCommentId, commentEvent.boardId, getRequestDateStringOfNow(), getToken())
                .setObserverThreadsAndSmartSubscribe({
                    Log.e("replyOnBoard()", "ERROR: ", it)
                }, {
                    when (it.code()) {
                        HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_CREATED -> handleReplyResponse(it.body(), comment, position)
                        else -> errorToast(R.string.failed_to_post_reply, it)
                    }
                })
    }

    private fun postReplyOnFeedItemComment(reply: String, commentEvent: CommentEvent, comment: FeedItemComment?, position: Int) {
        getTheRestApi().postReplyOnComment(reply, commentEvent.feedItemId, commentEvent.parentCommentId, commentEvent.boardId, getRequestDateStringOfNow(), getToken())
                .setObserverThreadsAndSmartSubscribe({
                    Log.e("replyOnFeedItem()", "ERROR: ", it)
                }, {
                    when (it.code()) {
                        HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_CREATED -> handleReplyResponse(it.body(), comment, position)
                        else -> errorToast(R.string.failed_to_post_reply, it)
                    }
                })
    }

    private fun handleCommentResponse(feedItemComment: FeedItemComment?) {
        feedItemComment?.run {
            addNameAndPhotoIfNotPresent()
            onCommentSuccessful(this)
        }
    }

    private fun handleReplyResponse(feedItemCommentReply: FeedItemCommentReply?, comment: FeedItemComment?, position: Int) {
        feedItemCommentReply?.run {
            addNameAndPhotoIfNotPresent()
            onReplySuccessful(this, comment, position)
        }
    }

    //    TODO: remove when merging FeedItemComment and FeedItemCommentReply
    private fun FeedItemCommentReply.addNameAndPhotoIfNotPresent() {
        val prefs = PreferenceManager.getSharedPrefs(getString(R.string.pref_user_details))!!
        if (DataUtil.isNullOrEmpty(commenterDisplayName)) {
            commenterDisplayName = prefs.getString(getString(R.string.pref_display_name), "")!!
        }
        if (DataUtil.isNullOrEmpty(commenterProfilePicUrl)) {
            commenterProfilePicUrl = prefs.getString(getString(R.string.pref_profile_pic_url), null)
        }
    }

    private fun FeedItemComment.addNameAndPhotoIfNotPresent() {
        val prefs = PreferenceManager.getSharedPrefs(getString(R.string.pref_user_details))!!
        if (DataUtil.isNullOrEmpty(commenterDisplayName)) {
            commenterDisplayName = prefs.getString(getString(R.string.pref_display_name), "")!!
        }
        if (DataUtil.isNullOrEmpty(commenterProfilePictureUrl)) {
            commenterProfilePictureUrl = prefs.getString(getString(R.string.pref_profile_pic_url), null)
        }
    }

    fun onCommentLiked() {
//        TODO: implement rest API call to like comment
    }

    fun onCommentUnliked() {
//        TODO: implement rest API call to unlike comment
    }
}