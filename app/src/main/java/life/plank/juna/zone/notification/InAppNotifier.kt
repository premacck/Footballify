package life.plank.juna.zone.notification

import android.content.Intent
import life.plank.juna.zone.R.string.*
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.ZoneLiveData
import life.plank.juna.zone.data.model.notification.JunaNotification
import life.plank.juna.zone.util.DataUtil.findString

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