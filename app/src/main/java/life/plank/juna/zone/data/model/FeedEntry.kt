package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import life.plank.juna.zone.util.masonry.base.AsymmetricItem

@Parcelize
data class FeedEntry(
        var feedItem: FeedItem = FeedItem(),
        var feedInteractions: FeedInteraction = FeedInteraction(),
        var cSpan: Int = 1,
        var rSpan: Int = 1
) : Parcelable, AsymmetricItem {
    override val columnSpan: Int
        get() = cSpan
    override val rowSpan: Int
        get() = rSpan
}