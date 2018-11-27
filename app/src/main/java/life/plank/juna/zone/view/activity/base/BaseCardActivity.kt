package life.plank.juna.zone.view.activity.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.view.ViewGroup
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.ZoneLiveData
import life.plank.juna.zone.data.model.notification.InAppNotification
import life.plank.juna.zone.data.model.notification.JunaNotification
import life.plank.juna.zone.notification.handleInAppNotification
import life.plank.juna.zone.util.customview.InAppNotificationLayout
import life.plank.juna.zone.util.facilis.BaseNavigationHelperActivity
import life.plank.juna.zone.util.facilis.getIfPresent
import life.plank.juna.zone.util.facilis.then

abstract class BaseCardActivity : BaseNavigationHelperActivity() {

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.hasExtra(getString(R.string.intent_zone_live_data))) {
                handleInAppNotification(intent.getParcelableExtra<ZoneLiveData>(getString(R.string.intent_zone_live_data)))
            } else if (intent.hasExtra(getString(R.string.intent_juna_notification))) {
                handleInAppNotification(intent.getParcelableExtra<JunaNotification>(getString(R.string.intent_juna_notification)))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(mMessageReceiver, IntentFilter(getString(R.string.intent_in_app_notification)))
    }

    override fun onPause() {
        super.onPause()
        try {
            unregisterReceiver(mMessageReceiver)
        } catch (e: Exception) {
            Log.e("unregisterReceiver", e.message, e)
        }
    }

    fun showInAppNotification(inAppNotification: InAppNotification) {
        val container = findViewById<ViewGroup>(getFragmentContainer())
        val notificationLayout = container.getIfPresent<InAppNotificationLayout>()

        if (notificationLayout == null) {
            addNotificationView(container, inAppNotification)
            return
        }
        notificationLayout.dismiss()?.then {
            container.removeView(notificationLayout)
            addNotificationView(container, inAppNotification)
        }
    }

    private fun addNotificationView(container: ViewGroup, inAppNotification: InAppNotification) {
        val notificationLayout = InAppNotificationLayout(this)
        container.addView(notificationLayout)
        notificationLayout.load(inAppNotification, this)
    }
}