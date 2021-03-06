package life.plank.juna.zone.ui.board.adapter.match.binder

import android.view.*
import com.ahamed.multiviewadapter.ItemBinder
import com.ahamed.multiviewadapter.ItemViewHolder
import com.bumptech.glide.Glide
import com.prembros.facilis.util.isNullOrEmpty
import kotlinx.android.synthetic.main.item_standings.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.ui.board.adapter.match.bindingmodel.StandingsBindingModel
import life.plank.juna.zone.ui.football.adapter.league.StandingTableAdapter
import life.plank.juna.zone.util.view.UIDisplayUtil.getDp

class StandingsBinder : ItemBinder<StandingsBindingModel, StandingsBinder.StandingsViewHolder>() {

    override fun create(inflater: LayoutInflater, parent: ViewGroup): StandingsViewHolder = StandingsViewHolder(inflater.inflate(R.layout.item_standings, parent, false))

    override fun bind(holder: StandingsViewHolder, item: StandingsBindingModel) {
        holder.itemView.run {
            standings_progress_bar.visibility = View.GONE
            see_all_standings.visibility = View.GONE
            if (item.errorMessage != null || isNullOrEmpty(item.standingsList)) {
                no_standings.visibility = View.VISIBLE
                no_standings.setText(item.errorMessage!!)
                standing_header_layout.visibility = View.INVISIBLE
                standing_recycler_view.visibility = View.INVISIBLE
                return
            }

            no_standings.visibility = View.INVISIBLE
            standing_header_layout.visibility = View.VISIBLE
            standing_recycler_view.visibility = View.VISIBLE
            val adapter = StandingTableAdapter(Glide.with(ZoneApplication.appContext), true)
            standing_recycler_view.adapter = adapter
            adapter.update(item.standingsList!!)
            layoutParams.height = getDp(140f).toInt()
            requestLayout()
        }
    }

    override fun canBindData(item: Any): Boolean {
        return item is StandingsBindingModel
    }

    class StandingsViewHolder(itemView: View) : ItemViewHolder<StandingsBindingModel>(itemView)
}
