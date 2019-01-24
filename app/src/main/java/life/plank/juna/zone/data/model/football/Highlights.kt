package life.plank.juna.zone.data.model.football

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Highlights(
        var highlightsLink: String?
) : Parcelable