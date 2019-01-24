package life.plank.juna.zone.view.base.fragment

import android.util.Log
import life.plank.juna.zone.R
import life.plank.juna.zone.data.api.*
import life.plank.juna.zone.data.model.feed.FeedEntry
import life.plank.juna.zone.data.model.notification.SocialNotification
import life.plank.juna.zone.util.common.errorToast
import life.plank.juna.zone.view.feed.*
import java.net.HttpURLConnection.HTTP_OK

/**
 * Parent class for fragments that contain tiles as well as the card layout
 */
abstract class CardTileFragment : BaseJunaCard(), FeedEntryContainer {

    override fun openFeedEntry(feedEntryList: MutableList<FeedEntry>, boardId: String, position: Int) =
            pushFragment(PostDetailContainerFragment.newInstance(feedEntryList, boardId, position), true)

    override fun openFeedEntry(position: Int) {
        val boardId = getTheBoardId()
        if (boardId != null) {
            pushFragment(PostDetailContainerFragment.newInstance(getFeedEntries(), boardId, position), true)
        }
    }

    override fun getFeedItemPeekPopup(position: Int) =
            FeedItemPeekPopup.newInstance(getFeedEntry(position), getTheBoardId(), true)

    protected fun getFeedEntryDetails(restApi: RestApi, socialNotification: SocialNotification) {
        restApi.getFeedEntry(socialNotification.childId!!).setObserverThreadsAndSmartSubscribe({
            Log.e("getFeedEntry()", "ERROR: ", it)
        }, {
            when (it.code()) {
                HTTP_OK -> it.body()?.run { onNewFeedEntry(this) }
                else -> errorToast(R.string.failed_to_retrieve_feed, it)
            }
        }, this)
    }

    abstract fun getFeedEntries(): List<FeedEntry>

    abstract fun getFeedEntry(position: Int): FeedEntry

    abstract fun updateForumComments()

    abstract fun getTheBoardId(): String?

    abstract fun onNewFeedEntry(feedEntry: FeedEntry)

    abstract fun onSocialNotificationReceive(socialNotification: SocialNotification)
}