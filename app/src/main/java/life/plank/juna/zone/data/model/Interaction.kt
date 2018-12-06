package life.plank.juna.zone.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Interaction(
        var likes: Int = 0,
        var dislikes: Int = 0,
        var shares: Int = 0,
        var pins: Int = 0,
        var comments: Int = 0,
        var posts: Int = 0,
        var blocks: Int = 0,
        var bans: Int = 0,
        var mutes: Int = 0,
        var reports: Int = 0,
        @SerializedName("emoji-reacts")
        var emojiReacts: Int = 0,
        var activeUsers: Int = 0,
        var followers: Int = 0
) : Parcelable