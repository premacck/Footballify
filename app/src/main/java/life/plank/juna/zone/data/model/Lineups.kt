package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Lineups(
        var homeTeamFormation: @RawValue List<FormationList>,
        var awayTeamFormation: @RawValue List<FormationList>,
        var errorMessage: Int
) : Parcelable