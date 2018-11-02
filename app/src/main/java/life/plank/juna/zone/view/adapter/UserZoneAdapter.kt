package life.plank.juna.zone.view.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_zone_user_feed.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.UserPreference
import life.plank.juna.zone.util.common.launch
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.view.activity.zone.ZoneActivity

class UserZoneAdapter(
        private val activity: Activity,
        private val userPreferenceList: MutableList<UserPreference>
) : RecyclerView.Adapter<UserZoneAdapter.UserZoneViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserZoneAdapter.UserZoneViewHolder {
        return UserZoneAdapter.UserZoneViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_zone_user_feed, parent, false))
    }

    override fun onBindViewHolder(holder: UserZoneAdapter.UserZoneViewHolder, position: Int) {
        val (zone) = userPreferenceList[position]
        holder.itemView.football!!.text = zone.name
        holder.itemView.follower_count!!.text = zone.followerCount.toString()
        holder.itemView.total_post_count!!.text = zone.contributionCount.toString()
        holder.itemView.interaction_count!!.text = zone.interactionCount.toString()

        holder.itemView.onDebouncingClick { activity.launch<ZoneActivity>() }
    }

    override fun getItemCount(): Int = userPreferenceList.size

    class UserZoneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
