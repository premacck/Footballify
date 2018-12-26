package life.plank.juna.zone.view.adapter.onboarding

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import kotlinx.android.synthetic.main.item_zone_grid.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.Zones
import life.plank.juna.zone.util.common.JunaDataUtil.findInt
import life.plank.juna.zone.util.view.UIDisplayUtil.getDp
import java.util.*

/**
 * Created by plank-dhamini on 18/7/2018.
 */
class SelectZoneAdapter(
        private val zonesList: ArrayList<Zones>,
        private val glide: RequestManager
) : RecyclerView.Adapter<SelectZoneAdapter.ZoneViewHolder>() {

    private var selectedZones: MutableSet<Zones> = HashSet()
    var selectedZoneNames: MutableSet<String> = HashSet()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ZoneViewHolder =
            ZoneViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_zone_grid, parent, false))

    override fun onBindViewHolder(holder: ZoneViewHolder, position: Int) {

        val zone = zonesList[position]

        val itemView = holder.itemView

        itemView.zone_title_text_view.text = zone.name
        itemView.followers_count.text = zone.followerCount.toString()

        glide.load(zone.imageUrl)
                .into(itemView.zone_image_view)

        setItemSelection(zone, holder, false)

        holder.itemView.setOnClickListener {
            setItemSelection(zone, holder, true)
        }
    }

    private fun setItemSelection(selectedZone: Zones, holder: ZoneViewHolder, isActionDone: Boolean) {
        holder.itemView.run {
            if (isActionDone) {
                if (selectedZones.contains(selectedZone)) {
                    selectedZones.remove(selectedZone)
                    selectedZoneNames.remove(selectedZone.id)
                } else {
                    selectedZones.add(selectedZone)
                    selectedZoneNames.add(selectedZone.id)
                }
            }
            val isItemSelected = selectedZones.contains(selectedZone)
            follow_image_view.visibility = if (isItemSelected) View.VISIBLE else View.INVISIBLE
            zone_image_view.imageAlpha = findInt(if (isItemSelected) R.integer.visiblilty_160 else R.integer.opaque)
            zone_title_text_view.visibility = if (isItemSelected) View.INVISIBLE else View.VISIBLE
            followers_count_layout.visibility = if (isItemSelected) View.INVISIBLE else View.VISIBLE

            if (zone_card.measuredWidth > 0 && zone_card.measuredHeight > 0) {
                val params = zone_card.layoutParams
                params.width = if (isItemSelected) zone_card.measuredWidth - getDp(22f).toInt() else zone_card.measuredWidth + getDp(22f).toInt()
                params.height = if (isItemSelected) zone_card.measuredHeight - getDp(22f).toInt() else zone_card.measuredHeight + getDp(22f).toInt()
                zone_card.layoutParams = params
            }
        }
    }

    override fun getItemCount(): Int {
        return zonesList.size
    }

    class ZoneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}