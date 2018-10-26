package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class FeedItemComment(
        var id: String,
        var message: String,
        var commenterDisplayName: String,
        var commenterProfilePicUrl: String?,
        var likeCount: Long,
        var hasLiked: Boolean,
        var replyCount: Long,
        var replies: @RawValue List<FeedItemCommentReply>?
) : Parcelable