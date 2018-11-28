package life.plank.juna.zone.notification

import android.text.SpannableStringBuilder
import life.plank.juna.zone.R.string.*
import life.plank.juna.zone.data.model.ZoneLiveData
import life.plank.juna.zone.data.model.notification.JunaNotification
import life.plank.juna.zone.util.AppConstants.*
import life.plank.juna.zone.util.DataUtil.findString
import life.plank.juna.zone.util.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.bold
import life.plank.juna.zone.util.semiBold

/**
 * Method to get suitable text for the social interaction notification message
 */
fun JunaNotification.buildNotificationMessage(): SpannableStringBuilder {
    val spannableStringBuilder = SpannableStringBuilder(userHandle.bold()).append(SINGLE_SPACE)
    when (action) {
        findString(intent_invite) ->
            spannableStringBuilder.append(findString(invited_you_to_board_x))
        findString(intent_post) -> {
            when (feedItemType) {
                IMAGE ->
                    spannableStringBuilder.append(findString(posted_image_on_board_x))
                VIDEO ->
                    spannableStringBuilder.append(findString(posted_video_on_board_x))
                AUDIO ->
                    spannableStringBuilder.append(findString(posted_audio_on_board_x))
                ROOT_COMMENT ->
                    spannableStringBuilder.append(findString(posted_on_board_x))
            }
        }
        findString(intent_comment) -> {
            if (isNullOrEmpty(parentCommentId)) {
                spannableStringBuilder.append(findString(commented_on_board_x))
            } else {
                spannableStringBuilder.append(findString(replied_on_))
            }
        }
        findString(intent_react) -> {
            if (isNullOrEmpty(feedItemId)) {
                spannableStringBuilder.append(findString(reacted_on_board_x))
            } else {
                spannableStringBuilder.append(findString(reacted_on_your_post))
            }
        }
    }
    return spannableStringBuilder.appendBoard(boardName)
}

private fun SpannableStringBuilder.appendBoard(name: String?): SpannableStringBuilder = append(name?.semiBold()).append(findString(_board))

/**
 * Method to get suitable text for the social live football data message
 */
fun ZoneLiveData.buildNotificationMessage(): SpannableStringBuilder {
    val spannableStringBuilder = SpannableStringBuilder()
    when (liveDataType) {
        MATCH_EVENTS -> {/*TODO: show goal, penalty, cards, substitution*/
        }
        TIME_STATUS_DATA -> {/*TODO: show match started, half time, full time*/
        }
        LINEUPS_DATA -> {/*TODO: show "lineups are available"*/
        }
        BOARD_ACTIVATED -> {/*TODO: show "board is now active"*/
        }
        SCORE_DATA -> {/*Not implemented*/
        }
        COMMENTARY_DATA -> {/*Not implemented*/
        }
        SCRUBBER_DATA -> {/*Not implemented*/
        }
        HIGHLIGHTS_DATA -> {/*Not implemented*/
        }
        MATCH_STATS_DATA -> {/*Not implemented*/
        }
        BOARD_DEACTIVATED -> {/*Not implemented*/
        }
    }
    return spannableStringBuilder
}