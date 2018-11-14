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
import life.plank.juna.zone.util.DataUtil.findString
import life.plank.juna.zone.util.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.PreferenceManager
import life.plank.juna.zone.util.PreferenceManager.getToken
import life.plank.juna.zone.util.errorToast
import life.plank.juna.zone.util.facilis.clearOnClickListener
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.view.adapter.post.PostCommentAdapter
import life.plank.juna.zone.view.fragment.base.BaseCommentContainerFragment
import org.jetbrains.anko.support.v4.runOnUiThread
import java.net.HttpURLConnection.*
import javax.inject.Inject
import javax.inject.Named

class ForumFragment : BaseCommentContainerFragment() {

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

        getComments(false)

        post_comment.onDebouncingClick {
            if (comment_edit_text.text.toString().isEmpty()) {
                comment_edit_text.setError(getString(R.string.please_enter_comment), resources.getDrawable(R.drawable.ic_error, null))
            } else {
                post_comment.clearFocus()
                postCommentOrReply(comment_edit_text.text.toString(), getCommentEventForBoardComment(boardId))
            }
        }
        forum_swipe_refresh_layout.setOnRefreshListener { getComments(true) }
    }

    //TODO: Remove hard coded data after backend integration.
    private fun setAdapterData() {
        val commentList = ArrayList<FeedItemComment>()
        adapter!!.setComments(commentList)
    }

    private fun getComments(isRefreshing: Boolean) {
        no_comment_text_view.visibility = View.GONE
        restApi.getCommentsForBoard(boardId, getToken())
                .doOnTerminate { runOnUiThread { if (isRefreshing) forum_swipe_refresh_layout.isRefreshing = false } }
                .setObserverThreadsAndSmartSubscribe({
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
                            no_comment_text_view.onDebouncingClick { getComments(false) }
                        }
                    }
                })
    }

    override fun specifyCommentEvent() {
        commentEvent = getCommentEventForBoardComment(boardId)
    }

    override fun getTheRestApi(): RestApi = restApi

    override fun onPostReplyOnComment(reply: String, position: Int, comment: FeedItemComment) =
            postCommentOrReply(reply, getCommentEventForReply(boardId, comment.id), true, comment)

    override fun onCommentSuccessful(feedItemComment: FeedItemComment) {
        comment_edit_text.text = null
        no_comment_text_view.visibility = View.GONE
        adapter!!.addComment(feedItemComment)
    }

    override fun onReplySuccessful(feedItemComment: FeedItemComment, comment: FeedItemComment?, position: Int) {
        comment?.run {
            if (isNullOrEmpty(replies)) {
                replies = ArrayList()
            }
            (replies as ArrayList).add(0, feedItemComment)
            adapter!!.onReplyPostedOnComment(position, this)
        }
    }
}