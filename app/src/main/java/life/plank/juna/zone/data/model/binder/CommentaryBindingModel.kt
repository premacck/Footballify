package life.plank.juna.zone.data.model.binder

import android.os.Parcelable
import androidx.annotation.StringRes
import com.prembros.facilis.util.isNullOrEmpty
import kotlinx.android.parcel.Parcelize
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.*
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