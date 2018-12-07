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
import life.plank.juna.zone.view.fragment.base.CardTileFragment

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
    when (junaNotification.action) {
        findString(intent_invite) -> showInAppNotification(InAppNotification(junaNotification))
        else -> {
            (supportFragmentManager.findFragment<CardTileFragment>() as? CardTileFragment)?.run {
                if (isInForeGround) {
                    onSocialNotificationReceive(junaNotification)
                } else {
                    showInAppNotification(InAppNotification(junaNotification))
                }
            } ?: showInAppNotification(InAppNotification(junaNotification))
        }
    }

}

fun BaseCardActivity.handleInAppNotification(zoneLiveData: ZoneLiveData) {
    (supportFragmentManager.findFragment<BaseMatchFragment>() as? BaseMatchFragment)?.run {
        if (isInForeGround) {
            onZoneLiveDataReceived(zoneLiveData)
        } else {
            zoneLiveData.sendCustomizedNotification { showInAppNotification(InAppNotification(zoneLiveData)) }
        }
    } ?: zoneLiveData.sendCustomizedNotification {
        showInAppNotification(InAppNotification(zoneLiveData))
    }
}