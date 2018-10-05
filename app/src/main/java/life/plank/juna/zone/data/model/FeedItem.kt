package life.plank.juna.zone.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class FeedItem(
        var id: String? = "",
        var title: String? = "",
        var url: String? = "",
        var source: String? = "",
        var summary: String? = "",
        var thumbnail: Thumbnail? = Thumbnail(),
        var dateCreated: String? = "",
        var contentType: String? = "",
        var description: String? = "",
        var interactions: Interaction? = Interaction(),
        var comments: @RawValue List<FeedItemComment>? = null,
        @SerializedName("actor") @Expose var user: User? = User()
) : Parcelable