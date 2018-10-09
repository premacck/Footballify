package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Emoji(
        var name: String?,
        //update emojiUrl to string once implemented on backend
        var emojiUrl: Int?,
        var reaction: Int?
) : Parcelable