package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BoardEvent(
        var type: String,
        var foreignId: Long,
        var homeTeamLogo: String? = null,
        var awayTeamLogo: String? = null,
        var leagueName: String? = null
) : Parcelable