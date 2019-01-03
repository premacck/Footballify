package life.plank.juna.zone.data.model.notification

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import life.plank.juna.zone.util.common.AppConstants.CardNotificationType

@Parcelize
data class CardNotification(
        @CardNotificationType var cardNotificationType: String,
        var profilePicUrl: String? = null,
        var userHandle: String,
        var displayName: String,
        var notificationMessage: String? = ""
) : Parcelable, BaseInAppNotification()