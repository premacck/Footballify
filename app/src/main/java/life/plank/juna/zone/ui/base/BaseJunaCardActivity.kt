package life.plank.juna.zone.ui.base

import android.content.*
import android.util.Log
import android.view.ViewGroup
import com.prembros.facilis.activity.BaseCardActivity
import com.prembros.facilis.util.*
import life.plank.juna.zone.R
import life.plank.juna.zone.component.customview.InAppNotificationLayout
import life.plank.juna.zone.data.api.RestApi
import life.plank.juna.zone.data.model.football.FootballLiveData
import life.plank.juna.zone.data.model.notification.*
import life.plank.juna.zone.notification.handleInAppNotification
import life.plank.juna.zone.util.view.dismissBoomMenuIfOpen

abstract class BaseJunaCardActivity : BaseCardActivity() {

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when {
                intent.hasExtra(getString(R.string.intent_zone_live_data)) -> handleInAppNotification(intent.getParcelableExtra<FootballLiveData>(getString(R.string.intent_zone_live_data)))
                intent.hasExtra(getString(R.string.intent_juna_notification)) -> handleInAppNotification(intent.getParcelableExtra<SocialNotification>(getString(R.string.intent_juna_notification)))
                intent.hasExtra(getString(R.string.intent_card_notification)) -> handleInAppNotification(intent.getParcelableExtra<CardNotification>(getString(R.string.intent_card_notification)))
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
        notificationLayout.dismiss().then {
            container.removeView(notificationLayout)
            addNotificationView(container, inAppNotification)
        }
    }

    private fun addNotificationView(container: ViewGroup, inAppNotification: InAppNotification) {
        val notificationLayout = InAppNotificationLayout(this)
        container.addView(notificationLayout)
        notificationLayout.load(inAppNotification, this)
    }

    abstract fun restApi(): RestApi?

    override fun onBackPressed() {
        if (dismissBoomMenuIfOpen()) {
            super.onBackPressed()
        }
    }
}