package life.plank.juna.zone.data.model.feed

import android.os.Parcelable
import kotlinx.android.parcel.*
import java.util.*

@Parcelize
data class FeedItemComment(
        var id: String,
        var message: String,
        var commenterDisplayName: String,
        var commenterHandle: String,
        var commenterProfilePictureUrl: String?,
        var likeCount: Long,
        var hasLiked: Boolean,
        var replyCount: Long,
        var time: Date,
        var replies: @RawValue List<FeedItemComment>? = null
) : Parcelable