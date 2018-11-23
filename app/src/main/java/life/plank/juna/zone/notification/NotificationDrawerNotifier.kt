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
import life.plank.juna.zone.data.model.notification.JunaNotification
import life.plank.juna.zone.util.DataUtil.findString
import life.plank.juna.zone.util.PreferenceManager
import life.plank.juna.zone.view.activity.board.JoinBoardActivity
import life.plank.juna.zone.view.activity.home.HomeActivity
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
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
            ZoneApplication.getContext().run {
                when (action) {
                    findString(intent_invite) -> {
                        intentFor<JoinBoardActivity>(findString(intent_board_id) to boardId).clearTop()
                    }
                    findString(intent_image), findString(intent_video), findString(intent_board_react) -> {
                        intentFor<HomeActivity>(findString(intent_board_id) to boardId).clearTop()
                    }
                    findString(intent_board_comment), findString(intent_board_comment_reply) -> {
                        intentFor<HomeActivity>(
                                findString(intent_board_id) to boardId,
                                findString(intent_comment_id) to commentId
                        ).clearTop()
                    }
                    findString(intent_feed_item_comment), findString(intent_feed_item_reply), findString(intent_feed_item_react) -> {
                        intentFor<HomeActivity>(
                                findString(intent_board_id) to boardId,
                                findString(intent_feed_item_id) to feedItemId,
                                findString(intent_comment_id) to commentId
                        ).clearTop()
                    }
                    else -> intentFor<HomeActivity>().clearTop()
                }
            },
            FLAG_ONE_SHOT
    )
    if (actor != PreferenceManager.CurrentUser.getDisplayName()) {
        when (action) {
            findString(intent_invite), findString(intent_board_comment), findString(intent_feed_item_comment),
            findString(intent_feed_item_reply), findString(intent_feed_item_react), findString(intent_kick),
            findString(intent_board_comment_reply) -> {
                sendTextNotification(pendingIntent)
            }
            findString(intent_image), findString(intent_video), findString(intent_board_react) -> {
                sendNotification(pendingIntent)
            }
        }
    }
}

/**
 * Method to send live football data notification in the notification drawer
 */
fun ZoneLiveData.prepareDrawerNotification() {
}

fun JunaNotification.sendTextNotification(pendingIntent: PendingIntent) {
    ZoneApplication.getContext()
            .getNotificationManager()
            .setNotificationChannel()
            .notify(0, getNotificationBuilder(buildNotificationMessage(), pendingIntent).build())
}

fun JunaNotification.sendNotification(pendingIntent: PendingIntent) {
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
                notificationBuilder.setStyle(NotificationCompat.BigPictureStyle().bigLargeIcon(bitmap))
                notificationManager.notify(1, notificationBuilder.build())
            }
        }
    } catch (e: Exception) {
        Log.e(TAG, "prepareDrawerNotification(): ", e)
        notificationManager.notify(0, notificationBuilder.build())
    }
}

fun getNotificationBuilder(messageBody: SpannableStringBuilder, pendingIntent: PendingIntent): NotificationCompat.Builder {
    return NotificationCompat.Builder(ZoneApplication.getContext(), CHANNEL_ID)
            .setContentTitle(findString(app_name))
            .setContentText(messageBody)
            .setAutoCancel(true)
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