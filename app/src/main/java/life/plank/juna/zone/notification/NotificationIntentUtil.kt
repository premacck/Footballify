package life.plank.juna.zone.notification

import android.app.Activity
import android.content.Intent
import life.plank.juna.zone.R
import life.plank.juna.zone.R.string.*
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.component.helper.findAndLaunchBoardById
import life.plank.juna.zone.component.helper.launchMatchBoard
import life.plank.juna.zone.data.model.football.FootballLiveData
import life.plank.juna.zone.data.model.notification.*
import life.plank.juna.zone.service.CommonDataService.findString
import life.plank.juna.zone.ui.base.BaseJunaCardActivity
import life.plank.juna.zone.ui.board.fragment.user.JoinBoardPopup
import life.plank.juna.zone.ui.home.HomeActivity
import life.plank.juna.zone.ui.user.card.CreateCardActivity
import life.plank.juna.zone.util.common.AppConstants.CardNotificationType.*
import life.plank.juna.zone.util.common.customToast
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor

/**
 * Function to get the intent which contains the relevant extras to be used by boards to handle the [SocialNotification] notification
 */
fun SocialNotification.getSocialNotificationIntent(): Intent {
    return ZoneApplication.appContext.run {
        when (action) {
            findString(intent_invite) -> {
                intentFor<HomeActivity>(
                        findString(intent_action) to action,
                        findString(intent_board_id) to parentId
                ).clearTop()
            }
            findString(intent_post), findString(intent_react) -> {
                intentFor<HomeActivity>(
                        findString(intent_action) to action,
                        findString(intent_board_id) to parentId,
                        findString(intent_child_id) to childId
                ).clearTop()
            }
            findString(intent_comment) -> {
                intentFor<HomeActivity>(
                        findString(intent_action) to action,
                        findString(intent_board_id) to parentId,
                        findString(intent_child_id) to childId,
                        findString(intent_sibling_id) to siblingId
                ).clearTop()
            }
            else -> intentFor<HomeActivity>(findString(intent_action) to action).clearTop()
        }
    }
}

/**
 * Function to get the intent which contains the relevant extras to be used by boards to handle the [FootballLiveData] notification
 */
fun FootballLiveData.getLiveFootballNotificationIntent(): Intent {
    return ZoneApplication.appContext.run {
        intentFor<HomeActivity>(
                findString(match_id_string) to matchId,
                findString(intent_live_data_type) to liveDataType
        ).clearTop()
    }
}

/**
 * Function to get the intent which contains the relevant extras to be used by boards to handle the [CardNotification] notification
 */
fun CardNotification.getCardNotificationIntent(): Intent {
    return ZoneApplication.appContext.run {
        when (cardNotificationType) {
            READY_TO_CREATE -> intentFor<CreateCardActivity>(findString(intent_card_notification_type) to cardNotificationType).clearTop()
            PUBLISHED -> {/*TODO: open card*/ intentFor<HomeActivity>(findString(intent_card_notification_type) to cardNotificationType).clearTop()
            }
            AVAILABLE -> {/*TODO: open card*/ intentFor<HomeActivity>(findString(intent_card_notification_type) to cardNotificationType).clearTop()
            }
            MODIFIED -> {/*TODO: open card*/ intentFor<HomeActivity>(findString(intent_card_notification_type) to cardNotificationType).clearTop()
            }
            else -> {/*TODO: open card*/ intentFor<HomeActivity>().clearTop()
            }
        }
    }
}

/**
 * Common function to apply the intent with respective extras and trigger the launch of respective board/card when opening an [InAppNotification]
 */
fun InAppNotification.triggerNotificationAction(baseCardActivity: BaseJunaCardActivity?) {
    if (notificationObject == null) return
    when (notificationObject) {
        is SocialNotification -> baseCardActivity?.triggerNotificationIntent((notificationObject as SocialNotification).getSocialNotificationIntent())
        is FootballLiveData -> baseCardActivity?.triggerNotificationIntent((notificationObject as FootballLiveData).getLiveFootballNotificationIntent())
        is CardNotification -> baseCardActivity?.triggerNotificationIntent((notificationObject as CardNotification).getCardNotificationIntent())
    }
}

/**
 * Function to apply the intent with respective extras and trigger the launch of respective board when opening a notification from notification feed
 */
fun BaseJunaCardActivity.triggerNotificationIntent(intent: Intent) {
    this.intent = intent
    restApi()?.run {
        when {
            intent.hasExtra(findString(intent_action)) -> {
                when (getActionFromIntent()) {
                    findString(intent_invite) -> pushPopup(JoinBoardPopup.newInstance(getBoardIdFromIntent()!!))
                    else -> findAndLaunchBoardById(this)
                }
            }
            intent.hasExtra(findString(match_id_string)) -> launchMatchBoard(this, intent.getLongExtra(getString(R.string.match_id_string), 0))
            intent.hasExtra(findString(intent_card_notification_type)) -> {
                when (intent.getStringExtra(findString(intent_card_notification_type))) {
//                    TODO: open respective pages
                    READY_TO_CREATE -> startActivity(intentFor<CreateCardActivity>())
                    PUBLISHED -> {
                    }
                    AVAILABLE -> {
                    }
                    MODIFIED -> {
                    }
                }
            }
        }
    } ?: customToast(R.string.rest_api_is_null)
}

/**
 * Function to handle and open the respective board, if [SocialNotification] extras are found in the intent.
 */
fun BaseJunaCardActivity.handleSocialNotificationIntentIfAny() {
    intent?.run {
        if (!hasExtra(getString(intent_action)) || !hasExtra(getString(intent_board_id))) return
    } ?: return
    restApi()?.run {
        when (getActionFromIntent()) {
            findString(intent_invite) -> pushPopup(JoinBoardPopup.newInstance(getBoardIdFromIntent()!!))
//            TODO: open board -> switch to tiles -> open feedItem
            findString(intent_post) -> findAndLaunchBoardById(this)
//            TODO: open board -> switch to forum -> if(parentCommentId == null) { scroll to commentId } else { scroll to a combination of commentId and parentCommentId }
            findString(intent_react) -> findAndLaunchBoardById(this)
//            TODO: open board -> switch to tiles -> if(feedItemId != null) { open feedItem }
            findString(intent_comment) -> findAndLaunchBoardById(this)
        }
    }
}

/**
 * Function to handle and open the respective board, if [FootballLiveData] extras are found in the intent.
 */
fun BaseJunaCardActivity.handleFootballLiveDataNotificationIntentIfAny() {
    if (!intent.hasExtra(getString(match_id_string))) return

    restApi()?.run { launchMatchBoard(this, intent.getLongExtra(getString(match_id_string), 0)) }
}

//region Helper functions to get extras related to SocialNotification and FootballLiveData
fun Activity.getLiveDataTypeFromIntent(): String? = intent?.getStringExtra(getString(intent_live_data_type))

fun Activity.getActionFromIntent(): String? = intent?.getStringExtra(getString(intent_action))

fun Activity.getBoardIdFromIntent(): String? = intent?.getStringExtra(getString(intent_board_id))

fun Activity.getChildIdFromIntent(): String? = intent?.getStringExtra(getString(intent_child_id))

fun Activity.getSiblingIdFromIntent(): String? = intent?.getStringExtra(getString(intent_sibling_id))
//endregion