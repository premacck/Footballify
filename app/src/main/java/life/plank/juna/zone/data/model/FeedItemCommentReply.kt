package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class FeedItemCommentReply(
        var id: String = "",
        var message: String,
        var commenterDisplayName: String = "",
        var commenterProfilePicUrl: String?,
        var time: Date
) : Parcelable