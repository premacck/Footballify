package life.plank.juna.zone.util.epoxy.modelview

import android.content.Context
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.airbnb.epoxy.*
import com.bumptech.glide.*
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_base_comment.view.*
import kotlinx.android.synthetic.main.item_post_comment.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.FeedItemComment
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.time.DateUtil
import life.plank.juna.zone.util.view.UIDisplayUtil.getDp
import life.plank.juna.zone.view.controller.ForumReplyController
import life.plank.juna.zone.view.fragment.base.BaseCommentContainerFragment

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class CommentView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val glide: RequestManager = Glide.with(this)

    init {
        View.inflate(context, R.layout.item_post_comment, this)
    }

    @ModelProp
    fun prepare(triple: Triple<FeedItemComment, Int, BaseCommentContainerFragment>) {
        val feedItemComment = triple.first
        comment_text_view.text =
                SpannableStringBuilder(feedItemComment.commenterHandle.semiBold().color(R.color.black))
                        .append(AppConstants.SPACE)
                        .append(feedItemComment.message.formatMentions())
        comment_time_text.text = DateUtil.getCommentDateAndTimeFormat(feedItemComment.time)

        glide.load(feedItemComment.commenterProfilePictureUrl)
                .apply(RequestOptions.overrideOf(getDp(20f).toInt(), getDp(20f).toInt()))
                .into(profile_pic)

        like_text_view.setText(if (feedItemComment.hasLiked) R.string.unlike else R.string.like)

        feedItemComment.replies?.run {
            val forumReplyController = ForumReplyController(feedItemComment, triple.second, triple.third)
            replies_list.adapter = forumReplyController.adapter
//            TODO: pass previousPageToken and nextPageToken when implemented
            forumReplyController.setData(this, null, null)
        }

        setOnclickListeners(feedItemComment, triple.second, triple.third)
    }

    private fun setOnclickListeners(feedItemComment: FeedItemComment, position: Int, commentContainerFragment: BaseCommentContainerFragment) {
        like_text_view.onDebouncingClick {
            when (like_text_view.text.toString()) {
                findString(life.plank.juna.zone.R.string.like) -> commentContainerFragment.onCommentLiked()
                findString(life.plank.juna.zone.R.string.unlike) -> commentContainerFragment.onCommentUnliked()
            }
            like_text_view.setText(if (like_text_view.text.toString() == findString(R.string.like)) R.string.unlike else R.string.like)
        }
        reply_text_view.onDebouncingClick {
            commentContainerFragment.replyAction(reply_text_view, base_comment, feedItemComment.commenterHandle, feedItemComment, position)
        }
    }
}