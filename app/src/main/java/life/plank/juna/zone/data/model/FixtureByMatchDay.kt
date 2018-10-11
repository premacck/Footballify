package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.util.*

@Parcelize
data class FixtureByMatchDay(
        var matchDay: Int = 0,
        var daySection: String = "",
        var fixtureByDateList: @RawValue List<FixtureByDate> = Collections.emptyList()
) : Parcelable