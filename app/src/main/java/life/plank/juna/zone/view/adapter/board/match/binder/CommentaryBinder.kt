package life.plank.juna.zone.view.adapter.board.match.binder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ahamed.multiviewadapter.ItemBinder
import com.ahamed.multiviewadapter.ItemViewHolder
import kotlinx.android.synthetic.main.item_live_commentary_small.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.binder.CommentaryBindingModel
import life.plank.juna.zone.interfaces.MatchStatsListener
import life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.view.adapter.board.match.CommentaryAdapter

class CommentaryBinder(private val listener: MatchStatsListener) : ItemBinder<CommentaryBindingModel, CommentaryBinder.CommentaryViewHolder>() {

    override fun create(inflater: LayoutInflater, parent: ViewGroup): CommentaryViewHolder = CommentaryViewHolder(inflater.inflate(R.layout.item_live_commentary_small, parent, false))

    override fun bind(holder: CommentaryViewHolder, item: CommentaryBindingModel) {
        holder.itemView.run {
            progress_bar.visibility = View.GONE
            if (isNullOrEmpty(item.commentaryList) || item.errorMessage != 0) {
                commentary_list.visibility = View.GONE
                see_all.visibility = View.GONE
                no_data.visibility = View.VISIBLE
                no_data.setText(item.errorMessage)
                return
            }

            (commentary_list.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).reverseLayout = true
            commentary_list.scrollToPosition(item.commentaryList!!.size - 1)
            commentary_list.visibility = View.VISIBLE
            see_all.visibility = View.VISIBLE
            no_data.visibility = View.GONE
            item.commentaryList?.run { commentary_list.adapter = CommentaryAdapter(this as ArrayList) }

            see_all.onDebouncingClick { listener.onCommentarySeeAllClick(this) }
        }
    }

    override fun canBindData(item: Any): Boolean = item is CommentaryBindingModel

    class CommentaryViewHolder(itemView: View) : ItemViewHolder<CommentaryBindingModel>(itemView)
}