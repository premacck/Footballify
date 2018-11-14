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
import life.plank.juna.zone.data.model.FeedItemCommentReply
import life.plank.juna.zone.util.AppConstants
import life.plank.juna.zone.util.UIDisplayUtil
import life.plank.juna.zone.util.UIDisplayUtil.getDp

class CommentReplyAdapter(private val glide: RequestManager, private val replies: List<FeedItemCommentReply>) : RecyclerView.Adapter<CommentReplyAdapter.PostCommentReplyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostCommentReplyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_base_comment, parent, false)
        return PostCommentReplyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostCommentReplyViewHolder, position: Int) {
        holder.itemView.comment_text_view!!.text =
                SpannableStringBuilder(UIDisplayUtil.getSemiBoldText(replies[position].commenterDisplayName, R.color.black))
                        .append(AppConstants.SPACE)
                        .append(replies[position].message)
        glide.load(replies[position].commenterProfilePicUrl)
                .apply(RequestOptions.overrideOf(getDp(20f).toInt(), getDp(20f).toInt()))
                .into(holder.itemView.profile_pic!!)

    }

    override fun getItemCount(): Int = replies.size

    class PostCommentReplyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}