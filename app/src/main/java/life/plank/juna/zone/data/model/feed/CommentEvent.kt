package life.plank.juna.zone.data.model.feed

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommentEvent(
        var isReply: Boolean,
        var boardId: String,
        var feedItemId: String?,
        var parentCommentId: String?,
        var nameToMention: String?
) : Parcelable {
    companion object {
        fun getForReply(boardId: String, parentCommentId: String): CommentEvent = CommentEvent(true, boardId, null, parentCommentId, null)

        fun getForBoardComment(boardId: String) = CommentEvent(false, boardId, null, null, null)

        fun getForFeedItemComment(boardId: String, feedItemId: String): CommentEvent = CommentEvent(false, boardId, feedItemId, null, null)
    }
}