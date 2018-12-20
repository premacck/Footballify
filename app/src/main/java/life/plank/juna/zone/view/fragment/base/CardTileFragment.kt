package life.plank.juna.zone.view.fragment.base

import android.util.Log
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.FeedEntry
import life.plank.juna.zone.data.model.notification.SocialNotification
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.interfaces.FeedEntryContainer
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.util.facilis.BaseCard
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import life.plank.juna.zone.view.fragment.clickthrough.FeedItemPeekPopup
import life.plank.juna.zone.view.fragment.post.PostDetailContainerFragment
import java.net.HttpURLConnection.HTTP_OK

/**
 * Parent class for fragments that contain tiles as well as the card layout
 */
abstract class CardTileFragment : BaseCard(), FeedEntryContainer {

    override fun openFeedEntry(feedEntryList: MutableList<FeedEntry>, boardId: String, position: Int) =
            pushFragment(PostDetailContainerFragment.newInstance(feedEntryList, boardId, position), true)

    override fun showFeedItemPeekPopup(position: Int) =
            pushPopup(FeedItemPeekPopup.newInstance(getFeedEntries(), getTheBoardId(), true, null, position))

    protected fun getFeedEntryDetails(restApi: RestApi, socialNotification: SocialNotification) {
        restApi.getFeedEntry(socialNotification.childId, getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e("getFeedEntry()", "ERROR: ", it)
        }, {
            when (it.code()) {
                HTTP_OK -> it.body()?.run { onNewFeedEntry(this) }
                else -> errorToast(R.string.failed_to_retrieve_feed, it)
            }
        }, this)
    }

    abstract fun getFeedEntries(): List<FeedEntry>

    abstract fun updateForumComments()

    abstract fun getTheBoardId(): String?

    abstract fun onNewFeedEntry(feedEntry: FeedEntry)

    abstract fun onSocialNotificationReceive(socialNotification: SocialNotification)
}