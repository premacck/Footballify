package life.plank.juna.zone.view.forum

import com.airbnb.epoxy.Typed3EpoxyController
import com.prembros.facilis.util.isNullOrEmpty
import life.plank.juna.zone.component.epoxymodelview.*
import life.plank.juna.zone.data.model.feed.FeedItemComment
import life.plank.juna.zone.view.base.fragment.BaseCommentContainerFragment

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