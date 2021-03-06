package life.plank.juna.zone.ui.base.fragment

import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.*
import com.prembros.facilis.util.isNullOrEmpty
import kotlinx.android.synthetic.main.fragment_forum.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.api.*
import life.plank.juna.zone.data.model.feed.*
import life.plank.juna.zone.sharedpreference.CurrentUser
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.util.time.DateUtil.getRequestDateStringOfNow
import life.plank.juna.zone.util.view.UIDisplayUtil.*
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import java.net.HttpURLConnection

abstract class BaseCommentContainerFragment : BaseJunaFragment() {

    protected lateinit var commentEvent: CommentEvent
    private var isReply: Boolean = false
    private var parentComment: FeedItemComment? = null
    private var parentCommentPosition: Int = -1
    private var replyPosition: Int = -1
    private var selectedItemView: View? = null
    private var selectedReplyTextView: TextView? = null

    override fun onStart() {
        super.onStart()
        specifyCommentEvent()
        getCommentEditText().textChangedListener {
            onTextChanged { charSequence, _, _, _ ->
                if (charSequence == null) resetReplyProperties()
                charSequence?.run { if (isEmpty()) resetReplyProperties() }
            }
        }
    }

    private fun resetReplyProperties() {
        selectedReplyTextView?.text = getString(R.string.reply)
        selectedItemView?.background = null
        selectedReplyTextView = null
        isReply = false
        parentComment = null
        parentCommentPosition = -1
        replyPosition = -1
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

    abstract fun commentsRecyclerView(): RecyclerView

    abstract fun specifyCommentEvent()

    abstract fun getTheRestApi(): RestApi

    abstract fun getCommentEditText(): EditText

    abstract fun onPostReplyOnComment(reply: String, position: Int, parentComment: FeedItemComment)

    abstract fun onCommentSuccessful(responseComment: FeedItemComment)

    abstract fun onReplySuccessful(responseReply: FeedItemComment, parentComment: FeedItemComment?, parentCommentPosition: Int, replyPosition: Int)

    fun replyAction(replyTextView: TextView, itemView: View, commenterHandle: String, parentComment: FeedItemComment, parentCommentPosition: Int, replyPosition: Int = -1) {
        if (replyTextView.text == getString(R.string.reply)) {
//            Resetting old focused items, if any
            selectedReplyTextView?.text = getString(R.string.reply)
            selectedItemView?.background = null

//            Setting focus on current item
            selectedReplyTextView = replyTextView
            selectedItemView = itemView
            selectedItemView?.background = resources.getDrawable(R.drawable.shimmer_rectangle, null)

            replyTextView.setText(R.string.cancel)
            val mentionText = "@$commenterHandle ".semiBold()
            getCommentEditText().setText(mentionText)
            getCommentEditText().setSelection(mentionText.length)
            getCommentEditText().requestFocus()
            showSoftKeyboard(getCommentEditText())
            isReply = true
            this.parentComment = parentComment
            this.parentCommentPosition = parentCommentPosition
            this.replyPosition = replyPosition + 1

            if (replyPosition == -1) {
                scrollToComment(parentCommentPosition)
            } else {
                scrollToReply(parentCommentPosition, replyPosition)
            }
        } else {
            selectedReplyTextView = null
            itemView.background = null
            selectedReplyTextView = null
            replyTextView.setText(R.string.reply)
            getCommentEditText().text = null
            getCommentEditText().clearFocus()
            hideSoftKeyboard(getCommentEditText())
            resetReplyProperties()
        }
    }

    protected fun scrollToComment(position: Int) {
        commentsRecyclerView().postDelayed({ commentsRecyclerView().scrollToPosition(position) }, 500)
    }

    protected fun scrollToReply(parentCommentPosition: Int, replyPosition: Int) {
        commentsRecyclerView().postDelayed({
            (commentsRecyclerView().layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(parentCommentPosition, -((replyPosition + 1) * getDp(54f).toInt()))
        }, 1000)
    }

    private fun setTextViewState(clickableState: Boolean, alpha: Float) {
        post_comment.isClickable = clickableState
        post_comment.alpha = alpha
    }

    protected fun postCommentOrReply(commentOrReply: String, commentEvent: CommentEvent, isCommentOrReplyOnBoard: Boolean = true, feedItemId: String? = null) {
        hideSoftKeyboard(activity?.window?.decorView)
        setTextViewState(false, 0.5f)
        if (isReply) {
            commentEvent.isReply = true
            commentEvent.parentCommentId = parentComment?.id
            commentEvent.feedItemId = feedItemId
        }
        if (commentEvent.isReply) {
//            Posting a reply
            parentComment?.run { onPostReplyOnComment(commentOrReply, parentCommentPosition, this) }
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
        getTheRestApi().postCommentOnBoard(comment, commentEvent.boardId, getRequestDateStringOfNow())
                .setObserverThreadsAndSmartSubscribe({
                    Log.e("commentOnBoard()", "ERROR: ", it)
                    setTextViewState(true, 1f)
                }, {
                    setTextViewState(true, 1f)
                    when (it.code()) {
                        HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_CREATED -> handleCommentResponse(it.body())
                        else -> errorToast(R.string.failed_to_post_comment, it)
                    }
                }, this)
    }

    private fun postCommentOnFeedItem(comment: String, commentEvent: CommentEvent) {
        getTheRestApi().postCommentOnFeedItem(comment, commentEvent.feedItemId!!, commentEvent.boardId, getRequestDateStringOfNow())
                .setObserverThreadsAndSmartSubscribe({
                    Log.e("commentOnFeedItem()", "ERROR: ", it)
                    setTextViewState(true, 1f)
                }, {
                    setTextViewState(true, 1f)
                    when (it.code()) {
                        HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_CREATED -> handleCommentResponse(it.body())
                        else -> errorToast(R.string.failed_to_post_comment, it)
                    }
                }, this)
    }

    protected fun postReplyOnBoardComment(reply: String, commentEvent: CommentEvent, parentComment: FeedItemComment?, position: Int) {
        getTheRestApi().postReplyOnBoardComment(reply, commentEvent.parentCommentId!!, commentEvent.boardId, getRequestDateStringOfNow())
                .setObserverThreadsAndSmartSubscribe({
                    Log.e("replyOnBoard()", "ERROR: ", it)
                    setTextViewState(true, 1f)
                }, {
                    setTextViewState(true, 1f)
                    when (it.code()) {
                        HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_CREATED -> handleReplyResponse(it.body(), parentComment, position)
                        else -> errorToast(R.string.failed_to_post_reply, it)
                    }
                }, this)
    }

    protected fun postReplyOnFeedItemComment(reply: String, commentEvent: CommentEvent, parentComment: FeedItemComment?, position: Int) {
        getTheRestApi().postReplyOnComment(reply, commentEvent.feedItemId!!, commentEvent.parentCommentId!!, commentEvent.boardId, getRequestDateStringOfNow())
                .setObserverThreadsAndSmartSubscribe({
                    Log.e("replyOnFeedItem()", "ERROR: ", it)
                    setTextViewState(true, 1f)
                }, {
                    setTextViewState(true, 1f)
                    when (it.code()) {
                        HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_CREATED -> handleReplyResponse(it.body(), parentComment, position)
                        else -> errorToast(R.string.failed_to_post_reply, it)
                    }
                }, this)
    }

    private fun handleCommentResponse(feedItemComment: FeedItemComment?) {
        feedItemComment?.run {
            addNameAndPhotoIfNotPresent()
            onCommentSuccessful(this)
        }
    }

    private fun handleReplyResponse(feedItemComment: FeedItemComment?, parentComment: FeedItemComment?, position: Int) {
        feedItemComment?.run {
            addNameAndPhotoIfNotPresent()
            onReplySuccessful(this, parentComment, position, replyPosition)
        }
    }

    private fun FeedItemComment.addNameAndPhotoIfNotPresent() {
        if (isNullOrEmpty(commenterHandle)) {
            commenterHandle = CurrentUser.handle
        }
        if (isNullOrEmpty(commenterProfilePictureUrl)) {
            commenterProfilePictureUrl = CurrentUser.profilePicUrl
        }
    }

    fun onCommentLiked() {
//        TODO: implement rest API call to like comment
    }

    fun onCommentUnliked() {
//        TODO: implement rest API call to unlike comment
    }
}