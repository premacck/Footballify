package life.plank.juna.zone.data.model.feed

import android.os.Parcelable
import com.prembros.asymrecycler.library.base.AsymItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FeedEntry(
        var feedItem: FeedItem = FeedItem(),
        var feedInteractions: FeedInteraction = FeedInteraction(),
        var cSpan: Int = 1,
        var rSpan: Int = 1
) : Parcelable, AsymItem {
    override val columnSpan: Int
        get() = cSpan
    override val rowSpan: Int
        get() = rSpan
}