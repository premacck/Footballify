package life.plank.juna.zone.view.adapter.board.match.binder

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ahamed.multiviewadapter.ItemBinder
import com.ahamed.multiviewadapter.ItemViewHolder
import cz.intik.overflowindicator.SimpleSnapHelper
import kotlinx.android.synthetic.main.item_match_highlights_layout.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.binder.HighlightsBindingModel
import life.plank.juna.zone.util.UIDisplayUtil.getScreenSize
import life.plank.juna.zone.util.customview.HighlightsAdapter

class MatchHighlightsBinder(private val activity: Activity) : ItemBinder<HighlightsBindingModel, MatchHighlightsBinder.MatchHighlightsViewHolder>() {

    private var snapHelper: SimpleSnapHelper? = null

    override fun create(inflater: LayoutInflater, parent: ViewGroup): MatchHighlightsViewHolder = MatchHighlightsViewHolder(inflater.inflate(R.layout.item_match_highlights_layout, parent, false))

    override fun bind(holder: MatchHighlightsViewHolder, item: HighlightsBindingModel) {
        holder.itemView.run {
            progress_bar.visibility = View.GONE
            val highlightsWidth = getScreenSize(activity.windowManager.defaultDisplay)[0]
            val highlightsHeight = highlightsWidth * 9 / 17

            if (snapHelper == null) {
                snapHelper = SimpleSnapHelper(overflow_pager_indicator)
                snapHelper?.attachToRecyclerView(list_highlights)
            }
            val adapter = HighlightsAdapter(highlightsHeight)
            list_highlights.adapter = adapter
            overflow_pager_indicator.attachToRecyclerView(list_highlights)
            adapter.update(item.highlightsList)
        }
    }

    override fun canBindData(item: Any): Boolean = item is HighlightsBindingModel

    class MatchHighlightsViewHolder(itemView: View) : ItemViewHolder<HighlightsBindingModel>(itemView)
}