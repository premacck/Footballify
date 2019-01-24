package life.plank.juna.zone.data.model.board

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class BoardEvent(
        var type: String,
        var foreignId: Long,
        var homeTeamLogo: String? = null,
        var awayTeamLogo: String? = null,
        var leagueName: String? = null,
        var matchStartTime: Date
) : Parcelable