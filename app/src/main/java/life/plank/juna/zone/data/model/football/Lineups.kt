package life.plank.juna.zone.data.model.football

import android.os.Parcelable
import kotlinx.android.parcel.*

@Parcelize
data class Lineups(
        var homeTeamFormation: @RawValue List<FormationList>,
        var awayTeamFormation: @RawValue List<FormationList>,
        var errorMessage: Int
) : Parcelable