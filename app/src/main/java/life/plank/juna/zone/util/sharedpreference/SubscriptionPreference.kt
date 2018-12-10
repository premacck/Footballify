package life.plank.juna.zone.util.sharedpreference

import android.content.SharedPreferences
import com.google.firebase.messaging.FirebaseMessaging
import life.plank.juna.zone.R.string.pref_subscription
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.getSharedPrefs

private fun getSubscriptionPrefs(): SharedPreferences = getSharedPrefs(findString(pref_subscription))

fun subscribeTo(vararg topic: String) {
    topic.forEach {
        FirebaseMessaging.getInstance().subscribeToTopic(it)
        getSubscriptionPrefs().edit().putBoolean(it, true).apply()
    }
}

fun unsubscribeFrom(vararg topic: String) {
    topic.forEach {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(it)
        getSubscriptionPrefs().edit().remove(it).apply()
    }
}

fun clearAllSubscriptions() = getSubscriptionPrefs().all.clear()

fun isSubscribed(topic: String): Boolean = getSubscriptionPrefs().getBoolean(topic, false)
