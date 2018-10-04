package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FeedEntry(
        var feedItem: FeedItem = FeedItem(),
        var feedInteractions: FeedInteraction = FeedInteraction()
) : Parcelable