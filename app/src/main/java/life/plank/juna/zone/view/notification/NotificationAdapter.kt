package life.plank.juna.zone.view.notification

import android.text.*
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.prembros.facilis.util.onReducingClick
import kotlinx.android.synthetic.main.item_notification.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.api.execute
import life.plank.juna.zone.data.model.notification.SocialNotification
import life.plank.juna.zone.notification.*
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.util.time.getTimeAgo
import life.plank.juna.zone.util.view.UIDisplayUtil.getDp
import life.plank.juna.zone.view.base.BaseJunaCardActivity

class NotificationAdapter(private val activity: BaseJunaCardActivity) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    val notificationList: MutableList<SocialNotification> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder =
            NotificationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false))

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        notificationList[position].run {
            holder.itemView.run {
                notification_message.text = notificationMessage?.formatMentions()
                notification_time.text = getTimeAgo(date)
                Glide.with(activity)
                        .load(lastActorIcon)
                        .apply(RequestOptions.circleCropTransform().override(getDp(20f).toInt(), getDp(20f).toInt()))
                        .into(profile_pic)
            }
            holder.itemView.onReducingClick {
                activity.run {
                    restApi()?.setNotificationAsRead(id)?.execute()
                    triggerNotificationIntent(getSocialNotificationIntent())
                }
            }
        }
    }

    private fun String.formatAsNotificationMessage(userHandles: List<String>): SpannableStringBuilder {
        val message = SpannableString(this)
        userHandles.forEach {
            if (contains(it, true)) {
                val startIndex = indexOf(it, ignoreCase = true)
                message.bold(startIndex, startIndex + it.length)
            }
        }
        return SpannableStringBuilder(message)
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