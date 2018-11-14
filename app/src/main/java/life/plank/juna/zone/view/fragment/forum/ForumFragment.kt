package life.plank.juna.zone.view.fragment.forum

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_forum.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication.getApplication
import life.plank.juna.zone.data.model.FeedItemComment
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.interfaces.FeedInteractionListener
import life.plank.juna.zone.util.DataUtil.findString
import life.plank.juna.zone.util.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.DateUtil.getRequestDateStringOfNow
import life.plank.juna.zone.util.PreferenceManager
import life.plank.juna.zone.util.PreferenceManager.getToken
import life.plank.juna.zone.util.errorToast
import life.plank.juna.zone.util.facilis.clearOnClickListener
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.view.adapter.post.PostCommentAdapter
import life.plank.juna.zone.view.fragment.base.BaseFragment
import java.net.HttpURLConnection.*
import javax.inject.Inject
import javax.inject.Named

class ForumFragment : BaseFragment(), FeedInteractionListener {

    @field: [Inject Named("default")]
    lateinit var restApi: RestApi

    lateinit var boardId: String
    private var adapter: PostCommentAdapter? = null

    companion object {
        private val TAG = ForumFragment::class.java.simpleName
        fun newInstance(boardId: String) = ForumFragment().apply { arguments = Bundle().apply { putString(findString(R.string.intent_board_id), boardId) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getApplication().uiComponent.inject(this)
        arguments?.run { boardId = getString(getString(R.string.intent_board_id))!! }
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_forum, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = PostCommentAdapter(Glide.with(this), this, getString(R.string.forum))
        post_comments_list.adapter = adapter
        setAdapterData()

        val profilePicUrl = PreferenceManager.getSharedPrefs(findString(R.string.pref_user_details)).getString(findString(R.string.pref_profile_pic_url), null)
        Glide.with(this)
                .load(profilePicUrl)
                .into(commenter_image)

        getComments()

        post_comment.onDebouncingClick { postCommentOnBoard() }
    }

    //TODO: Remove hard coded data after backend integration.
    private fun setAdapterData() {
        val commentList = ArrayList<FeedItemComment>()
        adapter!!.setComments(commentList)

    }

    private fun getComments() {
        no_comment_text_view.visibility = View.GONE
        restApi.getCommentsForBoard(boardId, getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "getComments()", it)
            errorToast(R.string.failed_to_get_feed_comments, it)
        }, {
            when (it.code()) {
                HTTP_OK -> adapter!!.setComments(it.body())
                HTTP_NOT_FOUND, HTTP_NO_CONTENT -> {
                    no_comment_text_view.setText(R.string.be_the_first_to_comment_on_this_forum)
                    no_comment_text_view.visibility = View.VISIBLE
                    no_comment_text_view.clearOnClickListener()
                }
                else -> {
                    no_comment_text_view.setText(R.string.failed_to_get_feed_comments_tap_to_retry)
                    no_comment_text_view.visibility = View.VISIBLE
                    errorToast(R.string.failed_to_get_feed_comments, it)
                    no_comment_text_view.onDebouncingClick { getComments() }
                }
            }
        })
    }

    private fun postCommentOnBoard() {
        restApi.postCommentOnBoard(comment_edit_text.text.toString(), boardId, getRequestDateStringOfNow(), getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "postCommentOnBoard()", it)
            errorToast(R.string.failed_to_post_comment, it)
        }, {
            when (it.code()) {
                HTTP_OK, HTTP_CREATED -> {
                    comment_edit_text.text = null
                    no_comment_text_view.visibility = View.GONE
                    adapter!!.addComment(it.body())
                }
                else -> errorToast(R.string.failed_to_post_comment, it)
            }
        })
    }

    override fun onCommentLiked() {}

    override fun onCommentDisliked() {}

    override fun onPostCommentOnFeed() {}

    override fun onPostReplyOnComment(reply: String, position: Int, comment: FeedItemComment) {
        restApi.postReplyOnBoardComment(reply, comment.id, boardId, getRequestDateStringOfNow(), getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "onPostReplyOnComment(): ", it)
            errorToast(R.string.failed_to_post_reply, it)
        }, {
            when (it.code()) {
                HTTP_OK, HTTP_CREATED -> {
                    if (isNullOrEmpty(comment.replies)) {
                        comment.replies = ArrayList()
                    }
                    val commentReply = it.body()
                    if (commentReply != null) {
                        (comment.replies as ArrayList).add(0, commentReply)
                        adapter!!.onReplyPostedOnComment(position, comment)
                    }
                }
                else -> errorToast(R.string.failed_to_post_reply, it)
            }
        })
    }
}