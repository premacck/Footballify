package life.plank.juna.zone.view.fragment.base

import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.FeedEntry
import life.plank.juna.zone.data.model.ZoneLiveData
import life.plank.juna.zone.data.model.notification.JunaNotification
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.DataUtil
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.util.common.errorToast
import life.plank.juna.zone.util.common.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import java.net.HttpURLConnection.HTTP_OK

abstract class BaseMatchFragment : CardTileFragment() {

    fun handleInAppNotificationIntent(intent: Intent) {
        val junaNotification = intent.getParcelableExtra<JunaNotification>(findString(R.string.intent_juna_notification))
        restApi().getFeedEntry(junaNotification.feedItemId, getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e("getFeedEntry()", "ERROR: ", it)
        }, {
            when (it.code()) {
                HTTP_OK -> it.body()?.run { onInAppNotificationReceived(this) }
                else -> errorToast(R.string.failed_to_retrieve_feed, it)
            }
        })
    }

    fun handleZoneLiveData(intent: Intent) {
        onZoneLiveDataReceived(DataUtil.getZoneLiveData(intent, getString(R.string.intent_zone_live_data), gson()))
    }

    abstract fun gson(): Gson

    abstract fun restApi(): RestApi

    abstract fun onInAppNotificationReceived(feedEntry: FeedEntry)

    abstract fun onZoneLiveDataReceived(zoneLiveData: ZoneLiveData)
}