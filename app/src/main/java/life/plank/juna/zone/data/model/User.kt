package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.util.*

@Parcelize
data class User(
        var userPreferences: List<UserPreference>? = null,
        var objectId: String? = "",
        var displayName: String = "",
        var emailAddress: String = "",
        var country: String? = "",
        var city: String? = "",
        var profilePictureUrl: String? = null,
        var id: String = "",
        var dob: @RawValue Date? = Date()
) : Parcelable