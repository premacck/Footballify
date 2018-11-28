package life.plank.juna.zone.data.model.notification

import android.os.Parcelable
import android.text.SpannableStringBuilder
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.ZoneLiveData
import life.plank.juna.zone.notification.buildNotificationMessage
import life.plank.juna.zone.util.DataUtil.findString
import life.plank.juna.zone.util.DataUtil.isNullOrEmpty

@Parcelize
data class InAppNotification(
        var message: @RawValue SpannableStringBuilder,
        var subMessage: String = findString(R.string.now),
        var imageUrl: String? = null,
        var junaNotification: JunaNotification? = null,
        var zoneLiveData: ZoneLiveData? = null
) : Parcelable {
    constructor(junaNotification: JunaNotification) : this(
            junaNotification.buildNotificationMessage(),
            if (!isNullOrEmpty(junaNotification.commentMessage)) junaNotification.commentMessage!! else findString(R.string.now),
            junaNotification.imageUrl,
            junaNotification
    )

    constructor(zoneLiveData: ZoneLiveData) : this(
            zoneLiveData.buildNotificationMessage(),
            findString(R.string.now),
            zoneLiveData = zoneLiveData
    )
}