package life.plank.juna.zone.notification

// todo: doc: //
import android.app.ActivityManager
import android.content.Context
import com.google.gson.Gson
import life.plank.juna.zone.*
import life.plank.juna.zone.data.model.football.FootballLiveData
import life.plank.juna.zone.data.model.notification.*
import life.plank.juna.zone.injection.module.NetworkModule.GSON
import life.plank.juna.zone.service.CommonDataService.findString
import life.plank.juna.zone.util.common.AppConstants.*
import org.jetbrains.anko.*
import org.json.JSONObject

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

fun FootballLiveData.sendCustomizedNotification(action: () -> Unit) {
    when (liveDataType) {
        LIVE_FOOTBALL_MATCH, MATCH_EVENTS, TIME_STATUS_DATA, LINEUPS_DATA, BOARD_ACTIVATED -> action()
    }
}