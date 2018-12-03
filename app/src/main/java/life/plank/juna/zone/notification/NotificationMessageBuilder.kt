package life.plank.juna.zone.notification

import android.text.SpannableStringBuilder
import life.plank.juna.zone.R.string.*
import life.plank.juna.zone.data.model.ZoneLiveData
import life.plank.juna.zone.data.model.notification.JunaNotification
import life.plank.juna.zone.data.network.dagger.module.NetworkModule.GSON
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.common.bold
import life.plank.juna.zone.util.common.semiBold

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
        MATCH_EVENTS -> {
            /*TODO: show goal, penalty, cards*/
            val matchEventList = getMatchEventList(GSON)
            matchEventList?.run {
                if (!isNullOrEmpty(this)) {
                    for (matchEvent in this) {
                        when (matchEvent.eventType) {
                            GOAL -> {
                                /*<homeTeam> vs <awayTeam>\n'GOAL_' by <playerName>, assist by <relatedPlayerName>\n<result>*/
                            }
                            YELLOW_CARD -> {
                                /*<homeTeam> vs <awayTeam>\n<playerName> gets yellow card*/
                            }
                            RED_CARD, YELLOW_RED -> {
                                /*<homeTeam> vs <awayTeam>\n<playerName> gets red card*/
                            }
                            PENALTY -> {
                                /*<homeTeam> vs <awayTeam>\nPenalty to <teamName>*/
                            }
                        }
                    }
                }
            }
        }
        TIME_STATUS_DATA -> {
            /*TODO: show match started, half time, full time*/
            val liveTimeStatus = getLiveTimeStatus(GSON)
            when (liveTimeStatus.timeStatus) {
                LIVE -> {
                    /*<homeTeam> vs <awayTeam>\nMatch is now Live!*/
                }
                else -> {
                    /*<homeTeam> vs <awayTeam>\ngetDisplayTimeStatus(liveTimeStatus.timeStatus)*/
                }
            }
        }
        LINEUPS_DATA -> {
            /*TODO: show "lineups are available"*/
            /*<homeTeam> vs <awayTeam>\nLineups are now available!*/
        }
        BOARD_ACTIVATED -> {
            /*TODO: show "board is now active"*/
            /*<homeTeam> vs <awayTeam>\nBoard is now active!*/
        }
    }
    return spannableStringBuilder
}