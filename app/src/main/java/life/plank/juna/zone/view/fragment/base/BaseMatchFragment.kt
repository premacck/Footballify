package life.plank.juna.zone.view.fragment.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.google.gson.Gson
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.FeedEntry
import life.plank.juna.zone.data.model.ZoneLiveData
import life.plank.juna.zone.data.model.notification.JunaNotification
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.DataUtil
import life.plank.juna.zone.util.DataUtil.findString
import life.plank.juna.zone.util.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.errorToast
import life.plank.juna.zone.util.setObserverThreadsAndSmartSubscribe
import java.net.HttpURLConnection.HTTP_OK

abstract class BaseMatchFragment : CardTileFragment() {

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.hasExtra(getString(R.string.intent_zone_live_data))) {
                handleZoneLiveData(intent)
            } else if (intent.hasExtra(getString(R.string.intent_content_type))) {
                handleInAppNotificationIntent(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        context?.registerReceiver(mMessageReceiver, IntentFilter(getString(R.string.intent_in_app_notification)))
    }

    override fun onPause() {
        super.onPause()
        try {
            context?.unregisterReceiver(mMessageReceiver)
        } catch (e: Exception) {
            Log.e("unregisterReceiver", e.message, e)
        }
    }

    protected fun handleInAppNotificationIntent(intent: Intent) {
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

    protected fun handleZoneLiveData(intent: Intent) {
        onZoneLiveDataReceived(DataUtil.getZoneLiveData(intent, getString(R.string.intent_zone_live_data), gson()))
    }

    abstract fun gson(): Gson

    abstract fun restApi(): RestApi

    abstract fun onInAppNotificationReceived(feedEntry: FeedEntry)

    abstract fun onZoneLiveDataReceived(zoneLiveData: ZoneLiveData)
}