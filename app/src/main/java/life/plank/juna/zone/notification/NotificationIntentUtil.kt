package life.plank.juna.zone.notification

import android.app.Activity
import android.content.Intent
import life.plank.juna.zone.R
import life.plank.juna.zone.R.string.*
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.ZoneLiveData
import life.plank.juna.zone.data.model.notification.InAppNotification
import life.plank.juna.zone.data.model.notification.JunaNotification
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.util.common.findAndLaunchBoardById
import life.plank.juna.zone.util.common.launchMatchBoard
import life.plank.juna.zone.util.facilis.BaseCard
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.activity.home.HomeActivity
import life.plank.juna.zone.view.fragment.board.user.JoinBoardPopup
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

fun JunaNotification.getSocialNotificationIntent(): Intent {
    return ZoneApplication.getContext().run {
        when (action) {
            findString(intent_invite) -> {
                intentFor<HomeActivity>(
                        findString(intent_action) to action,
                        findString(intent_board_id) to boardId
                ).clearTop()
            }
            findString(intent_post), findString(intent_react) -> {
                intentFor<HomeActivity>(
                        findString(intent_action) to action,
                        findString(intent_board_id) to boardId,
                        findString(intent_feed_item_id) to feedItemId
                ).clearTop()
            }
            findString(intent_comment) -> {
                intentFor<HomeActivity>(
                        findString(intent_action) to action,
                        findString(intent_board_id) to boardId,
                        findString(intent_comment_id) to commentId,
                        findString(intent_parent_comment_id) to parentCommentId
                ).clearTop()
            }
            else -> intentFor<HomeActivity>(findString(intent_action) to action).clearTop()
        }
    }
}

fun ZoneLiveData.getLiveFootballNotificationIntent(): Intent {
    return ZoneApplication.getContext().run {
        intentFor<HomeActivity>(
                findString(match_id_string) to matchId,
                findString(intent_live_data_type) to liveDataType
        ).clearTop()
    }
}

fun BaseCardActivity.triggerNotificationIntent(intent: Intent) {
    this.intent = intent
    restApi()?.run {
        when {
            intent.hasExtra(findString(intent_action)) -> {
                when (getActionFromIntent()) {
                    findString(intent_invite) -> pushPopup(JoinBoardPopup.newInstance(intent.getStringExtra(findString(intent_board_id))))
                    else -> findAndLaunchBoardById(this)
                }
            }
            intent.hasExtra(findString(match_id_string)) -> launchMatchBoard(this, intent.getLongExtra(getString(R.string.match_id_string), 0))
        }
    } ?: toast(R.string.rest_api_is_null)
}

fun BaseCardActivity.handleSocialNotificationIntentIfAny() {
    intent?.run {
        if (!hasExtra(getString(intent_action)) || !hasExtra(getString(intent_board_id))) return

        val action = getStringExtra(getString(intent_action))
        val boardId = getStringExtra(getString(intent_board_id))
//        TODO: un-comment when required
//        val commentId = getStringExtra(getString(intent_comment_id))
//        val feedItemId = getStringExtra(getString(intent_feed_item_id))
//        val parentCommentId = getStringExtra(getString(intent_parent_comment_id))

        restApi()?.run {
            when (getActionFromIntent()) {
                findString(intent_invite) -> pushPopup(JoinBoardPopup.newInstance(boardId))
//            TODO: open board -> switch to tiles -> open feedItem
                findString(intent_post) -> findAndLaunchBoardById(this)
//            TODO: open board -> switch to forum -> if(parentCommentId == null) { scroll to commentId } else { scroll to a combination of commentId and parentCommentId }
                findString(intent_react) -> findAndLaunchBoardById(this)
//            TODO: open board -> switch to tiles -> if(feedItemId != null) { open feedItem }
                findString(intent_comment) -> findAndLaunchBoardById(this)
            }
        }
    }
}

fun Activity.getLiveDataTypeFromIntent(): String? = intent?.getStringExtra(getString(intent_live_data_type))

fun Activity.getActionFromIntent(): String? = intent?.getStringExtra(getString(intent_action))

fun Activity.getBoardIdFromIntent(): String? = intent?.getStringExtra(getString(intent_board_id))

fun Activity.getFeedItemIdFromIntent(): String? = intent?.getStringExtra(getString(intent_feed_item_id))

fun Activity.getCommentIdFromIntent(): String? = intent?.getStringExtra(getString(intent_comment_id))

fun Activity.getParentCommentIdFromIntent(): String? = intent?.getStringExtra(getString(intent_parent_comment_id))

fun BaseCardActivity.handleFootballLiveDataNotificationIntentIfAny() {
    if (!intent.hasExtra(getString(match_id_string))) return

    restApi()?.run { launchMatchBoard(this, intent.getLongExtra(getString(match_id_string), 0)) }
}

fun InAppNotification.triggerNotificationAction(baseCardActivity: BaseCardActivity?) {
    when {
        junaNotification != null -> baseCardActivity?.triggerNotificationIntent(junaNotification?.getSocialNotificationIntent()!!)
        zoneLiveData != null -> baseCardActivity?.triggerNotificationIntent(zoneLiveData?.getLiveFootballNotificationIntent()!!)
    }
}

fun BaseCard.getSocialNotificationIntentActionFromActivity(): String? = activity?.getActionFromIntent()

fun BaseCard.getLiveFootballNotificationIntentActionFromActivity(): String? = activity?.getLiveDataTypeFromIntent()