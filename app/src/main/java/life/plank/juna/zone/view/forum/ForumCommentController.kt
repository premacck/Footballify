package life.plank.juna.zone.view.forum

import com.airbnb.epoxy.Typed3EpoxyController
import com.prembros.facilis.util.isNullOrEmpty
import life.plank.juna.zone.component.epoxymodelview.*
import life.plank.juna.zone.data.model.feed.FeedItemComment
import life.plank.juna.zone.view.base.fragment.BaseCommentContainerFragment

class ForumCommentController(private val commentContainerFragment: BaseCommentContainerFragment) : Typed3EpoxyController<List<FeedItemComment>, String, String>() {

    override fun buildModels(comments: List<FeedItemComment>?, previousPageToken: String?, nextPageToken: String?) {
        LoadMoreViewModel_()
                .id(0)
                .isForNextPage(false)
                .onClick { /*TODO: Load previous page from here*/ }
                .addIf(!isNullOrEmpty(previousPageToken), this)

        comments?.forEach {
            CommentViewModel_()
                    .id(it.toString())
                    .prepare(Triple(it, comments.indexOf(it), commentContainerFragment))
                    .addTo(this)
        }

        LoadMoreViewModel_()
                .id(1)
                .isForNextPage(true)
                .onClick { /*TODO: Load next page from here*/ }
                .addIf(!isNullOrEmpty(nextPageToken), this)
    }
}