package life.plank.juna.zone.ui.feed

import androidx.annotation.StringRes
import com.airbnb.epoxy.AutoModel
import com.prembros.facilis.activity.BaseCardActivity
import com.prembros.facilis.fragment.BaseFragment
import com.prembros.facilis.util.isNullOrEmpty
import life.plank.juna.zone.R
import life.plank.juna.zone.component.epoxymodelview.*
import life.plank.juna.zone.data.model.feed.FeedEntry
import life.plank.juna.zone.ui.base.component.EpoxyController3

class FeedEntryGridController(private val feedEntryContainer: FeedEntryContainer) : EpoxyController3<MutableList<FeedEntry>, Boolean, Int>() {

    @AutoModel
    lateinit var textView: TextModelViewModel_

    override fun buildModels(feedEntryList: MutableList<FeedEntry>?, hasError: Boolean, @StringRes message: Int?) {
        if (hasError) {
            textView.withText(message ?: R.string.something_went_wrong)
        } else {
            if (isNullOrEmpty(feedEntryList)) {
                textView.withText(message ?: R.string.no_feed_entries_found)
            } else {
                feedEntryList?.forEach {
                    FeedEntryGridViewModel_()
                            .id(feedEntryList.indexOf(it))
                            .withFeedItem(it.feedItem)
                            .onClick { feedEntryContainer.openFeedEntry(feedEntryList.indexOf(it)) }
                            .onLongClick(Pair(
                                    feedEntryContainer.getFeedItemPeekPopup(feedEntryList.indexOf(it)),
                                    ((feedEntryContainer as? BaseFragment)?.activity as? BaseCardActivity)
                                            ?: (feedEntryContainer as BaseCardActivity)
                            ))
                            .addTo(this)
                }
            }
        }
    }
}