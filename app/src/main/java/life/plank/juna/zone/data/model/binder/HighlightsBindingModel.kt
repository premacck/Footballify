package life.plank.juna.zone.data.model.binder

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import life.plank.juna.zone.data.model.Highlights
import life.plank.juna.zone.data.model.MatchDetails
import life.plank.juna.zone.view.adapter.multiview.binder.MatchHighlightsBinder

@Parcelize
data class HighlightsBindingModel(var highlightsList: List<Highlights>? = null) : Parcelable {
    companion object {
        fun from(matchDetails: MatchDetails): HighlightsBindingModel {
            return HighlightsBindingModel(matchDetails.highlights)
        }
    }
}
