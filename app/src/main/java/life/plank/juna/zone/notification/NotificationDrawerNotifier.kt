package life.plank.juna.zone.notification

import android.app.*
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.prembros.facilis.util.isNullOrEmpty
import life.plank.juna.zone.*
import life.plank.juna.zone.R.string.*
import life.plank.juna.zone.data.model.FootballLiveData
import life.plank.juna.zone.data.model.notification.*
import life.plank.juna.zone.util.common.JunaDataUtil.findString
import life.plank.juna.zone.util.common.asciiToInt
import org.jetbrains.anko.*

private const val CHANNEL_ID = "juna_notification_channel"
private const val TAG = "DrawerNotifier"
private const val GROUP_SOCIAL_NOTIFICATION = "life.plank.juna.zone.SOCIAL_NOTIFICATION"
private const val GROUP_LIVE_FOOTBALL_NOTIFICATION = "life.plank.juna.zone.LIVE_FOOTBALL_NOTIFICATION"

/**
 * Method to send social interaction notification in the notification drawer
 */
fun SocialNotification.prepareDrawerNotification() {
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
            sendImageNotification(pendingIntent, false)
        findString(intent_post) -> {
            if (!isNullOrEmpty(feedItemIcon) && !isNullOrEmpty(privateBoardIcon ?: lastActorIcon)) {
                sendImageNotification(pendingIntent, true)
            } else if (!isNullOrEmpty(privateBoardIcon ?: lastActorIcon)) {
                sendImageNotification(pendingIntent, false)
            } else {
                sendTextNotification(pendingIntent)
            }
        }
    }
}

/**
 * Method to send live football data notification in the notification drawer
 */
fun FootballLiveData.prepareDrawerNotification() {
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
                            .setStyle(NotificationCompat.BigTextStyle().bigText(notificationMessage))
                            .setGroup(if (this is SocialNotification) GROUP_SOCIAL_NOTIFICATION else GROUP_LIVE_FOOTBALL_NOTIFICATION)
                            .setGroupSummary(true)
                            .build())
}

fun SocialNotification.sendImageNotification(pendingIntent: PendingIntent, isBigImage: Boolean) {
    val notificationManager = ZoneApplication.getContext().getNotificationManager().setNotificationChannel()
    val notificationBuilder = getNotificationBuilder(SpannableStringBuilder(notificationMessage), pendingIntent)
    try {
        ZoneApplication.getContext().doAsync {
            val boardIconBitmap = Glide.with(ZoneApplication.getContext())
                    .asBitmap()
                    .apply(RequestOptions.circleCropTransform())
                    .load(privateBoardIcon ?: lastActorIcon)
                    .submit().get()
            uiThread {
                notificationBuilder.setLargeIcon(boardIconBitmap).setGroup(GROUP_SOCIAL_NOTIFICATION)
                if (isBigImage) {
                    doAsync {
                        val feedItemIconBitmap = Glide.with(ZoneApplication.getContext()).asBitmap().load(feedItemIcon).submit().get()
                        uiThread {
                            notificationBuilder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(feedItemIconBitmap).bigLargeIcon(null))
                        }
                    }
                } else {
                    notificationBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(notificationMessage))
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