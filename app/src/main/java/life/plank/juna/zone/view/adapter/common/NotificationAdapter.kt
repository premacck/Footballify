package life.plank.juna.zone.view.adapter.common

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_notification.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.notification.SocialNotification
import life.plank.juna.zone.notification.*
import life.plank.juna.zone.util.common.execute
import life.plank.juna.zone.util.facilis.onFancyClick
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.time.getTimeAgo
import life.plank.juna.zone.view.activity.base.BaseCardActivity

class NotificationAdapter(private val activity: BaseCardActivity) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    val notificationList: MutableList<SocialNotification> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder =
            NotificationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false))

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        notificationList[position].run {
            holder.itemView.run {
                notification_message.text = notificationMessage
                notification_time.text = getTimeAgo(date)
            }
            holder.itemView.onFancyClick {
                activity.restApi()?.setNotificationAsRead(id, getToken())?.execute()
                activity.triggerNotificationIntent(getSocialNotificationIntent())
            }
        }
    }

    override fun getItemCount(): Int = notificationList.size

    fun update(notificationList: MutableList<SocialNotification>) {
        this.notificationList.clear()
        this.notificationList.addAll(notificationList)
        notifyDataSetChanged()
    }

    fun clear() {
        notificationList.clear()
        notifyDataSetChanged()
    }

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}