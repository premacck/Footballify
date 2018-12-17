package life.plank.juna.zone.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import life.plank.juna.zone.R
import life.plank.juna.zone.R.string.*
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.ZoneLiveData
import life.plank.juna.zone.data.model.notification.BaseInAppNotification
import life.plank.juna.zone.data.model.notification.JunaNotification
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.util.common.asciiToInt
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

private const val CHANNEL_ID = "juna_notification_channel"
private const val TAG = "DrawerNotifier"
private const val GROUP_SOCIAL_NOTIFICATION = "life.plank.juna.zone.SOCIAL_NOTIFICATION"
private const val GROUP_LIVE_FOOTBALL_NOTIFICATION = "life.plank.juna.zone.LIVE_FOOTBALL_NOTIFICATION"

/**
 * Method to send social interaction notification in the notification drawer
 */
fun JunaNotification.prepareDrawerNotification() {
    val pendingIntent = PendingIntent.getActivity(
            ZoneApplication.getContext(),
            0,
            getSocialNotificationIntent(),
            FLAG_ONE_SHOT
    )
    when (action) {
        findString(intent_comment), findString(intent_react), findString(intent_kick) ->
            sendTextNotification(pendingIntent)
        findString(intent_invite) ->
            sendNotification(pendingIntent, false)
        findString(intent_post) ->
            when (feedItemType) {
                IMAGE, VIDEO -> sendNotification(pendingIntent, true)
                AUDIO, ROOT_COMMENT -> sendNotification(pendingIntent, false)
            }
    }
}

/**
 * Method to send live football data notification in the notification drawer
 */
fun ZoneLiveData.prepareDrawerNotification() {
    sendCustomizedNotification {
        sendTextNotification(PendingIntent.getActivity(ZoneApplication.getContext(), 0, getLiveFootballNotificationIntent(), FLAG_ONE_SHOT))
    }
}

fun BaseInAppNotification.sendTextNotification(pendingIntent: PendingIntent) {
    val notificationMessage = getNotificationMessage()
    ZoneApplication.getContext()
            .getNotificationManager()
            .setNotificationChannel()
            .notify(toString().asciiToInt(),
                    getNotificationBuilder(notificationMessage, pendingIntent)
                            .setGroup(if (this is JunaNotification) GROUP_SOCIAL_NOTIFICATION else GROUP_LIVE_FOOTBALL_NOTIFICATION)
                            .setGroupSummary(true)
                            .setStyle(NotificationCompat.BigTextStyle().bigText(notificationMessage))
                            .build())
}

fun JunaNotification.sendNotification(pendingIntent: PendingIntent, isBigImage: Boolean) {
    val notificationManager = ZoneApplication.getContext().getNotificationManager().setNotificationChannel()
    val notificationBuilder = getNotificationBuilder(buildNotificationMessage(), pendingIntent)
    try {
        ZoneApplication.getContext().doAsync {
            val boardIconBitmap = Glide.with(ZoneApplication.getContext()).asBitmap().load(boardIconUrl).submit().get()
            uiThread {
                if (isBigImage) {
                    notificationBuilder.setLargeIcon(boardIconBitmap)
                    val feedItemThumbnail = Glide.with(ZoneApplication.getContext()).asBitmap().load(feedItemThumbnailUrl).submit().get()
                    notificationBuilder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(feedItemThumbnail).bigLargeIcon(null))
                } else {
                    notificationBuilder.setLargeIcon(boardIconBitmap)
                }
                notificationManager.notify(toString().asciiToInt(), notificationBuilder.build())
            }
        }
    } catch (e: Exception) {
        Log.e(TAG, "prepareDrawerNotification(): ", e)
        notificationManager.notify(toString().asciiToInt(), notificationBuilder.build())
    }
}

fun getNotificationBuilder(messageBody: SpannableStringBuilder, pendingIntent: PendingIntent): NotificationCompat.Builder {
    return NotificationCompat.Builder(ZoneApplication.getContext(), CHANNEL_ID)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
}

fun NotificationManager.setNotificationChannel(): NotificationManager {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, findString(channel_name), importance)
        channel.description = findString(channel_description)
        channel.enableLights(true)
        channel.lightColor = Color.BLUE
        channel.enableVibration(true)
        channel.setShowBadge(false)
        createNotificationChannel(channel)
    }
    return this
}

fun Context.getNotificationManager(): NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager