package life.plank.juna.zone.notification

import android.content.Intent
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.notification.JunaNotification
import life.plank.juna.zone.util.DataUtil.findString
import life.plank.juna.zone.view.activity.board.JoinBoardActivity
import life.plank.juna.zone.view.activity.home.HomeActivity
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor

fun JunaNotification.getNotificationIntent(): Intent {
    return ZoneApplication.getContext().run {
        when (action) {
            findString(R.string.intent_invite) -> {
                intentFor<JoinBoardActivity>(
                        findString(R.string.intent_action) to action,
                        findString(R.string.intent_board_id) to boardId
                ).clearTop()
            }
            findString(R.string.intent_post), findString(R.string.intent_react) -> {
                intentFor<HomeActivity>(
                        findString(R.string.intent_action) to action,
                        findString(R.string.intent_board_id) to boardId,
                        findString(R.string.intent_feed_item_id) to feedItemId
                ).clearTop()
            }
            findString(R.string.intent_comment) -> {
                intentFor<HomeActivity>(
                        findString(R.string.intent_action) to action,
                        findString(R.string.intent_board_id) to boardId,
                        findString(R.string.intent_comment_id) to commentId,
                        findString(R.string.intent_parent_comment_id) to parentCommentId
                ).clearTop()
            }
            else -> intentFor<HomeActivity>(findString(R.string.intent_action) to action).clearTop()
        }
    }
}