package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class NextMatch(
        var homeTeamLogo: String = "",
        var awayTeamLogo: String = "",
        var matchStartTime: Date = Date(),
        var leagueName: String = "",
        var displayName: String = ""
) : Parcelable