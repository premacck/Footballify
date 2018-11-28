package life.plank.juna.zone.data.model.notification

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class JunaNotification(
        var action: String = "",
        var userHandle: String = "",
        var userDisplayName: String = "",
        var userProfilePictureUrl: String? = "",
        var boardId: String? = "",
        var boardName: String? = "",
        var boardIconUrl: String = "",
        var feedItemId: String? = "",
        var feedItemType: String? = "",
        var feedItemThumbnailUrl: String? = "",
        var commentId: String? = "",
        var parentCommentId: String? = "",
        var commentMessage: String? = "",
        var imageUrl: String? = ""
) : Parcelable, BaseInAppNotification()