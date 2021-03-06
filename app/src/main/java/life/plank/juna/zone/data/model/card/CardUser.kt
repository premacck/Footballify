package life.plank.juna.zone.data.model.card

import android.os.Parcelable
import com.google.gson.annotations.*
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CardUser(
        @SerializedName("objectId") @Expose var id: String,
        var handle: String,
        var displayName: String,
        var profilePictureUrl: String? = null,
        var cardPictureUrl: String? = null,
        var followersCount: Int = 0,
        var cardCount: Int = 0
) : Parcelable