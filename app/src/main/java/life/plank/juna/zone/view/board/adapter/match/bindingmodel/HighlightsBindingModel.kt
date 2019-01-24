package life.plank.juna.zone.view.board.adapter.match.bindingmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import life.plank.juna.zone.data.model.football.*

@Parcelize
data class HighlightsBindingModel(var highlightsList: List<Highlights>? = null) : Parcelable {
    companion object {
        fun from(matchDetails: MatchDetails): HighlightsBindingModel {
            return HighlightsBindingModel(matchDetails.highlights)
        }
    }
}
