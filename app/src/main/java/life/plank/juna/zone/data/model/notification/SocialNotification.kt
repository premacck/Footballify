package life.plank.juna.zone.data.model.notification

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class SocialNotification(
//        Notification related fields
        var id: String = "",
        var date: Date,
        var action: String,
        var isRead: Boolean,
        var isSeen: Boolean,
        var notificationMessage: String? = "",
//        User related fields
        var parentId: String,
        var childId: String? = null,
        var siblingId: String? = null,
        var privateBoardIcon: String? = null,
        var homeTeamIcon: String? = null,
        var awayTeamIcon: String? = null,
        var feedItemIcon: String? = null,
        var lastActorIcon: String? = null
) : Parcelable, BaseInAppNotification()