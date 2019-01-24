package life.plank.juna.zone.data.model.feed

import android.os.Parcelable
import com.google.gson.annotations.*
import kotlinx.android.parcel.*
import life.plank.juna.zone.data.model.board.Interaction
import life.plank.juna.zone.data.model.user.User
import life.plank.juna.zone.util.common.AppConstants.*
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class FeedItem(
        var id: String? = "",
        var title: String,
        var url: String? = null,
        var source: String? = null,
        var summary: String? = null,
        var thumbnail: Thumbnail? = Thumbnail(),
        var dateCreated: Date? = null,
        var contentType: String,
        var backgroundColor: MutableList<String>? = ArrayList(),
        var interactions: Interaction = Interaction(),
        var description: String? = null,
        var comments: @RawValue List<FeedItemComment>? = null,
        @SerializedName("actor") @Expose var user: User? = null
) : Parcelable {

    constructor() : this("", "", "", "", "", Thumbnail(), Date(), "", null, Interaction())

    /**
     * Constructor for WebLink Posts
     */
    constructor(title: String, url: String?, source: String?, summary: String?, description: String?, imageUrl: String) : this(
            title = title,
            url = url,
            source = source,
            summary = summary,
            description = description,
            thumbnail = Thumbnail(imageUrl),
            contentType = NEWS
    )

    /**
     * Constructor for Text Posts
     */
    constructor(title: String, backgroundColor: MutableList<String>?) : this(
            title = title,
            backgroundColor = backgroundColor,
            contentType = ROOT_COMMENT
    )
}