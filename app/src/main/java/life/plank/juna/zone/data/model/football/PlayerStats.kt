package life.plank.juna.zone.data.model.football

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlayerStats(
        var position: Int?,
        var playerName: String?,
        var footballTeamLogo: String?,
        var goal: Int?,
        var assist: Int?,
        var yellowCard: Int?,
        var redCard: Int?
) : Parcelable