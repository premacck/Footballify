package life.plank.juna.zone.notification

import android.content.Intent
import com.prembros.facilis.util.findFragment
import life.plank.juna.zone.R.string.*
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.football.FootballLiveData
import life.plank.juna.zone.data.model.notification.*
import life.plank.juna.zone.service.CommonDataService.findString
import life.plank.juna.zone.view.base.BaseJunaCardActivity
import life.plank.juna.zone.view.base.fragment.*

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

/**
 * Method to send in-app card notification
 */
fun CardNotification.sendInAppNotification() {
    ZoneApplication.getContext().sendBroadcast(
            Intent(findString(intent_in_app_notification)).putExtra(findString(intent_card_notification), this)
    )
}

fun BaseJunaCardActivity.handleInAppNotification(socialNotification: SocialNotification) {
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

fun BaseJunaCardActivity.handleInAppNotification(footballLiveData: FootballLiveData) {
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

fun BaseJunaCardActivity.handleInAppNotification(cardNotification: CardNotification) {
    showInAppNotification(InAppNotification(cardNotification))
}