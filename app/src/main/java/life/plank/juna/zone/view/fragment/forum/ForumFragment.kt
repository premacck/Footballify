package life.plank.juna.zone.view.fragment.forum

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.InputType.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_forum.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication.getApplication
import life.plank.juna.zone.data.model.FeedItemComment
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.notification.getCommentIdFromIntent
import life.plank.juna.zone.notification.getParentCommentIdFromIntent
import life.plank.juna.zone.util.common.AppConstants.NEW_LINE
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.common.errorToast
import life.plank.juna.zone.util.common.onTerminate
import life.plank.juna.zone.util.common.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.util.facilis.clearOnClickListener
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.sharedpreference.PreferenceManager
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import life.plank.juna.zone.view.adapter.post.PostCommentAdapter
import life.plank.juna.zone.view.fragment.base.BaseCommentContainerFragment
import java.net.HttpURLConnection.*
import javax.inject.Inject


class ForumFragment : BaseCommentContainerFragment() {

    @Inject
    lateinit var restApi: RestApi

    private var boardId: String? = null
    private var adapter: PostCommentAdapter? = null
    private var commentId: String? = null
    private var parentCommentId: String? = null

    companion object {
        private val TAG = ForumFragment::class.java.simpleName
        fun newInstance(boardId: String?) = ForumFragment().apply { arguments = Bundle().apply { putString(findString(R.string.intent_board_id), boardId) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getApplication().uiComponent.inject(this)
        arguments?.run { boardId = getString(getString(R.string.intent_board_id)) }
        commentId = activity?.getCommentIdFromIntent()
        parentCommentId = activity?.getParentCommentIdFromIntent()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_forum, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (boardId == null) return

        adapter = PostCommentAdapter(Glide.with(this), this, getString(R.string.forum))
        post_comments_list.adapter = adapter

        prepareViews()

        setListeners()
    }

    private fun prepareViews() {
        Glide.with(this)
                .load(PreferenceManager.CurrentUser.getProfilePicUrl())
                .into(commenter_image)

        if (PreferenceManager.App.isEnterToSend()) {
            comment_edit_text.inputType = TYPE_CLASS_TEXT or TYPE_TEXT_FLAG_CAP_SENTENCES
            comment_edit_text.imeOptions = EditorInfo.IME_ACTION_SEND
        } else {
            comment_edit_text.inputType = TYPE_CLASS_TEXT or TYPE_TEXT_FLAG_MULTI_LINE or TYPE_TEXT_FLAG_CAP_SENTENCES
            comment_edit_text.imeOptions = EditorInfo.IME_ACTION_NONE
        }
    }

    private fun setListeners() {
        post_comment.onDebouncingClick {
            if (!comment_edit_text.text.toString().trim().isEmpty() && !isNullOrEmpty(boardId)
                    && !comment_edit_text.text.toString().matches("@[a-zA-Z0-9]+\\s*".toRegex())) {
                post_comment.clearFocus()
                postCommentOrReply(comment_edit_text.text.toString(), getCommentEventForBoardComment(boardId!!))
            }

        }
        forum_swipe_refresh_layout.setOnRefreshListener { getComments(true) }

        comment_edit_text.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    if (!comment_edit_text.text.toString().trim().isEmpty() && !isNullOrEmpty(boardId)
                            && !comment_edit_text.text.toString().matches("@[a-zA-Z0-9]+\\s*".toRegex())) {
                        post_comment.clearFocus()
                        postCommentOrReply(comment_edit_text.text.toString(), getCommentEventForBoardComment(boardId!!))
                    }
                    true
                }
                EditorInfo.IME_ACTION_NONE -> {
                    comment_edit_text.append(NEW_LINE)
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getComments(false)
    }

    fun getComments(isRefreshing: Boolean) {
        no_comment_text_view.visibility = View.GONE
        restApi.getCommentsForBoard(boardId, getToken())
                .onTerminate { if (isRefreshing) forum_swipe_refresh_layout.isRefreshing = false }
                .setObserverThreadsAndSmartSubscribe({
                    Log.e(TAG, "getComments()", it)
                    errorToast(R.string.failed_to_get_feed_comments, it)
                }, {
                    when (it.code()) {
                        HTTP_OK -> {
                            val commentList = it.body()
                            commentId?.run {
                                if (isNullOrEmpty(parentCommentId)) {
//                                    Scroll to comment
                                    var positionToScroll = commentList?.indexOf(commentList.find { comment -> comment.id == commentId })
                                            ?: 0
                                    if (positionToScroll == -1) positionToScroll = 0
                                    post_comments_list.scrollToPosition(positionToScroll)
                                } else {
//                                    Scroll to reply
                                    var parentCommentIndex = commentList?.indexOf(commentList.find { comment -> comment.id == parentCommentId })
                                            ?: 0
                                    if (parentCommentIndex == -1) parentCommentIndex = 0
                                    post_comments_list.scrollToPosition(parentCommentIndex)
                                }
                            }
                            adapter?.setComments(commentList)
                        }
                        HTTP_NOT_FOUND, HTTP_NO_CONTENT -> {
                            no_comment_text_view.setText(R.string.be_the_first_to_comment_on_this_forum)
                            no_comment_text_view.visibility = View.VISIBLE
                            no_comment_text_view.clearOnClickListener()
                        }
                        else -> {
                            no_comment_text_view.setText(R.string.failed_to_get_feed_comments_tap_to_retry)
                            no_comment_text_view.visibility = View.VISIBLE
                            errorToast(R.string.failed_to_get_feed_comments, it)
                            no_comment_text_view.onDebouncingClick { getComments(false) }
                        }
                    }
                }, this)
    }

    override fun specifyCommentEvent() {
        boardId?.run {
            commentEvent = getCommentEventForBoardComment(this)
        }
    }

    override fun commentsRecyclerView(): RecyclerView = post_comments_list

    override fun getTheRestApi(): RestApi = restApi

    override fun getCommentEditText(): EditText = comment_edit_text

    override fun onPostReplyOnComment(reply: String, position: Int, parentComment: FeedItemComment) {
        boardId?.run {
            postReplyOnBoardComment(reply, getCommentEventForReply(this, parentComment.id), parentComment, position)
        }
    }

    override fun onCommentSuccessful(responseComment: FeedItemComment) {
        comment_edit_text.text = null
        no_comment_text_view.visibility = View.GONE
        adapter?.addComment(responseComment)
        post_comments_list.smoothScrollToPosition(0)
    }

    override fun onReplySuccessful(responseReply: FeedItemComment, parentComment: FeedItemComment?, parentCommentPosition: Int, replyPosition: Int) {
        comment_edit_text.text = null
        parentComment?.run {
            if (isNullOrEmpty(replies)) {
                replies = ArrayList()
            }
            (replies as ArrayList).add(replyPosition, responseReply)
            adapter?.onReplyPostedOnComment(parentCommentPosition, this)
        }
    }
}