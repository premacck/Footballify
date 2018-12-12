package life.plank.juna.zone.notification

import android.text.SpannableStringBuilder
import life.plank.juna.zone.R.string.*
import life.plank.juna.zone.data.model.ZoneLiveData
import life.plank.juna.zone.data.model.notification.BaseInAppNotification
import life.plank.juna.zone.data.model.notification.JunaNotification
import life.plank.juna.zone.data.network.dagger.module.NetworkModule.GSON
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.util.common.DataUtil.*
import life.plank.juna.zone.util.common.bold
import life.plank.juna.zone.util.common.maybeAppend
import life.plank.juna.zone.util.common.semiBold

fun BaseInAppNotification.getNotificationMessage(): SpannableStringBuilder {
    return when {
        this is JunaNotification -> buildNotificationMessage()
        this is ZoneLiveData -> buildNotificationMessage()
        else -> SpannableStringBuilder()
    }
}

/**
 * Method to get suitable text for the social interaction notification message
 */
fun JunaNotification.buildNotificationMessage(): SpannableStringBuilder {
    val leadingString = (if (!isNullOrEmpty(userHandles)) userHandles[0] else findString(someone)).bold()
    val spannableStringBuilder = SpannableStringBuilder(leadingString).append(SINGLE_SPACE)
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
    val header = "$homeTeamName ${findString(vs)} $visitingTeamName:"
    val spannableStringBuilder = SpannableStringBuilder(header.bold()).append(NEW_LINE)
    when (liveDataType) {
        MATCH_EVENTS -> {
            val matchEventList = getMatchEventList(GSON)
            matchEventList?.run {
                if (!isNullOrEmpty(this)) {
                    for (matchEvent in this) {
                        when (matchEvent.eventType) {
                            GOAL, OWN_GOAL ->
                                spannableStringBuilder.append(matchEvent.result, NEW_LINE)
                                        .append((if (matchEvent.eventType == GOAL) findString(goal) else OWN_GOAL_LOWERCASE).semiBold())
                                        .append(findString(_by_), matchEvent.playerName.semiBold(), COMMA)
                                        .maybeAppend(findString(assist_by_), !isNullOrEmpty(matchEvent.relatedPlayerName))
                                        .maybeAppend(matchEvent.relatedPlayerName, !isNullOrEmpty(matchEvent.relatedPlayerName))
                            YELLOW_CARD ->
                                spannableStringBuilder.append(matchEvent.playerName, findString(_receives_yellow_card))
                            RED_CARD, YELLOW_RED ->
                                spannableStringBuilder.append(matchEvent.playerName, findString(_receives_red_card))
                            PENALTY, MISSED_PENALTY ->
                                spannableStringBuilder
                                        .maybeAppend(matchEvent.result, matchEvent.eventType == PENALTY)
                                        .maybeAppend(NEW_LINE, matchEvent.eventType == PENALTY)
                                        .append(findString(if (matchEvent.eventType == PENALTY) penalty_conceded_by else penalty_missed_by, matchEvent.playerName))
                        }
                    }
                }
            }
        }
        TIME_STATUS_DATA -> {
            val liveTimeStatus = getLiveTimeStatus(GSON)
            when (liveTimeStatus?.timeStatus) {
                LIVE -> spannableStringBuilder.append(findString(match_is_now_live))
                else -> spannableStringBuilder.append(getDisplayTimeStatus(liveTimeStatus?.timeStatus))
            }
        }
        LINEUPS_DATA -> spannableStringBuilder.append(findString(lineups_are_now_available))
        BOARD_ACTIVATED -> spannableStringBuilder.append(findString(match_board_is_now_active))
    }
    return spannableStringBuilder
}