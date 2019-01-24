package life.plank.juna.zone.data.model.feed

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FeedInteraction(
        var likes: Int = 0,
        var dislikes: Int = 0,
        var shares: Int = 0,
        var hasLiked: Boolean = false,
        var hasDisliked: Boolean = false,
        var hasShared: Boolean = false,
        var hasCommented: Boolean = false,
        var hasPinned: Boolean = false,
        var myReaction: Int = 0,
        var pinId: String? = "",
        var previousPosition: Int = 0
) : Parcelable