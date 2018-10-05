package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FootballTeam(
        var id: Int?,
        var foreignId: Int?,
        var name: String?,
        var isNationalTeam: Boolean?,
        var founded: Int?,
        var logoLink: String
) : Parcelable