package life.plank.juna.zone.view.adapter.common

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_notification.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.notification.SocialNotification
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.execute
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken

class NotificationAdapter(private val restApi: RestApi) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    val notificationList: MutableList<SocialNotification> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false))
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        notificationList[position].run {
            holder.itemView.run {
                notification_message.text = notificationMessage
                notification_time.setText(R.string.now)
            }
            holder.itemView.onDebouncingClick {
                restApi.setNotificationAsRead(id, getToken()).execute()
//            TODO: open specified notification
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