package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserPreference(
        var zone: Zones? = null,
        var zonePreferences: ZonePreferences? = null
) : Parcelable