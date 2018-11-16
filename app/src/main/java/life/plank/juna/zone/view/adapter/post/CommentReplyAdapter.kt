package life.plank.juna.zone.view.adapter.post

import android.support.v7.widget.RecyclerView
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_base_comment.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.FeedItemComment
import life.plank.juna.zone.util.*
import life.plank.juna.zone.util.UIDisplayUtil.getDp
import life.plank.juna.zone.util.facilis.onDebouncingClick
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
            doAsync {
                val formattedReply =
                        SpannableStringBuilder(replies[position].commenterDisplayName.semiBold().color(R.color.black))
                                .append(AppConstants.SPACE)
                                .append(replies[position].message.formatMentions())
                uiThread { comment_text_view.text = formattedReply }
            }

            comment_time_text.text = DateUtil.getCommentDateAndTimeFormat(replies[position].time)

            glide.load(replies[position].commenterProfilePictureUrl)
                    .apply(RequestOptions.overrideOf(getDp(20f).toInt(), getDp(20f).toInt()))
                    .into(profile_pic!!)

            reply_text_view.onDebouncingClick {
                commentContainerFragment.replyAction(reply_text_view, replies[position].commenterDisplayName, parentComment, parentCommentPosition, position)
            }
        }
    }

    override fun getItemCount(): Int = replies.size

    class PostCommentReplyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}