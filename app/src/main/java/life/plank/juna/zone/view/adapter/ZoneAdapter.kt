package life.plank.juna.zone.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_zone_grid.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.Zones
import life.plank.juna.zone.interfaces.OnClickZoneItemListener
import java.util.*

/**
 * Created by plank-dhamini on 18/7/2018.
 */
class ZoneAdapter(
        private val context: Context,
        private val zones: ArrayList<Zones>,
        private val onClickZoneItemListener: OnClickZoneItemListener,
        private val picasso: Picasso)
    : RecyclerView.Adapter<ZoneAdapter.ZoneViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ZoneViewHolder {
        return ZoneViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_zone_grid, parent, false))
    }

    override fun onBindViewHolder(holder: ZoneViewHolder, position: Int) {

        val (name, _, imageUrl, id, _, followerCount) = zones[position]

        val itemView = holder.itemView

        itemView.zone_title_text_view.text = name
        itemView.followers_count.text = followerCount.toString()
        picasso.load(imageUrl)
                .into(itemView.zone_image_view)
        itemView.zone_image_view.setOnClickListener { _ ->
            if (itemView.follow_image_view.visibility == View.VISIBLE) {
                itemView.follow_image_view.visibility = View.INVISIBLE
                itemView.zone_image_view.setAlpha(context.resources.getInteger(R.integer.opaque))
                itemView.zone_title_text_view.visibility = View.VISIBLE
                itemView.followers_count_layout.visibility = View.VISIBLE

                val params = itemView.zone_card.layoutParams
                params.width = itemView.zone_card.measuredWidth + context.resources.getInteger(R.integer.zone_grid_layout_param)
                params.height = itemView.zone_card.measuredHeight + context.resources.getInteger(R.integer.zone_grid_layout_param)
                itemView.zone_card.layoutParams = params
                onClickZoneItemListener.onItemClick(id, false)

            } else {
                itemView.follow_image_view.visibility = View.VISIBLE
                itemView.zone_image_view.imageAlpha = context.resources.getInteger(R.integer.visiblilty_160)
                itemView.zone_title_text_view.visibility = View.INVISIBLE
                itemView.followers_count_layout.visibility = View.INVISIBLE

                val params = itemView.zone_card.layoutParams
                params.width = itemView.zone_card.measuredWidth - context.resources.getInteger(R.integer.zone_grid_layout_param)
                params.height = itemView.zone_card.measuredHeight - context.resources.getInteger(R.integer.zone_grid_layout_param)
                itemView.zone_card.layoutParams = params
                onClickZoneItemListener.onItemClick(id, true)
            }
        }
    }

    override fun getItemCount(): Int {
        return zones.size
    }

    class ZoneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}