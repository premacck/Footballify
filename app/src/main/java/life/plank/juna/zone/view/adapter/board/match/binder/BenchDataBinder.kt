package life.plank.juna.zone.view.adapter.board.match.binder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ahamed.multiviewadapter.ItemBinder
import com.ahamed.multiviewadapter.ItemViewHolder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_bench_data_layout.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.binder.SubstitutionBindingModel
import life.plank.juna.zone.util.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.UIDisplayUtil.getDp
import life.plank.juna.zone.view.adapter.board.match.BenchDataAdapter

class BenchDataBinder(private val picasso: Picasso) : ItemBinder<SubstitutionBindingModel, BenchDataBinder.SubstitutionViewHolder>() {

    override fun create(inflater: LayoutInflater, parent: ViewGroup): SubstitutionViewHolder {
        return SubstitutionViewHolder(inflater.inflate(R.layout.item_bench_data_layout, parent, false))
    }

    override fun bind(holder: SubstitutionViewHolder, item: SubstitutionBindingModel) {
        holder.itemView.run {
            if (item.errorMessage != null || isNullOrEmpty(item.substitutionEvents)) {
                no_substitutions_yet.setText(item.errorMessage!!)
                no_substitutions_yet.visibility = View.VISIBLE
                bench_data_recycler_view.visibility = View.GONE
                return
            }

            no_substitutions_yet.visibility = View.GONE
            bench_data_recycler_view.visibility = View.VISIBLE

            val adapter = BenchDataAdapter()
            bench_data_recycler_view.adapter = adapter
            adapter.update(item.substitutionEvents)

            loadImage(picasso, item.homeTeam!!.logoLink, home_team_logo)
            loadImage(picasso, item.awayTeam!!.logoLink, visiting_team_logo)
            loadImage(picasso, item.homeTeam!!.logoLink, home_team_logo_under_manager)
            loadImage(picasso, item.awayTeam!!.logoLink, visiting_team_logo_under_manager)
        }
    }

    private fun loadImage(picasso: Picasso, logo: String, target: ImageView?) {
        picasso.load(logo)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .resize(getDp(14f).toInt(), getDp(14f).toInt())
                .into(target)
    }

    override fun canBindData(item: Any): Boolean {
        return item is SubstitutionBindingModel
    }

    class SubstitutionViewHolder(itemView: View) : ItemViewHolder<SubstitutionBindingModel>(itemView)
}
