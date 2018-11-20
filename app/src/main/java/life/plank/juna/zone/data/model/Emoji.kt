package life.plank.juna.zone.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Emoji(
        @SerializedName("key") @Expose var emoji: Int = 0,
        @SerializedName("value") @Expose var emojiCount: Int = 0
) : Parcelable