package life.plank.juna.zone.data.model.notification

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class JunaNotification(
        var action: String = "",
        var actor: String = "",
        var boardId: String? = "",
        var boardName: String? = "",
        var feedItemId: String? = "",
        var feedItemMessage: String? = "",
        var imageUrl: String? = ""
) : Parcelable