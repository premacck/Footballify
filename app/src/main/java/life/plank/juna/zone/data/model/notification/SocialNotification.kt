package life.plank.juna.zone.data.model.notification

import android.os.Parcelable
import com.google.gson.annotations.*
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class SocialNotification(
//        Notification related fields
        var id: String = "",
        var date: Date,
        var action: String,
        var isRead: Boolean,
        var isSeen: Boolean,
        var notificationMessage: String,
//        User related fields
        @SerializedName("actorIds") @Expose var userHandles: List<String> = emptyList(),
        var parentId: String,
        var childId: String? = null,
        var siblingId: String? = null,
        var contentType: String? = null,
        var iconUrl: List<String>? = null
) : Parcelable, BaseInAppNotification()