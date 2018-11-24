package life.plank.juna.zone.data.model.notification

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import life.plank.juna.zone.R
import life.plank.juna.zone.util.DataUtil.findString

@Parcelize
data class InAppNotification(
        var message: String,
        var subMessage: String = findString(R.string.now),
        var imageUrl: String? = null
) : Parcelable