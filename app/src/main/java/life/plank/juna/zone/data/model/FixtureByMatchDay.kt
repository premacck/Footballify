package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class FixtureByMatchDay(
        var daySection: String,
        var matchDay: Int,
        var fixtureByDateList: @RawValue List<FixtureByDate>
) : Parcelable