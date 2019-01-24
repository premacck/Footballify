package life.plank.juna.zone.data.model.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
        var userPreferences: List<UserPreference>? = null,
        var objectId: String? = "",
        var displayName: String = "",
        var handle: String = "",
        var emailAddress: String = "",
        var country: String? = "",
        var city: String? = "",
        var profilePictureUrl: String? = null,
        var id: String = "",
        var dateOfBirth: String = ""
) : Parcelable