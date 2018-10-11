package life.plank.juna.zone.data.model.binder

import android.os.Parcelable
import android.support.annotation.StringRes
import kotlinx.android.parcel.Parcelize

import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.MatchDetails
import life.plank.juna.zone.data.model.Standings
import life.plank.juna.zone.view.adapter.multiview.binder.StandingsBinder

import life.plank.juna.zone.util.DataUtil.isNullOrEmpty

@Parcelize
data class StandingsBindingModel(var standingsList: List<Standings>? = null, @field:StringRes var errorMessage: Int? = null) : Parcelable {
    companion object {
        fun from(matchDetails: MatchDetails): StandingsBindingModel {
            return StandingsBindingModel(
                    matchDetails.standingsList,
                    if (isNullOrEmpty(matchDetails.standingsList)) R.string.failed_to_get_standings else null
            )
        }
    }
}
