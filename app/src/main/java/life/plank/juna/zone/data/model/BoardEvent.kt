package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BoardEvent(
        var type: String,
        var foreignId: Int
) : Parcelable