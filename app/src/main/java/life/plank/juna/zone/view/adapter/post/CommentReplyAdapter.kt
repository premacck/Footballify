package life.plank.juna.zone.view.adapter.post

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_base_comment.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.FeedItemComment
import life.plank.juna.zone.util.common.AppConstants
import life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.common.color
import life.plank.juna.zone.util.common.formatMentions
import life.plank.juna.zone.util.common.semiBold
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.time.DateUtil
import life.plank.juna.zone.util.view.UIDisplayUtil.getDp
import life.plank.juna.zone.view.fragment.base.BaseCommentContainerFragment
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class CommentReplyAdapter(
        private val glide: RequestManager,
        private val parentComment: FeedItemComment,
        private val parentCommentPosition: Int,
        private val replies: List<FeedItemComment>,
        private val commentContainerFragment: BaseCommentContainerFragment
) : RecyclerView.Adapter<CommentReplyAdapter.PostCommentReplyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostCommentReplyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_base_comment, parent, false)
        return PostCommentReplyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostCommentReplyViewHolder, position: Int) {
        holder.itemView.run {
            val reply = replies[position]
            doAsync {
                //            TODO: remove the if block below when backend completes forum comment mentions
                if (isNullOrEmpty(reply.commenterHandle)) {
                    reply.commenterHandle = reply.commenterDisplayName.replace(" ", "")
                }
                val formattedReply =
                        SpannableStringBuilder(reply.commenterHandle.semiBold().color(R.color.black))
                                .append(AppConstants.SPACE)
                                .append(reply.message.formatMentions())
                uiThread { comment_text_view.text = formattedReply }
            }

            comment_time_text.text = DateUtil.getCommentDateAndTimeFormat(reply.time)

            glide.load(reply.commenterProfilePictureUrl)
                    .apply(RequestOptions.overrideOf(getDp(20f).toInt(), getDp(20f).toInt()))
                    .into(profile_pic!!)

            reply_text_view.onDebouncingClick {
                commentContainerFragment.replyAction(reply_text_view, holder.itemView, reply.commenterHandle, parentComment, parentCommentPosition, position)
            }

            like_text_view.onDebouncingClick {}
        }
    }

    override fun getItemCount(): Int = replies.size

    class PostCommentReplyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}