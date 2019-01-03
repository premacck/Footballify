package life.plank.juna.zone.notification

import android.app.Activity
import android.content.Intent
import life.plank.juna.zone.*
import life.plank.juna.zone.R.string.*
import life.plank.juna.zone.data.model.FootballLiveData
import life.plank.juna.zone.data.model.notification.*
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.util.common.AppConstants.CardNotificationType.*
import life.plank.juna.zone.util.common.JunaDataUtil.findString
import life.plank.juna.zone.view.activity.base.BaseJunaCardActivity
import life.plank.juna.zone.view.activity.home.HomeActivity
import life.plank.juna.zone.view.cardmaker.CreateCardActivity
import life.plank.juna.zone.view.fragment.board.user.JoinBoardPopup
import org.jetbrains.anko.*

fun SocialNotification.getSocialNotificationIntent(): Intent {
    return ZoneApplication.getContext().run {
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

fun FootballLiveData.getLiveFootballNotificationIntent(): Intent {
    return ZoneApplication.getContext().run {
        intentFor<HomeActivity>(
                findString(match_id_string) to matchId,
                findString(intent_live_data_type) to liveDataType
        ).clearTop()
    }
}

fun CardNotification.getCardNotificationIntent(): Intent {
    return ZoneApplication.getContext().run {
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

fun Activity.getLiveDataTypeFromIntent(): String? = intent?.getStringExtra(getString(intent_live_data_type))

fun Activity.getActionFromIntent(): String? = intent?.getStringExtra(getString(intent_action))

fun Activity.getBoardIdFromIntent(): String? = intent?.getStringExtra(getString(intent_board_id))

fun Activity.getChildIdFromIntent(): String? = intent?.getStringExtra(getString(intent_child_id))

fun Activity.getSiblingIdFromIntent(): String? = intent?.getStringExtra(getString(intent_sibling_id))

fun BaseJunaCardActivity.handleFootballLiveDataNotificationIntentIfAny() {
    if (!intent.hasExtra(getString(match_id_string))) return

    restApi()?.run { launchMatchBoard(this, intent.getLongExtra(getString(match_id_string), 0)) }
}

fun InAppNotification.triggerNotificationAction(baseCardActivity: BaseJunaCardActivity?) {
    if (notificationObject == null) return
    when (notificationObject) {
        is SocialNotification -> baseCardActivity?.triggerNotificationIntent((notificationObject as SocialNotification).getSocialNotificationIntent())
        is FootballLiveData -> baseCardActivity?.triggerNotificationIntent((notificationObject as FootballLiveData).getLiveFootballNotificationIntent())
        is CardNotification -> baseCardActivity?.triggerNotificationIntent((notificationObject as CardNotification).getCardNotificationIntent())
    }
}