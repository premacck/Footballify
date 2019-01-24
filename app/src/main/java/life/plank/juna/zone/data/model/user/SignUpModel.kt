package life.plank.juna.zone.data.model.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SignUpModel(
        var objectId: String?,
        var displayName: String?,
        var emailAddress: String?,
        var country: String?,
        var city: String?,
        var identityProvider: String?,
        var givenName: String?,
        var surname: String?
) : Parcelable