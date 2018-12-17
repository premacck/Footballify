package life.plank.juna.zone.util.common

import android.app.Activity
import androidx.core.app.ShareCompat
import life.plank.juna.zone.R
import life.plank.juna.zone.util.common.AppConstants.NEW_LINE
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty

fun shareBoardExternally(activity: Activity?, homeTeamName: String?, visitingTeamName: String?, boardId: String) {
    activity?.run {
        val isTeamNameAvailable = !isNullOrEmpty(homeTeamName) && !isNullOrEmpty(visitingTeamName)
        ShareCompat.IntentBuilder.from(this)
                .setType(getString(R.string.text_content_type))
                .setChooserTitle(R.string.share_board_to)
                .setText(StringBuilder()
                        .maybeAppend(getString(R.string.check_out_), isTeamNameAvailable)
                        .maybeAppend("$homeTeamName ${getString(R.string.vs)} $visitingTeamName", isTeamNameAvailable)
                        .maybeAppend(getString(R.string.display_name_match_board), isTeamNameAvailable)
                        .maybeAppend(NEW_LINE, isTeamNameAvailable)
                        .maybeAppend(NEW_LINE, isTeamNameAvailable)
                        .append(getShareableLink(boardId))
                        .toString()
                ).startChooser()
    }
}

fun getShareableLink(boardId: String) =
        StringBuilder(findString(R.string.deep_link_base_url))
                .append(findString(R.string.deep_link_board_suffix))
                .append(boardId)
                .toString()