package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Emoji(
        var name: String?,
        var emojiDrawable: Int?,
        var reaction: Int?,
        var reactionCount: Int = 0
) : Parcelable