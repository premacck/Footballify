package life.plank.juna.zone.view.adapter.user

import android.app.Activity
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_zone_user_feed.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.UserPreference
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.facilis.onFancyClick
import life.plank.juna.zone.util.sharedpreference.PreferenceManager
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.activity.zone.ZoneActivity
import life.plank.juna.zone.view.fragment.onboarding.TeamSelectionFragment

class UserZoneAdapter(
        private val activity: Activity,
        private val restApi: RestApi,
        private val userPreferenceList: MutableList<UserPreference>
) : RecyclerView.Adapter<UserZoneAdapter.UserZoneViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserZoneViewHolder {
        return UserZoneViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_zone_user_feed, parent, false))
    }

    override fun onBindViewHolder(holder: UserZoneViewHolder, position: Int) {
        val (zone) = userPreferenceList[position]
        holder.itemView.football!!.text = zone?.name

        val userPreferences = PreferenceManager.CurrentUser.getUserPreferences()
        if (userPreferences.isNotEmpty() && !isNullOrEmpty(userPreferences[0].zonePreferences)) {
            holder.itemView.time_to_next_match.showNextMatchOnly(restApi)
        }

        holder.itemView.onFancyClick {
            if (DataUtil.isNullOrEmpty(userPreferenceList[0].zonePreferences)) {
                (activity as? BaseCardActivity)?.pushFragment(TeamSelectionFragment.newInstance(), true)
            } else {
                (activity.launch<ZoneActivity>())
            }
        }
    }

    override fun getItemCount(): Int = userPreferenceList.size

    class UserZoneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
