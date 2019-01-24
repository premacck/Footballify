package life.plank.juna.zone.view.board.adapter.match

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_commentary.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.football.Commentary
import life.plank.juna.zone.util.common.getDesignedCommentaryString
import life.plank.juna.zone.util.view.UIDisplayUtil.alternateBackgroundColor

class CommentaryAdapter(var commentaries: MutableList<Commentary>) : RecyclerView.Adapter<CommentaryAdapter.CommentsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder =
            CommentsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_commentary, parent, false))

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        commentaries[position].run {
            alternateBackgroundColor(holder.itemView, position)
            holder.itemView.time.text =
                    if (extraMinute > 0) "$minute+$extraMinute"
                    else minute.toString()

            holder.itemView.commentary.text = getDesignedCommentaryString(comment)
            return@run null
        }
    }

    override fun getItemCount(): Int = commentaries.size

    fun update(commentaries: List<Commentary>) {
        this.commentaries.addAll(commentaries)
        notifyDataSetChanged()
    }

    fun updateNew(commentaries: List<Commentary>) {
        val previousIndex = this.commentaries.size
        this.commentaries.addAll(commentaries)
        notifyItemRangeInserted(previousIndex, commentaries.size)
    }

    class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}