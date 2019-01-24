package life.plank.juna.zone.data.model.football

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LiveTimeStatus(
        var timeStatus: String = "",
        var minute: Int = 0,
        var extraMinute: Int = 0
) : Parcelable