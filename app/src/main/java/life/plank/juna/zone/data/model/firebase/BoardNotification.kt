package life.plank.juna.zone.data.model.firebase

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BoardNotification(
        var contentType: String = "",
        var thumbnailImageUrl: String = "",
        var thumbnailWidth: Int = 0,
        var thumbnailHeight: Int = 0,
        var imageUrl: String = "",
        var action: String = "",
        var actor: String = "",
        var title: String = "",
        var foreignId: Long = 0,

        var userId: String = "",
        var inviteeUserId: String = "",
        var invitationLink: String = "",
        var inviterName: String = "",
        var boardId: String = ""
) : Parcelable