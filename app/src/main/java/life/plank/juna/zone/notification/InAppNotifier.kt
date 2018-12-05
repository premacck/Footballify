package life.plank.juna.zone.notification

import android.content.Intent
import life.plank.juna.zone.R.string.*
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.ZoneLiveData
import life.plank.juna.zone.data.model.notification.InAppNotification
import life.plank.juna.zone.data.model.notification.JunaNotification
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.util.facilis.findFragment
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.fragment.base.BaseMatchFragment

/**
 * Method to send in-app social interaction notification
 */
fun JunaNotification.sendInAppNotification() {
    ZoneApplication.getContext().sendBroadcast(
            Intent(findString(intent_in_app_notification)).putExtra(findString(intent_juna_notification), this)
    )
}

/**
 * Method to send in-app live football data notification
 */
fun ZoneLiveData.sendInAppNotification() {
    ZoneApplication.getContext().sendBroadcast(
            Intent(findString(intent_in_app_notification)).putExtra(findString(intent_zone_live_data), this)
    )
}

fun BaseCardActivity.handleInAppNotification(junaNotification: JunaNotification) {
//    TODO: defer to active fragments if they are in the foreground, else:
    showInAppNotification(InAppNotification(junaNotification))
}

fun BaseCardActivity.handleInAppNotification(zoneLiveData: ZoneLiveData) {
    (supportFragmentManager.findFragment<BaseMatchFragment>() as? BaseMatchFragment)?.run {
        if (isInForeGround) {
            onZoneLiveDataReceived(zoneLiveData)
        } else {
            zoneLiveData.sendCustomizedNotification { showInAppNotification(InAppNotification(zoneLiveData)) }
        }
    }
            ?: zoneLiveData.sendCustomizedNotification { showInAppNotification(InAppNotification(zoneLiveData)) }
}