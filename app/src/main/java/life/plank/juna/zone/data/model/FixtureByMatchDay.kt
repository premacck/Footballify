package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class FixtureByMatchDay(
        var matchDay: Int,
        var daySection: String,
        var fixtureByDateList: @RawValue List<FixtureByDate>
) : Parcelable