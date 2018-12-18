package life.plank.juna.zone.notification

import android.content.Intent
import life.plank.juna.zone.R.string.*
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.FootballLiveData
import life.plank.juna.zone.data.model.notification.InAppNotification
import life.plank.juna.zone.data.model.notification.SocialNotification
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.util.facilis.findFragment
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.fragment.base.BaseMatchFragment
import life.plank.juna.zone.view.fragment.base.CardTileFragment

/**
 * Method to send in-app social interaction notification
 */
fun SocialNotification.sendInAppNotification() {
    ZoneApplication.getContext().sendBroadcast(
            Intent(findString(intent_in_app_notification)).putExtra(findString(intent_juna_notification), this)
    )
}

/**
 * Method to send in-app live football data notification
 */
fun FootballLiveData.sendInAppNotification() {
    ZoneApplication.getContext().sendBroadcast(
            Intent(findString(intent_in_app_notification)).putExtra(findString(intent_zone_live_data), this)
    )
}

fun BaseCardActivity.handleInAppNotification(socialNotification: SocialNotification) {
    when (socialNotification.action) {
        findString(intent_invite) -> showInAppNotification(InAppNotification(socialNotification))
        else -> {
            (supportFragmentManager.findFragment<CardTileFragment>() as? CardTileFragment)?.run {
                if (isInForeGround) {
                    onSocialNotificationReceive(socialNotification)
                } else {
                    showInAppNotification(InAppNotification(socialNotification))
                }
            } ?: showInAppNotification(InAppNotification(socialNotification))
        }
    }

}

fun BaseCardActivity.handleInAppNotification(footballLiveData: FootballLiveData) {
    (supportFragmentManager.findFragment<BaseMatchFragment>() as? BaseMatchFragment)?.run {
        if (isInForeGround) {
            onZoneLiveDataReceived(footballLiveData)
        } else {
            footballLiveData.sendCustomizedNotification { showInAppNotification(InAppNotification(footballLiveData)) }
        }
    } ?: footballLiveData.sendCustomizedNotification {
        showInAppNotification(InAppNotification(footballLiveData))
    }
}