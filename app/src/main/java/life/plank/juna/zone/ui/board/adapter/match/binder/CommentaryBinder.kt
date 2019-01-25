package life.plank.juna.zone.ui.board.adapter.match.binder

import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahamed.multiviewadapter.*
import com.prembros.facilis.util.*
import kotlinx.android.synthetic.main.item_live_commentary_small.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ui.board.adapter.match.CommentaryAdapter
import life.plank.juna.zone.ui.board.adapter.match.bindingmodel.CommentaryBindingModel
import life.plank.juna.zone.ui.football.MatchStatsListener

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

            (commentary_list.layoutManager as LinearLayoutManager).reverseLayout = true
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