package life.plank.juna.zone.data.model.notification

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//TODO: Remove this class and replace its usages with [SocialNotification] once backend develops the proper notification model
@Parcelize
data class PseudoNotification(
        var notificationId: String,
        var message: String,
        var isSeen: Boolean,
        var isRead: Boolean
) : Parcelable