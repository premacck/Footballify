package life.plank.juna.zone.data.model.notification

import android.os.Parcelable
import android.text.SpannableStringBuilder
import kotlinx.android.parcel.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.FootballLiveData
import life.plank.juna.zone.notification.buildNotificationMessage
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.util.time.getTimeAgo

@Parcelize
data class InAppNotification(
        var message: @RawValue SpannableStringBuilder,
        var subMessage: String = findString(R.string.now),
        var imageUrl: List<String>? = null,
        var socialNotification: SocialNotification? = null,
        var footballLiveData: FootballLiveData? = null
) : Parcelable {
    constructor(socialNotification: SocialNotification) : this(
            SpannableStringBuilder(socialNotification.notificationMessage),
            getTimeAgo(socialNotification.date),
            socialNotification.iconUrl,
            socialNotification
    )

    constructor(footballLiveData: FootballLiveData) : this(
            footballLiveData.buildNotificationMessage(),
            findString(R.string.now),
            footballLiveData = footballLiveData
    )
}