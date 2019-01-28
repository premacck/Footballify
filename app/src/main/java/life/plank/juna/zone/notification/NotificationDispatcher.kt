package life.plank.juna.zone.notification

import android.app.ActivityManager
import android.content.Context
import com.google.gson.Gson
import life.plank.juna.zone.*
import life.plank.juna.zone.data.model.football.FootballLiveData
import life.plank.juna.zone.data.model.notification.*
import life.plank.juna.zone.firebase.JunaNotificationService
import life.plank.juna.zone.injection.module.NetworkModule.GSON
import life.plank.juna.zone.service.CommonDataService.findString
import life.plank.juna.zone.util.common.AppConstants.*
import org.jetbrains.anko.*
import org.json.JSONObject

/**
 * Base function for dispatching different notifications to the app.
 * IMPORTANT NOTE: All the new notifications must be dispatched from this method, rather than implementing them directly in [JunaNotificationService]
 */
fun dispatch(dataPayload: Map<String, String>) {
    when {
        dataPayload.containsKey(findString(R.string.intent_action)) -> //        Data payload contains social interaction notification
            dispatchSocialNotification(dataPayload)
        dataPayload.containsKey(findString(R.string.intent_live_data_type)) -> //        Data payload contains football live data notification
            dispatchLiveFootballNotification(dataPayload)
        dataPayload.containsKey(findString(R.string.intent_card_notification_type)) -> //        Data payload contains cards' notification
            dispatchCardNotification(dataPayload)
    }
}

/**
 * Function to dispatch [SocialNotification] to the app
 * If the app is in foreground, then an 'in-app' notification will be sent, else notification will be sent in the notification drawer
 */
fun dispatchSocialNotification(dataPayload: Map<String, String>) {
    ZoneApplication.getContext().doAsync {
        val junaNotification = GSON.fromJson<SocialNotification>(GSON.toJsonTree(dataPayload.toMap()), SocialNotification::class.java)
        uiThread {
            if (it.isForeground()) {
                junaNotification.sendInAppNotification()
            } else {
                junaNotification.prepareDrawerNotification()
            }
        }
    }
}

/**
 * Function to dispatch [FootballLiveData] to the app
 * If the app is in foreground, then an 'in-app' notification will be sent, else notification will be sent in the notification drawer
 */
fun dispatchLiveFootballNotification(dataPayload: Map<String, String>) {
    ZoneApplication.getContext().doAsync {
        val zoneLiveData = Gson().fromJson<FootballLiveData>(JSONObject(dataPayload).toString(), FootballLiveData::class.java)
        uiThread {
            if (it.isForeground()) {
                zoneLiveData.sendInAppNotification()
            } else {
                zoneLiveData.prepareDrawerNotification()
            }
        }
    }
}

/**
 * Function to dispatch [CardNotification] to the app
 * If the app is in foreground, then an 'in-app' notification will be sent, else notification will be sent in the notification drawer
 */
fun dispatchCardNotification(dataPayload: Map<String, String>) {
    ZoneApplication.getContext().doAsync {
        val cardNotification = Gson().fromJson<CardNotification>(JSONObject(dataPayload).toString(), CardNotification::class.java)
        uiThread {
            if (it.isForeground()) {
                cardNotification.sendInAppNotification()
            } else {
                cardNotification.prepareDrawerNotification()
            }
        }
    }
}

/**
 * Function to determine whether the app is in the foreground or not
 */
private fun Context.isForeground(): Boolean {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val appProcesses = activityManager.runningAppProcesses ?: return false
    val packageName = packageName
    for (appProcess in appProcesses) {
        if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName == packageName) {
            return true
        }
    }
    return false
}

/**
 * Function to check the [FootballLiveData] notification, filter and show only the specified notification types.
 * This decision was made because there are a lot of frequent notifications coming in case of live football match, like [COMMENTARY_DATA],
 * which might be not optimal to flood the user with too many notification during a match.
 */
fun FootballLiveData.sendCustomizedNotification(action: () -> Unit) {
    when (liveDataType) {
        LIVE_FOOTBALL_MATCH, MATCH_EVENTS, TIME_STATUS_DATA, LINEUPS_DATA, BOARD_ACTIVATED -> action()
    }
}