package life.plank.juna.zone.view.adapter.common

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_notification.view.*
import life.plank.juna.zone.R
import java.util.*

class NotificationAdapter(private val notificationList: ArrayList<String>) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false))
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.itemView.notification.text = notificationList[position]
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    fun clear() {
        notificationList.clear()
        notifyDataSetChanged()
    }

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}