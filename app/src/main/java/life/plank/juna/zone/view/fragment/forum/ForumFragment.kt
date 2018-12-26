package life.plank.juna.zone.view.fragment.forum

import android.os.Bundle
import android.text.InputType.*
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prembros.facilis.util.*
import kotlinx.android.synthetic.main.fragment_forum.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication.getApplication
import life.plank.juna.zone.data.model.FeedItemComment
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.notification.*
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.util.common.AppConstants.NEW_LINE
import life.plank.juna.zone.util.common.JunaDataUtil.findString
import life.plank.juna.zone.util.sharedpreference.PreferenceManager
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import life.plank.juna.zone.view.controller.ForumCommentController
import life.plank.juna.zone.view.fragment.base.BaseCommentContainerFragment
import java.net.HttpURLConnection.*
import javax.inject.Inject

class ForumFragment : BaseCommentContainerFragment() {

    @Inject
    lateinit var restApi: RestApi

    private var boardId: String? = null
    private var commentList: MutableList<FeedItemComment>? = null
    private var forumCommentController: ForumCommentController? = null
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
        commentId = activity?.getChildIdFromIntent()
        parentCommentId = activity?.getSiblingIdFromIntent()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_forum, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (boardId == null) return

        prepareList()
        prepareViews()
        setListeners()
    }

    private fun prepareList() {
        forumCommentController = ForumCommentController(this)
        post_comments_list.adapter = forumCommentController?.adapter

        forumCommentController?.addModelBuildListener {
            if (!isNullOrEmpty(commentId)) {
                commentList?.run {
                    if (isNullOrEmpty(parentCommentId)) {
//                        Scroll to comment
                        var commentIndex = indexOf(find { comment -> comment.id == commentId })
                        if (commentIndex == -1) commentIndex = 0
                        scrollToComment(commentIndex)
                    } else {
//                        Scroll to reply
                        val comment = find { comment -> comment.id == parentCommentId }
                        var parentCommentIndex = indexOf(comment)
                        if (parentCommentIndex == -1) parentCommentIndex = 0

                        val replyIndex = comment?.replies?.indexOf(comment.replies?.find { reply -> reply.id == commentId })
                                ?: -1
                        if (replyIndex == -1) {
                            scrollToComment(parentCommentIndex)
                        } else {
                            scrollToReply(parentCommentIndex, replyIndex)
                        }
                        parentCommentId = null
                        activity?.removeIntentExtra(R.string.intent_sibling_id)
                    }
                }
                commentId = null
                activity?.removeIntentExtra(R.string.intent_child_id)
            }
        }
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
                            commentList = it.body()
//                            TODO: pass previousPageToken and nextPageToken when implemented
                            forumCommentController?.setData(commentList, null, null)
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
        commentList?.add(0, responseComment)
//        TODO: pass previousPageToken and nextPageToken when implemented
        forumCommentController?.setData(commentList, null, null)
        scrollToComment(0)
    }

    override fun onReplySuccessful(responseReply: FeedItemComment, parentComment: FeedItemComment?, parentCommentPosition: Int, replyPosition: Int) {
        comment_edit_text.text = null
        commentList?.get(parentCommentPosition)?.run {
            if (replies == null) {
                replies = ArrayList()
            }
            (replies as ArrayList).add(responseReply)
            replyCount++
        }
//        TODO: pass previousPageToken and nextPageToken when implemented
        forumCommentController?.setData(commentList, null, null)
        scrollToReply(parentCommentPosition, replyPosition)
    }
}