package life.plank.juna.zone.data.model.notification

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InAppNotification(
        var message: String,
        var imageUrl: String? = null
) : Parcelable