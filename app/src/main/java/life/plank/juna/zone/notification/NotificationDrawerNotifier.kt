package life.plank.juna.zone.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.text.SpannableStringBuilder
import android.util.Log
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
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.CurrentUser.getUserId
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

private const val CHANNEL_ID = "juna_notification_channel"
private const val TAG = "DrawerNotifier"

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
            sendNotification(pendingIntent, true)
    }
}

/**
 * Method to send live football data notification in the notification drawer
 */
fun ZoneLiveData.prepareDrawerNotification() {
    when (liveDataType) {
        LIVE_FOOTBALL_MATCH, MATCH_EVENTS, TIME_STATUS_DATA, LINEUPS_DATA, BOARD_ACTIVATED ->
            sendTextNotification(PendingIntent.getActivity(ZoneApplication.getContext(), 0, getLiveFootballNotificationIntent(), FLAG_ONE_SHOT))
    }
}

fun BaseInAppNotification.sendTextNotification(pendingIntent: PendingIntent) {
    val notificationMessage = getNotificationMessage()
    ZoneApplication.getContext()
            .getNotificationManager()
            .setNotificationChannel()
            .notify(getUserId()?.asciiToInt()
                    ?: 0, getNotificationBuilder(notificationMessage, pendingIntent)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(notificationMessage))
                    .build())
}

fun JunaNotification.sendNotification(pendingIntent: PendingIntent, isBigImage: Boolean) {
    val notificationManager = ZoneApplication.getContext().getNotificationManager().setNotificationChannel()
    val notificationBuilder = getNotificationBuilder(buildNotificationMessage(), pendingIntent)
    try {
        ZoneApplication.getContext().doAsync {
            val bitmap =
                    Glide.with(ZoneApplication.getContext())
                            .asBitmap()
                            .load(imageUrl)
                            .submit()
                            .get()
            uiThread {
                if (isBigImage) {
                    notificationBuilder.setStyle(NotificationCompat.BigPictureStyle().bigLargeIcon(bitmap))
                } else {
                    notificationBuilder.setLargeIcon(bitmap)
                }
                notificationManager.notify(getUserId()?.asciiToInt()
                        ?: 0, notificationBuilder.build())
            }
        }
    } catch (e: Exception) {
        Log.e(TAG, "prepareDrawerNotification(): ", e)
        notificationManager.notify(getUserId()?.asciiToInt() ?: 0, notificationBuilder.build())
    }
}

fun getNotificationBuilder(notificationMessage: SpannableStringBuilder, pendingIntent: PendingIntent): NotificationCompat.Builder {
    return NotificationCompat.Builder(ZoneApplication.getContext(), CHANNEL_ID)
            .setContentTitle(findString(app_name))
            .setContentText(notificationMessage)
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