package life.plank.juna.zone.data.model.notification

import android.os.Parcelable
import android.text.SpannableStringBuilder
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.FootballLiveData
import life.plank.juna.zone.notification.buildNotificationMessage
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty

@Parcelize
data class InAppNotification(
        var message: @RawValue SpannableStringBuilder,
        var subMessage: String = findString(R.string.now),
        var imageUrl: String? = null,
        var socialNotification: SocialNotification? = null,
        var footballLiveData: FootballLiveData? = null
) : Parcelable {
    constructor(socialNotification: SocialNotification) : this(
            socialNotification.buildNotificationMessage(),
            if (!isNullOrEmpty(socialNotification.commentMessage)) socialNotification.commentMessage!! else findString(R.string.now),
            socialNotification.boardIconUrl,
            socialNotification
    )

    constructor(footballLiveData: FootballLiveData) : this(
            footballLiveData.buildNotificationMessage(),
            findString(R.string.now),
            footballLiveData = footballLiveData
    )
}