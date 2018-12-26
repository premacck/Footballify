package life.plank.juna.zone.view.controller

import com.airbnb.epoxy.Typed3EpoxyController
import com.prembros.facilis.util.isNullOrEmpty
import life.plank.juna.zone.data.model.FeedItemComment
import life.plank.juna.zone.util.epoxy.modelview.*
import life.plank.juna.zone.view.fragment.base.BaseCommentContainerFragment

class ForumReplyController(
        private val parentComment: FeedItemComment,
        private val parentCommentPosition: Int,
        private val commentContainerFragment: BaseCommentContainerFragment
) : Typed3EpoxyController<List<FeedItemComment>, String, String>() {

    override fun buildModels(replies: List<FeedItemComment>?, previousPageToken: String?, nextPageToken: String?) {
        LoadMoreViewModel_()
                .id(0)
                .isForNextPage(false)
                .onClick { /*TODO: Load previous page from here*/ }
                .addIf(!isNullOrEmpty(previousPageToken), this)

        replies?.forEach {
            ReplyViewModel_()
                    .id(it.toString())
                    .withPosition(replies.indexOf(it))
                    .withParentComment(parentComment)
                    .withParentCommentPosition(parentCommentPosition)
                    .withContainerFragment(commentContainerFragment)
                    .prepare(it)
                    .addTo(this)
        }

        LoadMoreViewModel_()
                .id(1)
                .isForNextPage(true)
                .onClick { /*TODO: Load next page from here*/ }
                .addIf(!isNullOrEmpty(nextPageToken), this)
    }
}