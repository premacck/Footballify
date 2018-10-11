package life.plank.juna.zone.data.model.binder

import android.os.Parcelable
import android.support.annotation.StringRes
import kotlinx.android.parcel.Parcelize

import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.FootballTeam
import life.plank.juna.zone.data.model.MatchDetails
import life.plank.juna.zone.data.model.MatchEvent
import life.plank.juna.zone.view.adapter.multiview.binder.SubstitutionBinder

import life.plank.juna.zone.util.DataUtil.extractSubstitutionEvents
import life.plank.juna.zone.util.DataUtil.isNullOrEmpty

@Parcelize
data class SubstitutionBindingModel(
        var substitutionEvents: List<MatchEvent>? = null,
        var homeTeam: FootballTeam? = null,
        var awayTeam: FootballTeam? = null,
        @field:StringRes var errorMessage: Int? = null
) : Parcelable {
    companion object {
        fun from(matchDetails: MatchDetails): SubstitutionBindingModel {
            var substitutionEvents = extractSubstitutionEvents(matchDetails.matchEvents)
            return SubstitutionBindingModel(
                    substitutionEvents,
                    matchDetails.homeTeam,
                    matchDetails.awayTeam,
                    if (isNullOrEmpty(substitutionEvents)) R.string.no_substitutions_yet else null
            )
        }
    }
}
