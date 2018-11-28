package life.plank.juna.zone.notification

import android.app.ActivityManager
import android.content.Context
import com.google.gson.Gson
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.ZoneLiveData
import life.plank.juna.zone.data.model.notification.JunaNotification
import life.plank.juna.zone.util.DataUtil.findString
import life.plank.juna.zone.util.PreferenceManager
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject

fun dispatch(dataPayload: Map<String, String>) {
    if (dataPayload.containsKey(findString(R.string.intent_action))) {
//        Data payload contains social interaction notification
        dispatchSocialNotification(dataPayload)
    } else if (dataPayload.containsKey(findString(R.string.intent_live_event_type))) {
//        Data payload contains football live data notification
        dispatchLiveFootballNotification(dataPayload)
    }
}

fun dispatchSocialNotification(dataPayload: Map<String, String>) {
    ZoneApplication.getContext().doAsync {
        val junaNotification = Gson().fromJson<JunaNotification>(JSONObject(dataPayload).toString(), JunaNotification::class.java)
        if (junaNotification.userHandle != PreferenceManager.CurrentUser.getHandle()) {
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
        val zoneLiveData = Gson().fromJson<ZoneLiveData>(JSONObject(dataPayload).toString(), ZoneLiveData::class.java)
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