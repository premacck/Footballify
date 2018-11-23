package life.plank.juna.zone.view.adapter.post.binder

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ahamed.multiviewadapter.ItemBinder
import com.ahamed.multiviewadapter.ItemViewHolder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_base_comment.view.*
import kotlinx.android.synthetic.main.item_post_comment.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.FeedItemComment
import life.plank.juna.zone.util.AppConstants
import life.plank.juna.zone.util.DataUtil.findString
import life.plank.juna.zone.util.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.DateUtil.getCommentDateAndTimeFormat
import life.plank.juna.zone.util.UIDisplayUtil.getDp
import life.plank.juna.zone.util.color
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.formatMentions
import life.plank.juna.zone.util.semiBold
import life.plank.juna.zone.view.adapter.post.CommentReplyAdapter
import life.plank.juna.zone.view.fragment.base.BaseCommentContainerFragment

class PostCommentBinder(
        private val glide: RequestManager,
        private val commentContainerFragment: BaseCommentContainerFragment,
        private val fragment: String
) : ItemBinder<FeedItemComment, PostCommentBinder.PostCommentViewHolder>() {

    override fun create(inflater: LayoutInflater, parent: ViewGroup): PostCommentViewHolder {
        return PostCommentViewHolder(inflater.inflate(R.layout.item_post_comment, parent, false))
    }

    override fun bind(holder: PostCommentViewHolder, item: FeedItemComment) {
        if (fragment == findString(R.string.forum)) {
            holder.itemView.view_replies_text_view.visibility = View.GONE
            holder.itemView.replies_list.visibility = View.VISIBLE
        } else {
            holder.itemView.view_replies_text_view.visibility = if (isNullOrEmpty(item.replies)) View.GONE else View.VISIBLE
            if (holder.isItemExpanded) {
                holder.itemView.view_replies_text_view.setText(R.string.hide_replies)
                holder.itemView.replies_list.visibility = View.VISIBLE
            } else {
                val replyCount = if (item.replyCount > 0) item.replyCount.toString() else ""
                holder.itemView.view_replies_text_view.text = ZoneApplication.getContext().getString(R.string.view_n_replies, replyCount)
                holder.itemView.replies_list.visibility = View.GONE
            }
        }

        holder.itemView.comment_text_view.text =
                SpannableStringBuilder(item.commenterDisplayName.semiBold().color(R.color.black))
                        .append(AppConstants.SPACE)
                        .append(item.message.formatMentions())
        holder.itemView.comment_time_text.text = getCommentDateAndTimeFormat(item.time)

        glide.load(item.commenterProfilePictureUrl)
                .apply(RequestOptions.overrideOf(getDp(20f).toInt(), getDp(20f).toInt()))
                .into(holder.itemView.profile_pic)

        holder.itemView.like_text_view.setText(if (item.hasLiked) R.string.unlike else R.string.like)

        item.replies?.run {
            holder.itemView.replies_list.adapter = CommentReplyAdapter(glide, item, holder.adapterPosition, this, commentContainerFragment)
        }

        holder.setOnclickListeners(item)
    }

    private fun PostCommentViewHolder.setOnclickListeners(item: FeedItemComment) {
        itemView.like_text_view.onDebouncingClick {
            when (itemView.like_text_view.text.toString()) {
                findString(R.string.like) -> commentContainerFragment.onCommentLiked()
                findString(R.string.unlike) -> commentContainerFragment.onCommentUnliked()
            }
            itemView.like_text_view.setText(if (itemView.like_text_view.text.toString() == findString(R.string.like)) R.string.unlike else R.string.like)
        }
        itemView.reply_text_view.onDebouncingClick {
            commentContainerFragment.replyAction(itemView.reply_text_view, itemView.base_comment, item.commenterDisplayName, item, adapterPosition)
        }
        itemView.view_replies_text_view.onDebouncingClick {
            itemView.view_replies_text_view.setText(if (isItemExpanded) R.string.hide_replies else R.string.show_replies)
            toggleExpansion()
            itemView.replies_list.visibility = if (isItemExpanded) View.VISIBLE else View.GONE
        }
    }

    override fun canBindData(item: Any): Boolean {
        return item is FeedItemComment
    }

    class PostCommentViewHolder(itemView: View) : ItemViewHolder<FeedItemComment>(itemView) {
        fun toggleExpansion() = toggleItemExpansion()
    }
}