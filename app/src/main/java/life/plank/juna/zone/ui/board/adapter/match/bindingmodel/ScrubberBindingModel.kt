package life.plank.juna.zone.ui.board.adapter.match.bindingmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import life.plank.juna.zone.data.model.football.*

@Parcelize
data class ScrubberBindingModel(var scrubberDataList: List<ScrubberData>? = null) : Parcelable {
    companion object {
        fun from(matchDetails: MatchDetails): ScrubberBindingModel {
            return ScrubberBindingModel(matchDetails.scrubberDataList)
        }
    }
}
