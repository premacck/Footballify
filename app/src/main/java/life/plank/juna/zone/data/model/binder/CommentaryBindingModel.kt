package life.plank.juna.zone.data.model.binder

import android.os.Parcelable
import android.support.annotation.StringRes
import kotlinx.android.parcel.Parcelize

import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.Commentary
import life.plank.juna.zone.data.model.MatchDetails

import life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty
import java.util.*

@Parcelize
data class CommentaryBindingModel(var commentaryList: List<Commentary>? = Collections.emptyList(), @StringRes var errorMessage: Int = 0) : Parcelable {
    companion object {
        fun from(matchDetails: MatchDetails): CommentaryBindingModel {
            return CommentaryBindingModel(
                    matchDetails.commentary,
                    if (isNullOrEmpty(matchDetails.commentary)) R.string.commentaries_not_available else 0
            )
        }
    }
}