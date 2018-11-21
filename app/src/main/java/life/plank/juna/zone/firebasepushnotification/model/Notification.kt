package life.plank.juna.zone.firebasepushnotification.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Notification(
        var action: String = "",
        var from: String = "",
        var boardId: String = "",
        var boardName: String = "",
        var feedItemId: String = "",
        var feedItemMessage: String = ""
) : Parcelable