package life.plank.juna.zone.notification

import android.app.ActivityManager
import android.content.Context
import com.google.gson.Gson
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.FootballLiveData
import life.plank.juna.zone.data.model.notification.SocialNotification
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.util.sharedpreference.PreferenceManager
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject

fun dispatch(dataPayload: Map<String, String>) {
    if (dataPayload.containsKey(findString(R.string.intent_action))) {
//        Data payload contains social interaction notification
        dispatchSocialNotification(dataPayload)
    } else if (dataPayload.containsKey(findString(R.string.intent_live_data_type))) {
//        Data payload contains football live data notification
        dispatchLiveFootballNotification(dataPayload)
    }
}

fun dispatchSocialNotification(dataPayload: Map<String, String>) {
    ZoneApplication.getContext().doAsync {
        val junaNotification = Gson().fromJson<SocialNotification>(JSONObject(dataPayload).toString(), SocialNotification::class.java)
        if (!junaNotification.userHandles.contains(PreferenceManager.CurrentUser.getHandle())) {
            uiThread {
                if (it.isForeground()) {
                    junaNotification.sendInAppNotification()
                } else {
                    junaNotification.prepareDrawerNotification()
                }
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