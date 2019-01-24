package life.plank.juna.zone.data.model.board

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BoardTemperature(
        var userCount: Long,
        var postCount: Long,
        var interactionCount: Long
) : Parcelable