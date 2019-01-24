package life.plank.juna.zone.data.model.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ZonePreferences(
        var teams: List<String>? = null,
        var leagues: List<String>? = null
) : Parcelable
