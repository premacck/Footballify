package life.plank.juna.zone.data.model.notification

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class JunaNotification(
//        Notification related fields
        var id: String = "",
        var time: Date = Date(),
        var action: String = "",
        var isRead: Boolean,
        var isSeen: Boolean,
//        User related fields
        var userHandles: List<String> = emptyList(),
        var lastCommenterImageUrl: String? = null,
        var lastCommenterUserId: String? = null,
//        Board related fields
        var boardId: String = "",
        var boardName: String = "",
        var boardType: String = "",
        var boardIconUrl: String? = null,
//        Feed item related fields
        var feedItemId: String? = "",
        var feedItemType: String? = "",
        var feedItemThumbnailUrl: String? = "",
//        Comment related fields
        var commentId: String? = "",
        var parentCommentId: String? = "",
        var commentMessage: String? = ""
) : Parcelable, BaseInAppNotification()