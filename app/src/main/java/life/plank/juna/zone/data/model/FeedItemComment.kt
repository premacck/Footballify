package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.util.*

@Parcelize
data class FeedItemComment(
        var id: String,
        var message: String,
        var commenterDisplayName: String,
        var commenterProfilePictureUrl: String?,
        var likeCount: Long,
        var hasLiked: Boolean,
        var replyCount: Long,
        var time: Date,
        var replies: @RawValue List<FeedItemCommentReply>?
) : Parcelable