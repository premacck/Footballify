package life.plank.juna.zone.view.board.adapter.match.bindingmodel

import android.os.Parcelable
import androidx.annotation.StringRes
import com.prembros.facilis.util.isNullOrEmpty
import kotlinx.android.parcel.Parcelize
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.football.*

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
