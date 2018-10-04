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

//        TODO: Change this to camelcase once done on backend
        var UserId: String = "",
        var InviteeUserId: String = "",
        var InvitationLink: String = "",
        var InviterName: String = "",
        var BoardId: String = ""
) : Parcelable