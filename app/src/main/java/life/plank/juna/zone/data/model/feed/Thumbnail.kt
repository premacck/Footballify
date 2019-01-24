package life.plank.juna.zone.data.model.feed

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Thumbnail(
        var imageUrl: String = "",
        var imageHeight: Int = 0,
        var imageWidth: Int = 0
) : Parcelable