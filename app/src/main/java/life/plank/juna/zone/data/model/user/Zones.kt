package life.plank.juna.zone.data.model.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Zones(
        var name: String = "",
        var category: String? = "",
        var imageUrl: String? = "",
        var id: String = "",
        var zoneIds: Set<String>? = null,
        var followerCount: Int = 0,
        var contributionCount: Int = 0,
        var interactionCount: Int = 0
) : Parcelable